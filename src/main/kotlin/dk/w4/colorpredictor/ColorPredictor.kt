package dk.w4.colorpredictor

import dk.w4.colorpredictor.knn.color.KnnClassifier
import dk.w4.colorpredictor.knn.color.ModelData
import dk.w4.colorpredictor.preperation.ModelGenerator


fun main(args: Array<String>) {
    println("================================================================")
    println("Generate Model")
    val modelGenerator = ModelGenerator()
    modelGenerator.generateModel("data/image/train", "data/image/model.json")
    val model: List<ModelData> = modelGenerator.loadModel("data/image/model.json")
    println("================================================================")
    modelGenerator.generateTargets("data/image/test", "data/image/testModel.json")
    val testModel: List<ModelData> = modelGenerator.loadModel("data/image/testModel.json")
    println("================================================================")

    val classifier = KnnClassifier(3, 10)

    var errorCount = 0.0
    val startTestTime = System.currentTimeMillis()
    var totalTest = 0
    val resultText = StringBuilder()

    for (value in testModel) {
        val result: String = classifier.doClassify(value, model)
        println("${value.identifier} -> $result")
        if (result != value.identifier.split(" : ").first()){
            errorCount++
        }
        totalTest++
    }

    println("================================================================")
    println("Total test : $totalTest")
    println("Error rate : ${(errorCount / totalTest) * 100}%")
    val duration = (System.currentTimeMillis() - startTestTime).toDouble() / 1000
    println("Total duration take : $duration (s)")
    println("================Summary result =================================")
    println(resultText.toString())
    println("=====================***========================================")
    classifier.close()
}
