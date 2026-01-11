package com.mold.digitalization.utils;

import java.security.SecureRandom;

/**
 * 密码工具类：提供符合规则的随机密码生成
 * 规则：8-20位，仅字母与数字，且同时包含字母与数字
 */
public final class PasswordUtil {

    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String ALPHANUM = LETTERS + DIGITS;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    /**
     * 生成默认长度为10的合规随机密码
     */
    public static String generateDefaultPassword() {
        return generateCompliantPassword(10);
    }

    /**
     * 生成指定长度的合规随机密码（8-20）
     */
    public static String generateCompliantPassword(int length) {
        if (length < 8) {
            length = 8;
        }
        if (length > 20) {
            length = 20;
        }

        // 确保至少包含一个字母和一个数字
        StringBuilder sb = new StringBuilder(length);

        // 先放一个字母和一个数字
        sb.append(randomChar(LETTERS));
        sb.append(randomChar(DIGITS));

        // 剩余位数使用字母数字混合
        for (int i = 2; i < length; i++) {
            sb.append(randomChar(ALPHANUM));
        }

        // 打乱顺序，避免前两位固定
        return shuffle(sb.toString());
    }

    private static char randomChar(String dict) {
        return dict.charAt(RANDOM.nextInt(dict.length()));
    }

    private static String shuffle(String input) {
        char[] arr = input.toCharArray();
        for (int i = arr.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return new String(arr);
    }
}

