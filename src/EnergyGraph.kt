import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.IndexOutOfBoundsException
import kotlin.math.sqrt

class EnergyGraph(private val bufferedImage: BufferedImage) : MutableList<MutableList<Pixel>> by mutableListOf() {
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

    init {
        createEnergyGraph()
        updateEnergyAll()
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

    private fun getGradient(color1: Color, color2: Color): Int {
        val red = (color1.red - color2.red)
        val green = (color1.green - color2.green)
        val blue = (color1.blue - color2.blue)
        return red * red + green * green + blue * blue
    }

    /** Returns coordinates, prevents OutOfBounds exception */
    private fun getCoordinates(coordinate: Int, maxValue: Int): Pair<Int, Int> {
        return when (coordinate) {
            0 -> Pair(0, 2)
            maxValue - 1 -> Pair(maxValue - 3, maxValue - 1)
            else -> Pair(coordinate - 1, coordinate + 1)
        }
    }

    /** Returns energy of a single pixel */
    private fun getEnergy(x: Int, y: Int): Double {
        val (x1, x2) = getCoordinates(x, width)
        val (y1, y2) = getCoordinates(y, height)
        val xGradient = getGradient(this[y][x1], this[y][x2])
        val yGradient = getGradient(this[y1][x], this[y2][x])
        return sqrt((xGradient + yGradient).toDouble())
    }

    /** Updates energy of all elements of the graph*/
    private fun updateEnergyAll() {
        for (y in this.indices) {
            for (x in this[y].indices) {
                this[y][x].energy = getEnergy(x, y)
            }
        }
    }

    private fun updateEnergySeam(seam: MutableList<Coordinates>) {
        for (vertex in seam) {
            for (x in (vertex.x - 2)..(vertex.x + 1)) {
                try {
                    this[vertex.y][x].energy = getEnergy(x, vertex.y)
                } catch (e: IndexOutOfBoundsException) {
                    continue
                }
            }
        }
    }

//    fun markSeam(seam: MutableList<Coordinates>) {
//        for (vertex in seam) {
//            this[vertex.y][vertex.x] = Pixel(Color(255, 0, 0).rgb)
//        }
//    }

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
}