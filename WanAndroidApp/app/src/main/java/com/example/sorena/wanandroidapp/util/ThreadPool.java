package com.example.sorena.wanandroidapp.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class ThreadPool
{
    private static final int CorePoolSize = 15;
    private static final int MaximumPoolSize = 50;
    private static final int BaseTime = 5;
    private static final int QueueMaxSize = 500;
    private static final int InitSize = 5;
    private static final TimeUnit BaseTimeUnit = TimeUnit.SECONDS;

    private static final ThreadPool threadPool;

    private ThreadPoolExecutor threadPoolExecutor;

    static {
        threadPool = new ThreadPool();
    }

    private ThreadPool(){
        threadPoolExecutor = new ThreadPoolExecutor(CorePoolSize,MaximumPoolSize,
                BaseTime,BaseTimeUnit, new LinkedBlockingDeque<>(QueueMaxSize));
    }

    public static ThreadPool getInstance(){
        return threadPool;
    }

    //添加任务
    public void addTask(Runnable task){
        if (task != null){
            //如果线程池已经爆满不能处理新的任务,就不用线程池来处理任务
            if (threadPoolExecutor.getPoolSize() == MaximumPoolSize){
                new Thread(task).start();
            }//否则,添加到线程池
            else {
                threadPoolExecutor.execute(task);
            }
        }
    }

}
