package org.jexpress.demo.grpc;

import io.grpc.stub.StreamObserver;
import org.jexpress.demo.grpc.proto.generated2.Hello2Request;
import org.jexpress.demo.grpc.proto.generated2.Hello2Response;
import org.jexpress.demo.grpc.proto.generated2.Hello2ServiceGrpc;
import org.summerboot.jexpress.boot.annotation.GrpcService;
import org.summerboot.jexpress.nio.grpc.Counter;
import org.summerboot.jexpress.nio.grpc.StatusReporter;

@GrpcService
public abstract class Hello2Service extends Hello2ServiceGrpc.Hello2ServiceImplBase implements StatusReporter {

    protected Counter counter;

    @Override
    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    @Override
    public void hello2(
            Hello2Request request,
            StreamObserver<Hello2Response> responseObserver) {
        counter.incrementHit();

        String greeting = hello(request.getFirstName(), request.getLastName());
        counter.incrementBiz();

        Hello2Response helloResponse = Hello2Response.newBuilder()
                .setGreeting(greeting)
                .build();

        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
        counter.incrementProcessed();
    }

    abstract protected String hello(String firstName, String lastName);
}
