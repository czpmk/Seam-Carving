import java.awt.image.BufferedImage
import java.io.File
import java.lang.Exception
import javax.imageio.ImageIO
import kotlin.time.measureTime

const val inputPath = "/home/mc/IntelliJProjects/Seam-Carving/source_images/a.png"
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
    var outputImage: BufferedImage?

    val energyGraph = EnergyGraph(inputImage, "sobel")
    energyGraph.blurGreyValues()
    energyGraph.updateEnergyAll()

    val seamFinder = SeamFinder(energyGraph)
    val time = measureTime {
        outputImage = seamFinder.removeLines(150, 50)
    }
    println("calculations time: $time")
    saveImage(outputImage ?: throw Exception("null image"))
}

fun main() {
    val time = measureTime { pipeline() }
    println("total time: $time")
}