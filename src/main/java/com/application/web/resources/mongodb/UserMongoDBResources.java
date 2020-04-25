package com.application.web.resources.mongodb;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.mongodb.UserMongoDB;
import com.application.repository.mongodb.UserMongoDBRepository;
import com.application.web.resources.exception.BusinessErrorException;
import com.application.web.resources.util.JPAUtils;
import com.application.web.resources.util.ResponseUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@Api(value = "UserMongoDB用户接口", tags = {"UserMongoDB用户接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
@Slf4j
public class UserMongoDBResources {
    private final UserMongoDBRepository userMongoDBRepository;

    @ApiOperationSupport
    @ApiOperation(value = "保存接口", notes = "保存用户")
    @Timed
    @PostMapping("/userMongoDB")
    public ResponseEntity<UserMongoDB> save(@Valid @RequestBody UserMongoDB userMongoDB) throws URISyntaxException {
        UserMongoDB savedUserMongoDB = userMongoDBRepository.save(userMongoDB);
        return ResponseEntity.created(new URI("/api/userMongoDB/" + savedUserMongoDB.getId())).body(savedUserMongoDB);
    }

    @ApiOperationSupport
    @ApiOperation(value = "更新接口", notes = "更新用户")
    @Timed
    @PutMapping("/userMongoDB")
    public ResponseEntity<UserMongoDB> update(@Valid @RequestBody UserMongoDB userMongoDB) throws URISyntaxException {
        UserMongoDB savedUserMongoDB = userMongoDBRepository.save(userMongoDB);
        return ResponseEntity.created(new URI("/api/userMongoDB/" + savedUserMongoDB.getId())).body(savedUserMongoDB);
    }

    @ApiParam(name = "id", value = "用户id", required = true, defaultValue = "1", example = "1")
    @ApiOperation(value = "删除接口", notes = "删除用户")
    @Timed
    @DeleteMapping("/userMongoDB/{id}")
    public ResponseEntity<Void> delete(@ApiParam(value = "主键id") @PathVariable Long id) {
        userMongoDBRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiParam(name = "id", value = "用户id", required = true, defaultValue = "1", example = "1")
    @ApiOperation(value = "查询接口", notes = "查询用户(根据id)")
    @Timed
    @GetMapping("/userMongoDB/{id}")
    public ResponseEntity<UserMongoDB> find(@ApiParam(value = "主键id") @PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(userMongoDBRepository.findById(id));
    }


    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/userMongoDBs")
    public ResponseEntity<Page<UserMongoDB>> findAll(
            @QuerydslPredicate(root = UserMongoDB.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.ok().body(userMongoDBRepository.findAll(predicate, pageable));
    }
}
