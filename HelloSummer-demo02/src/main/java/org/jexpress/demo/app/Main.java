package org.jexpress.demo.app;

import org.summerboot.jexpress.boot.SummerApplication;
import org.summerboot.jexpress.boot.annotation.Version;
import org.summerboot.jexpress.boot.config.NamedDefaultThreadFactory;
import org.summerboot.jexpress.nio.server.AbortPolicyWithReport;
import org.summerboot.jexpress.util.concurrent.EmptyBlockingQueue;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Version(value = "Hello Summer v2.0.0", logFileName = "hellosummerv2")
public class Main {

    public static void main(String... args) throws InterruptedException {
        int core = Integer.parseInt(args[0]);// 1
        int max = Integer.parseInt(args[1]);// 1
        int queue = Integer.parseInt(args[2]);// 0;
        long keepAliveSec = Integer.parseInt(args[3]);// 10;
        boolean allowCoreThreadTimeOut = Boolean.parseBoolean(args[4]);// false;
        boolean useVirtualThread = Boolean.parseBoolean(args[5]);// false;
        final long waitSec = Long.parseLong(args[6]);// 10;
        int taskAmount = Integer.parseInt(args[7]);// 10;
        testThread(core, max, queue, keepAliveSec, allowCoreThreadTimeOut, useVirtualThread, waitSec, taskAmount);
        //SummerApplication.run();
//        SummerApplication app = SummerApplication.run();
//        app.shutdown();
//        app.start();
    }

    public static void testThread(int core, int max, int queue, long keepAliveSec, boolean allowCoreThreadTimeOut, boolean useVirtualThread, long waitSec, int taskAmount) throws InterruptedException {
        BlockingQueue<Runnable> bq = queue > 0 ? new LinkedBlockingQueue<>(queue) : new EmptyBlockingQueue();
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(core, max, keepAliveSec, TimeUnit.SECONDS, bq, NamedDefaultThreadFactory.build("AAA", useVirtualThread), new AbortPolicyWithReport("TestThreadExecutor"));
        tpe.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
        final long startTs = System.currentTimeMillis();

        int check = 0;
        do {
            check++;
            TimeUnit.SECONDS.sleep(1);
            System.out.println(getTs(startTs) + "checking " + tpe);
        } while (check < 5);
        System.out.print("Enter to continue:");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        for (int i = 0; i < taskAmount; i++) {
            final int k = i;
            Runnable asyncTask = () -> {
                System.out.println(getTs(startTs) + "running: task" + k + " - " + System.currentTimeMillis());
                long lastExecutedTs = AL.getAndSet(System.currentTimeMillis());
                long elapsedTime = System.currentTimeMillis() - lastExecutedTs;
                try {
                    TimeUnit.SECONDS.sleep(waitSec);
                } catch (InterruptedException ex) {
                }
                System.out.println(getTs(startTs) + "done: task" + k + " - " + System.currentTimeMillis());
            };
            //if (tpe.getActiveCount() < 1) {
            try {
                System.out.println(getTs(startTs) + "before: task" + k + " - " + tpe);
                tpe.execute(asyncTask);
                System.out.println(getTs(startTs) + "after: task" + k + " - " + tpe);
            } catch (RejectedExecutionException ex) {
                System.out.println(getTs(startTs) + "Rejected task" + i + " - " + ex);
            }
//            } else {
//                System.out.println("Skipped#" + i);
//            }
        }

        //tpe.shutdown(); task-1 will be NOT be executed due to shutdown while in queue
        check = 0;
        while (tpe.getActiveCount()>0) {
            check++;
            TimeUnit.SECONDS.sleep(1);
            System.out.println(getTs(startTs) + "checking " + tpe);
        }
        tpe.shutdownNow();
    }

    private static AtomicLong AL = new AtomicLong(0);
    private static String getTs(long startTs) {
        return "[" + (System.currentTimeMillis() - startTs) / 1000 + "] ";
    }
}
