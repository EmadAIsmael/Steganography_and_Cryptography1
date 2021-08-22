package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class Picture {

    private var width: Int = 0
    private var height: Int = 0
    private lateinit var img: BufferedImage

    constructor(width: Int, height: Int) {
        // create a blank image with width by height
        this.img = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        this.width = this.img.width
        this.height = this.img.height
    }

    constructor(fileName: String) {
        open(fileName)
    }

    fun open(fileName: String) {
        // read image from file
        try {
            val imgFile = File(fileName)
            this.img = ImageIO.read(imgFile)
            this.width = this.img.width
            this.height = this.img.height
        } catch (e: Exception) {
            println("Can't read input file!")
        }
    }

    fun save(fileName: String): Boolean {
        val f = File(fileName)
        var result = false
        try {
            if (!ImageIO.write(this.img, "PNG", f)) {
                throw RunTimeException("Unexpected error writing image")
            } else
                result = true
        } catch (e: IOException) {
            println(e.message)
        } catch (e: RunTimeException) {
            println(e.message)
        }
        return result
    }

    fun Int.setLeastSignificantBitToOne() = this or 1

    fun changeLeastSignificantBit() {
        for (col in 0 until width) {
            for (row in 0 until height) {
                val color = Color(img.getRGB(col, row))
                val r = color.red
                val g = color.green
                val b = color.blue

                val newColor = Color(
                    r.setLeastSignificantBitToOne(),
                    g.setLeastSignificantBitToOne(),
                    b.setLeastSignificantBitToOne()
                )
                img.setRGB(col, row, newColor.rgb)
            }

        }
    }

    inner class FileNotFoundException(message: String) : Throwable()

    inner class RunTimeException(message: String) : Throwable()

    fun width() = this.width
    fun height() = this.height

    fun getRGB(x: Int, y: Int): IntArray {
        // returns an Int representing color value at x, y.
        return unpackColor(this.img.getRGB(x, y))
    }

    fun setRGB(x: Int, y: Int, color: Int) {
        // sets color value at x, y to value of parameter color.
        this.img.setRGB(x, y, color)
    }

    fun unpackColor(color: Int): IntArray {
        // returns 4 (32 bit Int) numbers
        // representing the components of an RGB color;
        // alpha, red, green, and blue
        return intArrayOf(
            (color shr 24) and 0xff,            // a
            (color shr 16) and 0xff,            // r
            (color shr 8) and 0xff,             // g
            (color shr 0) and 0xff              // b
        )
    }

    // packs 4 Ints into one Int value representing
    // an RGB color.
    fun packColor(a: Int, r: Int, g: Int, b: Int): Int =
        ((a and 0xff) shl 24) or
                ((r and 0xff) shl 16) or
                ((g and 0xff) shl 8) or
                ((b and 0xff) shl 0)
}
