package com.mold.digitalization.integration;

import com.mold.digitalization.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 密码复杂度验证集成测试
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PasswordComplexityIntegrationTest {

    @Autowired
    private UserService userService;

    /**
     * 测试有效密码 - 满足所有复杂度要求
     */
    @Test
    void testValidPasswordComplexity() {
        // 鍖呭惈澶у啓瀛楁瘝銆佸皬鍐欏瓧姣嶃€佹暟瀛楀拰鐗规畩瀛楃锛岄暱搴﹀ぇ浜?
        String validPassword = "MyPass123!";
        
        // 閫氳繃鍙嶅皠璋冪敤绉佹湁方法杩涜测试
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(userService, validPassword);
            assertTrue(result, "有效的复杂密码应该通过验证");
        } catch (Exception e) {
            fail("反射调用isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试密码长度不足 - 小于8个字符
     */
    @Test
    void testPasswordTooShort() {
        // 密码长度不足8个字符
        String shortPassword = "Abc1!";
        
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(userService, shortPassword);
            assertFalse(result, "长度不足的密码应该验证失败");
        } catch (Exception e) {
            fail("鍙嶅皠璋冪敤isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试缂哄皯澶у啓瀛楁瘝
     */
    @Test
    void testPasswordMissingUpperCase() {
        // 缂哄皯澶у啓瀛楁瘝
        String noUpperCasePassword = "mypass123!";
        
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(userService, noUpperCasePassword);
            assertFalse(result, "缺少大写字母的密码应该验证失败");
        } catch (Exception e) {
            fail("鍙嶅皠璋冪敤isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试缂哄皯灏忓啓瀛楁瘝
     */
    @Test
    void testPasswordMissingLowerCase() {
        // 缂哄皯灏忓啓瀛楁瘝
        String noLowerCasePassword = "MYPASS123!";
        
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(userService, noLowerCasePassword);
            assertFalse(result, "缺少小写字母的密码应该验证失败");
        } catch (Exception e) {
            fail("鍙嶅皠璋冪敤isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试缂哄皯鏁板瓧
     */
    @Test
    void testPasswordMissingNumber() {
        // 缂哄皯鏁板瓧
        String noNumberPassword = "MyPassword!";
        
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(userService, noNumberPassword);
            assertFalse(result, "缺少数字的密码应该验证失败");
        } catch (Exception e) {
            fail("鍙嶅皠璋冪敤isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试缂哄皯鐗规畩瀛楃
     */
    @Test
    void testPasswordMissingSpecialChar() {
        // 缂哄皯鐗规畩瀛楃
        String noSpecialCharPassword = "MyPass123";
        
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            Boolean result = (Boolean) method.invoke(userService, noSpecialCharPassword);
            assertFalse(result, "缺少特殊字符的密码应该验证失败");
        } catch (Exception e) {
            fail("鍙嶅皠璋冪敤isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试甯歌开卞瘑鐮?- 应该琚嫆缁?     */
    @Test
    void testCommonWeakPasswords() {
        // 测试甯歌鐨勫急密码
        String[] weakPasswords = {
            "Pass123",
            "PasswordTest",
            "123456789"
        };
        
        try {
            java.lang.reflect.Method method = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            method.setAccessible(true);
            
            for (String weakPassword : weakPasswords) {
                Boolean result = (Boolean) method.invoke(userService, weakPassword);
                assertFalse(result, "甯歌开卞瘑鐮?'" + weakPassword + "' 应该验证失败");
            }
        } catch (Exception e) {
            fail("鍙嶅皠璋冪敤isValidPasswordComplexity方法失败: " + e.getMessage());
        }
    }

    /**
     * 测试闅忔満鐢熸垚密码功能 - 纭繚鐢熸垚鐨勫瘑鐮佹弧瓒冲鏉傚害瑕佹眰
     */
    @Test
    void testGeneratedPasswordComplexity() {
        try {
            // 获取骞惰皟鐢ㄧ敓鎴愰殢鏈哄瘑鐮佺殑方法
            java.lang.reflect.Method generateMethod = UserService.class.getDeclaredMethod("generateRandomPassword");
            generateMethod.setAccessible(true);
            String generatedPassword = (String) generateMethod.invoke(userService);
            
            assertNotNull(generatedPassword, "生成的密码不应为空");
            assertTrue(generatedPassword.length() >= 8, "生成的密码长度应至少为8");
            
            // 验证鐢熸垚鐨勫瘑鐮佹弧瓒冲鏉傚害瑕佹眰
            java.lang.reflect.Method validateMethod = UserService.class.getDeclaredMethod("isValidPasswordComplexity", String.class);
            validateMethod.setAccessible(true);
            Boolean isValid = (Boolean) validateMethod.invoke(userService, generatedPassword);
            
            assertTrue(isValid, "生成的随机密码应该满足复杂度要求: " + generatedPassword);
            
            // 杈撳嚭鐢熸垚鐨勫瘑鐮佺敤浜庤皟璇曪紙鍙€夛級
            System.out.println("生成的符合复杂度要求的密码: " + generatedPassword);
        } catch (Exception e) {
            fail("测试鐢熸垚密码功能失败: " + e.getMessage());
        }
    }
}