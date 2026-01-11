package com.mold.digitalization.service;

import com.mold.digitalization.entity.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> listAll();
    List<Department> search(String keyword);
    Department getById(Long id);
    boolean create(Department department);
    boolean update(Department department);
    boolean delete(Long id);
    boolean isNameExists(String name, Long excludeId);
    /**
     * 检查部门是否关联了用户
     * @param departmentId 部门ID
     * @return true - 有关联用户，false - 没有关联用户
     */
    boolean hasAssociatedUsers(Long departmentId);
    /**
     * 获取部门关联的用户数量
     * @param departmentId 部门ID
     * @return 用户数量
     */
    int getUserCountByDepartmentId(Long departmentId);
    /**
     * 获取部门详情，包含关联用户数量
     * @param id 部门ID
     * @return 部门详情
     */
    Department getDepartmentDetail(Long id);
    
    /**
     * 查询部门下的用户列表
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<com.mold.digitalization.entity.User> getUsersByDepartmentId(Long departmentId);
    
    /**
     * 查询部门下的用户ID列表
     * @param departmentId 部门ID
     * @return 用户ID列表
     */
    List<Long> getUserIdsByDepartmentId(Long departmentId);
    
    /**
     * 分页查询部门下的用户列表
     * @param departmentId 部门ID
     * @param offset 偏移量
     * @param limit 每页数量
     * @return 用户列表
     */
    List<com.mold.digitalization.entity.User> getUsersByDepartmentId(Long departmentId, int offset, int limit);
    
    /**
     * 获取部门树结构
     * @return 部门树列表
     */
    List<Department> getDepartmentTree();
    
    /**
     * 获取部门树结构（带搜索功能）
     * @param keyword 搜索关键词
     * @return 部门树列表
     */
    List<Department> getDepartmentTree(String keyword);
    
    /**
     * 获取指定部门的子部门列表
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<Department> getChildrenByParentId(Long parentId);
    
    /**
     * 检查部门是否有子部门
     * @param departmentId 部门ID
     * @return true - 有子部门，false - 没有子部门
     */
    boolean hasChildren(Long departmentId);
}

