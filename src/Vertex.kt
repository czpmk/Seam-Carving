class Vertex(private val graph: EnergyGraph, val x: Int, val y: Int, var energySum: Double = Double.MAX_VALUE) {
    val energy: Double
        get() {
            return graph[y][x].energy
        }
    var predecessor: Vertex? = null
}