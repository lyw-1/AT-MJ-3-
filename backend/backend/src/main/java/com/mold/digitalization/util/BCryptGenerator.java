package com.mold.digitalization.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt密码生成器
 */
public class BCryptGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: BCryptGenerator <password>");
            System.exit(1);
        }
        
        String password = args[0];
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        
        System.out.println("Original Password: " + password);
        System.out.println("BCrypt Hash: " + encodedPassword);
    }
}