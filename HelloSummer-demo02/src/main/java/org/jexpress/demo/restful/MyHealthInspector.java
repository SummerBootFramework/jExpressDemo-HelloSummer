package org.jexpress.demo.restful;

import com.google.inject.Inject;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.boot.instrumentation.BootHealthInspectorImpl;
import org.summerboot.jexpress.boot.instrumentation.HealthInspector;
import org.summerboot.jexpress.integration.cache.AuthTokenCache;
import org.summerboot.jexpress.nio.server.domain.ServiceError;

@Service(binding = HealthInspector.class)
public class MyHealthInspector extends BootHealthInspectorImpl {

    @Inject
    private AuthTokenCache cache;

    @Override
    protected void healthCheck(@Nonnull ServiceError error, @Nullable Logger callerLog) {
        error.addErrors(cache.ping(callerLog));
    }
}
