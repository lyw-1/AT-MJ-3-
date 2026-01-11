package com.mold.digitalization.config;

import com.mold.digitalization.mapper.UserMapper;
import com.mold.digitalization.mapper.UserRoleMapper;
import com.mold.digitalization.entity.Role;
import com.mold.digitalization.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 鑷畾涔塙serDetailsService实现
 * 鐢ㄤ簬浠庢暟鎹簱鍔犺浇用户淇℃伅骞惰浆鎹负Spring Security闇€瑕佺殑UserDetails瀵硅薄
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Load user by username from the database
    User user = userMapper.selectByUsername(username);
    if (user == null) {
        throw new UsernameNotFoundException("User not found: " + username);
    }

    // Get roles and convert to SimpleGrantedAuthority
    List<Role> roles = userRoleMapper.selectRolesByUserId(user.getId());
    List<SimpleGrantedAuthority> authorities = roles.stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()))
        .collect(Collectors.toList());

    // Return Spring Security UserDetails object
    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        user.getStatus() == 1, // enabled if status == 1
        true, // accountNonExpired
        true, // credentialsNonExpired
        true, // accountNonLocked
        authorities
    );
    }
}
