package cryptography

class Steganography {
    private lateinit var picture: Picture

    fun doHide() {
        println("Input image file:")
        val inFile = readLine()!!

        println("Output image file:")
        val outFile = readLine()!!

        println("Input Image: $inFile")
        println("Output Image: $outFile")

        picture = Picture(inFile)
        try {
            picture.changeLeastSignificantBit()
            if (picture.save(outFile))
                println("Image $outFile is saved.")
        } catch (e: UninitializedPropertyAccessException) {

        }
    }
}