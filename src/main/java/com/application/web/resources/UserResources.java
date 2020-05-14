package com.application.web.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.User;
import com.application.repository.jpa.UserRepository;
import com.application.service.UserService;
import com.application.web.resources.exception.BusinessErrorException;
import com.application.web.resources.util.ResponseUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@Api(value = "User", tags = {"User用户管理接口"})
@RestController
@RequestMapping("api")
@Slf4j
@AllArgsConstructor
public class UserResources {
    private final UserRepository userRepository;
    private final UserService userService;

    @ApiOperationSupport(ignoreParameters = {"dams", "roles"})
    @ApiOperation(value = "保存接口", notes = "保存用户")
    @Timed
    @PostMapping("/user")
    public ResponseEntity<User> save(@Valid @RequestBody User user) throws URISyntaxException {
        User savedUser = userService.saveOrUpdate(user);
        return ResponseEntity.created(new URI("/api/user/" + savedUser.getId())).body(savedUser);
    }

    @ApiOperationSupport(ignoreParameters = {"dams", "roles"})
    @ApiOperation(value = "更新接口", notes = "更新用户")
    @Timed
    @PutMapping("/user")
    public ResponseEntity<User> update(@Valid @RequestBody User user) throws URISyntaxException {
        User savedUser = userService.saveOrUpdate(user);
        return ResponseEntity.created(new URI("/api/user/" + savedUser.getId())).body(savedUser);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "删除接口", notes = "删除用户")
    @Timed
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "查询接口", notes = "查询用户(根据id)")
    @Timed
    @GetMapping("/user/{id}")
    public ResponseEntity<User> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(userRepository.findById(id));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/users")
    public ResponseEntity<Page<User>> findAllUser(
            @QuerydslPredicate(root = User.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok().body(userRepository.findAll(predicate, pageable));
    }
}
