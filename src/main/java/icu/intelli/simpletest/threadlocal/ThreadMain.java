package icu.intelli.simpletest.threadlocal;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangshuo
 * @date 2021/07/20
 */
public class ThreadMain {

    public static void main(String[] args) throws InterruptedException {
        inheritableThreadLocal();
        poolInheritableThreadLocal();
        poolTransmittableThreadLocal();
    }

    private static void threadLocal() {
        ThreadLocalUtil.set(new Person());
    }

    private static void inheritableThreadLocal() {
        InheritableThreadLocalUtil.set("666");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                Object val = InheritableThreadLocalUtil.get();
                System.out.println(val);
                InheritableThreadLocalUtil.remove();
            }
            ).start();
        }
        InheritableThreadLocalUtil.remove();
    }

    private static void poolInheritableThreadLocal() {
        // 创建线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();
        // 父线程要将变量共享给线程池创建的线程
        InheritableThreadLocalUtil.set("222");
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                // 在子线程中获取父线程的共享变量
                Object val = InheritableThreadLocalUtil.get();
                if (Objects.isNull(val)) {
                    // 由于 InheritableThreadLocal 不能达到该需求，所以会出现获取到的变量为 null
                    System.out.println(val);
                }
                // 移除新线程中 inheritableThreadLocal 属性中的共享变量，让垃圾回收器可以进行回收
                InheritableThreadLocalUtil.remove();
            });
        }
        // 任务都执行结束后，关闭线程池
        threadPool.shutdown();
    }

    private static void poolTransmittableThreadLocal() {
        // 通过 alibaba 提供的 TtlExecutors 对线程池进行包装
        ExecutorService threadPool = TtlExecutors.getTtlExecutorService(Executors.newCachedThreadPool());
        // 父线程要将变量共享给线程池创建的线程
        TransmittableThreadLocalUtil.set("666");
        for (int i = 0; i < 1000; i++) {
            threadPool.execute(() -> {
                // 在子线程中获取父线程的共享变量
                Object val = TransmittableThreadLocalUtil.get();
                if (Objects.isNull(val)) {
                    // 此时可以获取到共享变量，所以不会出现 null
                    System.out.println(val);
                }
                // 移除新线程中的共享变量，让垃圾回收器可以进行回收
                TransmittableThreadLocalUtil.remove();
            });
        }
        // 任务都执行结束后，关闭线程池
        threadPool.shutdown();
        // 移除父线程中的共享变量，让垃圾回收器可以进行回收
        TransmittableThreadLocalUtil.remove();
    }
}
