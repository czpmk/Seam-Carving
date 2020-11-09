import java.awt.Color
import java.awt.image.BufferedImage
import java.lang.Exception
import kotlin.IndexOutOfBoundsException

class EnergyGraph(private val bufferedImage: BufferedImage, kernelType: String) : MutableList<MutableList<Pixel>> by mutableListOf() {
    val height: Int
        get() {
            return try {
                this.size
            } catch (e: UninitializedPropertyAccessException) {
                0
            }
        }
    val width: Int
        get() {
            return try {
                this[0].size
            } catch (e: UninitializedPropertyAccessException) {
                0
            }
        }
    private val blurKernel = Kernel(this, "gaussian")
    private val energyKernel = Kernel(this, kernelType)

    init {
        createEnergyGraph()
    }

    /** Copies colors of every pixel in an BufferedImage to EnergyGraph */
    private fun createEnergyGraph() {
        for (y in 0 until bufferedImage.height) {
            val newRow = mutableListOf<Pixel>()
            for (x in 0 until bufferedImage.width) {
                newRow.add(Pixel(bufferedImage.getRGB(x, y)))
            }
            this.add(newRow)
        }
    }

    /** Blur the picture*/
    fun blurGreyValues() {
        for (y in 0 until height) {
            for (x in 0 until width) {
                blurKernel.applyTo(x, y)
            }
        }
    }

    /** Updates energy of all elements of the graph*/
    fun updateEnergyAll() {
        for (y in this.indices) {
            for (x in this[y].indices) {
                energyKernel.applyTo(x, y)
            }
        }
    }

    private fun updateEnergySeam(seam: MutableList<Coordinates>) {
        for (vertex in seam) {
            for (x in (vertex.x - 2)..(vertex.x + 1)) {
                try {
                    energyKernel.applyTo(x, vertex.y)
                } catch (e: IndexOutOfBoundsException) {
                    continue
                }
            }
        }
    }

    /** Removes seam and updates  neighbouring elements' energies */
    fun removeSeam(seam: MutableList<Coordinates>) {
        for (vertex in seam) {
            this[vertex.y].removeAt(vertex.x)
        }
        updateEnergySeam(seam)
    }

    fun transpose() {
        val newGraph = mutableListOf<MutableList<Pixel>>()
        for (x in 0 until width) {
            val newRow = mutableListOf<Pixel>()
            for (y in 0 until height) {
                newRow.add(this[y][x])
            }
            newGraph.add(newRow)
        }
        this.clear()
        for (i in newGraph.indices) {
            this.add(newGraph[i])
        }
    }

    fun toBufferedImage(): BufferedImage {
        val newImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
        for (y in this.indices) {
            for (x in this[0].indices) {
                newImage.setRGB(x, y, this[y][x].rgb)
            }
        }
        return newImage
    }

    fun energyToBufferedImage(): BufferedImage {
        val newImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
        var maxEnergy = -20.0
        for (y in this.indices) {
            val energy: Double = this[y].maxByOrNull { it.energy }?.energy ?: throw Exception("eee1")
            if (energy > maxEnergy) maxEnergy = energy
        }
        val newColor = { energy: Double -> ((energy / maxEnergy) * 255).toInt() }
        val colorOf = { pixel: Pixel ->
            val col = newColor(pixel.energy)
            Color(col, col, col).rgb
        }
        for (y in this.indices) {
            for (x in this[0].indices) {
                newImage.setRGB(x, y, colorOf(this[y][x]))
            }
        }
        return newImage
    }

    fun sumToBufferedImage(): BufferedImage {
        val newImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)
        var maxEnergy = -20.0
        for (y in this.indices) {
            val energy: Double = this[y].maxByOrNull { it.energySum }?.energySum ?: throw Exception("eee1")
            if (energy > maxEnergy) maxEnergy = energy
        }
        val newColor = { energy: Double -> ((energy / maxEnergy) * 255).toInt() }
        val colorOf = { pixel: Pixel ->
            val col = newColor(pixel.energySum)
            Color(col, col, col).rgb
        }
        for (y in this.indices) {
            for (x in this[0].indices) {
                newImage.setRGB(x, y, colorOf(this[y][x]))
            }
        }
        return newImage
    }

    fun grayToBufferedImage(): BufferedImage {
        val newImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_BYTE_GRAY)
        for (y in this.indices) {
            for (x in this[0].indices) {
                newImage.setRGB(x, y, this[y][x].grayColor())
            }
        }
        return newImage
    }
}