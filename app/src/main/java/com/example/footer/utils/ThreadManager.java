package com.example.footer.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 乃军 on 2018/1/10.
 */

public class ThreadManager {
   private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
   private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
   private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
   private static final long KEEP_ALIVE = 10L;


   private static final ThreadFactory sThreadFactory = new ThreadFactory() {
   private final AtomicInteger mCount = new AtomicInteger(1);

   public Thread newThread(Runnable r) {
         return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
      }
   };

  public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
          CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(), sThreadFactory);
}

