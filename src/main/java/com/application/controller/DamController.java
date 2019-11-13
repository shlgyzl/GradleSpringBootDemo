package com.application.controller;

import com.application.domain.Dam;
import com.application.repository.DamRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "DamController大坝控制层", tags = {"Dam大坝接口"})
@RestController
@RequestMapping("dam")
public class DamController {

    @Resource
    private DamRepository damRepository;

    @ApiOperation(value = "查询所有大坝", notes = "无任何条件限制")
    @GetMapping("/findAll")
    public ResponseEntity<List<Dam>> findAll() {
        return ResponseEntity.ok().body(new ArrayList<>());
    }

    @ApiOperation(value = "查询所有大坝", notes = "条件限制")
    @GetMapping("/findDamByCondition")
    @Transactional(value = "jpaTransactionManager")
    public ResponseEntity<List<Dam>> findDamByCondition(Dam dam) {
        Stream<Dam> stream = damRepository.findAllByIdIsNotNull();
        List<Dam> collect = stream.limit(10).collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @ApiOperation(value = "新增大坝", notes = "POST请求")
    @PostMapping("/createDam")
    public ResponseEntity<Void> createDam(@RequestBody Dam dam) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation(value = "修改大坝", notes = "PUT请求")
    @PutMapping("/updateDam")
    public ResponseEntity<Void> updateDam(@RequestBody Dam dam) {
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "删除大坝", notes = "DELETE请求")
    @DeleteMapping("/deleteDam/{id}")
    public ResponseEntity<Void> deleteDam(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }
}
