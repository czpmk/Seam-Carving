class Element(energy: Double, val x: Int, val y: Int) {
    var checked = false
    var energySum: Double = energy
    var parent: Element? = null
}