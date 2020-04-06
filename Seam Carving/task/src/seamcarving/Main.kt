
package seamcarving

import java.awt.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.min
import kotlin.math.sqrt
fun main(args: Array<String>) {

    
    val inputFile = args[args.indexOf("-in") + 1]
    val outputFile = args[args.indexOf("-out") + 1]

    println("$inputFile $outputFile ")

    val path = "C:\\Users\\igors\\IdeaProjects\\Seam Carving\\Seam Carving\\task"
    val bufferedImage = ImageIO.read(File("$path/$inputFile"))
    val w = bufferedImage.width
    val h = bufferedImage.height
    val bos = ByteArrayOutputStream()
    val energy = {a: Double, b: Double -> sqrt(a + b)}
    val list = {a: List<Int>, b: List<Int> -> listOf(a[0]-b[0], a[1]-b[1], a[2]-b[2]) }
    //val toHex = {a: Int -> (a shl 16 and 0xffffff) + (a shl 8 and 0xffffff) + (a and 0xffffff)}
    fun takeRGB (x: Int, y: Int): List<Int>{
        val rgb = bufferedImage.getRGB(x, y)
        val red = rgb shr 16 and 0xff
        val green = rgb shr 8 and 0xff
        val blue = rgb and 0xff
        return listOf(red, green, blue)
    }
    fun xGradient(X: Int, Y: Int): Double{
        var x: Int
        var y: Int
        when(X){
            0 -> {
                x = X + 1
                y = Y
            }
            w - 1 -> {
                x = X - 1
                y = Y
            }
            else -> {
                x = X
                y = Y
            }
        }
        val firstPix = takeRGB(x - 1, y)
        val secondPix = takeRGB(x + 1, y)
        return (list(firstPix, secondPix).map {it * it}.sum().toDouble())
    }
    fun yGradient(X: Int, Y: Int): Double{
        var x: Int
        var y: Int
        when(Y){
            0 -> {
                x = X
                y = Y + 1
            }
            h - 1 -> {
                x = X
                y = Y - 1
            }
            else -> {
                x = X
                y = Y
            }
        }
        val firstPix = takeRGB(x, y - 1)
        val secondPix = takeRGB(x, y + 1)
        return (list(firstPix, secondPix).map {it * it}.sum().toDouble())
    }

    //val energyArray = mutableListOf<Double>()
    val energyMap = mutableMapOf<String, Double>()
    val edgeMap = mutableMapOf<String, Double>()

    fun verticalMin(i: Int, j:Int) = when(i){
        0 -> min(edgeMap ["($i, ${j-1})"]!!, edgeMap ["(${i+1}, ${j-1})"]!!) + energyMap["($i, $j)"]!!
        w - 1 -> min(edgeMap ["($i, ${j-1})"]!!, edgeMap ["(${i-1}, ${j-1})"]!!) + energyMap["($i, $j)"]!!
        else -> min(min(edgeMap ["($i, ${j-1})"]!!, edgeMap ["(${i-1}, ${j-1})"]!!), edgeMap ["(${i+1}, ${j-1})"]!! ) + energyMap["($i, $j)"]!!
    }



    for (j in 0 until h){
        for(i in 0 until w){
            energyMap["($i, $j)"] = (energy(xGradient(i, j), yGradient(i, j)))
            edgeMap["($i, $j)"] = if (i == 0) energyMap["($i, $j)"]!! else  Double.MAX_VALUE
            if (j > 0){
                edgeMap ["($i, $j)"] = verticalMin(i, j)
            }
        }
    }
    var minSeam = Double.MAX_VALUE
    var index = w
    for (i in 0 until w){
        if (edgeMap["($i, ${h-1})"]!! < minSeam){
            minSeam = edgeMap["($i, ${h-1})"]!!
            index = i
        }
    }
    bufferedImage.setRGB(index, h-1, 0xff0000 )
    for (j in h-2 downTo 0){
        when(index){
            0 -> {
                if (edgeMap["($index, $j)"]!! < edgeMap["(${index+1}, $j)"]!!){
                    bufferedImage.setRGB(index, j, 0xff0000 )
                }
                else{
                    index += 1
                    bufferedImage.setRGB(index, j, 0xff0000 )
                }
            }
            w-1 -> {
                if (edgeMap["($index, $j)"]!! < edgeMap["(${index-1}, $j)"]!!){
                    bufferedImage.setRGB(index, j, 0xff0000 )
                }
                else{
                    index -= 1
                    bufferedImage.setRGB(index, j, 0xff0000 )
                }
            }
            else -> {
                if (edgeMap["($index, $j)"]!! < edgeMap["(${index+1}, $j)"]!!){
                    if (edgeMap["($index, $j)"]!! < edgeMap["(${index-1}, $j)"]!!){
                        bufferedImage.setRGB(index, j, 0xff0000 )
                    }
                    else{
                        index -= 1
                        bufferedImage.setRGB(index, j, 0xff0000 )
                    }
                }
                else{
                    if (edgeMap["(${index+1}, $j)"]!! < edgeMap["(${index-1}, $j)"]!!){
                        index += 1
                        bufferedImage.setRGB(index, j, 0xff0000 )
                    }
                    else{
                        index -= 1
                        bufferedImage.setRGB(index, j, 0xff0000 )
                    }
                }

            }
        }
    }
    /*//val maxEnergy = energyArray.max()!!
    //var intensity: Int
    var k = 0
    for (i in 0 until w){
        for(j in 0 until h){
            intensity = (255.0 * energyArray[k] / maxEnergy).toInt()
            k++
            bufferedImage.setRGB(i, j, toHex(intensity) )
        }
    }*/

    ImageIO.write(bufferedImage, "png", bos)
    val data = bos.toByteArray()
    val bis = ByteArrayInputStream(data)
    val bImage2 = ImageIO.read(bis)
    ImageIO.write(bImage2, "png", File("$path/$outputFile"))
    println("image created")
}





