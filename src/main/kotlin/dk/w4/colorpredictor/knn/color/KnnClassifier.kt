package dk.w4.colorpredictor.knn.color

import java.util.*
import java.util.concurrent.*


class KnnClassifier(private val k: Int, numberOfThread: Int) {

    private var threadPoolExecutor: ThreadPoolExecutor = Executors.newFixedThreadPool(numberOfThread) as ThreadPoolExecutor

    private fun reversePriorityQueue(init: Int): PriorityBlockingQueue<SampleDistanceValue> {
        return PriorityBlockingQueue<SampleDistanceValue>(
            init,
            Comparator<SampleDistanceValue> { o1: SampleDistanceValue, o2: SampleDistanceValue ->
                if (o1.value < o2.value) {
                    -1
                } else if (o1.value === o2.value) {
                    0
                } else {
                    1
                }
            }
        )
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun doClassify(target: SampleData, sampleData: List<SampleData>): String {

        val distanceValues: MutableList<CompletableFuture<SampleDistanceValue>> = ArrayList()

        sampleData.forEach { item ->
            val completableFuture: CompletableFuture<SampleDistanceValue> = CompletableFuture()
            threadPoolExecutor.submit {
                val distance: Double = DistanceCalculator.getDistance(target.data, item.data)
                //System.out.println("current thread name : "+Thread.currentThread().getName() +" , "+target.getFileName() +" vs "+sampleData.getFileName() + " value : "+distance);
                completableFuture.complete(SampleDistanceValue(item.identifier, distance))
            }
            distanceValues.add(completableFuture)
        }
        val combine = CompletableFuture.allOf(*distanceValues.toTypedArray())
        combine.get() //wait for all tasks to complete

        val reverseDistancePriorityQueue: PriorityBlockingQueue<SampleDistanceValue> = reversePriorityQueue(10)
        distanceValues.parallelStream().forEach { reverseDistancePriorityQueue.add(it.get()) }

        val classification: MutableMap<String, Int> = HashMap()

        for (i in 0 until k) {
            val value: Optional<SampleDistanceValue> = Optional.of<SampleDistanceValue>(reverseDistancePriorityQueue.poll())
            value.ifPresent { it: SampleDistanceValue ->
                classification.merge(it.identifier, 1) { v1: Int, v2: Int -> v1 + v2 }
            }
        }

        val entry =
            Collections.max<Map.Entry<String, Int>>(
                classification.entries,
                java.util.Map.Entry.comparingByValue()
            )
        return entry.key
    }

    fun close() {
        threadPoolExecutor.shutdown()
    }

}
