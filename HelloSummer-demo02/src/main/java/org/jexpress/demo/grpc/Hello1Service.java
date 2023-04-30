package org.jexpress.demo.grpc;

import io.grpc.stub.StreamObserver;
import org.jexpress.demo.grpc.proto.generated1.Hello1Request;
import org.jexpress.demo.grpc.proto.generated1.Hello1Response;
import org.jexpress.demo.grpc.proto.generated1.Hello1ServiceGrpc;
import org.summerboot.jexpress.boot.annotation.GrpcService;
import org.summerboot.jexpress.nio.grpc.Counter;
import org.summerboot.jexpress.nio.grpc.StatusReporter;

@GrpcService
public abstract class Hello1Service extends Hello1ServiceGrpc.Hello1ServiceImplBase implements StatusReporter {

    protected Counter counter;

    @Override
    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void hello1(
            Hello1Request request,
            StreamObserver<Hello1Response> responseObserver) {
        counter.incrementHit();

        String greeting = hello(request.getFirstName(), request.getLastName());
        counter.incrementBiz();

        Hello1Response helloResponse = Hello1Response.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
        counter.incrementProcessed();
    }

    abstract protected String hello(String firstName, String lastName);

}
