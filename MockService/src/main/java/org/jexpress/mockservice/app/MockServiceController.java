package org.jexpress.mockservice.app;

import com.google.inject.Singleton;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import javax.script.ScriptException;
import org.apache.commons.lang3.StringUtils;

import org.summerboot.jexpress.boot.annotation.Controller;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;

/**
 * @author 魏泽北
 */
@Singleton
@Controller
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class MockServiceController {

    // curl -v -k https://localhost:8211/service1/action1 -H "Accept: application/json" -H "Authorization: Bearer abcdefg"
    // curl -v -k https://localhost:8211/service1/action1 -H "Accept: application/json" -H "Authorization: Bearer abcdefg" -X POST -d '{"name":"John Done","title":"Boss"}'
    @GET
    @POST
    @PUT
    @PATCH
    @DELETE
    @Path("")
    public String mockService(String body, @Parameter(hidden = true) final ServiceContext context) throws IOException, ScriptException, NoSuchMethodException {
        String url = context.uri();
        Map<String, String> queryParam = new LinkedHashMap();
        String action = parseUrlQueryParam(url, queryParam);

        String filePath = "mock_response" + action + "_" + context.method();
        String fileName = filePath + ".js";
        context.memo("js.file", fileName);
        String jsCode = Utils.loadFileContent(fileName, true, Utils.JS_FILE_CONTENT);
        String postFix = Utils.javascriptRuleEngine(jsCode, context.requestHeaders().entries(), queryParam, body, context);
        if (!StringUtils.isBlank(postFix)) {
            filePath += "_case_" + postFix;
        }

        fileName = filePath + ".properties";
        context.memo("response.header.file", fileName);
        Properties responseHeaders = Utils.loadProperties(fileName, true);
        HttpResponseStatus status = Utils.setResponseHeaders(responseHeaders, context);
        context.status(status);

        fileName = filePath + ".txt";
        context.memo("response.body.file", fileName);
        return Utils.loadFileContent(fileName, true, null);
    }

    public static String parseUrlQueryParam(String url, Map<String, String> queryParam) {
        String[] request = url.split("\\?", 2);
        String action = request[0];
        if (request.length < 2) {
            return action;
        }
        String queryParamString = URLDecoder.decode(request[1], StandardCharsets.UTF_8);
        String[] pairs = queryParamString.split("&");

        for (String param : pairs) {
            String[] keyValuePair = param.split("=", 2);
            queryParam.put(keyValuePair[0], keyValuePair[1]);
        }

        return action;
    }

}
