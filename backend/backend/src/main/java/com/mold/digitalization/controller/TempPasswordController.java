package com.mold.digitalization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 涓存椂密码鐢熸垚控制鍣? 浠呯敤浜庡紑鍙戞祴璇?
 */
@RestController
@RequestMapping("/temp")
public class TempPasswordController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/generate-password")
    public String generatePassword(@RequestParam String password) {
        String encoded = passwordEncoder.encode(password);
        return "鍘熷密码(示例): " + password + "<br>鍔犲瘑鍚? " + encoded;
    }
}
