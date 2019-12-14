package dk.w4.colorpredictor.knn.color

import kotlin.math.pow
import kotlin.math.sqrt

object DistanceCalculator {
    fun getDistanceBetweenPixels(data1: Triple<Int, Int, Int>, data2: Triple<Int, Int, Int>): Double {
        return sqrt(
            (data1.first.toDouble() - data2.first.toDouble()).pow(2.0)
                    + (data1.second.toDouble() - data2.second.toDouble()).pow(2.0)
                    + (data1.third.toDouble() - data2.third.toDouble()).pow(2.0)
        )
    }

    fun getDistance(
        data1: Array<Triple<Int, Int, Int>>,
        data2: Array<Triple<Int, Int, Int>>
    ): Double {
        var sum = 0.0
        data1.forEach { pixel ->
            data2.forEach { sum += getDistanceBetweenPixels(pixel, it) }
        }
        return sum / (data1.size * data2.size)
    }
}