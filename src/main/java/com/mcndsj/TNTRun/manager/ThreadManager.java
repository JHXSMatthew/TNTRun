package com.mcndsj.TNTRun.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Matthew on 1/07/2016.
 */
public class ThreadManager {

    private static ThreadManager pool ;

    private ExecutorService service = Executors.newSingleThreadExecutor();

    public static void runTask(Runnable run){
        if(pool ==null){
            pool = new ThreadManager();
        }
        pool.service.execute(run);
    }

}
