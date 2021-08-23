class Box<T>(val furniture: T, val volume: Int) {
    fun getBoxVolume(): Int = volume
    fun getFurnitureFromBox(): T = furniture
}

// Don't change classes below
class Fridge
class Armchair