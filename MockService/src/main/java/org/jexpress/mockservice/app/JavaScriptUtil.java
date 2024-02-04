package org.jexpress.mockservice.app;

import com.oracle.truffle.js.scriptengine.GraalJSScriptEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import org.apache.commons.lang3.StringUtils;
import org.graalvm.polyglot.Context;
import org.summerboot.jexpress.nio.server.domain.ServiceContext;

/**
 *
 * @author DuXiao
 */
public class JavaScriptUtil {

    private static String JS_CODE1 = """                         
                        var app = {                        
                         ruleEngine: function(requestBody, requestHeader) {
                        """;
    private static String JS_CODE2 = """                         
                         }
                        }
                        """;

    public static String ruleEngine(String jsCode, String requestBody, List<Map.Entry<String, String>> listOfEntry, final ServiceContext context) throws ScriptException, NoSuchMethodException {
        if (StringUtils.isBlank(jsCode)) {
            return null;
        }
        Map<String, String> requestHeader = new HashMap();
        for (Map.Entry<String, String> entry : listOfEntry) {
            requestHeader.put(entry.getKey(), entry.getValue());
        }
        ScriptEngine graalEngine = GraalJSScriptEngine.create(null,
                Context.newBuilder("js")
                        .allowAllAccess(true)
        );
        jsCode = JS_CODE1 + jsCode + JS_CODE2;
        if (context != null) {
            context.memo("jsCode", jsCode);
        }
        graalEngine.eval(jsCode);
        Invocable invocable = (Invocable) graalEngine;
        Object thiz = graalEngine.get("app");

        Object result = invocable.invokeMethod(thiz, "ruleEngine", requestBody, requestHeader);
        return result == null ? null : result.toString();
    }
}
