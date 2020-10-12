class Element(val pixel: Pixel, val x: Int, val y: Int) {
    var checked = false
    var energySum: Double = Double.MAX_VALUE
    var parent: Element? = null
}