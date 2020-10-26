import java.awt.image.BufferedImage
import java.lang.Exception

class SeamFinder(inputImage: BufferedImage) {
    private var energyGraph: EnergyGraph = EnergyGraph(inputImage)
    private lateinit var vertices: Array<Array<Vertex?>>
    private var queue = Queue()

    private fun newVerticesGraph() {
        vertices = Array(energyGraph.height) { Array(energyGraph.width) { null } }
    }

    private fun addFirstRow() {
        for (x in energyGraph[0].indices) {
            val y = 0
            vertices[y][x] = Vertex(energyGraph, x, y, energyGraph[y][x].energy)
            queue.put(vertices[y][x] ?: throw Exception("Can not add null to Queue"))
        }
    }

    private fun enqueueChildren(predecessor: Vertex) {
        val y = predecessor.y + 1
        for (x in (predecessor.x - 1)..(predecessor.x + 1)) {
            try {
                when {
                    // uninitialized child
                    vertices[y][x] == null -> {
                        vertices[y][x] = Vertex(energyGraph, x, y, predecessor.energySum + energyGraph[y][x].energy)
                        vertices[y][x]!!.predecessor = predecessor
                    }
                    // energySum can be updated
                    vertices[y][x]!!.energy + predecessor.energySum < vertices[y][x]!!.energySum -> {
                        vertices[y][x]!!.energySum = vertices[y][x]!!.energy + predecessor.energySum
                        vertices[y][x]!!.predecessor = predecessor
                    }
                    // nothing was changed, do not enqueue
                    else -> continue
                }
                queue.put(vertices[y][x] ?: throw Exception("Vertex expected, found null"))
            } catch (e: IndexOutOfBoundsException) {
                continue
            }
        }
    }

    private fun getLowestSum(): Vertex {
        var minVertex: Vertex
        while (queue.isNotEmpty()) {
            minVertex = queue.extractMin()
            if (minVertex.y == energyGraph.lastIndex) {
                return minVertex
            } else {
                enqueueChildren(minVertex)
            }
        }
        throw Exception("No min vertex found")
    }

    private fun getSeam(): MutableList<Coordinates> {
        var vertex = getLowestSum()
        val seam = mutableListOf<Coordinates>()
        seam.add(Coordinates(vertex.x, vertex.y))
        while (vertex.predecessor != null) {
            vertex = vertex.predecessor!!
            seam.add(Coordinates(vertex.x, vertex.y))
        }
        return seam
    }

    fun removeLines(vertical: Int, horizontal: Int): BufferedImage {
        repeat(vertical) {
            newVerticesGraph()
            addFirstRow()
            energyGraph.removeSeam(getSeam())
            queue.clear()
            println(it)
        }
        if (horizontal > 0) {
            energyGraph.transpose()
            repeat(horizontal) {
                newVerticesGraph()
                addFirstRow()
                energyGraph.removeSeam(getSeam())
                queue.clear()
                println(it)
            }
            energyGraph.transpose()
        }
        return energyGraph.toBufferedImage()
    }
}