package org.jexpress.demo.restful;

import com.google.inject.Singleton;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.http.HttpHeaders;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.nio.server.BootHttpRequestHandler;
import org.summerboot.jexpress.nio.server.DefaultHttpRequestHandler;
import org.summerboot.jexpress.nio.server.RequestProcessor;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;

@Singleton
@Service(binding = ChannelHandler.class, named = BootHttpRequestHandler.BINDING_NAME, implTag="my")
public class HttpRequestHandler extends DefaultHttpRequestHandler {

    @Override
    protected boolean preProcess(RequestProcessor processor, HttpHeaders httpRequestHeaders, String httpRequestPath, ServiceContext context) throws Exception {
        return true;
    }

}
