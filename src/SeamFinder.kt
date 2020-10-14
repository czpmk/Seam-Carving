import java.lang.Exception

class SeamFinder(private var graph: EnergyGraph) {
    private var queue = Queue()

    private fun addFirstRow() {
        var newElement: Element
        for (i in graph[0].indices) {
            newElement = Element(graph[0][i], i, 0)
            newElement.energySum = newElement.pixel.energy
            queue.add(newElement)
        }
    }

    private fun getChildren(predecessor: Element): MutableList<Element> {
        val x = predecessor.x
        val y = predecessor.y
        val children = mutableListOf<Element>()
        var child: Element
        for (i in (x - 1)..(x + 1)) {
            try {
                child = Element(graph[y + 1][x + i], x + i, y + 1)
            } catch (e: IndexOutOfBoundsException) {
                continue
            }
            children.add(child)
        }
        return children
    }

    private fun addChildrenToQueue(predecessor: Element) {
        val children = getChildren(predecessor)
        for (child in children) {
            if (child.predecessor == null || predecessor.energySum + child.pixel.energy < child.energySum) {
                child.energySum = predecessor.energySum + child.pixel.energy
                child.predecessor = predecessor
                queue.add(child)
            }
        }
    }

    private fun getSeamLastElement(): Element {
        var minElement: Element
        while (queue.isNotEmpty()) {
            minElement = queue.extractMin()
            if (minElement.y == graph.lastIndex) {
                return minElement
            }
            addChildrenToQueue(minElement)
        }
        throw Exception("No seam element found")
    }

    fun getSeam(): MutableList<Coordinates> {
        addFirstRow()
        val seamList = mutableListOf<Coordinates>()
        var lastElement: Element = getSeamLastElement()
        while (true) {
            seamList.add(Coordinates(lastElement.x, lastElement.y))
            println(seamList.last().x.toString() + " " + seamList.last().y)
            lastElement = lastElement.predecessor ?: break
        }
        return seamList
    }
}