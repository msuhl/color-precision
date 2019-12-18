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

    val wrong = HashMap<String, Int>()
    val totalTestMap: HashMap<String, Int> = HashMap()

    model.map { wrong[it.identifier] = 0; totalTestMap[it.identifier] = 0; }

    for (value in testModel) {
        val result: String = classifier.doClassify(value, model)
        println("${value.identifier} -> $result")
        val key = value.identifier.split(" : ").first()
        if (result != key) {
            errorCount++
            wrong[key] = wrong[key]?.plus(1) ?: 0
        }
        totalTestMap[key] = totalTestMap[key]?.plus(1) ?: 0
        totalTest++
    }

    println("================================================================")
    println("Total test : $totalTest")
    println("Error rate : ${(errorCount / totalTest) * 100}%")
    val duration = (System.currentTimeMillis() - startTestTime).toDouble() / 1000
    println("Total duration take : $duration (s)")
    println("================Summary result =================================")
    println("test: wrong / total")
    totalTestMap.keys.forEach { println("$it: ${wrong[it] ?: 0} / ${totalTestMap[it]}") }
    println("=====================***========================================")
    classifier.close()
}
