package dk.w4.colorpredictor

import dk.w4.colorpredictor.knn.color.KnnClassifier
import dk.w4.colorpredictor.knn.color.ModelData
import dk.w4.colorpredictor.preperation.ImageProvider
import dk.w4.colorpredictor.preperation.ModelGenerator
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

class ColorPredictor {
    fun PredictColor(target: ModelData, model: List<ModelData>, k: Int): String {
        val classifier = KnnClassifier(k, 10)
        val result: String = classifier.doClassify(target, model)
        classifier.close()
        return result
    }

    fun loadModel(modelPath: String): List<ModelData> {
        val modelGenerator = ModelGenerator()
        return modelGenerator.loadModel(modelPath)
    }

    fun getTargetModelData(identifier: String, image: BufferedImage): ModelData {
        val rgbs = ImageProvider.getImageAvg(image)

        return ModelData(identifier, arrayOf(Triple(rgbs[0], rgbs[1], rgbs[2])))
    }

    fun getTargetModelData(identifier: String, url: String): ModelData {
        val image = ImageIO.read(URL(url))
        val rgbs = ImageProvider.getImageAvg(image)

        return ModelData(identifier, arrayOf(Triple(rgbs[0], rgbs[1], rgbs[2])))
    }
}