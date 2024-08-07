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
        System.out.println("My application status updated");
    }

    /**
     * @param healthOk
     * @param paused
     * @param retryIndex
     * @param nextInspectionIntervalSeconds
     */
    @Override
    public void onHealthInspectionFailed(boolean healthOk, boolean paused, long retryIndex, int nextInspectionIntervalSeconds) {
        super.onHealthInspectionFailed(healthOk, paused, retryIndex, nextInspectionIntervalSeconds);
        System.out.println("My health inspection failed");
    }
}
