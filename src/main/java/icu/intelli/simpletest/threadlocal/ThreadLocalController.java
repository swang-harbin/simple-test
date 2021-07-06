package icu.intelli.simpletest.threadlocal;

import icu.intelli.simpletest.holder.InheritableThreadLocalUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangshuo
 * @date 2021/07/02
 */
@RestController
@RequestMapping("/thread-local")
public class ThreadLocalController {

    @RequestMapping("/inheritable")
    public void inheritable(String val) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName.concat("：set：val = ").concat(threadName).concat("：").concat(val));
        InheritableThreadLocalUtil.set(val);
        for (int i = 0; i < 5; i++) {
            new Thread((() ->
                    System.out.println(Thread.currentThread().getName().concat("：get：val = ").concat((String) InheritableThreadLocalUtil.getAndRemove()))
            ), threadName.concat("的子线程：") + i).start();
        }
    }
}
