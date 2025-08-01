package org.jexpress.demo.restful.client;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.jexpress.demo.restful.service.MyController;
import org.summerboot.jexpress.integration.httpclient.HttpClientConfig;
import org.summerboot.jexpress.integration.httpclient.RPCDelegate_HTTPClientImpl;
import org.summerboot.jexpress.integration.httpclient.RPCResult;
import org.summerboot.jexpress.nio.server.SessionContext;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;

public class HttpClient extends RPCDelegate_HTTPClientImpl {

    /**
     * @return
     */
    @Override
    protected HttpClientConfig getHttpClientConfig() {
        return DemoHttpClientConfig.cfg;
    }

    public void send(String... messages) throws IOException {
        SessionContext serviceContext = SessionContext.build(0);

        final URI uri = URI.create("https://localhost:8222/api/v1/echo");
        HttpRequest.Builder reqBuilder = HttpRequest.newBuilder(uri);
        HttpResponseStatus successStatus = HttpResponseStatus.OK;
        RPCResult<MyController.ResponseDto> result = rpcEx(serviceContext, reqBuilder, successStatus);
        if (result != null) {
            MyController.ResponseDto dto = result.update(MyController.ResponseDto.class, serviceContext).successResponse();
            System.out.println("HTTP Response: " + dto);
            /*
{
  "clientPrivacy" : "clientPrivacy",
  "clientNonPrivacy" : "clientNonPrivacy",
  "secretList" : [ "aa", "bb" ]
}
             */
        }
    }
}
