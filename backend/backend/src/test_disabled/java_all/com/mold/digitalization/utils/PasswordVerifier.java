package com.mold.digitalization.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码验证测试类
 * 用于验证数据库中的密码哈希是否与指定密码匹配
 */
public class PasswordVerifier {

    public static void main(String[] args) {
        // 数据库中的密码哈希
        String storedHash = "$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW";
        // 尝试的密码
        String password = "123456";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        boolean matches = encoder.matches(password, storedHash);

        System.out.println("数据库密码哈希: " + storedHash);
        System.out.println("尝试密码: " + password);
        System.out.println("匹配结果: " + matches);

        // 如果密码不匹配，则尝试生成新的哈希并再次验证
        if (!matches) {
            String newHash = encoder.encode(password);
            System.out.println("新生成的密码哈希: " + newHash);
            System.out.println("新哈希匹配结果: " + encoder.matches(password, newHash));
        }
    }
}
