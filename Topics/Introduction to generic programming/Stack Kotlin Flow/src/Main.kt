class MyStack<T>(data: List<T>) {
    val items = data.toMutableList()

    // implement methods
    fun push(data: T) = items.add(data)

    fun pop(): T = items.removeAt(items.lastIndex)
}