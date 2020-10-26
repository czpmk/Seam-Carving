import java.lang.Exception

class Queue : MutableList<Vertex> by mutableListOf() {
    private var minVertex: Vertex? = null
    fun put(element: Vertex) {
        if (this.none { it === element }) this.add(element)
    }

    fun extractMin(): Vertex {
        val minVertex = minByOrNull { it.energySum }
        this.remove(minVertex)
        return minVertex ?: throw Exception("Can not extract element of empty list")
    }
}