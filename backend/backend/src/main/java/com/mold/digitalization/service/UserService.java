package com.mold.digitalization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 * 瀹氫箟用户鐩稿叧鐨勪笟鍔℃湇鍔℃柟锟? */
public interface UserService extends IService<User> {
    
    /**
     * 鏍规嵁用户名嶆煡璇㈢敤鎴蜂俊锟?     * @param username 用户锟?     * @return 用户淇℃伅
     */
    User getUserByUsername(String username);
    
    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要
    // /**
    //  * 鏍规嵁閭查询用户淇℃伅锛堝疄闄呮寜鎵嬫満鍙锋煡璇紝鍥犱负数据搴撹〃涓病鏈塭mail瀛楁锛?
    //  * @param email 閭锛堝疄闄呮帴鏀舵墜鏈哄彿锛?
    //  * @return 用户淇℃伅
    //  */
    // User getUserByEmail(String email);
    
    /**
     * 鏍规嵁鎵嬫満鍙锋煡璇㈢敤鎴蜂俊锟?     * @param phone 鎵嬫満锟?     * @return 用户淇℃伅
     */
    User getUserByPhone(String phone);
    
    /**
     * 鏍规嵁用户ID获取用户淇℃伅
     * @param id 用户ID
     * @return 用户淇℃伅
     */
    User getUserById(Long id);
    
    /**
     * 创建鏂扮敤锟?     * @param user 用户淇℃伅
     * @return 鏄惁创建成功
     */
    boolean createUser(User user);
    
    /**
     * 更新用户淇℃伅
     * @param user 用户淇℃伅
     * @return 鏄惁更新成功
     */
    boolean updateUser(User user);
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 鏄惁删除成功
     */
    boolean deleteUser(Long userId);
    
    /**
     * 获取鎵€鏈夌敤鎴峰垪锟?     * @return 用户鍒楄〃
     */
    List<User> getAllUsers();
    
    /**
     * 获取用户鍒楄〃锛堝垎椤碉級
     * @param page 椤电爜
     * @param pageSize 姣忛〉鏁伴噺
     * @param keyword 鎼滅储鍏抽敭锟?     * @return 鍒嗛〉用户数据
     */
    Map<String, Object> getUserList(int page, int pageSize, String keyword);
    
    /**
     * 閲嶇疆用户密码
     * @param userId 用户ID
     * @param newPassword 鏂板瘑锟?     * @return 鏄惁閲嶇疆成功
     */
    boolean resetPassword(Long userId, String newPassword);
    
    /**
     * 获取用户鎷ユ湁鐨勮鑹睮D鍒楄〃
     * @param userId 用户ID
     * @return 瑙掕壊ID鍒楄〃
     */
    List<Long> getUserRoleIds(Long userId);
    
    /**
     * 更新用户瑙掕壊鍏宠仈
     * @param userId 用户ID
     * @param roleIds 瑙掕壊ID鍒楄〃
     * @return 鏄惁更新成功
     */
    boolean updateUserRoles(Long userId, List<Long> roleIds);
    
    /**
     * 更新用户鐘讹拷?     * @param userId 用户ID
     * @param status 鐘舵€侊細0-绂佺敤锟?-鍚敤
     * @return 鏄惁成功
     */
    boolean updateUserStatus(Long userId, Integer status);
    
    /**
     * 验证用户鐧诲綍
     * @param username 用户锟?     * @param password 密码
     * @return 验证鏄惁成功
     */
    boolean validateUser(String username, String password);
    
    /**
     * 鏍规嵁用户ID获取用户瑙掕壊鍒楄〃
     * @param userId 用户ID
     * @return 瑙掕壊鍒楄〃
     */
    List<Role> getUserRoles(Long userId);
}
