import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.measureTime

const val inputPath = "/home/mc/IntelliJProjects/Seam-Carving/source_images/blue.png"
const val outputPath = "output_images/trees-out.png"
var inputFile = File(inputPath)
var outputFile = File(outputPath)

fun loadImage(): BufferedImage {
    return ImageIO.read(inputFile)
}

fun saveImage(image: BufferedImage) {
    ImageIO.write(image, "png", outputFile)
}

fun pipeline() {
    val inputImage: BufferedImage = loadImage()
    val seamFinder = SeamFinder(inputImage)
    val outputImage = seamFinder.removeLines(150, 50)
    saveImage(outputImage)
}

fun main() {
    val time = measureTime { pipeline() }
    println(time)
}