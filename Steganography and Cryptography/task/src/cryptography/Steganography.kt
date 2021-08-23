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

        println("Password:")
        val password = readLine()!!

        picture = Picture(inFile)
        try {
            // picture.changeLeastSignificantBit()
            if (writeMessageToImage(message, password)) {
                if (picture.save(outFile))
                    println("Message saved in $outFile image.")
            }
        } catch (e: UninitializedPropertyAccessException) {

        }
    }

    private fun imageCanHoldMessage(w: Int, h: Int, msgArray: ByteArray): Boolean {
        return w * h >= msgArray.size * 8
    }

    private fun writeMessageToImage(message: String, password: String): Boolean {
        val h = picture.height()
        val w = picture.width()

        // val encrKey = getEncryptionKey(message, password)
        val encryptedMessageArray = encryptMessage(message, password) +
                byteArrayOf(picture.byteNULL, picture.byteNULL, picture.byteETX)

        if (!imageCanHoldMessage(w, h, encryptedMessageArray)) {
            println("The input image is not large enough to hold this message.")
            return false
        }

        var row = 0
        var col = 0
        outOfBounds@ for (byte in encryptedMessageArray) {
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

    private fun getEncryptionKey(message: String, password: String): String {
        val msgLen = message.length
        val pswdLen = password.length

        val encrKey = password.repeat(msgLen / pswdLen + 1)

        return encrKey
    }

    private fun getEncryptionKey(message: ByteArray, password: ByteArray): ByteArray {
        val msgLen = message.size
        val pswdLen = password.size

        var encrKey: ByteArray = byteArrayOf()
        repeat(msgLen / pswdLen + 1) {
            encrKey += password
        }

        return encrKey
    }

    private fun encryptMessage(message: String, password: String): ByteArray {
        val messageArray = message.encodeToByteArray()
        val encrKey = getEncryptionKey(message, password)
        val encrKeyArray = encrKey.encodeToByteArray()

        val encryptedMessage =
            messageArray.zip(encrKeyArray) { m: Byte, k: Byte -> (m.toInt() xor k.toInt()).toByte() }
                .toByteArray()
        return encryptedMessage
    }

    fun decryptMessage(encryptedMessageArray: ByteArray, encryptionKey: ByteArray): String {

        val decryptedMessage =
            (encryptedMessageArray.zip(encryptionKey)).map { (m: Byte, k: Byte) ->
                (m.toInt() xor k.toInt()).toByte()
            }.toTypedArray().toByteArray().toString(Charsets.UTF_8)

        return decryptedMessage
    }

    private fun Byte.getBitAt(pos: Int): Int = (this.toInt() ushr pos) and 1

    fun doShow() {
        println("Input image file:")
        val inFile = readLine()!!

        println("Password:")
        val password = readLine()!!

        picture = Picture(inFile)
        val encryptedMessageArray = picture.readMessageFromImage()
        val encrKey = getEncryptionKey(encryptedMessageArray, password.encodeToByteArray())

        val message = decryptMessage(encryptedMessageArray, encrKey)
        println("Message:\n$message")
    }
}
