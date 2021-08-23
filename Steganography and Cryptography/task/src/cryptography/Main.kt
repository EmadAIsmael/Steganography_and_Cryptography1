package cryptography

import java.util.*
import kotlin.system.exitProcess

fun isValidInput(input: String): Boolean {
    return input.lowercase(Locale.getDefault()) in arrayOf("hide", "show", "exit")
}

fun displayMenu(): String {
    var input: String
    do {
        println("Task (hide, show, exit):")
        input = readLine()!!
        if (!isValidInput(input)) {
            println("Wrong task: [$input]")
        } else
            break
    } while (true)
    return input
}

fun doRequest(input: String): Boolean {
    val requests = mapOf(
        "hide" to {
            /*println("Hiding message in image.")*/
            Steganography().doHide()
        },
        "show" to {
            /*println("Obtaining message from image.")*/
            Steganography().doShow()
        },
        "exit" to { println("Bye!"); exitProcess(0) },
    )
    requests[input]?.invoke()
    return input != "exit"
}

fun main() {
    do {
        val input = displayMenu()
        if (!doRequest(input))
            break
    } while (true)
}
