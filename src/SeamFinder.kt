class SeamFinder(var graph: EnergyGraph) {
    private var queue = Queue()

    init {
        addFirstRow()
    }

    private fun addFirstRow() {
        var newElement: Element
        for (i in graph[0].indices) {
            newElement = Element(graph[0][i], i, 0)
            newElement.energySum = newElement.pixel.energy
            queue.add(newElement)
        }
    }
}