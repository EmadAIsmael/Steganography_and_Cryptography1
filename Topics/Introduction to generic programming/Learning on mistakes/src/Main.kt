data class Container<T>(var value: T)

fun main() {
    val container = Container("Good job!")
    println(container.value)
}