package com.mold.digitalization.controller;

import com.mold.digitalization.entity.Department;
import com.mold.digitalization.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/departments")
@Api(tags = "部门管理")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    @ApiOperation("获取部门列表")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<List<Department>> list(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(departmentService.search(keyword));
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取部门信息")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Department> getById(@ApiParam("部门ID") @PathVariable Long id) {
        Department department = departmentService.getById(id);
        if (department != null) {
            return ResponseEntity.ok(department);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @ApiOperation("创建部门")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Department> create(@ApiParam("部门信息") @RequestBody Department department) {
        try {
            if (departmentService.isNameExists(department.getName(), null)) {
                return ResponseEntity.badRequest().body(null);
            }
            boolean created = departmentService.create(department);
            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED).body(department);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation("更新部门信息")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Department> update(@ApiParam("部门ID") @PathVariable Long id, @ApiParam("部门信息") @RequestBody Department department) {
        Department exist = departmentService.getById(id);
        if (exist == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (departmentService.isNameExists(department.getName(), id)) {
            return ResponseEntity.badRequest().body(null);
        }
        
        department.setId(id);
        boolean updated = departmentService.update(department);
        if (updated) {
            return ResponseEntity.ok(department);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除部门")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Void> delete(@ApiParam("部门ID") @PathVariable Long id) {
        Department exist = departmentService.getById(id);
        if (exist == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 检查部门是否有子部门
        if (departmentService.hasChildren(id)) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean deleted = departmentService.delete(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-name")
    @ApiOperation("检查部门名称是否存在")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Map<String, Boolean>> checkName(@RequestParam String name, @RequestParam(required = false) Long excludeId) {
        boolean exists = departmentService.isNameExists(name, excludeId);
        Map<String, Boolean> result = Map.of("exists", exists);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/tree")
    @ApiOperation("获取部门树结构")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<List<Department>> getTree(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(departmentService.getDepartmentTree(keyword));
    }
    
    @GetMapping("/children/{parentId}")
    @ApiOperation("获取子部门列表")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<List<Department>> getChildren(@ApiParam("父部门ID") @PathVariable Long parentId) {
        return ResponseEntity.ok(departmentService.getChildrenByParentId(parentId));
    }
    
    @GetMapping("/has-children/{id}")
    @ApiOperation("检查部门是否有子部门")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Map<String, Boolean>> hasChildren(@ApiParam("部门ID") @PathVariable Long id) {
        boolean hasChildren = departmentService.hasChildren(id);
        Map<String, Boolean> result = Map.of("hasChildren", hasChildren);
        return ResponseEntity.ok(result);
    }
}
