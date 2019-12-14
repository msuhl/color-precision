package dk.w4.colorpredictor.knn.chars

// Distance calculator using Euclidean fomula
object DistanceCalculator {
    fun getDistance(data1: DoubleArray, data2: DoubleArray): Double {
        require(data1.size == data2.size) { " Data length mismatched ! " }
        var sum = 0.0
        for (i in data1.indices) {
            sum += Math.pow(data1[i] - data2[i], 2.0)
        }
        return Math.sqrt(sum)
    }
}