package com.mold.digitalization.controller;

import com.mold.digitalization.entity.MoldInitialParameter;
import com.mold.digitalization.service.MoldInitialParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mold-initial-parameters")
public class MoldInitialParameterController extends BaseController {

    @Autowired
    private MoldInitialParameterService service;

    @GetMapping("/{id}")
    public ResponseEntity<MoldInitialParameter> getById(@PathVariable Long id) {
        MoldInitialParameter p = service.getById(id);
        if (p != null) return success(p);
        return notFoundGeneric("Not found");
    }

    @GetMapping("/mold/{moldCode}")
    public ResponseEntity<List<MoldInitialParameter>> getByMoldCode(@PathVariable String moldCode) {
        return success(service.getByMoldCode(moldCode));
    }

    @GetMapping("/mold/{moldCode}/latest")
    public ResponseEntity<MoldInitialParameter> getLatestByMoldCode(@PathVariable String moldCode) {
        MoldInitialParameter p = service.getLatestByMoldCode(moldCode);
        if (p != null) return success(p);
        return notFoundGeneric("Not found");
    }

    @GetMapping("/application/{applicationNo}")
    public ResponseEntity<MoldInitialParameter> getByApplicationNo(@PathVariable String applicationNo) {
        MoldInitialParameter p = service.getByApplicationNo(applicationNo);
        if (p != null) return success(p);
        return notFoundGeneric("Not found");
    }

    @PostMapping
    public ResponseEntity<MoldInitialParameter> create(@RequestBody MoldInitialParameter param) {
        // 唯一性校验：模号与申请编号均不可重复
        try {
            // 结构字段验证：只允许斜边模、直压模、收边模
            if (param.getStructure() != null && !param.getStructure().isBlank()) {
                List<String> allowedStructures = List.of("斜边模", "直压模", "收边模");
                if (!allowedStructures.contains(param.getStructure())) {
                    return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST)
                            .body(null);
                }
            }
            
            if (param.getMoldCode() != null) {
                MoldInitialParameter existsByCode = service.getLatestByMoldCode(param.getMoldCode());
                if (existsByCode != null) {
                    return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                            .body(null);
                }
            }
            if (param.getApplicationNo() != null) {
                MoldInitialParameter existsByApp = service.getByApplicationNo(param.getApplicationNo());
                if (existsByApp != null) {
                    return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT)
                            .body(null);
                }
            }
            boolean ok = service.create(param);
            if (ok) return success(param);
            return ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MoldInitialParameter param) {
        // 结构字段验证：只允许斜边模、直压模、收边模
        if (param.getStructure() != null && !param.getStructure().isBlank()) {
            List<String> allowedStructures = List.of("斜边模", "直压模", "收边模");
            if (!allowedStructures.contains(param.getStructure())) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).build();
            }
        }
        param.setId(id);
        boolean ok = service.updateParam(param);
        if (ok) return success();
        return notFoundGeneric("Update failed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            // 优先使用业务删除
            boolean ok = service.deleteParam(id);
            if (!ok) {
                // 兜底：直接按主键删除
                ok = service.removeById(id);
            }
            if (!ok) return notFoundGeneric("Not Found");
            return success(java.util.Map.of("id", id));
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> listAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String applicationNumber,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) String moldNumber,
            @RequestParam(required = false) String specification,
            @RequestParam(required = false) String totalShrinkage,
            @RequestParam(required = false) String responsiblePerson,
            @RequestParam(required = false) String remarks) {
        try {
            // 构建查询条件
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MoldInitialParameter> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            
            if (applicationNumber != null && !applicationNumber.isBlank()) {
                queryWrapper.like("application_no", applicationNumber);
            }
            if (productCategory != null && !productCategory.isBlank()) {
                queryWrapper.like("category", productCategory);
            }
            if (moldNumber != null && !moldNumber.isBlank()) {
                queryWrapper.like("mold_code", moldNumber);
            }
            if (specification != null && !specification.isBlank()) {
                queryWrapper.like("product_spec", specification);
            }
            if (totalShrinkage != null && !totalShrinkage.isBlank()) {
                queryWrapper.like("total_shrinkage", totalShrinkage);
            }
            if (responsiblePerson != null && !responsiblePerson.isBlank()) {
                queryWrapper.like("owner_name", responsiblePerson);
            }
            if (remarks != null && !remarks.isBlank()) {
                queryWrapper.like("remark", remarks);
            }
            
            // 构建分页信息
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<MoldInitialParameter> pageParam = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
            
            // 执行分页查询
            com.baomidou.mybatisplus.core.metadata.IPage<MoldInitialParameter> result = service.page(pageParam, queryWrapper);
            
            // 构建响应数据
            java.util.Map<String, Object> response = new java.util.HashMap<>();
            response.put("list", result.getRecords());
            response.put("total", result.getTotal());
            response.put("page", result.getCurrent());
            response.put("size", result.getSize());
            
            return success(response);
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<MoldInitialParameter>> search(
            @RequestParam(required = false) String applicationNo,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String moldCode,
            @RequestParam(required = false) String productSpec,
            @RequestParam(required = false) String material,
            @RequestParam(required = false) String ownerName
    ) {
        var qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MoldInitialParameter>();
        if (applicationNo != null && !applicationNo.isBlank()) qw.like("application_no", applicationNo);
        if (category != null && !category.isBlank()) qw.eq("category", category);
        if (moldCode != null && !moldCode.isBlank()) qw.like("mold_code", moldCode);
        if (productSpec != null && !productSpec.isBlank()) qw.eq("product_spec", productSpec);
        if (material != null && !material.isBlank()) qw.eq("material", material);
        if (ownerName != null && !ownerName.isBlank()) qw.like("owner_name", ownerName);
        qw.orderByDesc("update_time");
        return success(service.list(qw));
    }

    @PutMapping("/{id}/fields")
    public ResponseEntity<MoldInitialParameter> updateFields(@PathVariable Long id, @RequestBody MoldInitialParameter payload) {
        MoldInitialParameter one = service.getById(id);
        if (one == null) return notFoundGeneric("Not Found");
        // 唯一性校验：当变更模号或申请编号时，需确保不与其他记录重复
        if (payload.getMoldCode() != null) {
            MoldInitialParameter byCode = service.getLatestByMoldCode(payload.getMoldCode());
            if (byCode != null && !byCode.getId().equals(id)) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(null);
            }
        }
        if (payload.getApplicationNo() != null) {
            MoldInitialParameter byApp = service.getByApplicationNo(payload.getApplicationNo());
            if (byApp != null && !byApp.getId().equals(id)) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(null);
            }
        }
        // 结构字段验证：只允许斜边模、直压模、收边模
        if (payload.getStructure() != null) {
            List<String> allowedStructures = List.of("斜边模", "直压模", "收边模");
            if (!allowedStructures.contains(payload.getStructure())) {
                return ResponseEntity.status(org.springframework.http.HttpStatus.BAD_REQUEST).body(null);
            }
        }
        // 简单字段更新
        one.setApplicationNo(payload.getApplicationNo());
        one.setCategory(payload.getCategory());
        one.setMoldCode(payload.getMoldCode());
        one.setProductSpec(payload.getProductSpec());
        one.setMaterial(payload.getMaterial());
        one.setHrc(payload.getHrc());
        one.setStructure(payload.getStructure());
        one.setTotalShrinkage(payload.getTotalShrinkage());
        one.setCoreSize(payload.getCoreSize());
        one.setOutline(payload.getOutline());
        one.setThickness(payload.getThickness());
        one.setLocationHolePitch(payload.getLocationHolePitch());
        one.setInletDiameter(payload.getInletDiameter());
        one.setHoleCount(payload.getHoleCount());
        one.setHoleDepth(payload.getHoleDepth());
        one.setPorosityType(payload.getPorosityType());
        one.setSlotWidth(payload.getSlotWidth());
        one.setSlotDepth(payload.getSlotDepth());
        one.setCutInAmount(payload.getCutInAmount());
        one.setCenterDistance(payload.getCenterDistance());
        one.setFeedRatio(payload.getFeedRatio());
        one.setCoreStepHeight(payload.getCoreStepHeight());
        one.setOwnerName(payload.getOwnerName());
        one.setRemark(payload.getRemark());
        one.setUpdateTime(java.time.LocalDateTime.now());
        service.updateById(one);
        return success(one);
    }

    // 上方已提供统一删除逻辑

    @DeleteMapping("/by-app/{appNo}")
    public ResponseEntity<?> deleteByApplicationNo(@PathVariable String appNo) {
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<MoldInitialParameter> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        qw.eq("application_no", appNo);
        MoldInitialParameter one = service.getOne(qw, false);
        if (one == null) return notFoundGeneric("Not Found");
        boolean ok = service.removeById(one.getId());
        if (!ok) return internalServerError("Delete failed");
        return success(java.util.Map.of("id", one.getId()));
    }
}
