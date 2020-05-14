package com.application.web.resources.redis;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.redis.UserRedis;
import com.application.repository.redis.UserRedisRepository;
import com.application.web.resources.exception.BusinessErrorException;
import com.application.web.resources.util.ResponseUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
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

@Api(value = "UserRedis用户接口", tags = {"UserRedis用户接口"})
@RestController
@RequestMapping("api")
@AllArgsConstructor
public class UserRedisResources {
    private final UserRedisRepository userRedisRepository;

    @ApiOperationSupport(ignoreParameters = {"roles"})
    @ApiOperation(value = "保存接口", notes = "保存权限")
    @Timed
    @PostMapping("/userRedis")
    public ResponseEntity<UserRedis> save(@Valid @RequestBody UserRedis userRedis) throws URISyntaxException {
        UserRedis savedUserRedis = userRedisRepository.save(userRedis);
        return ResponseEntity.created(new URI("/api/userRedis/" + savedUserRedis.getId())).body(savedUserRedis);
    }

    @ApiOperationSupport(ignoreParameters = {"roles"})
    @ApiOperation(value = "更新接口", notes = "更新权限")
    @Timed
    @PutMapping("/userRedis")
    public ResponseEntity<UserRedis> update(@Valid @RequestBody UserRedis userRedis) throws URISyntaxException {
        UserRedis savedUserRedis = userRedisRepository.save(userRedis);
        return ResponseEntity.created(new URI("/api/userRedis/" + savedUserRedis.getId())).body(savedUserRedis);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "删除接口", notes = "删除权限")
    @Timed
    @DeleteMapping("/userRedis/{id}")
    public ResponseEntity<Void> delete(@ApiParam(value = "主键id") @PathVariable Long id) {
        userRedisRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true,
                    paramType = "path", example = "1", dataTypeClass = Long.class)
    })
    @ApiOperation(value = "查询接口", notes = "查询权限(根据id)")
    @Timed
    @GetMapping("/userRedis/{id}")
    public ResponseEntity<UserRedis> find(@ApiParam(value = "主键id") @PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(userRedisRepository.findById(id));
    }


    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/userRedis")
    public ResponseEntity<Iterable<UserRedis>> findAll(
            @QuerydslPredicate(root = UserRedis.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {


        // 目前redis没有实现分页查询
        return ResponseEntity.ok().body(userRedisRepository.findAll());
    }
}
