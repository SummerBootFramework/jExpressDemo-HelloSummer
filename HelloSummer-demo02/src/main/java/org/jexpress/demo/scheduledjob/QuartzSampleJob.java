package org.jexpress.demo.scheduledjob;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.summerboot.jexpress.boot.annotation.Scheduled;
import org.summerboot.jexpress.integration.cache.AuthTokenCache;

import java.util.concurrent.TimeUnit;

//@Scheduled(cron="0 15 10 ? * 6L 2012-2015")// cron expression: Fire at 10:15am on every last Friday of every month during the years 2012, 2013, 2014 and 2015
@Scheduled(daysOfMonth = 1, daysOfMonthField = "Configurable_DaysOfMonth", hour = 2, minute = 3)
// monthly: every 2:03am 1st day of the month
//@Scheduled(daysOfWeek=1, hour=14, minute=15)// weekly: 2:15pm every Sunday
//@Scheduled(daysOfWeek={1, 6, 7}, hour=14, minute=15)// weekly: 2:15pm every Sunday, Friday and Saturday
//@Scheduled(hour = 14, minute = 15, second = 16)// daily: 2:15:16pm everyday
//@Scheduled(minute = 15, second = 16)// hourly: every hour at the 15th minute and the 16th second
//@Scheduled(second = 16)// minutely: every minute at the 16th second
//@Scheduled(fixedRate = 10_000, initialDelay=5_000)// start job after 5 seconds, run job every 10 secsonds no matter how long the job takes
//@Scheduled(fixedDelay = 10_000, initialDelay = 5_000)// start job after 5 seconds, when the job finished wait 10 seconds then start it again
@Singleton
public class QuartzSampleJob implements Job {

    private static int[] Configurable_DaysOfMonth = {2, 3};

    @Inject
    private AuthTokenCache cache;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("cache: " + cache);
        System.out.println(this.hashCode() + ": " + context.getTrigger().getDescription() + "\n\t running @" + context.getFireTime() + "\n\tnext time:" + context.getNextFireTime());
        try {
            TimeUnit.SECONDS.sleep(4);
            System.out.println(this.hashCode() + ": sleep 4 seconds done");
        } catch (InterruptedException ex) {
            System.out.println(this.hashCode() + ": sleep 4 seconds error: " + ex);
        }
    }
}
