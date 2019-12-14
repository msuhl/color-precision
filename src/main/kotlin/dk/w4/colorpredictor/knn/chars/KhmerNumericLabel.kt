package dk.w4.colorpredictor.knn

import java.util.concurrent.ConcurrentHashMap

// Util class to map Khmer numeral to Arabic number.
object KhmerNumericLabel {
    private val labelMap: ConcurrentHashMap<String, String> = object : ConcurrentHashMap<String, String>() {
        init {
            put("0", "០")
            put("1", "១")
            put("2", "២")
            put("3", "៣")
            put("4", "៤")
            put("5", "៥")
            put("6", "៦")
            put("7", "៧")
            put("8", "៨")
            put("9", "៩")
        }
    }

    fun valueOf(numeric: String): String? {
        require(labelMap.containsKey(numeric)) { "$numeric does not exist in mapping " }
        return labelMap[numeric]
    }
}
