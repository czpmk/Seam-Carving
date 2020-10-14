import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

const val inputPath = "/home/mc/IntelliJProjects/Seam-Carving/source_images/small.png"
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
    val energyGraph = EnergyGraph(inputImage)
    val seamFinder = SeamFinder(energyGraph)
    energyGraph.markSeam(seamFinder.getSeam())
    energyGraph.toBufferedImage()
    val outputImage = energyGraph.toBufferedImage()
    saveImage(outputImage)
}

fun main() {
    pipeline()
}
