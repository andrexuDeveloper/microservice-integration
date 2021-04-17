/**
 * @(#)ExecutorServiceUtil.java Copyright 2011 JUST IN MOBILE, Inc. All rights reserved.
 */
package com.keruyun.infra.commons.utils;

import java.util.concurrent.*;


public class ExecutorServiceUtil {

    private static int fixedThreadPoolSize = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static ExecutorService fixedThreadPoolService = new ThreadPoolExecutor(fixedThreadPoolSize, fixedThreadPoolSize,
            10000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());


    public static void execute(Runnable command) {
        fixedThreadPoolService.execute(command);

    }

    public static Future<?> submit(Runnable command) {
        return fixedThreadPoolService.submit(command);
    }

    public static void executeInThreadPool(Runnable command) {
        fixedThreadPoolService.execute(command);
    }


    public static void closeTask() {
        if (!fixedThreadPoolService.isShutdown()) {
            fixedThreadPoolService.shutdown();
        }
    }


}
