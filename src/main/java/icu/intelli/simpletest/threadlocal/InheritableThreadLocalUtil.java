package icu.intelli.simpletest.threadlocal;

/**
 * @author wangshuo
 * @date 2021/07/20
 */
public class InheritableThreadLocalUtil {

    private InheritableThreadLocalUtil() {
    }

    private static final ThreadLocal<Object> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static Object get() {
        return THREAD_LOCAL.get();
    }

    public static void set(Object obj) {
        THREAD_LOCAL.set(obj);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }

}