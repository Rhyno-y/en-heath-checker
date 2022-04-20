package io.krosslab.enhealthchecker

import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import io.krosslab.grpc.model.HealthCheckServiceGrpc
import io.krosslab.grpc.model.Response
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class GrpcController : HealthCheckServiceGrpc.HealthCheckServiceImplBase() {
    override fun isAlive(request: Empty?, responseObserver: StreamObserver<Response>?) {
        responseObserver!!.onNext(
            Response.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setMessage("alive")
                .build()
        )
        responseObserver.onCompleted()
    }
}