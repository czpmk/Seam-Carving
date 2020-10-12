import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

const val inputPath = "/home/mc/IntelliJProjects/Seam-Carving/source_images/pexels-bri-schneiter-346529.png"
const val outputPath = "output_images/pexels-bri-schneiter-346529-shrunk.png"
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
    saveImage(inputImage)
}

fun main() {
    pipeline()
}
