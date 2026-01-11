package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.Permission;
import java.util.List;

/**
 * 鏉冮檺服务接口
 * 鎻愪緵鏉冮檺鐩稿叧鐨勪笟鍔℃搷浣?
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 鏍规嵁用户ID获取用户鏉冮檺鍒楄〃
     * @param userId 用户ID
     * @return 鏉冮檺鍒楄〃
     */
    List<Permission> getPermissionsByUserId(Long userId);

    /**
     * 鏍规嵁瑙掕壊ID获取鏉冮檺鍒楄〃
     * @param roleId 瑙掕壊ID
     * @return 鏉冮檺鍒楄〃
     */
    List<Permission> getPermissionsByRoleId(Long roleId);

    /**
     * 获取鏉冮檺鏍戠粨鏋?
     * @return 鏉冮檺鏍戝垪琛?
     */
    List<Permission> getPermissionTree();

    /**
     * 涓鸿鑹插垎閰嶆潈闄?
     * @param roleId 瑙掕壊ID
     * @param permissionIds 鏉冮檺ID鍒楄〃
     * @return 鏄惁鍒嗛厤成功
     */
    boolean assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    /**
     * 鏍规嵁鏉冮檺缂栫爜获取鏉冮檺淇℃伅
     * @param code 鏉冮檺缂栫爜
     * @return 鏉冮檺实体
     */
    Permission getPermissionByCode(String code);

    /**
     * 获取鎵€鏈夊惎鐢ㄧ殑鏉冮檺
     * @return 鍚敤鐨勬潈闄愬垪琛?
     */
    List<Permission> getAllEnabledPermissions();
}
