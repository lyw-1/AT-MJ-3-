package com.mold.digitalization.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码强度检查工具类
 * 用于验证密码是否符合安全要求
 */
public class PasswordStrengthChecker {

    // 至少包含一个字母（不区分大小写）
    private static final Pattern LETTER_PATTERN = Pattern.compile("[A-Za-z]");
    // 至少包含一个数字
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");
    // 仅允许字母和数字
    private static final Pattern ALNUM_ONLY_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");
    
    /**
     * 检查密码强度
     * @param password 待检查的密码
     * @return 密码强度结果对象
     */
    public static PasswordStrengthResult checkPasswordStrength(String password) {
        boolean hasLetter = hasMatch(LETTER_PATTERN, password);
        boolean hasDigit = hasMatch(DIGIT_PATTERN, password);
        boolean alnumOnly = password != null && ALNUM_ONLY_PATTERN.matcher(password).matches();

        // 检查长度（保持与DTO上的@Size一致：8-20）
        boolean hasValidLength = password != null && password.length() >= 8 && password.length() <= 20;

        // 新规则：长度合规 + 仅字母数字 + 同时包含字母与数字
        StrengthLevel level;
        boolean meetsSecurityRequirements;
        if (!hasValidLength) {
            level = StrengthLevel.INVALID_LENGTH;
            meetsSecurityRequirements = false;
        } else if (!(alnumOnly && hasLetter && hasDigit)) {
            level = StrengthLevel.WEAK;
            meetsSecurityRequirements = false;
        } else {
            // 规则满足，归为中等即可
            level = StrengthLevel.MEDIUM;
            meetsSecurityRequirements = true;
        }

        return new PasswordStrengthResult(
            level,
            meetsSecurityRequirements,
            // 兼容旧字段语义：
            hasLetter, // 作为hasLowercase的替代标识用途
            false,     // 不再区分大写，小写此处设置为false（不影响校验结果使用）
            hasDigit,
            false,     // 不要求特殊字符
            hasValidLength,
            getStrengthMessage(level)
        );
    }
    
    /**
     * 检查字符串是否匹配给定的正则表达式
     */
    private static boolean hasMatch(Pattern pattern, String input) {
        if (input == null) return false;
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
    
    /**
     * 获取密码强度对应的提示信息
     */
    private static String getStrengthMessage(StrengthLevel level) {
        switch (level) {
            case INVALID_LENGTH:
                return "密码长度必须为8-20个字符之间";
            case WEAK:
                return "密码不符合规则：仅允许字母和数字，且需同时包含字母与数字";
            case MEDIUM:
                return "密码符合规则（8-20位，仅字母和数字，且需同时包含字母与数字）";
            case STRONG:
                return "密码强度：强";
            case VERY_STRONG:
                return "密码强度：非常强";
            default:
                return "密码强度未知";
        }
    }
    
    /**
     * 密码强度结果类
     */
    public static class PasswordStrengthResult {
        private final StrengthLevel level;
        private final boolean meetsSecurityRequirements;
        private final boolean hasLowercase;
        private final boolean hasUppercase;
        private final boolean hasDigit;
        private final boolean hasSpecialChar;
        private final boolean hasValidLength;
        private final String message;
        
        public PasswordStrengthResult(StrengthLevel level, boolean meetsSecurityRequirements,
                                    boolean hasLowercase, boolean hasUppercase,
                                    boolean hasDigit, boolean hasSpecialChar,
                                    boolean hasValidLength, String message) {
            this.level = level;
            this.meetsSecurityRequirements = meetsSecurityRequirements;
            this.hasLowercase = hasLowercase;
            this.hasUppercase = hasUppercase;
            this.hasDigit = hasDigit;
            this.hasSpecialChar = hasSpecialChar;
            this.hasValidLength = hasValidLength;
            this.message = message;
        }
        
        // Getters
        public StrengthLevel getLevel() {
            return level;
        }
        
        public boolean isMeetsSecurityRequirements() {
            return meetsSecurityRequirements;
        }
        
        public boolean isHasLowercase() {
            return hasLowercase;
        }
        
        public boolean isHasUppercase() {
            return hasUppercase;
        }
        
        public boolean isHasDigit() {
            return hasDigit;
        }
        
        public boolean isHasSpecialChar() {
            return hasSpecialChar;
        }
        
        public boolean isHasValidLength() {
            return hasValidLength;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * 密码强度等级枚举
     */
    public enum StrengthLevel {
        INVALID_LENGTH,  // 长度不符合要求
        WEAK,           // 弱密码
        MEDIUM,         // 中等强度密码
        STRONG,         // 强密码
        VERY_STRONG     // 非常强的密码
    }
}
