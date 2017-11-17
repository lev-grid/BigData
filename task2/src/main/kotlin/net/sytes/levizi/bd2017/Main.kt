package net.sytes.levizi.bd2017

import io.grpc.ManagedChannelBuilder
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.util.*

fun factor(n: Long): List<Long> {
    var current = n
    val result = ArrayList<Long>()
    var d: Long = 2
    while (d * d <= current) {
        if (current % d == 0.toLong()) {
            result.add(d)
            current /= d
        } else d += 1
    }
    if (current > 1)
        result.add(current)
    return result
}

class SampleService(): SampleServiceGrpc.SampleServiceImplBase() {

    override fun getFactorization(request: FactorRequest, responseObserver: StreamObserver<FactorResponse>) {
        val result = factor(request.num).joinToString(" ")

        println(result)

        val response = FactorResponse.newBuilder()
                .setResult(result).build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }
}

fun main(args: Array<String>) {
    val server = ServerBuilder
            .forPort(5757)
            .addService(SampleService())
            .build()
    server.start()

    println("Simple grpc service started")

    server.awaitTermination()

    println("Simple grpc service stopped")
}