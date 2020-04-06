package com.application.resources;

import com.application.domain.jpa.User;
import com.application.domain.mongodb.QUserMD;
import com.application.domain.mongodb.UserMD;
import com.application.dto.UserDTO;
import com.application.event.UserEvent;
import com.application.repository.jpa.UserRepository;
import com.application.repository.jpa.dao.impl.UserDaoImpl;
import com.application.repository.mongodb.UserMongoDBRepository;
import com.application.resources.util.ResponseUtil;
import com.application.resources.vm.LoginVM;
import com.application.security.jwt.JWTFilter;
import com.application.security.jwt.TokenProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

@Api(value = "UserResources用户控制层", tags = {"User用户信息接口"})
@RestController
@RequestMapping("api/user")
@Slf4j
public class UserResources {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final UserDaoImpl userDao;

    private final UserRepository userRepository;

    private final UserMongoDBRepository userMongoDBRepository;

    public UserResources(ApplicationEventPublisher applicationEventPublisher, TokenProvider tokenProvider, AuthenticationManager authenticationManager, UserDaoImpl userDao, UserRepository userRepository, UserMongoDBRepository userMongoDBRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.userRepository = userRepository;
        this.userMongoDBRepository = userMongoDBRepository;
    }

    @ApiOperation(value = "查询大坝下所属用户", notes = "条件限制")
    @GetMapping("/find/predicate")
    public ResponseEntity<Iterable<User>> findByPredicate(
            @QuerydslPredicate(root = User.class) Predicate predicate) {
        return ResponseEntity.ok(userRepository.findAll(predicate));
    }

    @ApiOperation(value = "分页条件查询大坝", notes = "条件限制")
    @GetMapping("/find/page/predicate")
    public ResponseEntity<Page<User>> findByPageAndPredicate(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(userRepository.findAll(predicate, pageable));
    }

    @ApiOperation(value = "保存用户", notes = "条件限制")
    @PostMapping("/save")
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        @Valid User save = userRepository.save(user);
        applicationEventPublisher.publishEvent(new UserEvent(this, save));
        return ResponseEntity.ok().body(save);
    }

    @ApiOperation(value = "更新用户", notes = "条件限制")
    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(userRepository.save(user));
    }

    @ApiOperation(value = "查询用户详情", notes = "条件限制")
    @GetMapping("/find/{id}")
    @Transactional
    public ResponseEntity<BeanMap> find(@PathVariable("id") User user) {
        // 如果使用了QueryDsl则自动查询user

        // 遇到懒加载属性,直接调用该属性的toString()方法,此方法将会发送一个连表查询,没有n+1的情况
        User load = userDao.load(user.getId());
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

    @ApiOperation(value = "查询用户", notes = "条件限制(mongodb)")
    @GetMapping("/find/mongodb/{id}")
    public ResponseEntity<UserMD> findMongoDB(@PathVariable("id") Long id) {
        return ResponseUtil.wrapOrNotFound(userMongoDBRepository.findById(id));
    }

    @ApiOperation(value = "保存用户", notes = "条件限制(mongodb)")
    @PostMapping("/save/mongodb")
    @Transactional
    public ResponseEntity<UserMD> saveMongoDB(@Valid @RequestBody UserMD userMD) throws URISyntaxException {
        userMongoDBRepository.save(userMD);
        return ResponseEntity.created(new URI("/api/user/find/mongodb/" + userMD.getId())).build();
    }

    @ApiOperation(value = "删除用户", notes = "条件限制(mongodb)")
    @DeleteMapping("/delete/mongodb/{id}")
    @Transactional
    public ResponseEntity<Void> deleteMongoDB(@PathVariable("id") Long id) {
        userMongoDBRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询所有用户", notes = "条件限制(mongodb)")
    @GetMapping("/find/predicate/mongodb")
    @Transactional
    public ResponseEntity<Iterable<UserMD>> findByPredicateMongoDB(
            @QuerydslPredicate(root = UserMD.class) Predicate predicate) {
        Iterable<UserMD> iterable = userMongoDBRepository.findAll(predicate,
                Sort.by(Sort.Order.asc(QUserMD.userMD.id.toString())));
        return ResponseEntity.ok().body(iterable);
    }

    @ApiOperation(value = "查询所有用户", notes = "条件限制(简单查询)")
    @GetMapping(value = "/find/simple", params = "login")
    @Transactional
    public ResponseEntity<Set<UserDTO>> findBySimple(String login) {
        return ResponseEntity.ok().body(userRepository.findAllByLogin(login));
    }

    @ApiOperation(value = "用户登录", notes = "无条件限制")
    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
