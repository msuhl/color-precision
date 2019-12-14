package dk.w4.colorpredictor.knn.chars

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer
import java.util.stream.Collectors

// Data laoder for loading text file of handwrinting into collection of SampleData object.
// Using map to group its by folder name.
class DataLoader(private val path: String) {
    val data: ConcurrentHashMap<String, MutableList<SampleData>> = ConcurrentHashMap()

    @Throws(IOException::class)
    private fun fromFileToData(name: String, path: Path): SampleData {
        val lines = Files.readAllLines(path)
        if (lines.size == 0) throw RuntimeException("Could not find data in training file! $path")
        val training = DoubleArray(lines.size * lines[0].length)
        val atomicInteger = AtomicInteger(0)
        lines.forEach(Consumer { it: String ->
            for (i in 0 until it.length) {
                training[atomicInteger.get() * it.length + i] = java.lang.Double.valueOf(it[i].toString())
            }
            atomicInteger.incrementAndGet()
        })
        return SampleData(name, training, path.fileName.toString())
    }

    @Throws(IOException::class)
    fun load() {
        val directories = Files.list(
            Paths.get(path)
        ).parallel()
            .filter { path: Path? ->
                Files.isDirectory(
                    path
                )
            }
            .collect(
                Collectors.toList()
            )
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
                        val sampleData = fromFileToData(name, filePath)
                        if (!data.containsKey(name)) {
                            val list: MutableList<SampleData> = ArrayList()
                            data[name] = list
                        }
                        data[name]!!.add(sampleData)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}