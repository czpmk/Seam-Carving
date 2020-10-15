import java.awt.image.BufferedImage
import java.lang.Exception

class SeamFinder(inputImage: BufferedImage) {
    private var graph: EnergyGraph = EnergyGraph(inputImage)
    private var visitedVertices = mutableMapOf<Int, Vertex>()
    private var queue = Queue()

    private fun getHashOf(x: Int, y: Int): Int {
        return (y shl 20) + x
    }

    private fun addFirstRow() {
        for (x in graph[0].indices) {
            val newElement = Vertex(graph, x, 0)
            newElement.energySum = newElement.energy
            visitedVertices[newElement.hashCode()] = newElement
            queue.add(newElement)
        }
    }

    private fun getChildren(vertex: Vertex): MutableList<Vertex> {
        val x = vertex.x
        val y = vertex.y
        val children = mutableListOf<Vertex>()
        var child: Vertex
        for (i in (-1)..1) {
            try {
                child = visitedVertices[getHashOf(x + i, y + 1)] ?: Vertex(graph, x + i, y + 1)
                children.add(child)
            } catch (e: IndexOutOfBoundsException) {
                continue
            }
        }
        return children
    }

    private fun enqueueChildren(predecessor: Vertex) {
        val children = getChildren(predecessor)
        for (child in children) {
            if (child.predecessor == null || child.energy + predecessor.energySum < child.energySum) {
                child.energySum = child.energy + predecessor.energySum
                child.predecessor = predecessor
            }
            queue.add(child)
            visitedVertices[child.hashCode()] = child
        }
    }

    private fun getLowestEnergySumVertex(): Vertex? {
        var minVertex: Vertex
        while (queue.isNotEmpty()) {
            minVertex = queue.extractMin()
            if (minVertex.y == graph.lastIndex) return minVertex
            enqueueChildren(minVertex)
        }
        return null
    }

    private fun getSeam(): MutableList<Coordinates> {
        var vertex = getLowestEnergySumVertex() ?: throw Exception("No lowest EnergySum found")
        val seam = mutableListOf<Coordinates>()
        while (true) {
            seam.add(Coordinates(vertex.x, vertex.y))
            vertex = vertex.predecessor ?: break
        }
        return seam
    }

    fun reduceSize(seamNumberV: Int, seamNumberH: Int): BufferedImage {
        repeat(seamNumberV) {
            addFirstRow()
            graph.removeSeam(getSeam())
            visitedVertices.clear()
            queue.clear()
        }
        if (seamNumberH != 0) {
            graph.transpose()
            repeat(seamNumberH) {
                addFirstRow()
                graph.removeSeam(getSeam())
                visitedVertices.clear()
                queue.clear()
            }
            graph.transpose()
        }
        return graph.toBufferedImage()
    }
}