package org.jexpress.demo.grpc.client;

import io.grpc.ManagedChannel;
import java.io.IOException;
import java.net.URI;
import javax.annotation.Nullable;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.jexpress.demo.grpc.proto.generated1.Hello1Request;
import org.jexpress.demo.grpc.proto.generated1.Hello1Response;
import org.jexpress.demo.grpc.proto.generated1.Hello1ServiceGrpc;
import org.summerboot.jexpress.nio.grpc.GRPCClient;

public class Hello1Client extends GRPCClient<Hello1Client> {

    public Hello1Client(URI uri, @Nullable KeyManagerFactory keyManagerFactory, @Nullable TrustManagerFactory trustManagerFactory,
            @Nullable String overrideAuthority, @Nullable Iterable<String> ciphers, @Nullable String... tlsVersionProtocols) throws IOException {
        super(uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
    }

    private Hello1ServiceGrpc.Hello1ServiceBlockingStub blockingStub;

    @Override
    protected void onConnected(ManagedChannel channel) {
        this.blockingStub = Hello1ServiceGrpc.newBlockingStub(channel);
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
