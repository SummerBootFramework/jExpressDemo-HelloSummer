package org.jexpress.demo.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.summerboot.jexpress.nio.server.domain.Err;
import org.summerboot.jexpress.nio.server.SessionContext;
import org.summerboot.jexpress.util.BeanUtil;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AsyncDemo {
    public static void main(String[] args) throws JsonProcessingException, ExecutionException, InterruptedException {
        asyncMethod();
    }

    public static void asyncMethod() throws JsonProcessingException, ExecutionException, InterruptedException {

        List<Object> list1 = new ArrayList();
        List<Object> list2 = new ArrayList();

        int task1Time = 10;
        boolean task1Success = true;
        int task2Time = 5;
        boolean task2Success = true;

        SessionContext context = SessionContext.build(0);

        long start = System.currentTimeMillis();

        CompletableFuture<String> classAFuture = CompletableFuture.supplyAsync(() -> task1(task1Time, task1Success))
                .thenApply(v -> {
                    System.out.println(OffsetDateTime.now() + " task1 thenApply(v=" + v + ")");
                    list1.add(v);
                    return v + "applied";
                })
                .exceptionally((ex) -> {
                    System.out.println(OffsetDateTime.now() + " task1 exceptionally(ex=" + ex + ")");
                    Err e = new Err(task1Time, "task1", ex.toString(), ex);
                    context.error(e);
                    return null;
                })
                .whenComplete((v, ex) -> {
                    System.out.println(OffsetDateTime.now() + " task1 whenComplete(v=" + v + ", ex=" + ex + ")");
                    list2.add(v);
                });
        System.out.println(OffsetDateTime.now() + " task1 built, time cost=" + (System.currentTimeMillis() - start));

        CompletableFuture<Integer> classBFuture = CompletableFuture.supplyAsync(() -> task2(task2Time, task2Success))
                .thenApply(v -> {
                    System.out.println(OffsetDateTime.now() + " task2 thenApply(v=" + v + ")");
                    list1.add(v);
                    return v + 1;
                })
                .exceptionally((ex) -> {
                    System.out.println(OffsetDateTime.now() + " task2 exceptionally(ex=" + ex + ")");
                    Err e = new Err(task2Time, "task2", ex.toString(), ex);
                    context.error(e);
                    return -1;
                })
                .handle((v, ex) -> {
                    System.out.println(OffsetDateTime.now() + " task2 whenComplete(v=" + v + ", ex=" + ex + ")");
                    if (ex != null) {
                        Err e = new Err(task2Time, "task2", ex.toString(), ex);
                        context.error(e);
                    }
                    list2.add(v);
                    return v;
                });
        System.out.println(OffsetDateTime.now() + " task2 built, time cost=" + (System.currentTimeMillis() - start));

        CompletableFuture.allOf(classAFuture, classBFuture).join();
        System.out.println(OffsetDateTime.now() + " allOf.join, time cost=" + (System.currentTimeMillis() - start));

        String a = classAFuture.get();
        System.out.println(OffsetDateTime.now() + " a=" + a + ", time cost=" + (System.currentTimeMillis() - start));
        Integer b = classBFuture.get();
        System.out.println(OffsetDateTime.now() + " b=" + b + ", time cost=" + (System.currentTimeMillis() - start));

        System.out.println(OffsetDateTime.now() + " allpyList=" + list1 + "；completeList=" + list2 + ", time cost=" + (System.currentTimeMillis() - start));
        System.out.println(BeanUtil.toJson(context.error(), true, false));
    }

    public static String task1(Integer i, boolean success) {
        try {
            System.out.println(OffsetDateTime.now() + " task1 thread start: " + Thread.currentThread().getName() + ", task#=" + i);
            Thread.sleep(i * 1000);
            System.out.println(OffsetDateTime.now() + " task1 thread end: " + Thread.currentThread().getName() + ", task#=" + i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!success) {
            throw new RuntimeException("task1 exception");
        }
        return "param" + i;
    }

    public static Integer task2(Integer i, boolean success) {
        int ret = i * 1000;
        try {
            System.out.println(OffsetDateTime.now() + " task2 thread start: " + Thread.currentThread().getName() + ", task#=" + i);
            Thread.sleep(ret);
            System.out.println(OffsetDateTime.now() + " task2 thread end: " + Thread.currentThread().getName() + ", task#=" + i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!success) {
            throw new RuntimeException("task2 exception");
        }
        return ret;
    }
}
