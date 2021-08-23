package cryptography


class Steganography {
    private lateinit var picture: Picture

    fun doHide() {
        println("Input image file:")
        val inFile = readLine()!!

        println("Output image file:")
        val outFile = readLine()!!

        println("Message to hide:")
        val message = readLine()!!

        picture = Picture(inFile)
        try {
            // picture.changeLeastSignificantBit()
            if (writeMessageToImage(message)) {
                if (picture.save(outFile))
                    println("Message saved in $outFile image.")
            }
        } catch (e: UninitializedPropertyAccessException) {

        }
    }

    private fun imageCanHoldMessage(w: Int, h: Int, msgArray: ByteArray): Boolean {
        return w * h >= msgArray.size * 8
    }

    private fun writeMessageToImage(message: String): Boolean {
        val h = picture.height()
        val w = picture.width()
        val messageArray =
            message.encodeToByteArray() + byteArrayOf(
                picture.byteNULL,
                picture.byteNULL,
                picture.byteETX
            )

        if (!imageCanHoldMessage(w, h, messageArray)) {
            println("The input image is not large enough to hold this message.")
            return false
        }

        var row = 0
        var col = 0
        outOfBounds@ for (byte in messageArray) {
            // for (bitPos in 0..7) {
            for (bitPos in 7 downTo 0) {
                if (col >= w) {
                    row++
                    if (row >= h) break@outOfBounds
                    col = 0
                }
                // println("byte: $byte - bit#$bit - row#$row - col#$col \n"
                val currentMessageBit = byte.getBitAt(bitPos)
                picture.changeColorAt(col, row, currentMessageBit)

                col++
            }
        }
        return true
    }

    fun Byte.getBitAt(pos: Int): Int = (this.toInt() ushr pos) and 1

    fun doShow() {
        println("Input image file:")
        val inFile = readLine()!!

        picture = Picture(inFile)
        val message = picture.readMessageFromImage()
        println("Message:\n$message")
    }
}
