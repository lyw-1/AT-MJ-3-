package com.mold.digitalization.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mold.digitalization.mapper.UserRoleMapper;
import com.mold.digitalization.entity.UserRole;
import com.mold.digitalization.event.UserRoleChangeEvent;
import com.mold.digitalization.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户瑙掕壊鍏宠仈服务实现绫?
 * 实现用户瑙掕壊鍏宠仈鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉?*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceImpl.class);
    
    @Autowired
    private UserRoleMapper userRoleMapper;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 鏍规嵁用户ID查询用户鐨勬墍鏈夎鑹?
     * @param userId 用户ID
     * @return 用户瑙掕壊鍏宠仈鍒楄〃
     */
    @Override
    public List<UserRole> getUserRolesByUserId(Long userId) {
        if (userId == null) {
            logger.warn("Query user roles failed: userId is null");
            return new ArrayList<>();
        }
        logger.debug("Querying user roles by userId={}", userId);
        return userRoleMapper.selectByUserId(userId);
    }

    /**
     * 鏍规嵁瑙掕壊ID查询鎷ユ湁璇ヨ鑹茬殑鎵€鏈夌敤鎴?
     * @param roleId 瑙掕壊ID
     * @return 用户瑙掕壊鍏宠仈鍒楄〃
     */
    @Override
    public List<UserRole> getUserRolesByRoleId(Long roleId) {
        if (roleId == null) {
            logger.warn("Query user roles failed: roleId is null");
            return new ArrayList<>();
        }
        logger.debug("Querying user roles by roleId={}", roleId);
        return userRoleMapper.selectByRoleId(roleId);
    }

    /**
     * 缁欑敤鎴峰垎閰嶈鑹?
     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 鏄惁鍒嗛厤成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleToUser(Long userId, Long roleId) {
        if (userId == null || roleId == null) {
            logger.error("Assign role failed: userId or roleId is null");
            return false;
        }
        
        logger.info("Start assigning role: userId={} roleId={}", userId, roleId);
        
        // 浣跨敤浼樺寲鐨凷QL妫€鏌ュ叧鑱斿叧绯绘槸鍚﹀凡瀛樺湪
        int count = userRoleMapper.existsByUserIdAndRoleId(userId, roleId);
        if (count > 0) {
            logger.info("UserId={} already has roleId={}; no action needed", userId, roleId);
            return true;
        }

        // 创建鏂扮殑用户瑙掕壊鍏宠仈
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);
        
        int result = userRoleMapper.insert(userRole);
    logger.info("Assign role result for userId={} roleId={}: {}", userId, roleId, result > 0 ? "success" : "fail");
        
        // 鍙戝竷用户瑙掕壊鍙樻洿浜嬩欢
        if (result > 0) {
            eventPublisher.publishEvent(new UserRoleChangeEvent(this, userId, roleId, "ASSIGN"));
        }
        
        return result > 0;
    }

    /**
     * 浠庣敤鎴风Щ闄よ鑹?
     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 鏄惁绉婚櫎成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRoleFromUser(Long userId, Long roleId) {
        if (userId == null || roleId == null) {
            logger.error("Remove role failed: userId or roleId is null");
            return false;
        }
        
        logger.info("Start removing role: userId={} roleId={}", userId, roleId);
        
        // check whether the mapping exists
        int count = userRoleMapper.existsByUserIdAndRoleId(userId, roleId);
        if (count == 0) {
            logger.warn("UserId={} does not have roleId={}; nothing to remove", userId, roleId);
            return false;
        }
        
        int result = userRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
    logger.info("Remove role result for userId={} roleId={}: {}", userId, roleId, result > 0 ? "success" : "fail");
        
        // 鍙戝竷用户瑙掕壊鍙樻洿浜嬩欢
        if (result > 0) {
            eventPublisher.publishEvent(new UserRoleChangeEvent(this, userId, roleId, "REMOVE"));
        }
        
        return result > 0;
    }

    /**
     * 鎵归噺缁欑敤鎴峰垎閰嶅涓鑹?
     * @param userId 用户ID
     * @param roleIds 瑙掕壊ID鍒楄〃
     * @return 鏄惁鍒嗛厤成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAssignRolesToUser(Long userId, List<Long> roleIds) {
        if (userId == null) {
            logger.error("Batch assign roles failed: userId is null");
            return false;
        }
        
        logger.info("Start batch assign roles: userId={} count={}", userId, roleIds != null ? roleIds.size() : 0);
        
        // 删除用户褰撳墠鎷ユ湁浣嗕笉鍦ㄦ柊鍒楄〃涓殑瑙掕壊
        if (CollectionUtils.isEmpty(roleIds)) {
            // 濡傛灉瑙掕壊鍒楄〃为空锛屽垯删除用户鐨勬墍鏈夎鑹?
            userRoleMapper.deleteByUserId(userId, null);
            logger.info("Cleared all roles for userId={}", userId);
            
            // 鍙戝竷用户瑙掕壊娓呯┖浜嬩欢
            eventPublisher.publishEvent(new UserRoleChangeEvent(this, userId, null, "CLEAR_ALL"));
            
            return true;
        }
        
        // 删除用户鐜版湁浣嗕笉鍦ㄦ柊鍒楄〃涓殑瑙掕壊
    userRoleMapper.deleteByUserId(userId, roleIds);
    logger.debug("Deleted roles not in new list for userId={}", userId);
        
        // 鎵归噺鎻掑叆鏂扮殑瑙掕壊鍏宠仈
        List<UserRole> userRoleList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        for (Long roleId : roleIds) {
            // 鍙彃鍏ヤ笉瀛樺湪鐨勮鑹插叧鑱?
            if (userRoleMapper.existsByUserIdAndRoleId(userId, roleId) == 0) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setCreateTime(now);
                userRole.setUpdateTime(now);
                userRoleList.add(userRole);
            }
        }
        
        boolean hasNewRoles = false;
        if (!CollectionUtils.isEmpty(userRoleList)) {
            int result = userRoleMapper.insertBatch(userRoleList);
            logger.info("Inserted {} new roles for userId={}", result, userId);
            hasNewRoles = result > 0;
        } else {
            logger.info("UserId={} has no new roles to add", userId);
        }
        
        // 鍙戝竷鎵归噺鍒嗛厤瑙掕壊浜嬩欢锛堝鏋滄湁鏂拌鑹叉坊鍔狅級
        if (hasNewRoles && !roleIds.isEmpty()) {
            eventPublisher.publishEvent(new UserRoleChangeEvent(this, userId, roleIds.get(0), "BATCH_ASSIGN"));
        }
        
        return true;
    }
}
