package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户瑙掕壊鍏宠仈Mapper接口
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    
    /**
     * 鏍规嵁用户ID查询瑙掕壊鍏宠仈
     * @param userId 用户ID
     * @return 瑙掕壊鍏宠仈鍒楄〃
     */
    List<UserRole> selectByUserId(@Param("userId") Long userId);
    
    /**
     * 鏍规嵁用户ID查询瑙掕壊鍒楄〃
     * @param userId 用户ID
     * @return 瑙掕壊鍒楄〃
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);
    
    /**
     * 鏍规嵁瑙掕壊ID查询用户鍏宠仈
     * @param roleId 瑙掕壊ID
     * @return 用户鍏宠仈鍒楄〃
     */
    List<UserRole> selectByRoleId(@Param("roleId") Long roleId);
    
    /**
     * 删除用户瑙掕壊鍏宠仈
     * @param userId 用户ID
     * @return 删除鏁伴噺
     */
    int deleteByUserId(@Param("userId") Long userId);
    
    /**
     * 删除用户瑙掕壊鍏宠仈锛堟帓闄ゆ寚瀹氳鑹诧級
     * @param userId 用户ID
     * @param excludeRoleIds 鎺掗櫎鐨勮鑹睮D鍒楄〃
     * @return 删除鏁伴噺
     */
    int deleteByUserId(@Param("userId") Long userId, @Param("excludeRoleIds") List<Long> excludeRoleIds);
    
    /**
     * 鎵归噺鎻掑叆用户瑙掕壊鍏宠仈
     * @param userRoles 用户瑙掕壊鍏宠仈鍒楄〃
     * @return 鎻掑叆鏁伴噺
     */
    int batchInsert(@Param("userRoles") List<UserRole> userRoles);
    
    /**
     * 妫€鏌ョ敤鎴疯鑹插叧鑱旀槸鍚﹀瓨鍦?
     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 瀛樺湪鏁伴噺
     */
    int existsByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    /**
     * 删除用户瑙掕壊鍏宠仈
     * @param userId 用户ID
     * @param roleId 瑙掕壊ID
     * @return 删除鏁伴噺
     */
    int deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    /**
     * 鎵归噺鎻掑叆用户瑙掕壊鍏宠仈锛堝埆鍚嶏級
     * @param userRoles 用户瑙掕壊鍏宠仈鍒楄〃
     * @return 鎻掑叆鏁伴噺
     */
    int insertBatch(@Param("userRoles") List<UserRole> userRoles);
}
