package com.application.resources;

import com.application.domain.enumeration.BusinessErrorType;
import com.application.domain.jpa.Dam;
import com.application.repository.jpa.DamRepository;
import com.application.resources.exception.BusinessErrorException;
import com.application.resources.util.ResponseUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

/**
 * @author yanghaiyong 2019年11月5日 23:51:35
 */

@Api(value = "Dam", tags = {"Dam大坝管理接口"})
@RestController
@RequestMapping("api")
public class DamResources {

    private final DamRepository damRepository;

    public DamResources(DamRepository damRepository) {
        this.damRepository = damRepository;
    }

    @ApiOperation(value = "保存接口", notes = "保存大坝")
    @Timed
    @PostMapping("/dam")
    public ResponseEntity<Dam> save(@Valid @RequestBody Dam dam) throws URISyntaxException {
        @Valid Dam save = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }

    @ApiOperation(value = "更新接口", notes = "更新大坝")
    @Timed
    @PutMapping("/dam")
    public ResponseEntity<Dam> update(@Valid @RequestBody Dam dam) throws URISyntaxException {
        @Valid Dam save = damRepository.save(dam);
        return ResponseEntity.created(new URI("/api/dam/" + save.getId())).body(save);
    }

    @ApiOperation(value = "删除接口", notes = "删除大坝")
    @Timed
    @DeleteMapping("/dam/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        damRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "查询接口", notes = "查询大坝(根据id)")
    @Timed
    @GetMapping("/dam/{id}")
    public ResponseEntity<Dam> find(@PathVariable Long id) {
        if (ObjectUtils.isEmpty(id)) {
            throw new BusinessErrorException(BusinessErrorType.PARAMETER_EXCEPTION);
        }
        return ResponseUtil.wrapOrNotFound(damRepository.findById(id));
    }

    @ApiOperation(value = "高级分页查询", notes = "条件限制")
    @Timed
    @GetMapping(value = "/dams")
    public ResponseEntity<Page<Dam>> findAllDam(
            @QuerydslPredicate(root = Dam.class) Predicate predicate,
            @ApiIgnore @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        predicate = Optional.ofNullable(predicate).orElse(new BooleanBuilder());
        return ResponseEntity.ok().body(damRepository.findAll(predicate, pageable));
    }
}
