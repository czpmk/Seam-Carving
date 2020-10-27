import java.awt.image.BufferedImage

fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
        this.subList(fromIndex.coerceAtLeast(0), toIndex.coerceAtMost(this.size))

class SeamFinder(inputImage: BufferedImage) {
    private var energyGraph: EnergyGraph = EnergyGraph(inputImage)

    private fun updateFirstRow() {
        val y = 0
        for (x in 0 until energyGraph.width) {
            energyGraph[y][x].energySum = energyGraph[y][x].energy
            energyGraph[y][x].predecessor = null
        }
    }

    private fun setLowestSumOf (x: Int, y: Int) {
        // xDiff is a difference -> predecessor's.x - child.x (-1 to 1)
        var xDiff: Int = energyGraph[y - 1].safeSubList(x - 1, x + 2).withIndex().minByOrNull { (_, v) -> v.energySum }?.index
                ?: throw Exception("null index value in setLowestSum")
        // shift 1 step left except if x is at position 0
        if (x > 0) xDiff -= 1
        energyGraph[y][x].energySum = energyGraph[y - 1][x + xDiff].energySum + energyGraph[y][x].energy
        energyGraph[y][x].predecessor = Coordinates(x + xDiff, y - 1)
    }

    private fun updateEnergySums() {
        for (y in 1 until energyGraph.height) {
            for (x in 0 until energyGraph.width) {
                setLowestSumOf(x, y)
            }
        }
    }

    private fun getSeam(): MutableList<Coordinates> {
        val seam = mutableListOf<Coordinates>()
        val y = energyGraph.lastIndex
        val lowestX = energyGraph[y].withIndex().minByOrNull { (_, v) -> v.energySum }?.index
                ?: throw Exception("null index found in getSeam()")
        var coords = Coordinates(lowestX, y)
        seam.add(coords)
        while (true) {
            coords = energyGraph[coords.y][coords.x].predecessor ?: break
            seam.add(coords)
        }
        return seam
    }

    private fun removeVerticalSeams(n: Int) {
        repeat(n) {
            updateFirstRow()
            updateEnergySums()
            energyGraph.removeSeam(getSeam())

            println(it)
        }
    }

    private fun removeHorizontalSeams(n: Int) {
        energyGraph.transpose()
        repeat(n) {
            updateFirstRow()
            updateEnergySums()
            energyGraph.removeSeam(getSeam())

            println(it)
        }
        energyGraph.transpose()
    }

    fun removeLines(vertical: Int, horizontal: Int): BufferedImage {
        removeVerticalSeams(vertical)
        removeHorizontalSeams(horizontal)

        return energyGraph.toBufferedImage()
    }

    fun showEnergy(): BufferedImage {
        updateFirstRow()
        updateEnergySums()
        return energyGraph.sumToBufferedImage()
    }
}