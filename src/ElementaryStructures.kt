import java.awt.Color
import java.lang.Exception

class Pixel(intColor: Int) : Color(intColor) {
    var energy: Double = -255.0
}

class Coordinates(val x: Int, val y: Int)

class Element(val pixel: Pixel, val x: Int, val y: Int) {
    var energySum: Double = Double.MAX_VALUE
    var predecessor: Element? = null
}

class Queue : MutableMap<Int, Element> by mutableMapOf() {
    private val removedElements = mutableMapOf<Int?, Element?>()

    fun add(element: Element) {
        if (element.pixel.hashCode() !in removedElements) {
            this[element.pixel.hashCode()] = element
        }
    }

    fun extractMin(): Element {
        val minEntry = minByOrNull { it.value.energySum }
        removedElements[minEntry?.key] = minEntry?.value
        return this.remove(minEntry?.key) ?: throw Exception("Empty list can not return element")
    }
}