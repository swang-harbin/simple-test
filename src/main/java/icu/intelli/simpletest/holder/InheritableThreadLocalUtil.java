package icu.intelli.simpletest.holder;

/**
 * 用于父子线程间共享对象
 *
 * @author wangshuo
 * @date 2021/07/01
 */
public class InheritableThreadLocalUtil {


    private InheritableThreadLocalUtil() {
    }

    private static final ThreadLocal<Object> THREAD_LOCAL = new java.lang.InheritableThreadLocal<>();

    public static Object get() {
        return THREAD_LOCAL.get();
    }

    public static void set(Object obj) {
        THREAD_LOCAL.set(obj);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static Object getAndRemove() {
        Object obj = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        return obj;
    }
}
