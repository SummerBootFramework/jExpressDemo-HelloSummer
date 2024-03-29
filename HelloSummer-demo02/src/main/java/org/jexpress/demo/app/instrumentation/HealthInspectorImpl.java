package org.jexpress.demo.app.instrumentation;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.Logger;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.boot.instrumentation.BootHealthInspectorImpl;
import org.summerboot.jexpress.boot.instrumentation.HealthInspector;
import org.summerboot.jexpress.integration.cache.AuthTokenCache;
import org.summerboot.jexpress.nio.server.domain.ServiceError;

@Singleton
@Service(binding = HealthInspector.class)
public class HealthInspectorImpl extends BootHealthInspectorImpl {
    @Inject
    private AuthTokenCache cache;

    @Override
    protected void healthCheck(@Nonnull ServiceError error, @Nullable Logger callerLogger) {
        error.addErrors(cache.ping(callerLogger));

//        int errorCode = 123;
//        Err e = new Err(errorCode, null, "Mock failure", null, null);
//        error.addErrors(e);
    }
}
