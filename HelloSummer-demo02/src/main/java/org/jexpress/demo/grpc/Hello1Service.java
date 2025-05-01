package org.jexpress.demo.grpc;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import org.jexpress.demo.grpc.proto.generated1.Hello1Request;
import org.jexpress.demo.grpc.proto.generated1.Hello1Response;
import org.jexpress.demo.grpc.proto.generated1.Hello1ServiceGrpc;
import org.summerboot.jexpress.boot.annotation.GrpcService;
import org.summerboot.jexpress.boot.annotation.Ping;
import org.summerboot.jexpress.nio.grpc.GRPCServer;
import org.summerboot.jexpress.nio.server.domain.Err;
import org.summerboot.jexpress.nio.server.domain.ProcessorSettings;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;
import org.summerboot.jexpress.security.auth.Authenticator;
import org.summerboot.jexpress.security.auth.Caller;

import java.net.SocketAddress;

@GrpcService
public abstract class Hello1Service extends Hello1ServiceGrpc.Hello1ServiceImplBase {


    @Ping
    @Override
    public void hello1(Hello1Request request, StreamObserver<Hello1Response> responseObserver) {
        ServiceContext serviceContext = GRPCServer.ServiceContext.get();
        SocketAddress addr = Authenticator.GrpcCallerAddr.get();
        Caller caller = Authenticator.GrpcCaller.get();
        String uid = Authenticator.GrpcCallerId.get();
        ProcessorSettings processorSettings = new ProcessorSettings();
        serviceContext.processorSettings(processorSettings);
        
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
            serviceContext.error(new Err(1, null, null, ex, null));
            Status status = Status.INTERNAL.withCause(ex).withDescription(ex.toString());
            responseObserver.onError(status.asException());
        }
    }

    abstract protected String hello(String firstName, String lastName) throws Throwable;

}
