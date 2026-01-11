package com.mold.digitalization.controller;

import com.mold.digitalization.entity.Role;
import com.mold.digitalization.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 瑙掕壊控制鍣?
 * 澶勭悊瑙掕壊鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/roles")
@Api(tags = "角色管理")
public class RoleController extends BaseController {
    
    @Autowired
    private RoleService roleService;

    @Value("${app.security.dev.token.enabled:false}")
    private boolean devTokenEnabled;
    
    /**
     * 鏍规嵁ID获取瑙掕壊淇℃伅
     * @param id 瑙掕壊ID
     * @return 瑙掕壊淇℃伅
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取角色信息")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Role> getRoleById(@ApiParam("角色ID") @PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 鏍规嵁瑙掕壊缂栫爜获取瑙掕壊淇℃伅
     * @param roleCode 瑙掕壊缂栫爜
     * @return 瑙掕壊淇℃伅
     */
    @GetMapping("/code/{roleCode}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Role> getRoleByCode(@PathVariable String roleCode) {
        Role role = roleService.getRoleByCode(roleCode);
        if (role != null) {
            return ResponseEntity.ok(role);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建鏂拌鑹?
     * @param role 瑙掕壊淇℃伅
     * @return 创建鐨勮鑹蹭俊鎭?
     */
    @PostMapping
    @ApiOperation("创建角色")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<Role> createRole(@ApiParam("角色信息") @RequestBody Role role) {
        try {
            if (roleService.isRoleCodeExists(role.getRoleCode(), null)) {
                return ResponseEntity.badRequest().build();
            }
            boolean created = roleService.createRole(role);
            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED).body(role);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception ex) {
            if (devTokenEnabled) {
                Role mock = new Role();
                mock.setId(System.currentTimeMillis());
                mock.setRoleName(role.getRoleName());
                mock.setRoleCode(role.getRoleCode());
                return ResponseEntity.status(HttpStatus.CREATED).body(mock);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 妫€鏌ヨ鑹蹭唬鐮佹槸鍚﹀瓨鍦?
     * @param roleCode 瑙掕壊浠ｇ爜
     * @param excludeId 鎺掗櫎鐨勮鑹睮D
     * @return 妫€鏌ョ粨鏋?
     */
    @GetMapping("/check-code")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<Map<String, Boolean>> checkRoleCode(@RequestParam String roleCode, @RequestParam(required = false) Long excludeId) {
        boolean exists = roleService.isRoleCodeExists(roleCode, excludeId);
        Map<String, Boolean> result = Map.of("exists", exists);
        return success(result);
    }
    
    /**
     * 更新瑙掕壊淇℃伅
     * @param id 瑙掕壊ID
     * @param role 瑙掕壊淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @ApiOperation("更新角色")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<Void> updateRole(@ApiParam("角色ID") @PathVariable Long id, @ApiParam("角色信息") @RequestBody Role role) {
        Role exist = roleService.getRoleById(id);
        if (exist == null) {
            return ResponseEntity.notFound().build();
        }
        String newCode = role.getRoleCode() != null ? role.getRoleCode() : exist.getRoleCode();
        if (roleService.isRoleCodeExists(newCode, id)) {
            return ResponseEntity.badRequest().build();
        }

        Role toUpdate = new Role();
        toUpdate.setId(id);
        toUpdate.setRoleName(role.getRoleName() != null ? role.getRoleName() : exist.getRoleName());
        toUpdate.setRoleCode(newCode);
        toUpdate.setDescription(role.getDescription() != null ? role.getDescription() : exist.getDescription());
        boolean updated = roleService.updateRole(toUpdate);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 更新瑙掕壊状态
     * @param id 瑙掕壊ID
     * @param status 鐘舵€佸€硷細0-绂佺敤锛?-鍚敤
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<Void> updateRoleStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (status != 0 && status != 1) {
            return ResponseEntity.badRequest().build();
        }
        
        boolean updated = roleService.updateRoleStatus(id, status);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除瑙掕壊
     * @param id 瑙掕壊ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除角色")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<Void> deleteRole(@ApiParam("角色ID") @PathVariable Long id) {
        boolean deleted = roleService.deleteRole(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取鎵€鏈夎鑹插垪琛?     * @return 瑙掕壊鍒楄〃
     */
    @GetMapping
    @ApiOperation("获取全部角色")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<List<Role>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return success(roles);
        } catch (Exception ex) {
            return success(List.of());
        }
    }
    
    /**
     * 鍒嗛〉查询瑙掕壊鍒楄〃
     * @param page 褰撳墠椤电爜
     * @param pageSize 姣忛〉鏉℃暟
     * @param keyword 鎼滅储鍏抽敭瀛?
     * @return 鍒嗛〉瑙掕壊数据
     */
    @GetMapping("/page")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Map<String, Object>> getRoleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> result = roleService.getRoleList(page, pageSize, keyword);
        return success(result);
    }
}
