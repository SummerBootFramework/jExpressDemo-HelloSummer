package org.jexpress.demo.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.NameResolverProvider;
import java.io.IOException;
import java.net.URI;
import javax.annotation.Nullable;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.jexpress.demo.grpc.proto.generated2.Hello2Request;
import org.jexpress.demo.grpc.proto.generated2.Hello2Response;
import org.jexpress.demo.grpc.proto.generated2.Hello2ServiceGrpc;
import org.summerboot.jexpress.nio.grpc.BearerAuthCredential;
import org.summerboot.jexpress.nio.grpc.GRPCClient;

public class Hello2Client extends GRPCClient<Hello2Client> {

    public Hello2Client(NameResolverProvider nameResolverProvider, URI uri, @Nullable KeyManagerFactory keyManagerFactory, @Nullable TrustManagerFactory trustManagerFactory,
            @Nullable String overrideAuthority, @Nullable Iterable<String> ciphers, @Nullable String... tlsVersionProtocols) throws IOException {
        super(nameResolverProvider, uri, keyManagerFactory, trustManagerFactory, overrideAuthority, ciphers, tlsVersionProtocols);
    }

    private Hello2ServiceGrpc.Hello2ServiceBlockingStub blockingStub;

    @Override
    protected void onConnected(ManagedChannel channel) {
        String jwt = "jwt2";
        BearerAuthCredential bearerAuthCredential = new BearerAuthCredential(jwt);
        this.blockingStub = Hello2ServiceGrpc.newBlockingStub(channel).withCallCredentials(bearerAuthCredential);
    }

    public String hello(String firstName, String lastName) {
        Hello2Request request = Hello2Request.newBuilder()
                .setFirstName(firstName)
                .setLastName(lastName)
                .build();
        Hello2Response response = blockingStub.hello2(request);
        return response.getGreeting();
    }
}
