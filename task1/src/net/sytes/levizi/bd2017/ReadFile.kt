package net.sytes.levizi.bd2017

import java.io.FileInputStream
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class Runa(private val buffer: MappedByteBuffer) : Runnable {
    private var min = Long.MAX_VALUE
    private var max = Long.MIN_VALUE
    private var sum = BigInteger("0")
    override fun run() {
        while (buffer.position() < buffer.limit()) {
            val result = buffer.int.toLong() - Int.MIN_VALUE
            if (result < min) min = result
            if (result > max) max = result
            sum += BigInteger.valueOf(result)
        }
    }
    fun getMin(): Long = min
    fun getMax(): Long = max
    fun getSum(): BigInteger = sum
}

fun noThreads(fileName: String) {
    val startTime = System.currentTimeMillis()
    val channel = FileInputStream(fileName).channel
    var min = Long.MAX_VALUE
    var max = Long.MIN_VALUE
    var sum = BigInteger("0")
    val fileSize = channel.size()
    while (channel.position() < fileSize) {
        val byteBuffer = ByteBuffer.wrap(ByteArray(4096))
        channel.read(byteBuffer)
        byteBuffer.position(0)
        var position = byteBuffer.position()
        while (position < byteBuffer.limit() && position < fileSize) {
            val result = byteBuffer.int.toLong() - Int.MIN_VALUE
            if (result < min) min = result
            if (result > max) max = result
            sum += BigInteger.valueOf(result)
            position = byteBuffer.position()
        }
    }
    println("\nnoThreads results:")
    println("Min  : $min")
    println("Max  : $max")
    println("Sum  : $sum")
    println("Time : ${(System.currentTimeMillis() - startTime)/1000.0} sec")
}

fun yesThreads(fileName: String, numThreads: Int) {
    val startTime = System.currentTimeMillis()
    val channel = FileInputStream(fileName).channel
    val size = channel.size() / numThreads
    val mMod = FileChannel.MapMode.READ_ONLY
    val runArray = Array(numThreads) { Runa(channel.map(mMod, it * size, size)) }
    val threadArray = Array(numThreads) { Thread(runArray[it]) }
    threadArray.forEach { it.start() }
    threadArray.forEach { it.join() }
    println("\nyesThreads results:")
    println("Min  : ${runArray.map { it.getMin() }.min()}")
    println("Max  : ${runArray.map { it.getMax() }.max()}")
    println("Sum  : ${runArray.map { it.getSum() }.reduce { a, b -> a + b } }")
    println("Time : ${(System.currentTimeMillis() - startTime) / 1000.0} sec")
}

fun main (args: Array<String>) {
    val fileName = "files\\file.bin"
    val numThreads = 8
    noThreads(fileName)
    yesThreads(fileName, numThreads)
}