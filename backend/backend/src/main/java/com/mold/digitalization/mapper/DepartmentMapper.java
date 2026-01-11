package com.mold.digitalization.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mold.digitalization.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    List<Department> selectAll();
    List<Department> selectByKeyword(String keyword);
    Long selectIdByName(String name);
    int insertIgnoreByName(String name);
    /**
     * 查询部门关联的用户数量
     * @param departmentId 部门ID
     * @return 用户数量
     */
    int selectUserCountByDepartmentId(Long departmentId);
    /**
     * 查询部门详情，包含关联用户数量
     * @param id 部门ID
     * @return 部门详情
     */
    Department selectDepartmentDetail(Long id);
    /**
     * 批量查询部门信息
     * @param ids 部门ID列表
     * @return 部门列表
     */
    List<Department> selectBatchByIds(List<Long> ids);
    /**
     * 统计部门数量
     * @return 部门总数
     */
    int countDepartments();
    /**
     * 分页查询部门列表
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 部门列表
     */
    List<Department> selectDepartmentsPage(int offset, int limit);
    
    /**
     * 查询部门下的用户列表
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<com.mold.digitalization.entity.User> selectUsersByDepartmentId(Long departmentId);
    
    /**
     * 查询部门下的用户ID列表
     * @param departmentId 部门ID
     * @return 用户ID列表
     */
    List<Long> selectUserIdsByDepartmentId(Long departmentId);
}
