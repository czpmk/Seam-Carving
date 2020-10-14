import java.awt.Color
import java.lang.Exception

class Pixel(intColor: Int) : Color(intColor) {
    var energy: Double = -255.0
}

class Coordinates(val x: Int, val y: Int)

class Vertex(private val graph: EnergyGraph, val x: Int, val y: Int) {
    val energy: Double
        get() {
            return graph[y][x].energy
        }
    var energySum: Double = Double.MAX_VALUE
    var predecessor: Vertex? = null
    override fun hashCode(): Int {
        return (y shl 20) + x
    }
    init {
        graph[y][x]
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vertex

        if (graph != other.graph) return false
        if (x != other.x) return false
        if (y != other.y) return false
        if (energySum != other.energySum) return false
        if (predecessor != other.predecessor) return false

        return true
    }
}

class Queue : MutableMap<Int, Vertex> by mutableMapOf() {
    fun add(vertex: Vertex) {
        this[vertex.hashCode()] = vertex
    }

    fun extractMin(): Vertex {
        val minEntry = minByOrNull { it.value.energySum }
        return this.remove(minEntry?.key) ?: throw Exception("Empty list can not return element")
    }
}