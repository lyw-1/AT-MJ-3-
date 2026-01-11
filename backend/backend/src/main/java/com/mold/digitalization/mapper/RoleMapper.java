package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

/**
 * 瑙掕壊Mapper接口
 * 鎻愪緵瑙掕壊琛ㄧ殑数据搴撴搷浣滄柟娉?
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    
    /**
     * 鏍规嵁瑙掕壊浠ｇ爜查询瑙掕壊淇℃伅
     * @param roleCode 瑙掕壊浠ｇ爜
     * @return 瑙掕壊淇℃伅
     */
    Role selectByRoleCode(String roleCode);
    
    /**
     * 鍒嗛〉查询瑙掕壊鍒楄〃
     * @param page 鍒嗛〉鍙傛暟
     * @param params 查询鍙傛暟
     * @return 鍒嗛〉瑙掕壊数据
     */
    IPage<Role> selectPage(IPage<Role> page, @Param("params") Map<String, Object> params);

    int insertRole(Role role);

    int updateRoleBasic(@Param("id") Long id,
                        @Param("roleName") String roleName,
                        @Param("roleCode") String roleCode,
                        @Param("description") String description);
}
