package com.mold.digitalization.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDepartmentMapper {
    String selectDepartmentNameByUserId(@Param("userId") Long userId);
    int upsertUserDepartment(@Param("userId") Long userId, @Param("departmentId") Long departmentId);
}
