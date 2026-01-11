package com.mold.digitalization.controller;

import com.mold.digitalization.annotation.SensitiveOperation;
import com.mold.digitalization.dto.PasswordResetRequest;
import com.mold.digitalization.dto.ResetPasswordResult;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.dto.UserCreateRequest;
import com.mold.digitalization.dto.UserUpdateRequest;
import com.mold.digitalization.dto.UserResponse;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.service.RoleService;
import com.mold.digitalization.entity.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.mold.digitalization.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

/**
 * 用户控制鍣?
 * 澶勭悊用户鐩稿叧鐨凥TTP璇锋眰
 */
@RestController
@RequestMapping("/api/users")
@Api(tags = "用户管理")
public class UserController extends BaseController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private com.mold.digitalization.mapper.UserDepartmentMapper userDepartmentMapper;
    @Autowired
    private com.mold.digitalization.mapper.DepartmentMapper departmentMapper;
    
    /**
     * 鏍规嵁用户ID获取用户淇℃伅
     * @param id 用户ID
     * @return 用户淇℃伅
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取用户信息")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV') or #id == authentication.principal.id")
    public ResponseEntity<?> getUserById(@ApiParam("用户ID") @PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(toResponse(user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
        }
    }
    
    /**
     * 鏍规嵁用户名嶈幏鍙栫敤鎴蜂俊鎭?
     * @param username 用户名?
     * @return 用户淇℃伅
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(toResponse(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 创建鏂扮敤鎴?
     * @param user 用户淇℃伅
     * @return 创建鐨勭敤鎴蜂俊鎭?
     */
    @PostMapping
    @ApiOperation("创建用户")
    @SensitiveOperation(level = "high", description = "Create user operation")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<UserResponse> createUser(@ApiParam("用户淇℃伅") @RequestBody UserCreateRequest request) {
        // 手动映射 DTO 到实体
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setStatus(request.getStatus());
        user.setDepartment(request.getDepartment());
        user.setEmail(request.getEmail());

        boolean created = userService.createUser(user);
        if (created) {
            // 保存部门关联
            try {
                if (request.getDepartment() != null && !request.getDepartment().trim().isEmpty()) {
                    String name = request.getDepartment().trim();
                    departmentMapper.insertIgnoreByName(name);
                    Long deptId = departmentMapper.selectIdByName(name);
                    if (deptId != null) {
                        userDepartmentMapper.upsertUserDepartment(user.getId(), deptId);
                    }
                }
            } catch (Exception ignored) {}
            
            return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * 更新用户淇℃伅
     * @param id 用户ID
     * @param user 用户淇℃伅
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @ApiOperation("更新用户信息")
    @SensitiveOperation(level = "high", description = "更新用户淇℃伅操作")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<Void> updateUser(@ApiParam("用户ID") @PathVariable Long id,
                                           @ApiParam("用户淇℃伅") @RequestBody UserUpdateRequest request) {
        User exist = userService.getUserById(id);
        if (exist == null) {
            return ResponseEntity.notFound().build();
        }
        User user = new User();
        user.setId(id);
        user.setUsername(request.getUsername() != null ? request.getUsername() : exist.getUsername());
        user.setRealName(request.getRealName() != null ? request.getRealName() : exist.getRealName());
        user.setPhone(request.getPhone() != null ? request.getPhone() : exist.getPhone());
        user.setStatus(request.getStatus() != null ? request.getStatus() : exist.getStatus());
        user.setDepartment(request.getDepartmentName() != null ? request.getDepartmentName() : exist.getDepartment());
        user.setEmail(request.getEmail() != null ? request.getEmail() : exist.getEmail());

        boolean updated = userService.updateUser(user);
        try {
            if (request.getDepartmentName() != null && !request.getDepartmentName().trim().isEmpty()) {
                String name = request.getDepartmentName().trim();
                departmentMapper.insertIgnoreByName(name);
                Long deptId = departmentMapper.selectIdByName(name);
                if (deptId != null) {
                    userDepartmentMapper.upsertUserDepartment(id, deptId);
                }
            }
        } catch (Exception ignored) {}
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户")
    @SensitiveOperation(level = "critical", description = "删除用户操作")
    @PreAuthorize("hasAnyRole('ADMIN','DEV')")
    public ResponseEntity<Void> deleteUser(@ApiParam("用户ID") @PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * 获取鎵€鏈夌敤鎴峰垪琛?
     * @return 用户鍒楄〃
     */
    @GetMapping
    @ApiOperation("Get all users")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','DEV')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        java.util.List<UserResponse> list = new java.util.ArrayList<>();
        for (User u : users) {
            UserResponse r = toResponse(u);
            try {
                java.util.List<Role> roles = roleService.getRolesByUserId(u.getId());
                java.util.List<String> names = new java.util.ArrayList<>();
                for (Role role : roles) {
                    String s = role.getRoleCode() != null ? role.getRoleCode() : role.getRoleName();
                    if (s != null && !s.isEmpty()) names.add(s);
                }
                r.setRoles(names);
            } catch (Exception ignored) {}
            list.add(r);
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/dev-create")
    @PreAuthorize("hasRole('DEV')")
    public ResponseEntity<?> devCreate(@RequestParam String username,
                                       @RequestParam(required = false, defaultValue = "devpass") String password,
                                       @RequestParam(required = false, defaultValue = "开发用户") String realName,
                                       @RequestParam(required = false, defaultValue = "13800000000") String phone,
                                       @RequestParam(required = false, defaultValue = "1") Integer status) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRealName(realName);
        user.setPhone(phone);
        user.setStatus(status);
        boolean ok = userService.createUser(user);
        if (!ok) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(java.util.Map.of("code",500,"message","create user failed"));
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("id", user.getId()));
    }
    
    /**
     * 用户鐧诲綍
     * @param username 用户名?
     * @param password 密码
     * @return 鐧诲綍结果锛堣繖閲岀畝鍖栧鐞嗭紝实际搴旇繑鍥瀟oken绛変俊鎭級
     */
    @PostMapping("/login")
    @PreAuthorize("permitAll")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean loginSuccess = userService.validateUser(username, password);
        if (loginSuccess) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
    
    /**
     * 管理员橀噸缃敤鎴峰瘑鐮?
     * @param id 用户ID
     * @param request 密码重置请求
     * @return 閲嶇疆结果
     */
    @PostMapping("/{id}/reset-password")
    @ApiOperation("Reset user's password")
    // 允许 ADMIN 与 SUPER_ADMIN 执行重置，避免超级管理员因角色不匹配导致 403
    @PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN','DEV')")
    @SensitiveOperation(level = "critical", description = "Reset user password")
    public ResponseEntity<?> resetUserPassword(@ApiParam("用户ID") @PathVariable Long id, 
                                                              @ApiParam("密码重置请求") @jakarta.validation.Valid @RequestBody PasswordResetRequest request) {
        // 妫€鏌ョ敤鎴锋槸鍚﹀瓨鍦?
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("用户不存在");
        }
        
        // 重置密码：若未提供新密码，则使用符合规则的默认初始密码
        String newPassword = request.getNewPassword();
        boolean isDefaultPassword = newPassword == null || newPassword.trim().isEmpty();
        if (isDefaultPassword) {
            // 动态生成符合规则的默认初始密码
            newPassword = PasswordUtil.generateDefaultPassword();
        }
        
        // 执行密码閲嶇疆
        boolean resetSuccess = userService.resetPassword(id, newPassword);
        if (resetSuccess) {
            // 鏋勫缓閲嶇疆结果
            ResetPasswordResult result = new ResetPasswordResult();
            result.setUserId(id);
            result.setResetTime(new Date());
            result.setDefaultPassword(isDefaultPassword);
            if (isDefaultPassword) {
                // 将默认密码值返回给管理员（响应中返回，操作日志中会进行脱敏处理）
                result.setDefaultPasswordValue(newPassword);
            }

            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * 将实体映射为响应 DTO
     */
    private UserResponse toResponse(User user) {
        if (user == null) return null;
        UserResponse resp = new UserResponse();
        resp.setId(user.getId());
        resp.setUsername(user.getUsername());
        resp.setRealName(user.getRealName());
        resp.setPhone(user.getPhone());
        resp.setEmail(user.getEmail());
        // 优先从关联表获取部门名
        try {
            String dept = userDepartmentMapper.selectDepartmentNameByUserId(user.getId());
            resp.setDepartmentName(dept != null ? dept : user.getDepartment());
        } catch (Exception e) { resp.setDepartmentName(user.getDepartment()); }
        resp.setStatus(user.getStatus());
        try {
            // 如果实体包含锁定字段，则映射；否则忽略
            java.lang.reflect.Method m = user.getClass().getMethod("getLocked");
            Object lockedVal = m.invoke(user);
            if (lockedVal instanceof Boolean) {
                resp.setLocked((Boolean) lockedVal);
            }
        } catch (Exception ignored) {}
        return resp;
    }
}
