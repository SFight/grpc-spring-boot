package com.vtears.grpcspring.service.proto;

import com.vtears.grpcspring.annotations.GrpcService;
import io.grpc.stub.StreamObserver;

/**
 * @author vtears
 * @date 2019/3/28 4:42 PM
 * @description grpc示例
 */
@GrpcService
public class HomeProtoService extends GreeterGrpc.GreeterImplBase {

    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
//        super.sayHello(request, responseObserver);
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
