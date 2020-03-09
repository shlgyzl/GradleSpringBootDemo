package com.application;

import com.application.domain.jpa.DefectType;
import org.assertj.core.util.Files;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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

    @Test
    public void test03() {
        //ZonedDateTime zone = Instant.now().atZone(ZoneId.systemDefault());
        //System.out.println(zone.toLocalDateTime());
        System.out.println(ZoneId.systemDefault());
    }

    @Test
    public void test04() {
        Set<DefectType> defectTypes = new HashSet<>(10);
        System.out.println(defectTypes.getClass().getSimpleName());
        System.out.println(System.identityHashCode(Float.NaN));
        System.out.println(System.identityHashCode(Float.NaN));
        System.out.println(System.identityHashCode(Float.NaN));
        System.out.println(System.identityHashCode(Float.NaN));
        System.out.println(64 >> 3);
        Runtime.getRuntime().runFinalization();
        String java_home = System.getenv("JAVA_HOME");
        System.out.println(Math.nextUp(1.2));
        System.out.println(1%0.2);

    }

    @Test
    public void test05() throws IOException {
        Path path = Paths.get("C:\\Users\\yanghaiyong\\Desktop\\timg.jpg");
        FileChannel fileChannel = new FileInputStream(path.toFile()).getChannel();
        MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, path.toFile().length());
        FileOutputStream outputStream = new FileOutputStream(Files.newFile("C:\\Users\\yanghaiyong\\Desktop\\timg2.jpg"));
        int write = outputStream.getChannel().write(buffer);
        System.out.println(write);

    }

    /*@Test
    public void test06() throws IOException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("notepad.exe");
        ProcessHandle processHandle = process.toHandle();
        System.out.println("进程是否运行" + processHandle.isAlive());
        System.out.println("进程id" + processHandle.pid());
        System.out.println("父进程" + processHandle.parent());

        ProcessHandle.Info info = processHandle.info();
        System.out.println(info.command());
        System.out.println(info.startInstant().map(n -> n.atZone(ZoneId.systemDefault())));
        System.out.println(info.totalCpuDuration());
        System.out.println(info.user());

        CompletableFuture<ProcessHandle> processHandleCompletableFuture = processHandle.onExit();
        processHandleCompletableFuture.thenAcceptAsync(n -> System.out.println("程序结束"));
    }*/

    @Test
    public void test07() throws Throwable {
        // 定义一个返回值为void的方法
        MethodType methodType = MethodType.methodType(void.class);
        MethodHandle handle = MethodHandles.lookup().findVirtual(CommonTest.class, "test01", methodType);
        Object invoke = handle.invoke(new CommonTest());
        String lowercaseLogin = "yanghaiyong".toLowerCase(Locale.CHINA);
        System.out.println(lowercaseLogin);
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

class OuterClass {
    private static int a = 6;

    private static class InnerClass {
        private static int a = 5;
    }

    public void print() {
        System.out.println(a);
        System.out.println(InnerClass.a);
    }

    public static void main(String[] args) {
        new OuterClass().print();
    }
}