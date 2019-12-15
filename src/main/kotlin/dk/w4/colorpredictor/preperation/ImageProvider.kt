package dk.w4.colorpredictor.preperation

import java.awt.Image
import java.awt.image.BufferedImage

object ImageProvider {
    fun getPixelRGB(pixel: Int): Triple<Int, Int, Int> {
        //val alpha = pixel shr 24 and 0xff
        val red = pixel shr 16 and 0xff
        val green = pixel shr 8 and 0xff
        val blue = pixel and 0xff
        return Triple(red, green, blue)
    }

    fun marchThroughImage(image: BufferedImage): Array<Array<Triple<Int, Int, Int>>> {
        val w = image.width
        val h = image.height
        val pixelRGB = arrayOf<Array<Triple<Int, Int, Int>>>()
        for (i in 0 until h) {
            var row = arrayOf<Triple<Int, Int, Int>>()
            for (j in 0 until w) {
                val pixel = image.getRGB(j, i)
                row += getPixelRGB(pixel)
            }
        }
        return pixelRGB
    }

    fun getImageAvg(image: BufferedImage): Array<Int> {
        val w = image.width
        val h = image.height

        var r = 0.0
        var g = 0.0
        var b = 0.0

        for (i in 0 until h) {
            for (j in 0 until w) {
                val pixel = image.getRGB(j, i)
                val rgb = getPixelRGB(pixel)
                r += rgb.first
                g += rgb.second
                b += rgb.third
            }
        }
        val red = (r / (w * h)).toInt()
        val green = (g / (w * h)).toInt()
        val blue = (b / (w * h)).toInt()
        return arrayOf(red, green, blue)
    }

    fun resize(img: BufferedImage, height: Int = 300, width: Int = 300): BufferedImage {
        val tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        val resized = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2d = resized.createGraphics()
        g2d.drawImage(tmp, 0, 0, null)
        g2d.dispose()
        return resized
    }
}