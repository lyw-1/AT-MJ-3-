package com.mold.digitalization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.common.exception.BusinessException;
import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.mapper.UserRoleMapper;
import com.mold.digitalization.dto.UserDTO;
import com.mold.digitalization.dto.UserQueryDTO;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.enums.ErrorCodeEnum;
import com.mold.digitalization.enums.UserStatusEnum;
import com.mold.digitalization.service.UserService;
import com.mold.digitalization.service.system.OperationLogService;
import com.mold.digitalization.utils.JwtUtil;
import com.mold.digitalization.vo.LoginVO;
import com.mold.digitalization.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现绫?
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private OperationLogService operationLogService;
    
    public LoginVO login(String username, String password) {
        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            
            // 设置认证淇℃伅鍒颁笂涓嬫枃
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 获取用户淇℃伅
            User user = (User) authentication.getPrincipal();
            
            // 鐢熸垚JWT浠ょ墝
            String token = jwtUtil.generateToken(user.getUsername());
            
            // 鏋勫缓返回瀵硅薄
            LoginVO loginVO = new LoginVO();
            loginVO.setToken(token);
            loginVO.setUserId(user.getId());
            loginVO.setUsername(user.getUsername());
            loginVO.setRealName(user.getRealName());
            
            // 获取用户瑙掕壊
            List<Role> roles = userRoleMapper.selectRolesByUserId(user.getId());
            loginVO.setRoles(roles.stream()
                    .map(Role::getRoleCode)
                    .collect(Collectors.toList()));
            
            // 记录鐧诲綍鏃ュ織
            OperationLog operationLog = new OperationLog();
            operationLog.setOperationType("用户鐧诲綍");
            operationLog.setOperationDesc("用户鐧诲綍系统");
            operationLog.setUserId(user.getId());
            operationLog.setUsername(user.getUsername());
            operationLog.setResultCode("200");
            operationLog.setOperationTime(LocalDateTime.now());
            operationLogService.saveOperationLog(operationLog);
            
            return loginVO;
        } catch (BadCredentialsException e) {
            // 记录鐧诲綍失败
            recordLoginFailure(username);
            throw new BusinessException(ErrorCodeEnum.LOGIN_FAILED);
        } catch (AuthenticationException e) {
            throw new BusinessException(ErrorCodeEnum.LOGIN_FAILED);
        }
    }
    
    /**
     * 记录鐧诲綍失败
     * 鐢变簬数据搴撹〃涓病鏈塴ogin_failed_count瀛楁锛岃繖閲岀洿鎺ラ攣瀹氱敤鎴?
     */
    private void recordLoginFailure(String username) {
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            // 鐩存帴閿佸畾用户锛屽洜涓烘暟鎹簱琛ㄤ腑娌℃湁login_failed_count瀛楁
            user.setStatus(UserStatusEnum.LOCKED.getCode());
            user.setLockedUntil(LocalDateTime.now().plusMinutes(30));
            user.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(user);
        }
    }
    
    @Override
    @Cacheable(value = "user", key = "#id")
    public User getUserById(Long id) {
        // 仅查询基础列，避免某些环境中选择不存在的列导致 SQL 异常
        User user = userMapper.selectBasicById(id);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        return user;
    }
    
    public IPage<UserVO> queryPage(UserQueryDTO queryDTO) {
        // 鏋勫缓查询鏉′欢
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(queryDTO.getUsername()), User::getUsername, queryDTO.getUsername())
                // realName 为非持久化字段，不能作为数据库查询条件
                .eq(queryDTO.getStatus() != null, User::getStatus, queryDTO.getStatus())
                .orderByDesc(User::getCreateTime);
        
        // 鍒嗛〉查询
        Page<User> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        if (cn.hutool.core.util.StrUtil.isNotBlank(queryDTO.getUsername())) {
            params.put("username", queryDTO.getUsername());
        }
        if (queryDTO.getStatus() != null) {
            params.put("status", queryDTO.getStatus());
        }
        IPage<User> userPage = userMapper.selectPage(page, params);
        
        // 杞崲涓篤O
        IPage<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> voList = userPage.getRecords().stream()
                .map(user -> {
                    UserVO userVO = new UserVO();
                    BeanUtil.copyProperties(user, userVO);
                    
                    // 获取用户瑙掕壊
                    List<Role> roles = userRoleMapper.selectRolesByUserId(user.getId());
                    userVO.setRoles(roles.stream()
                            .map(Role::getRoleCode)
                            .collect(Collectors.toList()));
                    
                    return userVO;
                })
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", key = "#user.id")
    public boolean updateUser(User user) {
        // 妫€鏌ョ敤鎴锋槸鍚﹀瓨鍦?
        User existUser = userMapper.selectBasicById(user.getId());
        if (existUser == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        // 妫€鏌ョ敤鎴峰悕鏄惁閲嶅
        if (!existUser.getUsername().equals(user.getUsername())) {
            User checkUser = userMapper.selectByUsername(user.getUsername());
            if (checkUser != null) {
                throw new BusinessException(ErrorCodeEnum.USERNAME_ALREADY_EXISTS);
            }
            userMapper.updateUsername(user.getId(), user.getUsername());
        }
        
        // 濡傛灉密码涓嶄负绌猴紝鍒欏姞瀵嗗瘑鐮?
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 濡傛灉密码为空锛屼繚鐣欏師密码
            user.setPassword(existUser.getPassword());
        }
        
        // 仅更新基础列，避免环境中不存在扩展列导致SQL异常
        Integer newStatus = user.getStatus();
        String newPassword = StrUtil.isNotBlank(user.getPassword()) ? user.getPassword() : null;
        int result = userMapper.updateBasic(user.getId(), newPassword, newStatus);
        try {
            boolean hasReal = userMapper.existsUserColumn("real_name") > 0;
            boolean hasDept = userMapper.existsUserColumn("department_name") > 0;
            boolean hasPhone = userMapper.existsUserColumn("phone") > 0;
            boolean hasEmail = userMapper.existsUserColumn("email") > 0;
            if (hasReal || hasDept || hasPhone || hasEmail) {
                userMapper.updateExtraFields(user.getId(), hasReal ? user.getRealName() : null, hasDept ? user.getDepartment() : null, hasPhone ? user.getPhone() : null, hasEmail ? user.getEmail() : null);
            }
        } catch (Exception ignored) {
            // 忽略异常
        }
        
        // 记录操作鏃ュ織
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType("更新用户");
        operationLog.setOperationDesc("更新用户淇℃伅: " + user.getUsername());
        operationLog.setUserId(getCurrentUserId());
        operationLog.setUsername(getCurrentUsername());
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
        
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", allEntries = true)
    public boolean createUser(User user) {
        // 妫€鏌ョ敤鎴峰悕鏄惁宸插瓨鍦?
        User existUser = userMapper.selectByUsername(user.getUsername());
        if (existUser != null) {
            throw new BusinessException(ErrorCodeEnum.USERNAME_ALREADY_EXISTS);
        }
        
        // 鍔犲瘑密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置榛樿状态
        user.setStatus(UserStatusEnum.ACTIVE.getCode());
        
        // 淇濆瓨用户
        int result = userMapper.insert(user);
        
        // 记录操作鏃ュ織
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType("创建用户");
        operationLog.setOperationDesc("创建用户: " + user.getUsername());
        operationLog.setUserId(getCurrentUserId());
        operationLog.setUsername(getCurrentUsername());
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
        
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", key = "#id")
    public boolean deleteUser(Long id) {
        // 妫€鏌ョ敤鎴锋槸鍚﹀瓨鍦?
        // 使用基础列查询，避免选择不存在的列
        User user = userMapper.selectBasicById(id);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        // 删除用户瑙掕壊鍏宠仈
        userRoleMapper.deleteByUserId(id);
        
        // 删除用户
        int result = userMapper.deleteById(id);
        
        // 记录操作鏃ュ織
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType("删除用户");
        operationLog.setOperationDesc("删除用户: " + user.getUsername());
        operationLog.setUserId(getCurrentUserId());
        operationLog.setUsername(getCurrentUsername());
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
        
        return result > 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", key = "#id")
    public boolean resetPassword(Long id, String newPassword) {
        // 妫€鏌ョ敤鎴稩D鏄惁涓簄ull
        if (id == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        // 妫€鏌ョ敤鎴锋槸鍚﹀瓨鍦?
        // 使用基础列查询，避免选择不存在的列
        User user = userMapper.selectBasicById(id);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        // 濡傛灉鏂板瘑鐮佷负绌猴紝鍒欑敓鎴愰殢鏈哄瘑鐮?
        if (StrUtil.isBlank(newPassword)) {
            newPassword = generateRandomPassword();
        }
        
    // 鍔犲瘑鏂板瘑鐮?
    String encryptedPassword = passwordEncoder.encode(newPassword);
        
        // 更新用户瀵硅薄
        user.setPassword(encryptedPassword);
        user.setStatus(UserStatusEnum.ACTIVE.getCode());
        user.setLockedUntil(null);
        
        // 浣跨敤updateById方法更新用户淇℃伅
        int result = userMapper.updateById(user);
        
        // 记录操作鏃ュ織
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType("閲嶇疆密码");
        operationLog.setOperationDesc("閲嶇疆用户密码: " + user.getUsername());
        operationLog.setUserId(getCurrentUserId());
        operationLog.setUsername(getCurrentUsername());
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
        
        return result > 0;
    }
    
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "user", key = "#id")
    public void changePassword(Long id, String oldPassword, String newPassword) {
        // 妫€鏌ョ敤鎴锋槸鍚﹀瓨鍦?
        // 使用基础列查询，避免选择不存在的列
        User user = userMapper.selectBasicById(id);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        // 验证鏃у瘑鐮?
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(ErrorCodeEnum.PASSWORD_INCORRECT);
        }
        
    // 鍔犲瘑鏂板瘑鐮?
    String encryptedPassword = passwordEncoder.encode(newPassword);
        
        // 更新用户瀵硅薄
        user.setPassword(encryptedPassword);
        
        // 浣跨敤updateById方法更新用户淇℃伅
        userMapper.updateById(user);
        
        // 记录操作鏃ュ織
        OperationLog operationLog = new OperationLog();
        operationLog.setOperationType("淇敼密码");
        operationLog.setOperationDesc("用户淇敼密码");
        operationLog.setUserId(id);
        operationLog.setUsername(user.getUsername());
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
    }
    
    @Override
    @CacheEvict(value = "user", key = "#userId")
    public boolean updateUserStatus(Long userId, Integer status) {
        // 妫€鏌ョ敤鎴锋槸鍚﹀瓨鍦?
        // 使用基础列查询，避免选择不存在的列
        User user = userMapper.selectBasicById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCodeEnum.USER_NOT_FOUND);
        }
        
        // 更新用户状态
        user.setStatus(status);
        
        // 浣跨敤updateById方法更新用户淇℃伅
        int result = userMapper.updateById(user);
        
        // 记录操作鏃ュ織
    OperationLog operationLog = new OperationLog();
    operationLog.setOperationType("Update User Status");
    operationLog.setOperationDesc("Updated user status: " + status);
        operationLog.setUserId(getCurrentUserId());
        operationLog.setUsername(getCurrentUsername());
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
        
        return result > 0;
    }
    
    /**
     * 获取用户瑙掕壊ID鍒楄〃
     */
    @Override
    public List<Long> getUserRoleIds(Long userId) {
        List<UserRole> userRoles = userRoleMapper.selectByUserId(userId);
        return userRoles.stream()
                .map(UserRole::getRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新用户瑙掕壊
     */
    @Override
    public boolean updateUserRoles(Long userId, List<Long> roleIds) {
        // 获取褰撳墠瑙掕壊
        List<Long> currentRoleIds = getUserRoleIds(userId);
        
        // 璁＄畻闇€瑕佸垹闄ょ殑瑙掕壊
        List<Long> toDeleteRoleIds = currentRoleIds.stream()
                .filter(roleId -> !roleIds.contains(roleId))
                .collect(Collectors.toList());
        
        // 璁＄畻闇€瑕佹坊鍔犵殑瑙掕壊
        List<Long> toAddRoleIds = roleIds.stream()
                .filter(roleId -> !currentRoleIds.contains(roleId))
                .collect(Collectors.toList());
        
        // 删除瑙掕壊
        if (!toDeleteRoleIds.isEmpty()) {
            userRoleMapper.deleteByUserId(userId, toDeleteRoleIds);
        }
        
        // 添加瑙掕壊
        if (!toAddRoleIds.isEmpty()) {
            // 鎵归噺创建UserRole瀵硅薄
            List<UserRole> userRoles = toAddRoleIds.stream()
                    .map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRole.setCreateTime(LocalDateTime.now());
                        userRole.setUpdateTime(LocalDateTime.now());
                        return userRole;
                    })
                    .collect(Collectors.toList());

            // 鎵归噺淇濆瓨
            userRoleMapper.batchInsert(userRoles);
        }
        
        return true;
    }
    
    /**
     * 获取褰撳墠用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            String username = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
            User user = userMapper.selectByUsername(username);
            return user != null ? user.getId() : null;
        }
        return null;
    }
    
    /**
     * 获取褰撳墠用户名?
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            return ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        }
        return null;
    }
    
    /**
     * 鐢熸垚闅忔満密码
     */
    private String generateRandomPassword() {
        // 新规则：仅字母与数字，且需同时包含字母与数字；默认长度10
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String allChars = letters + digits;

        StringBuilder password = new StringBuilder();
        // 保证至少包含一个字母与一个数字
        password.append(RandomUtil.randomChar(letters));
        password.append(RandomUtil.randomChar(digits));
        // 填充剩余长度至10
        for (int i = 2; i < 10; i++) {
            password.append(RandomUtil.randomChar(allChars));
        }

        // 返回构造的密码串（已满足新复杂度规则）
        return password.toString();
    }
    
    /**
     * 验证密码澶嶆潅锟?
     */
    private boolean isValidPasswordComplexity(String password) {
        if (StrUtil.isBlank(password)) {
            return false;
        }

        // 新规则：长度≥8，仅字母和数字，且需同时包含字母与数字
        if (password.length() < 8) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                // 出现非字母数字字符，直接不通过
                return false;
            }
        }

        return hasLetter && hasDigit;
    }
    
    /**
     * 娓呴櫎用户缂撳瓨鍜屼护鐗?
     */
    private void clearUserCacheAndToken(Long userId) {
        // 娓呴櫎用户缂撳瓨
        // 娉ㄦ剰锛欯CacheEvict娉ㄨВ浼氳嚜鍔ㄥ鐞嗙紦瀛樻竻闄?
        
        // 杩欓噷可以添加浠ょ墝榛戝悕鍗曢€昏緫锛屼娇褰撳墠浠ょ墝澶辨晥
        // 实际项目涓彲鑳介渶瑕佺淮鎶や竴涓护鐗岄粦鍚嶅崟
    }
    
    /**
     * 记录密码閲嶇疆鏃ュ織
     */
    private void recordPasswordResetLog(Long userId, String username) {
    OperationLog operationLog = new OperationLog();
    operationLog.setOperationType("Password Reset");
    operationLog.setOperationDesc("Admin reset user password");
        operationLog.setUserId(userId);
        operationLog.setUsername(username);
        operationLog.setResultCode("200");
        operationLog.setOperationTime(LocalDateTime.now());
        operationLogService.saveOperationLog(operationLog);
    }
    
    /**
     * 验证用户鐧诲綍
     */
    @Override
    public boolean validateUser(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return false;
        }
        
        // 验证密码
        return passwordEncoder.matches(password, user.getPassword());
    }
    
    /**
     * 获取用户鍒楄〃锛堝垎椤碉級
     */
    @Override
    public Map<String, Object> getUserList(int page, int pageSize, String keyword) {
        // 鏋勫缓查询鏉′欢
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            queryWrapper.like(User::getUsername, keyword)
                    .or()
                    .like(User::getPhone, keyword);
        }
        queryWrapper.orderByDesc(User::getCreateTime);
        
        // 鍒嗛〉查询
        Page<User> pageInfo = new Page<>(page, pageSize);
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        if (cn.hutool.core.util.StrUtil.isNotBlank(keyword)) {
            params.put("username", keyword);
        }
        IPage<User> userPage = userMapper.selectPage(pageInfo, params);
        
        // 鏋勫缓返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", userPage.getTotal());
        result.put("current", userPage.getCurrent());
        result.put("size", userPage.getSize());
        result.put("pages", userPage.getPages());
        result.put("records", userPage.getRecords());
        
        return result;
    }
    

    
    /**
     * 获取鎵€鏈夌敤鎴峰垪琛?
     */
    @Override
    public List<User> getAllUsers() {
        try {
            boolean hasReal = userMapper.existsUserColumn("real_name") > 0;
            boolean hasDept = userMapper.existsUserColumn("department_name") > 0;
            boolean hasPhone = userMapper.existsUserColumn("phone") > 0;
            boolean hasEmail = userMapper.existsUserColumn("email") > 0;
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> qw = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            java.util.List<String> cols = new java.util.ArrayList<>();
            cols.add("id");
            cols.add("username");
            cols.add("password");
            cols.add("status");
            if (hasReal) {
                cols.add("real_name");
            }
            if (hasDept) {
                cols.add("department_name");
            }
            if (hasPhone) {
                cols.add("phone");
            }
            if (hasEmail) {
                cols.add("email");
            }
            qw.select(cols.toArray(new String[0])).orderByDesc("id");
            return userMapper.selectList(qw);
        } catch (Exception e) {
            return userMapper.selectAllBasic();
        }
    }
    
    /**
     * 鏍规嵁鎵嬫満鍙疯幏鍙栫敤鎴?
     */
    @Override
    public User getUserByPhone(String phone) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        return userMapper.selectOne(queryWrapper);
    }
    
    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
    // /**
    //  * 鏍规嵁閭获取用户锛堝疄闄呮寜鎵嬫満鍙锋煡璇紝鍥犱负数据搴撹〃涓病鏈塭mail瀛楁锛?
    //  */
    // @Override
    // public User getUserByEmail(String email) {
    //     QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    //     queryWrapper.eq("phone", email);
    //     return userMapper.selectOne(queryWrapper);
    // }
    
    /**
     * 鏍规嵁用户名嶈幏鍙栫敤鎴?
     */
    @Override
    public User getUserByUsername(String username) {
        // 浣跨敤XML涓畾涔夌殑selectByUsername方法锛岄伩鍏嶆煡璇笉瀛樺湪鐨勫瓧娈?
        return userMapper.selectByUsername(username);
    }
    
    /**
     * 鏍规嵁用户ID获取用户瑙掕壊鍒楄〃
     */
    @Override
    public List<Role> getUserRoles(Long userId) {
        // 鐩存帴浣跨敤userRoleMapper鐨剆electRolesByUserId方法获取瑙掕壊鍒楄〃
        return userRoleMapper.selectRolesByUserId(userId);
    }
    
}
