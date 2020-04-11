package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.User;
import com.application.repository.jpa.UserRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.application.service.UserService;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "UserResources用户控制层", tags = {"User用户信息接口"})
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

    @ApiOperation(value = "保存接口", notes = "保存用户")
    @ApiResponses({
            @ApiResponse(code = 201, message = "保存成功", response = User.class)
    })
    @Timed
    @PostMapping("/user")
    @Transactional
    public ResponseEntity<User> save(@Valid @RequestBody @ApiParam(name = "用户实体") User user) throws URISyntaxException {
        @Valid User save = userService.save(user);
        return ResponseEntity.created(new URI("/api/user/" + save.getId())).body(save);
    }

    @ApiOperation(value = "更新接口", notes = "更新用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功", response = User.class)
    })
    @Timed
    @PutMapping("/user")
    @Transactional
    public ResponseEntity<User> update(@Valid @RequestBody @ApiParam(name = "用户实体") User user) throws URISyntaxException {
        return getUserResponseEntity(user);
    }

    private ResponseEntity<User> getUserResponseEntity(@ApiParam(name = "用户实体") @RequestBody @Valid User user) throws URISyntaxException {
        user.addAllRole(user.getRoles());
        @Valid User save = userRepository.save(user);
        return ResponseEntity.created(new URI("/api/user/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除用户")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @Timed
    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询用户(根据id)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = User.class)
    })
    @Timed
    @GetMapping("/user/{id}")
    public ResponseEntity<User> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(userRepository.findById(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = User.class, responseContainer = "List")
    })
    @Timed
    @GetMapping("/users-all")
    public ResponseEntity<Iterable<User>> findAllUser(@QuerydslPredicate(root = User.class) Predicate predicate) {
        return ResponseEntity.ok().body(userRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = User.class, responseContainer = "List")
    })
    @Timed
    @GetMapping(value = "/users-all", params = "page")
    @Transactional
    public ResponseEntity<Page<User>> findPageAllUser(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(userRepository.findAll(predicate, pageable));
    }
}
