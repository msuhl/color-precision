package dk.w4.colorpredictor

import java.io.File
import javax.imageio.ImageIO

fun main() {
    val predector = ColorPredictor()

    val file = File("/Users/silverbaq/Github/color-predictor/data/image/test/blue/8007-home.jpg")
    val image = ImageIO.read(file)
    val target = predector.getTargetModelData("my-image", image)
    val model = predector.loadModel("/Users/silverbaq/Github/color-predictor/data/image/model.json")

    val result = predector.PredictColor(target, model, 3)
    println(result) // blue
}