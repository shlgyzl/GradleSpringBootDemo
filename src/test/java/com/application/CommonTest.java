package com.application;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 常用测试类
 */
public class CommonTest {
    @Test
    public void test01() {
        // 获取CPU核心数
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    @Test
    public void test02() throws InterruptedException {
        // 同步辅助类,用于各个线程之间同步执行。
        // latch.await():会暂停当前方法所在的线程,直到latch值为0;latch.countDown():将倒数减一
        CountDownLatch latch = new CountDownLatch(5);
        Service service = new Service(latch);
        // 创建线程且指定线程执行方法的缩写形式,语法糖而已
        Runnable task = service::exec;

        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(task);
            thread.start();
        }

        System.out.println("main thread await. ");
        latch.await();
        System.out.println("main thread finishes await. ");
    }
}

class Service {
    private CountDownLatch latch;

    public Service(CountDownLatch latch) {
        this.latch = latch;
    }

    public void exec() {
        try {
            System.out.println(Thread.currentThread().getName() + " execute task. ");
            sleep(2);
            System.out.println(Thread.currentThread().getName() + " finished task. ");
        } finally {
            // 一定要如此书写,否则等待线程就会一直持续等待
            latch.countDown();
        }
    }

    private void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}