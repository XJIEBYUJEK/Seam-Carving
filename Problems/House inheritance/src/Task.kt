
open class House(val rooms: Int, val price: Int, open val coefficient: Double)
class Cabin(rooms: Int, price: Int, override val coefficient: Double = 1.0): House(rooms, price, coefficient)
class Bungalow(rooms: Int, price: Int, override val coefficient: Double = 1.2): House(rooms, price, coefficient)
class Cottage(rooms: Int, price: Int, override val coefficient: Double = 1.25 ): House(rooms, price, coefficient)
class Mansion(rooms: Int, price: Int, override val coefficient: Double = 1.4): House(rooms, price, coefficient)
class Palace(rooms: Int, price: Int, override val coefficient: Double = 1.6): House(rooms, price, coefficient)

fun totalPrice(house: House): Int{
    return (house.price * house.coefficient).toInt()
}
fun create(rooms: Int, Price: Int): House{
    val price = when{
        Price < 0 -> 0
        Price > 1000000 -> 1000000
        else -> Price
    }
    return when{
        rooms <= 1 -> Cabin(rooms, price)
        (rooms in 2..3) -> Bungalow(rooms, price)
        (rooms == 4) ->  Cottage(rooms, price)
        (rooms in 5..7) ->  Mansion(rooms, price)
        else -> Palace(rooms, price)
    }
}

fun main() {
    val rooms = readLine()!!.toInt()
    val price = readLine()!!.toInt()
    val house = create(rooms, price)
    println(totalPrice(house))
}

