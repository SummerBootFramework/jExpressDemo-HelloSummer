package org.jexpress.demo.grpc;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.jexpress.demo.grpc.proto.generated1.Hello1Request;
import org.jexpress.demo.grpc.proto.generated1.Hello1Response;
import org.jexpress.demo.grpc.proto.generated1.Hello1ServiceGrpc;
import org.summerboot.jexpress.boot.annotation.GrpcService;
import org.summerboot.jexpress.nio.grpc.GRPCServiceCounter;
import org.summerboot.jexpress.nio.grpc.StatusReporter;
import org.summerboot.jexpress.security.auth.Authenticator;
import org.summerboot.jexpress.security.auth.Caller;

@GrpcService
public abstract class Hello1Service extends Hello1ServiceGrpc.Hello1ServiceImplBase implements StatusReporter {

    protected GRPCServiceCounter counter;

    @Override
    public void setCounter(GRPCServiceCounter counter) {
        this.counter = counter;
    }

    @Override
    public void hello1(Hello1Request request, StreamObserver<Hello1Response> responseObserver) {
        counter.incrementHit();

        Caller caller = Authenticator.GrpcCaller.get();
        String uid = Authenticator.GrpcCallerId.get();
        try {
            String greeting = hello(request.getFirstName(), request.getLastName());
            Hello1Response helloResponse = Hello1Response.newBuilder()
                    .setGreeting(greeting)
                    .build();
            responseObserver.onNext(helloResponse);
            responseObserver.onCompleted();
        } catch (StatusException ex) {
            responseObserver.onError(ex);
        } catch (Throwable ex) {
            Status status = Status.INTERNAL.withCause(ex).withDescription(ex.toString());
            responseObserver.onError(status.asException());
        }
        counter.incrementBiz();
        counter.incrementProcessed();
    }

    abstract protected String hello(String firstName, String lastName) throws Throwable;

}
