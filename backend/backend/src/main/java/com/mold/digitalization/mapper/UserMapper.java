package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;

/**
 * 用户Mapper接口
 * 鎻愪緵用户琛ㄧ殑数据搴撴搷浣滄柟娉? */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    
    /**
     * 鏍规嵁用户名嶆煡璇㈢敤鎴蜂俊鎭?     * @param username 用户名?     * @return 用户淇℃伅
     */
    User selectByUsername(String username);
    
    // email瀛楁宸茬Щ闄わ紝涓嶅啀需要    // /**
    //  * 鏍规嵁閭查询用户淇℃伅锛堝疄闄呬笂鏄牴鎹墜鏈哄彿查询锛?    //  * @param email 鎵嬫満鍙凤紙涓轰簡鍏煎鐜版湁浠ｇ爜锛屽弬鏁板悕浠嶄负email锛?    //  * @return 用户淇℃伅
    //  */
    // User selectByEmail(String email);
    
    /**
     * 鏍规嵁鎵嬫満鍙锋煡璇㈢敤鎴蜂俊鎭?     * @param phone 鎵嬫満鍙?     * @return 用户淇℃伅
     */
    User selectByPhone(String phone);

    /**
     * 按ID查询基础列（仅包含实际存在的列：id、username、password、status）
     * 避免在某些环境中选择不存在的 department/role/phone/create_time/update_time 等列导致 SQL 错误。
     */
    User selectBasicById(@Param("id") Long id);

    int existsUserColumn(@Param("column") String column);

    int updateExtraFields(@Param("id") Long id,
                          @Param("realName") String realName,
                          @Param("departmentName") String departmentName,
                          @Param("phone") String phone,
                          @Param("email") String email);

    int updateBasic(@Param("id") Long id,
                     @Param("password") String password,
                     @Param("status") Integer status);

    int updateUsername(@Param("id") Long id, @Param("username") String username);
    
    /**
     * 鍒嗛〉查询用户鍒楄〃
     * @param page 鍒嗛〉瀵硅薄
     * @param params 查询鍙傛暟
     * @return 鍒嗛〉结果
     */
    IPage<User> selectPage(IPage<User> page, @Param("params") Map<String, Object> params);

    List<User> selectAllBasic();
    
    /**
     * 查询用户鎷ユ湁鐨勮鑹插垪琛?     * @param userId 用户ID
     * @return 瑙掕壊鍒楄〃
     */
    List<Role> selectUserRoles(@Param("userId") Long userId);

    /**
     * 閲嶇疆鐧诲綍失败娆℃暟锛堝疄闄呬笂鏄噸缃敤鎴风姸鎬佷负鍚敤锛?     * @param userId 用户ID
     * @return 褰卞搷琛屾暟
     */
    int resetLoginFailedCount(@Param("userId") Long userId);
    
    /**
     * 澧炲姞鐧诲綍失败娆℃暟
     * @param userId 用户ID
     * @return 褰卞搷琛屾暟
     */
    int incrementLoginFailedCount(@Param("userId") Long userId);

    /**
     * 閿佸畾用户锛堝疄闄呬笂鏄缃敤鎴风姸鎬佷负绂佺敤锛?     * @param userId 用户ID
     * @return 褰卞搷琛屾暟
     */
    int lockUser(@Param("userId") Long userId);
    
    /**
     * 瑙ｉ攣用户锛堝疄闄呬笂鏄缃敤鎴风姸鎬佷负鍚敤锛?     * @param userId 用户ID
     * @return 褰卞搷琛屾暟
     */
    int unlockUser(@Param("userId") Long userId);
    
    /**
     * 鎵归噺瑙ｉ攣杩囨湡用户锛堝疄闄呬笂鏄壒閲忓惎鐢ㄧ姸鎬佷负绂佺敤鐨勭敤鎴凤級
     * @param currentTime 褰撳墠鏃堕棿锛堜负浜嗗吋瀹圭幇鏈変唬鐮侊紝淇濈暀鍙傛暟锛?     * @return 瑙ｉ攣成功鐨勭敤鎴锋暟閲?     */
    int unlockExpiredUsers(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * 查询鎵€鏈夊凡杩囨湡浣嗕粛澶勪簬閿佸畾鐘舵€佺殑用户锛堝疄闄呬笂鏄煡璇㈢姸鎬佷负绂佺敤鐨勭敤鎴凤級
     * @param currentTime 褰撳墠鏃堕棿锛堜负浜嗗吋瀹圭幇鏈変唬鐮侊紝淇濈暀鍙傛暟锛?     * @return 杩囨湡閿佸畾用户鍒楄〃
     */
    List<User> selectExpiredLockedUsers(@Param("currentTime") LocalDateTime currentTime);

    /**
     * 更新用户鏈€鍚庣櫥褰曚俊鎭紙实际涓婃槸更新更新鏃堕棿锛?     * @param userId 用户ID
     * @param lastLoginIp 鏈€鍚庣櫥褰旾P锛堜负浜嗗吋瀹圭幇鏈変唬鐮侊紝淇濈暀鍙傛暟锛?     * @param lastLoginTime 鏈€鍚庣櫥褰曟椂闂?     * @return 褰卞搷琛屾暟
     */
    int updateLastLoginInfo(@Param("userId") Long userId, 
                           @Param("lastLoginIp") String lastLoginIp, 
                           @Param("lastLoginTime") LocalDateTime lastLoginTime);
}
