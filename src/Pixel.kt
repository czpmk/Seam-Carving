import java.awt.Color

class Pixel(intColor: Int) : Color(intColor) {
    var energy: Double = Double.MAX_VALUE
    var energySum: Double = Double.MAX_VALUE
    var predecessor: Coordinates? = null
}