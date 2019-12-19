package dk.w4.colorpredictor.preperation

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dk.w4.colorpredictor.knn.color.ModelData
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer
import java.util.stream.Collectors
import javax.imageio.ImageIO
import kotlin.collections.HashMap


class ModelGenerator() {

    fun generateModel(path: String, outputPath: String) {
        val data: ConcurrentHashMap<String, MutableList<Array<Int>>> = ConcurrentHashMap()
        val directories = Files.list(
            Paths.get(path)
        ).parallel()
            .filter { path: Path? ->
                Files.isDirectory(path)
            }
            .collect(Collectors.toList())

        directories.parallelStream().forEach { it: Path ->
            try {
                val files = Files.list(it).parallel()
                    .filter { path: Path? ->
                        Files.isRegularFile(path)
                    }.collect(
                        Collectors.toList()
                    )
                val name = it.fileName.toString()
                files.forEach(Consumer { filePath: Path ->
                    try {
                        val image = ImageIO.read(filePath.toFile())
                        val rgbs = ImageProvider.getImageAvg(image)
                        if (data[name] != null) {
                            data[name]?.add(rgbs)
                        } else {
                            data[name] = mutableListOf(rgbs)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (ex: Exception){
                        ex.printStackTrace()
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        saveModel(data.toMap(), outputPath)
    }

    fun generateTargets(path: String, outputPath: String) {
        val data: ConcurrentHashMap<String, MutableList<Array<Int>>> = ConcurrentHashMap()
        val directories = Files.list(
            Paths.get(path)
        ).parallel()
            .filter { path: Path? ->
                Files.isDirectory(path)
            }
            .collect(Collectors.toList())

        directories.parallelStream().forEach { it: Path ->
            try {
                val files = Files.list(it).parallel()
                    .filter { path: Path? ->
                        Files.isRegularFile(path)
                    }.collect(
                        Collectors.toList()
                    )
                files.forEach(Consumer { filePath: Path ->
                    try {
                        val name = "${it.fileName} : ${filePath.fileName}"

                        val image = ImageIO.read(filePath.toFile())
                        val rgbs = ImageProvider.getImageAvg(image)
                        if (data[name] != null) {
                            data[name]?.add(rgbs)
                        } else {
                            data[name] = mutableListOf(rgbs)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        saveModel(data.toMap(), outputPath)
    }

    private fun saveModel(model: Map<String, MutableList<Array<Int>>>, outputPath: String) {
        val json = Gson().toJson(model).toString()
        println(json)
        val file = File(outputPath)
        file.createNewFile()
        file.writeText(json)
    }

    fun loadModel(modelPath: String): List<ModelData> {
        val jsonString = modelPath
        val mapType: Type = object : TypeToken<HashMap<String, Array<Array<Int>>>>() {}.type
        val data: HashMap<String, Array<Array<Int>>> = Gson().fromJson(jsonString, mapType)
        val model2 =
            data.map { (key, value) -> ModelData(key, value.map { Triple(it[0], it[1], it[2]) }.toTypedArray()) }
        return model2
    }

    fun loadTestData(modelPath: String): List<ModelData> {
        val jsonString = File(modelPath).readText()
        val mapType: Type = object : TypeToken<HashMap<String, Array<Array<Int>>>>() {}.type
        val data: HashMap<String, Array<Array<Int>>> = Gson().fromJson(jsonString, mapType)
        val model2 =
            data.map { (key, value) -> ModelData(key, value.map { Triple(it[0], it[1], it[2]) }.toTypedArray()) }
        return model2
    }
}