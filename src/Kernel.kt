import java.lang.Exception
import kotlin.math.pow
import kotlin.math.sqrt

class Kernel(private val eG: EnergyGraph, type: String) {
    private val piX2 = 6.28
    private val e = 2.72
    private val stdDev = 1.0
    private val stdDevSqr = stdDev * stdDev
    private val kerSizeBy2 = 2
    private val sobelX = listOf(listOf(-1, 0, 1),
            listOf(-2, 0, 2),
            listOf(-1, 0, 1))
    private val sobelY = listOf(listOf(-1, -2, -1),
            listOf(0, 0, 0),
            listOf(1, 2, 1))
    private val basicX = listOf(listOf(0, 0, 0),
            listOf(-1, 0, 1),
            listOf(0, 0, 0))
    private val basicY = listOf(listOf(0, -1, 0),
            listOf(0, 0, 0),
            listOf(0, 1, 0))
    private val gaussianBlur = mutableListOf<MutableList<Double>>()
    private lateinit var discreteKernelX: List<List<Int>>
    private lateinit var discreteKernelY: List<List<Int>>
    private lateinit var continuousKernel: List<List<Double>>
    var applyTo: (Int, Int) -> Unit

    private fun generateNormalDistributionMap() {
        for (diffY in (-kerSizeBy2)..(kerSizeBy2)) {
            val newRow = mutableListOf<Double>()
            for (diffX in (-kerSizeBy2)..(kerSizeBy2)) {
                 newRow.add((1 / (piX2 * stdDevSqr)) * e.pow(-1 * (diffX * diffX + diffY * diffY) / (2 * stdDevSqr)))
            }
            gaussianBlur += newRow
        }
    }

    private fun fixedCoordinate(coord: Int, maxValue: Int): Int {
        return when (coord) {
            in 1 until maxValue -> coord
            0 -> 1
            maxValue -> maxValue - 1
            else -> throw IndexOutOfBoundsException("coord = $coord, maxValue = $maxValue")
        }
    }

    private fun discreteFilter(x: Int, y: Int) {
        val fixedX = fixedCoordinate(x, eG.width - 1)
        val fixedY = fixedCoordinate(y, eG.height - 1)
        var sumX = 0
        var sumY = 0
        for (j in (-1)..1) {
            innerLoop@ for (i in (-1)..1) {
                try {
                    sumX += (discreteKernelX[j + 1][i + 1] * eG[y + j][fixedX + i].gray)
                } catch (e: IndexOutOfBoundsException) {
                }
                try {
                    sumY += (discreteKernelY[j + 1][i + 1] * eG[fixedY + j][x + i].gray)
                } catch (e: IndexOutOfBoundsException) {
                    continue@innerLoop
                }
            }
        }
        eG[y][x].energy = sqrt((sumX * sumX + sumY * sumY).toDouble())
    }

    private fun continuousFilter(x: Int, y: Int) {
        var sum = 0.0
        for (X in (-kerSizeBy2)..(kerSizeBy2)) {
            for (Y in (-kerSizeBy2)..(kerSizeBy2)) {
                try {
                    sum += eG[y + Y][x + X].gray * continuousKernel[kerSizeBy2 + Y][kerSizeBy2 + X]
                } catch (e: IndexOutOfBoundsException) {
                    continue
                }
            }
        }
        eG[y][x].gray = sum.toInt()
    }


    init {
        when (type) {
            "basic" -> {
                discreteKernelX = basicX
                discreteKernelY = basicY
                applyTo = ::discreteFilter
            }
            "sobel" -> {
                discreteKernelX = sobelX
                discreteKernelY = sobelY
                applyTo = ::discreteFilter
            }
            "gaussian" -> {
                generateNormalDistributionMap()
                continuousKernel = gaussianBlur
                applyTo = ::continuousFilter
            }
            else -> {
                throw Exception("Kernel '$type' does not exist")
            }
        }
    }


}