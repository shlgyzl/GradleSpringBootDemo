package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Dam;
import com.application.repository.jpa.DamRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.*;
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

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "DamResources大坝控制层", tags = {"Dam大坝接口"})
@RestController
@RequestMapping("api")
public class DamResources {

    private final DamRepository damRepository;

    public DamResources(DamRepository damRepository) {
        this.damRepository = damRepository;
    }

    @ApiOperation(value = "保存接口", notes = "保存大坝")
    @ApiResponses({
            @ApiResponse(code = 201, message = "保存成功", response = Dam.class)
    })
    @Timed
    @PostMapping("/dam")
    @Transactional
    public ResponseEntity<Dam> save(@Valid @RequestBody @ApiParam(name = "大坝实体") Dam dam) throws URISyntaxException {
        @Valid Dam save = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }

    @ApiOperation(value = "更新接口", notes = "更新大坝")
    @ApiResponses({
            @ApiResponse(code = 200, message = "更新成功", response = Dam.class)
    })
    @Timed
    @PutMapping("/dam")
    @Transactional
    public ResponseEntity<Dam> update(@Valid @RequestBody @ApiParam(name = "大坝实体") Dam dam) throws URISyntaxException {
        @Valid Dam save = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除大坝")
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功")
    })
    @Timed
    @DeleteMapping("/dam/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询大坝(根据id)")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Dam.class)
    })
    @Timed
    @GetMapping("/dam/{id}")
    public ResponseEntity<Dam> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(damRepository.findById(id));
    }

    @ApiOperation(value = "高级查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Dam.class, responseContainer = "List")
    })
    @Timed
    @GetMapping("/dams-all")
    public ResponseEntity<Iterable<Dam>> findAllDam(@QuerydslPredicate(root = Dam.class) Predicate predicate) {
        return ResponseEntity.ok().body(damRepository.findAll(predicate));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功", response = Dam.class, responseContainer = "List")
    })
    @Timed
    @GetMapping(value = "/dams-all", params = "page")
    @Transactional
    public ResponseEntity<Page<Dam>> findPageAllDam(
            @QuerydslPredicate(root = Dam.class) Predicate predicate,
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(damRepository.findAll(predicate, pageable));
    }
}
