package org.jexpress.demo.app.instrumentation;

import com.google.inject.Singleton;
import org.summerboot.jexpress.boot.annotation.Service;
import org.summerboot.jexpress.boot.event.AppLifecycleHandler;
import org.summerboot.jexpress.boot.event.AppLifecycleListener;

@Singleton
@Service(binding = AppLifecycleListener.class)
public class AppLifecycleListenerImpl extends AppLifecycleHandler {
    /**
     * called when application paused or resumed by configuration/pause file or BottController's ${context-root}/status?pause=true|false
     *
     * @param healthOk
     * @param paused
     * @param serviceStatusChanged
     * @param reason
     */
    @Override
    public void onApplicationStatusUpdated(boolean healthOk, boolean paused, boolean serviceStatusChanged, String reason) {
        super.onApplicationStatusUpdated(healthOk, paused, serviceStatusChanged, reason);
        System.out.println("Application status updated: healthOk=" + healthOk + ", paused=" + paused + ", serviceStatusChanged=" + serviceStatusChanged + ", reason=" + reason);
    }

    /**
     * @param retryIndex
     * @param reason
     * @param nextInspectionIntervalSeconds
     */
    @Override
    public void onHealthInspectionFailed(int retryIndex, String reason, int nextInspectionIntervalSeconds) {
        super.onHealthInspectionFailed(retryIndex, reason, nextInspectionIntervalSeconds);
        System.out.println("Health inspection failed: " + reason);
    }
}
