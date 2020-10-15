import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

const val inputPath = "/home/mc/IntelliJProjects/Seam-Carving/source_images/trees.png"
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
    val outputImage = seamFinder.reduceSize(1, 0)
    saveImage(outputImage)
}

fun main() {
    pipeline()
}
