package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.UserRole;
import java.util.List;

/**
 * 用户瑙掕壊鍏宠仈服务接口
 * 瀹氫箟用户瑙掕壊鍏宠仈鐩稿叧鐨勪笟鍔℃湇鍔℃柟娉? */
public interface UserRoleService extends IService<UserRole> {
    
    /**
     * 鏍规嵁用户ID查询用户瑙掕壊鍏宠仈鍒楄〃
     * @param userId 用户ID
     * @return 用户瑙掕壊鍏宠仈鍒楄〃
     */
    List<UserRole> getUserRolesByUserId(Long userId);
    
    /**
     * 鏍规嵁瑙掕壊ID查询用户瑙掕壊鍏宠仈鍒楄〃
     * @param roleId 瑙掕壊ID
     * @return 用户瑙掕壊鍏宠仈鍒楄〃
     */
    List<UserRole> getUserRolesByRoleId(Long roleId);
    
    /**
     * 涓虹敤鎴峰垎閰嶈鑹?     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 鏄惁鍒嗛厤成功
     */
    boolean assignRoleToUser(Long userId, Long roleId);
    
    /**
     * 浠庣敤鎴风Щ闄よ鑹?     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 鏄惁绉婚櫎成功
     */
    boolean removeRoleFromUser(Long userId, Long roleId);
    
    /**
     * 鎵归噺涓虹敤鎴峰垎閰嶈鑹?     * @param userId 用户ID
     * @param roleIds 瑙掕壊ID鍒楄〃
     * @return 鏄惁鍒嗛厤成功
     */
    boolean batchAssignRolesToUser(Long userId, List<Long> roleIds);
}
