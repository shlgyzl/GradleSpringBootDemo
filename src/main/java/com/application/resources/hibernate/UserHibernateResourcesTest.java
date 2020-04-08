package com.application.resources.hibernate;

import com.application.domain.jpa.User;
import com.application.dto.UserDTO;
import com.application.repository.jpa.UserRepository;
import com.application.repository.jpa.dao.IUserDao;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api")
public class UserHibernateResourcesTest {
    private final UserRepository userRepository;
    private final IUserDao userDao;
    private final CacheManager cacheManager;

    public UserHibernateResourcesTest(UserRepository userRepository, IUserDao userDao, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.userDao = userDao;
        this.cacheManager = cacheManager;
    }


    @ApiOperation(value = "查询用户详情", notes = "条件限制")
    @GetMapping("/find/{id}")
    @Transactional
    public ResponseEntity<BeanMap> find(@PathVariable("id") User user) {
        // 如果使用了QueryDsl则自动查询User

        // 遇到懒加载属性,直接调用该属性的toString()方法,此方法将会发送一个连表查询,没有n+1的情况
        User load = userDao.findById(user.getId());
        BeanMap beanMap = BeanMap.create(load);
        // 使用原生sql查询
        System.out.println(userRepository.findByIdForNative(user.getId()));
        // 原生分页加排序查询
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());
        System.out.println(userRepository.findByLogin("Admin", pageRequest).getContent());

        // 普通排序
        userRepository.findByAndSort("Admin", Sort.by("login").descending());
        // SQL函数(只能这样写,不能写其他任何形式)
        userRepository.findByAndSort("Admin", JpaSort.unsafe("LENGTH(login)"));
        // SQL函数
        // 返回普通查询列(List<Object[]>)
        System.out.println(userRepository.findByAsArrayAndSort("Admin", Sort.by("fn_len").descending()));


        // 简单等值查询
        // QBE查询需要：已经序列化的实体,属性不能懒加载;有Example和ExampleMatcher进行组合条件
        // 实体仓库需要继承QueryByExampleExecutor<T>
        // 仅仅支持starts/contains/ends/regex 模糊查询
        // 不支持such as login = ?0 or (login = ?1 and firstName = ?2)

        ExampleMatcher matching = ExampleMatcher.matching();
        ExampleMatcher matcher = matching.withIgnoreNullValues()// 忽略空值
                .withIgnoreCase("login")// 忽略属性login大小写
                .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);// 以该属性值结束
        System.out.println(userRepository.findOne(Example.of(user, matcher)));

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("login", ExampleMatcher.GenericPropertyMatcher::endsWith)
                .withMatcher("password", ExampleMatcher.GenericPropertyMatcher::startsWith);
        System.out.println(userRepository.findOne(Example.of(user, exampleMatcher)));

        // 如果有多个属性对象则使用对象链进行匹配
        // 高级用法
        // ExampleMatcher.StringMatcher.EXACT 等值
        // ExampleMatcher.StringMatcher.DEFAULT 等值


        return ResponseEntity.ok().body(beanMap);
    }


    @ApiOperation(value = "查询所有用户", notes = "条件限制(简单查询)")
    @GetMapping(value = "/find/simple", params = "login")
    @Transactional
    @Cacheable(cacheNames = "UserList", key = "#login", cacheManager = "cacheManager")
    public ResponseEntity<Set<UserDTO>> findBySimple(String login) {
        Cache managerCache = cacheManager.getCache("default");
        Cache.ValueWrapper valueWrapper = managerCache.get(login);
        System.out.println(valueWrapper);
        return ResponseEntity.ok().body(userRepository.findAllByLogin(login));
    }
}
