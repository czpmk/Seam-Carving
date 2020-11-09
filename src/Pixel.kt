import java.awt.Color

private fun Int.toGray(): Int {
    val red: Int = this shr (16) and 255
    val green: Int = this shr (8) and 255
    val blue: Int = this and 255
    return (red * 0.2989 + green * 0.587 + blue * 0.114).toInt()
}

class Pixel(intColor: Int) : Color(intColor) {
    var gray: Int = intColor.toGray()
    var energy: Double = Double.MAX_VALUE
    var energySum: Double = Double.MAX_VALUE
    var predecessor: Coordinates? = null

    fun grayColor(): Int {
        return (gray shl 16) + (gray shl 8) + gray
    }
}