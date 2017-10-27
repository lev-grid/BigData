package net.sytes.levizi.bd2017

import java.io.File

fun randByte(): Byte = (Math.round(255 * Math.random()) - 128).toByte()

fun genFile(fileName: String, sizeMb: Int) {
    var byteChunk: ByteArray
    val oneMb = 1024 * 1024
    val file = File("files\\$fileName.bin")
    file.writeBytes(kotlin.ByteArray(0))
    (1..sizeMb).forEach {
        byteChunk = kotlin.ByteArray(oneMb) { randByte() }
        file.appendBytes(byteChunk)
    }
}

fun main(args: Array<String>) {
    genFile("file", 2048)
}