package dk.w4.colorpredictor

import dk.w4.colorpredictor.knn.chars.DataLoader
import dk.w4.colorpredictor.knn.chars.KhmerNumericLabel
import dk.w4.colorpredictor.knn.chars.KnnClassifier
import dk.w4.colorpredictor.knn.chars.SampleData
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutionException


fun main(args: Array<String>) {
    val dataLoader = DataLoader("data/train")
    dataLoader.load()
    val dataSamples: ConcurrentHashMap<String, MutableList<SampleData>> =
        dataLoader.data // load as map in order to group it by folder
    val testLoader = DataLoader("data/test")
    testLoader.load()
    val testSamples: ConcurrentHashMap<String, MutableList<SampleData>> =
        testLoader.data //load as map in order to group it by folder

    val classifier = KnnClassifier(3, 10)
    val samples: List<SampleData> =
        dataSamples.values.parallelStream().collect(
            { ArrayList() },
            { obj: ArrayList<SampleData>, c: List<SampleData> -> obj.addAll(c) }
        ) { obj: ArrayList<SampleData>, c: ArrayList<SampleData> ->
            obj.addAll(c)
        }
    var errorCount = 0.0
    val startTestTime = System.currentTimeMillis()
    var totalTest = 0
    val resultText = StringBuilder()
    try {
        for ((key, value) in testSamples) {
            println("Begin to test for  folder [ ./test/$key ]  for Khmer character : " + KhmerNumericLabel.valueOf(key))
            for (instance in value) {
                val result: String = classifier.doClassify(instance, samples)
                val matched = if (result == key) " matched  \t" else " not matched "
                resultText.append(
                    "Result found " + matched + ": " + KhmerNumericLabel.valueOf(result) + " for " + KhmerNumericLabel.valueOf(
                        instance.identifier
                    ) + " , file ->./test/" + instance.fileName
                ).append("\n")
                if (result != key) {
                    errorCount++
                }
                totalTest++
            }
        }
        println("================================================================")
        println("Error rate : " + errorCount / totalTest)
        val duration = (System.currentTimeMillis() - startTestTime).toDouble() / 1000
        println("Total duration take : $duration (s)")
        println("================Summary result =================================")
        println(resultText.toString())
        println("=====================***========================================")
    } catch (e: ExecutionException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        classifier.close()
    }
}
