package dk.w4.colorpredictor

import java.io.File
import javax.imageio.ImageIO

fun main() {
    val predector = ColorPredictor()

    val url1 = "https://sports-data.api.tv2.dk/sports-data-backend/images/participants/8007?type=kit&name=home"
    val url2 = "https://sports-data.api.tv2.dk/sports-data-backend/images/participants/8008?type=kit&name=home"
    val url3 = "https://sports-data.api.tv2.dk/sports-data-backend/images/participants/8008?type=kit&name=away"

    val target = predector.getTargetModelData("my-image", url3)
    val model = predector.loadModel("data/image/model.json")

    val result = predector.PredictColor(target, model, 3)
    println(result)
}