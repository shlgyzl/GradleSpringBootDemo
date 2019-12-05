package com.application;

import com.application.domain.User;
import com.application.repository.jpa.UserRepository;
import com.application.repository.jpa.dao.impl.UserDaoImpl;
import com.application.repository.mongodb.UserMongoDBRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableSpringDataWebSupport// 开启Web支持
@RestController
@Slf4j
public class Main {
    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserDaoImpl userDao;

    @Resource
    private UserMongoDBRepository userMongoDBRepository;

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Main.class);
        springApplication.run(args);
    }

    @GetMapping("/")
    public ResponseEntity<Void> index2() {
        System.out.println(Thread.currentThread().getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/index")
    public ResponseEntity<Void> index() {
        System.out.println(232);
        userMongoDBRepository.save(userRepository.findById(1L).get());
        Optional<User> user = userMongoDBRepository.findById(1L);
        log.debug("查询的集合: {}", user.toString());
        //userMongoDBRepository.delete(user.orElse(new User()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hibernate")
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
}
