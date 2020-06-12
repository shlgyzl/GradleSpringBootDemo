package com.application;

import com.application.config.fastjson.FastJsonConfiguration;
import com.application.web.domain.jpa.Authority;
import com.application.web.domain.jpa.DefectType;
import com.application.web.domain.jpa.Role;
import com.application.common.util.DomainUtil;
import org.assertj.core.util.Files;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
        System.out.println(1 % 0.2);

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

    @Test
    public void test08() throws Throwable {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String admin = bCryptPasswordEncoder.encode("123456");
        System.out.println(admin);

        System.out.println(LocalDateTime.now().toEpochSecond(Instant.now().atZone(ZoneId.systemDefault()).getOffset()));
        LocalDateTime.now(ZoneId.systemDefault()).toEpochSecond(OffsetDateTime.now(ZoneId.systemDefault()).getOffset());
        System.out.println();

    }

    @Test
    public void test09() {
        Role role = new Role();
        Authority authority = new Authority();
        role.setName("管理员");
        role.setId(1L);
        authority.setId(2L);
        authority.setName("后台管理");
        role.getAuthorities().add(authority);

        Role role2 = new Role();
        role2.setName("普通角色");
        role2.setId(2L);
        Authority authority_ = new Authority();
        authority_.setId(1L);
        authority_.setName("后台管理");
        role2.getAuthorities().add(authority_);
        DomainUtil.copyDeep(role, role2);
        System.out.println(role2);
    }

    @Test
    public void test10() {
        List<Integer> a = new ArrayList<>(10);
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(4);

        List<Integer> b = new ArrayList<>(10);
        b.add(1);
        b.add(2);
        b.add(6);

        b.removeAll(a);
        //b.addAll(a);
        a.retainAll(b);

        //System.out.println(a);
        //System.out.println(System.getenv());
    }

    @Test
    public void test11() {
        System.out.println(System.getenv());
    }

    @Test
    public void test12() throws Exception {
        //UserSig 计算公式，其中 secretkey 为计算 usersig 用的加密密钥
        String secretkey = "Gu5t9xGARNpq86cd98joQYCN3EXAMPLE";
        String userid = "123";
        String sdkappid = "123";
        String currtime = System.currentTimeMillis() + "";
        String expire = System.currentTimeMillis() + 3600000 + "";

        String fix = userid + sdkappid + currtime + expire;
        byte[] hmac256 = hmac256(secretkey.getBytes(StandardCharsets.UTF_8), (fix + Base64.getEncoder().encodeToString((fix).getBytes(StandardCharsets.UTF_8))));

        System.out.println(DatatypeConverter.printHexBinary(hmac256));
    }

    public byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void test13() {
        Integer a = 1;
        Integer b = a == 1 ? 3 : ++a;
        System.out.println(b);
        System.out.println(a);
    }

    @Test
    public void test14() throws ClassNotFoundException {
        Class<?> name = FastJsonConfiguration.class;
        Class<?> superclass = name.getSuperclass();
        System.out.println(superclass.getSimpleName());

        int length = Array.getLength(new String[]{});
        Object newInstance = Array.newInstance(String.class, 3, 6, 7, 2);
        System.out.println(newInstance);

    }

    @Test
    public void test15() throws IOException {
        URL url = new URL("https://cdn.rerecb.com/20191201/38uyDmeq/800kb/hls/i6vcT7Qs.ts");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        Path filePath = Paths.get(System.getProperty("user.dir"), "files","i6vcT7Qs.ts");
        java.nio.file.Files.createDirectories(filePath.getParent());
        java.nio.file.Files.copy(inputStream, filePath, REPLACE_EXISTING);
    }

    /*@Test
    public void test10() {
        String inputFoler = "C:\\Users\\yanghaiyong\\Downloads\\女生 白色衬衫 耳机 房间 书桌 4k动漫壁纸_彼岸图网.jpg";
        *//* 这儿填写你存放要缩小图片的文件夹全地址 *//*
        String outputFolder = "C:\\Users\\yanghaiyong\\Downloads\\女生 白色衬衫 耳机 房间 书桌 4k动漫壁纸_彼岸图网2.jpg";
        *//* 这儿填写你转化后的图片存放的文件夹 *//*
        writeHighQuality(zoomImage(inputFoler), outputFolder);
    }

    public BufferedImage zoomImage(String src) {

        BufferedImage result = null;

        try {
            File srcfile = new File(src);
            if (!srcfile.exists()) {
                System.out.println("文件不存在");

            }
            BufferedImage im = ImageIO.read(srcfile);

            *//* 原始图像的宽度和高度 *//*
            int width = im.getWidth();
            int height = im.getHeight();

            // 压缩计算
            float resizeTimes = 0.3f; *//* 这个参数是要转化成的倍数,如果是1就是转化成1倍 *//*

     *//* 调整后的图片的宽度和高度 *//*
            int toWidth = (int) (width * resizeTimes);
            int toHeight = (int) (height * resizeTimes);

            *//* 新生成结果图片 *//*
            result = new BufferedImage(toWidth, toHeight, BufferedImage.TYPE_INT_RGB);

            result.getGraphics().drawImage(im.getScaledInstance(toWidth, toHeight, java.awt.Image.SCALE_SMOOTH), 0, 0,
                    null);

        } catch (Exception e) {
            System.out.println("创建缩略图发生异常" + e.getMessage());
        }

        return result;

    }

    public boolean writeHighQuality(BufferedImage im, String fileFullPath) {
        try {
            *//* 输出到文件流 *//*
            FileOutputStream newimage = new FileOutputStream(fileFullPath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
            JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(im);
            *//* 压缩质量 *//*
            jep.setQuality(0.9f, true);
            encoder.encode(im, jep);
            *//* 近JPEG编码 *//*
            newimage.close();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            // 压缩完毕后，删除原文件
            *//*File file = new File(fileFullPath);
            // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("删除文件" + fileFullPath + "成功！");
                } else {
                    System.out.println("删除文件" + fileFullPath + "失败！");
                }
            } else {
                System.out.println("删除文件失败：" + fileFullPath + "不存在！");
            }*//*
        }
    }*/
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