package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Permission;
import java.util.List;

/**
 * 鏉冮檺Mapper接口
 * 鎻愪緵鏉冮檺数据访问操作
 */
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 鏍规嵁用户ID查询鏉冮檺
     * @param userId 用户ID
     * @return 鏉冮檺鍒楄〃
     */
    List<Permission> selectByUserId(Long userId);

    /**
     * 鏍规嵁瑙掕壊ID查询鏉冮檺
     * @param roleId 瑙掕壊ID
     * @return 鏉冮檺鍒楄〃
     */
    List<Permission> selectByRoleId(Long roleId);

    /**
     * 查询鏉冮檺鏍?     * @return 鏉冮檺鍒楄〃锛堟爲褰㈢粨鏋勶級
     */
    List<Permission> selectPermissionTree();

    /**
     * 鏍规嵁鏉冮檺缂栫爜查询
     * @param code 鏉冮檺缂栫爜
     * @return 鏉冮檺实体
     */
    Permission selectByCode(String code);

    /**
     * 查询鎵€鏈夊惎鐢ㄧ殑鏉冮檺
     * @return 鍚敤鐨勬潈闄愬垪琛?     */
    List<Permission> selectAllEnabled();

    /**
     * 鎻掑叆瑙掕壊鏉冮檺鍏崇郴
     * @param roleId 瑙掕壊ID
     * @param permissionIds 鏉冮檺ID鍒楄〃
     */
    void insertRolePermissions(Long roleId, List<Long> permissionIds);

    /**
     * 删除瑙掕壊鏉冮檺鍏崇郴
     * @param roleId 瑙掕壊ID
     */
    void deleteRolePermissions(Long roleId);
}
