package com.application.resources.mongodb;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.mongodb.UserMongoDB;
import com.application.repository.mongodb.UserMongoDBRepository;
import com.application.resources.exception.BusinessErrorException;
import com.querydsl.core.types.Predicate;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "UserMongoDBResources用户控制层", tags = {"UserMongoDB用户信息接口"})
@RestController
@RequestMapping("api/mongodb")
@Slf4j
public class UserMongoDBResources {
    private final UserMongoDBRepository userMongoDBRepository;

    public UserMongoDBResources(UserMongoDBRepository userMongoDBRepository) {
        this.userMongoDBRepository = userMongoDBRepository;
    }


    @ApiOperation(value = "高级查询", notes = "条件限制")
    @GetMapping("/mongodb-users-all")
    @Transactional
    public ResponseEntity<Iterable<UserMongoDB>> findAllUserMongoDB(@QuerydslPredicate(root = UserMongoDB.class) Predicate predicate) {
        return ResponseEntity.ok().body(userMongoDBRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @GetMapping(value = "/mongodb-users-all", params = "page")
    @Transactional
    public ResponseEntity<Page<UserMongoDB>> findPageAllUserMongoDB(
            @QuerydslPredicate(root = UserMongoDB.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(userMongoDBRepository.findAll(predicate, pageable));
    }

    @ApiOperation(value = "查询MongoDB用户", notes = "条件限制(根据id)")
    @GetMapping("/mongodb-user/{id}")
    public ResponseEntity<UserMongoDB> findById(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(userMongoDBRepository.findById(id));
    }

    @ApiOperation(value = "新增MongoDB用户", notes = "POST请求")
    @PostMapping("/mongodb-user")
    @Transactional
    public ResponseEntity<UserMongoDB> save(@Valid @RequestBody @ApiParam(value = "MongoDB用户实体") UserMongoDB userMongoDB) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userMongoDBRepository.save(userMongoDB));
    }

    @ApiOperation(value = "修改MongoDB用户", notes = "PUT请求")
    @PutMapping("/mongodb-user")
    @Transactional
    public ResponseEntity<UserMongoDB> update(@Valid @RequestBody @ApiParam(value = "MongoDB用户实体") UserMongoDB userMongoDB) {
        return ResponseEntity.ok(userMongoDBRepository.save(userMongoDB));
    }

    @ApiOperation(value = "删除MongoDB用户", notes = "DELETE请求")
    @DeleteMapping("/mongodb-user/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userMongoDBRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
