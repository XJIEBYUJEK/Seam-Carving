

fun main() {
    val a = mutableMapOf<String, Int>()
    val w = 10
    val h = 5
    var k = 0
    for (i in 0 until w){
        for(j in 0 until h){
            a["(${k % w}, ${k / w})"] = k
            k++
        }
    }
    println(a)
    for (j in 1 downTo -5){
        println(j)
    }

}