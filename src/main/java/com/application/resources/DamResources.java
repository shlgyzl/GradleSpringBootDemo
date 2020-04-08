package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Dam;
import com.application.repository.jpa.DamRepository;
import com.application.resources.exception.BusinessErrorException;
import com.querydsl.core.types.Predicate;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "DamResources大坝控制层", tags = {"Dam大坝接口"})
@RestController
@RequestMapping("api")
public class DamResources {

    @Resource
    private DamRepository damRepository;

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @GetMapping("/dams-all")
    @Transactional
    public ResponseEntity<Iterable<Dam>> findAllDam(@QuerydslPredicate(root = Dam.class) Predicate predicate) {
        return ResponseEntity.ok().body(damRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @GetMapping(value = "/dams-all", params = "page")
    @Transactional
    public ResponseEntity<Page<Dam>> findPageAllDam(
            @QuerydslPredicate(root = Dam.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(damRepository.findAll(predicate, pageable));
    }

    @ApiOperation(value = "查询大坝", notes = "条件限制(根据id)")
    @GetMapping("/dam/{id}")
    public ResponseEntity<Dam> findById(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(damRepository.findById(id));
    }

    @ApiOperation(value = "新增大坝", notes = "POST请求")
    @PostMapping("/dam")
    @Transactional
    public ResponseEntity<Dam> save(@Valid @RequestBody @ApiParam(value = "大坝实体") Dam dam) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(damRepository.save(dam));
    }

    @ApiOperation(value = "修改大坝", notes = "PUT请求")
    @PutMapping("/dam")
    @Transactional
    public ResponseEntity<Dam> update(@Valid @RequestBody @ApiParam(value = "大坝实体") Dam dam) {
        return ResponseEntity.ok(damRepository.save(dam));
    }

    @ApiOperation(value = "删除大坝", notes = "DELETE请求")
    @DeleteMapping("/dam/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
