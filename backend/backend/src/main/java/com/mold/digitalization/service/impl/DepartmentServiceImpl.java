package com.mold.digitalization.service.impl;

import com.mold.digitalization.entity.Department;
import com.mold.digitalization.mapper.DepartmentMapper;
import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.service.DepartmentService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Resource
    private DepartmentMapper departmentMapper;
    
    @Resource
    private UserMapper userMapper;

    @Override
    public List<Department> listAll() {
        return departmentMapper.selectAll();
    }

    @Override
    public List<Department> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return listAll();
        return departmentMapper.selectByKeyword(keyword.trim());
    }

    @Override
    public Department getById(Long id) {
        return departmentMapper.selectById(id);
    }

    @Override
    public boolean create(Department department) {
        return departmentMapper.insert(department) > 0;
    }

    @Override
    public boolean update(Department department) {
        return departmentMapper.updateById(department) > 0;
    }

    @Override
    public boolean delete(Long id) {
        // 检查部门是否关联了用户
        if (hasAssociatedUsers(id)) {
            return false;
        }
        return departmentMapper.deleteById(id) > 0;
    }

    @Override
    public boolean isNameExists(String name, Long excludeId) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        if (excludeId != null) {
            queryWrapper.ne("id", excludeId);
        }
        return departmentMapper.exists(queryWrapper);
    }

    @Override
    public boolean hasAssociatedUsers(Long departmentId) {
        // 查询部门详情，获取部门名称
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            return false;
        }
        
        // 查询该部门下的用户数量
        QueryWrapper<com.mold.digitalization.entity.User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("department_name", department.getName());
        return userMapper.selectCount(userQueryWrapper) > 0;
    }

    @Override
    public int getUserCountByDepartmentId(Long departmentId) {
        // 查询部门详情，获取部门名称
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            return 0;
        }
        
        // 查询该部门下的用户数量
        QueryWrapper<com.mold.digitalization.entity.User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("department_name", department.getName());
        return userMapper.selectCount(userQueryWrapper).intValue();
    }

    @Override
    public Department getDepartmentDetail(Long id) {
        // 查询部门基本信息
        Department department = departmentMapper.selectById(id);
        if (department == null) {
            return null;
        }
        
        // 这里可以扩展，例如添加用户数量等其他关联信息
        // 由于Department实体中没有userCount字段，这里暂时只返回基本信息
        return department;
    }
    
    @Override
    public List<com.mold.digitalization.entity.User> getUsersByDepartmentId(Long departmentId) {
        // 查询部门详情，获取部门名称
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            return List.of();
        }
        
        // 查询该部门下的用户列表
        QueryWrapper<com.mold.digitalization.entity.User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("department_name", department.getName());
        return userMapper.selectList(userQueryWrapper);
    }
    
    @Override
    public List<Long> getUserIdsByDepartmentId(Long departmentId) {
        // 查询部门详情，获取部门名称
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            return List.of();
        }
        
        // 查询该部门下的用户ID列表
        QueryWrapper<com.mold.digitalization.entity.User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("department_name", department.getName());
        userQueryWrapper.select("id");
        return userMapper.selectObjs(userQueryWrapper).stream()
                .map(obj -> Long.parseLong(obj.toString()))
                .toList();
    }
    
    @Override
    public List<com.mold.digitalization.entity.User> getUsersByDepartmentId(Long departmentId, int offset, int limit) {
        // 查询部门详情，获取部门名称
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            return List.of();
        }
        
        // 分页查询该部门下的用户列表
        QueryWrapper<com.mold.digitalization.entity.User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("department_name", department.getName());
        userQueryWrapper.last("LIMIT " + offset + ", " + limit);
        return userMapper.selectList(userQueryWrapper);
    }

    @Override
    public List<Department> getDepartmentTree() {
        return getDepartmentTree(null);
    }

    @Override
    public List<Department> getDepartmentTree(String keyword) {
        // 获取所有部门（或带关键词搜索的部门）
        List<Department> departments = search(keyword);
        return buildDepartmentTree(departments);
    }

    @Override
    public List<Department> getChildrenByParentId(Long parentId) {
        // 使用MyBatis Plus查询父部门ID为parentId的部门列表
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.orderByAsc("sort_order");
        return departmentMapper.selectList(queryWrapper);
    }

    @Override
    public boolean hasChildren(Long departmentId) {
        // 使用MyBatis Plus查询是否存在父部门ID为departmentId的部门
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", departmentId);
        return departmentMapper.exists(queryWrapper);
    }

    /**
     * 构建部门树结构
     * @param departments 部门列表
     * @return 部门树列表
     */
    private List<Department> buildDepartmentTree(List<Department> departments) {
        // 这里需要实现部门树的构建逻辑
        // 由于Department实体本身不包含children字段，我们可以使用Map来构建树结构
        // 然后返回根节点列表
        
        // 由于当前代码架构限制，我们先返回排序后的部门列表
        // 后续可以考虑扩展Department实体，添加children字段，或者使用DTO来返回树结构
        
        // 按parent_id和sort_order排序
        departments.sort((d1, d2) -> {
            // 先按parent_id排序，根部门（parent_id为null或0）排在前面
            Long p1 = d1.getParentId() == null ? 0 : d1.getParentId();
            Long p2 = d2.getParentId() == null ? 0 : d2.getParentId();
            if (!p1.equals(p2)) {
                return p1.compareTo(p2);
            }
            // 再按sort_order排序
            Integer s1 = d1.getSortOrder() == null ? 0 : d1.getSortOrder();
            Integer s2 = d2.getSortOrder() == null ? 0 : d2.getSortOrder();
            return s1.compareTo(s2);
        });
        
        return departments;
    }
}

