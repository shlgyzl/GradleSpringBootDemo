package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.User;
import com.application.repository.jpa.UserRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.application.service.UserService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Api(value = "User", tags = {"User用户管理接口"})
@RestController
@RequestMapping("api")
@Slf4j
public class UserResources {
    private final UserRepository userRepository;
    private final UserService userService;

    public UserResources(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @ApiOperationSupport
    @ApiOperation(value = "保存接口", notes = "保存用户")
    @Timed
    @PostMapping("/user")
    public ResponseEntity<User> save(@Valid @RequestBody User user) throws URISyntaxException {
        User savedUser = userService.saveOrUpdate(user);
        return ResponseEntity.created(new URI("/api/user/" + savedUser.getId())).body(savedUser);
    }

    @ApiOperationSupport
    @ApiOperation(value = "更新接口", notes = "更新用户")
    @Timed
    @PutMapping("/user")
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws URISyntaxException {
        User savedUser = userService.saveOrUpdate(user);
        return ResponseEntity.created(new URI("/api/user/" + savedUser.getId())).body(savedUser);
    }

    @ApiOperationSupport
    @ApiOperation(value = "删除接口", notes = "删除用户")
    @Timed
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperationSupport
    @ApiOperation(value = "查询接口", notes = "查询用户(根据id)")
    @Timed
    @GetMapping("/user/{id}")
    public ResponseEntity<User> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(userRepository.findById(id));
    }

    @ApiOperationSupport
    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/users")
    public ResponseEntity<Page<User>> findAllUser(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @ApiIgnore @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        predicate = Optional.ofNullable(predicate).orElse(new BooleanBuilder());
        return ResponseEntity.ok().body(userRepository.findAll(predicate, pageable));
    }
}
