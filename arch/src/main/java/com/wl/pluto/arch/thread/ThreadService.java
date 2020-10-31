package com.wl.pluto.arch.thread;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ================================================
 *
 * @author pluto
 * Created by szy on 2019-08-20
 * ================================================
 * @function <p>
 */
public class ThreadService {

    private static final ThreadService ourInstance = new ThreadService();

    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;

    public static ThreadService getInstance() {
        return ourInstance;
    }

    private ExecutorService executorService;

    private ThreadService() {


        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>(256);
        executorService = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, TimeUnit.SECONDS, taskQueue);
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }
}
