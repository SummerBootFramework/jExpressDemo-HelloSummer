package org.jexpress.demo.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.NameResolverProvider;
import org.jexpress.demo.grpc.proto.generated1.Hello1Request;
import org.jexpress.demo.grpc.proto.generated1.Hello1Response;
import org.jexpress.demo.grpc.proto.generated1.Hello1ServiceGrpc;
import org.summerboot.jexpress.nio.grpc.BearerAuthCredential;
import org.summerboot.jexpress.nio.grpc.GRPCClient;

import javax.annotation.Nullable;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.URI;

public class Hello1Client extends GRPCClient<Hello1Client> {

    public Hello1Client(NameResolverProvider nameResolverProvider, URI uri, @Nullable KeyManagerFactory keyManagerFactory, @Nullable TrustManagerFactory trustManagerFactory,
                        @Nullable String overrideAuthority, @Nullable Iterable<String> ciphers, @Nullable String... tlsVersionProtocols) throws IOException {
        super(nameResolverProvider, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
    }

    private Hello1ServiceGrpc.Hello1ServiceBlockingStub blockingStub;

    @Override
    protected void onConnected(ManagedChannel channel) {
        String jwt = "jwt1";
        BearerAuthCredential bearerAuthCredential = new BearerAuthCredential(jwt);
        this.blockingStub = Hello1ServiceGrpc.newBlockingStub(channel);//.withCallCredentials(bearerAuthCredential);
    }

    public String hello(String firstName, String lastName) {
        Hello1Request request = Hello1Request.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();
        Hello1Response response = blockingStub.hello1(request);
        return response.getGreeting();
    }

}
