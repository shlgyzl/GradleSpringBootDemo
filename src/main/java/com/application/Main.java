package com.application;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.application.domain.jpa.User;
import com.application.domain.mongodb.UserMongoDB;
import com.application.repository.jpa.UserRepository;
import com.application.repository.jpa.dao.impl.UserDaoImpl;
import com.application.repository.mongodb.UserMongoDBRepository;
import io.github.jhipster.config.DefaultProfileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Slf4j
@SpringBootApplication(exclude = {LiquibaseAutoConfiguration.class})
@EnableSpringDataWebSupport// 开启Web支持
@RestController
public class Main {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserRepository userRepository;

    @Resource
    private AmqpTemplate rabbitmqTemplate;


    @Resource
    private UserDaoImpl userDao;

    @Resource
    private UserMongoDBRepository userMongoDBRepository;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Main.class);
        DefaultProfileUtil.addDefaultProfile(springApplication);
        Environment env = springApplication.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using `localhost` as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "External: \t{}://{}:{}{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    @GetMapping("/")
    public ResponseEntity<Void> index2() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/index")
    public ResponseEntity<Void> index() {
        UserMongoDB save = userMongoDBRepository.save(new UserMongoDB());
        //Optional<UserMD> md = userMongoDBRepository.findById(save.getId());
        userMongoDBRepository.deleteAll();
        //log.debug("查询的集合: {}", user.toString());
        //userMongoDBRepository.delete(user.orElse(new User()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hibernate")
    @SentinelResource("resource")
    public ResponseEntity<User> hibernate() {
        // 小心懒加载
        log.debug("hibernate查询的集合");
        List<User> users = userRepository.findByIds(Collections.singleton(1L));
        log.debug("查询的集合{}", users);
        return ResponseEntity.ok().body(userDao.load(1L));
    }

    /**
     * 使用@DateTimeFormat一定要符合标准的date/time格式,所以yyyy-MM-dd hh:mm:ss 是不行的
     * 如果LocalDateTime本身不支持的格式,即使使用了也会出错比如：yyyy-MM-dd就会接收不到
     * localDateTime->yyyy-MM-dd HH:mm:ss或yyyy-MM-ddTHH:mm:ss
     * LocalDate->yyyy-MM-dd
     * LocalTime->HH:mm:ss
     * 以上这些格式需要使用格式化器@DateTimeFormat,否则只能使用相应的标准的格式
     *
     * @param date
     * @return
     */
    @GetMapping("/date")
    public ResponseEntity<Date> date(Date date) {
        return ResponseEntity.ok().body(date);
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/redis")
    @Transactional
    public ResponseEntity<User> redis() {
        User user = new User();
        //noinspection unchecked
        //redisTemplate.boundHashOps("user_redis").put("user",user);
        //System.out.println(add);
        redisTemplate.opsForValue().set("user", "123");
        System.out.println(redisTemplate.boundHashOps("user_redis").get("user"));
        return ResponseEntity.ok().body(user);
    }

    @GetMapping("/rabbit")
    public ResponseEntity<Void> rabbit() {
        String msg = "测试RabbitMQ";
        //发送消息
        rabbitmqTemplate.convertAndSend("executeTask", msg);
        log.info("消息：{},已发送", msg);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/map")
    @SentinelResource("resource")
    public Map<String, Object> getMap(@RequestBody User user) {
        Map<String, Object> map = new HashMap<>(3);
        user.setLogin("");
        map.put("作者信息", user);
        map.put("博客地址", "http://blog.itcodai.com");
        map.put("CSDN地址", null);
        map.put("粉丝数量", 4153);
        map.put("日期", new Date());
        return map;
    }
}
