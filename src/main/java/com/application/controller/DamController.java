package com.application.controller;

import com.application.domain.jpa.Dam;
import com.application.repository.jpa.DamRepository;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "DamController大坝控制层", tags = {"Dam大坝接口"})
@RestController
@RequestMapping("api/dam")
public class DamController {

    @Resource
    private DamRepository damRepository;

    @ApiOperation(value = "查询所有大坝", notes = "无任何条件限制")
    @GetMapping("/findAll")
    public ResponseEntity<List<Dam>> findAll() {
        return ResponseEntity.ok().body(damRepository.findAll());
    }

    @ApiOperation(value = "查询所有大坝", notes = "条件限制")
    @GetMapping("/findAllByPredicate")
    @Transactional(value = "jpaTransactionManager")
    public ResponseEntity<Iterable<Dam>> findDamByCondition(@QuerydslPredicate(root = Dam.class) Predicate predicate) {
        return ResponseEntity.ok().body(damRepository.findAll(predicate));
    }

    @ApiOperation(value = "新增大坝", notes = "POST请求")
    @PostMapping("/save")
    public ResponseEntity<Dam> save(@RequestBody Dam dam) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(damRepository.save(dam));
    }

    @ApiOperation(value = "修改大坝", notes = "PUT请求")
    @PutMapping("/update")
    public ResponseEntity<Dam> update(@RequestBody Dam dam) {
        return ResponseEntity.ok(damRepository.save(dam));
    }

    @ApiOperation(value = "删除大坝", notes = "DELETE请求")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
