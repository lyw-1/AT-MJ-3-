package com.mold.digitalization.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * SensitiveInfoUtil宸ュ叿绫荤殑鍗曞厓测试
 */
public class SensitiveInfoUtilTest {

    @Test
    public void testMaskMobile() {
        // 测试姝ｅ父鎵嬫満鍙?        String mobile = "13812345678";
        String masked = SensitiveInfoUtil.maskMobile(mobile);
        assertEquals("138****5678", masked);

        // 测试绌烘墜鏈哄彿
        assertEquals(null, SensitiveInfoUtil.maskMobile(null));
        assertEquals("", SensitiveInfoUtil.maskMobile(""));

        // 测试闈炴爣鍑嗘墜鏈哄彿
        assertEquals("123456", SensitiveInfoUtil.maskMobile("123456"));
    }

    @Test
    public void testMaskIdCard() {
        // 测试18浣嶈韩浠借瘉鍙?        String idCard = "110101199001011234";
        String masked = SensitiveInfoUtil.maskIdCard(idCard);
        assertEquals("1101**********1234", masked);

        // 测试15浣嶈韩浠借瘉鍙?        String idCard15 = "110101900101123";
        String masked15 = SensitiveInfoUtil.maskIdCard(idCard15);
        assertEquals("1101********123", masked15);

        // 测试绌鸿韩浠借瘉鍙?        assertEquals(null, SensitiveInfoUtil.maskIdCard(null));
        assertEquals("", SensitiveInfoUtil.maskIdCard(""));

        // 测试闈炴爣鍑嗚韩浠借瘉鍙?        assertEquals("123456", SensitiveInfoUtil.maskIdCard("123456"));
    }

    @Test
    public void testMaskPassword() {
        // 测试密码
        String password = "password123";
        String masked = SensitiveInfoUtil.maskPassword(password);
        assertEquals("***********", masked);

        // 测试绌哄瘑鐮?        assertEquals(null, SensitiveInfoUtil.maskPassword(null));
        assertEquals("", SensitiveInfoUtil.maskPassword(""));
    }

    @Test
    public void testMaskRealName() {
        // 测试涓枃鍚?        String name = "开犱笁";
        String masked = SensitiveInfoUtil.maskName(name);
        assertEquals("开?", masked);

        // 测试澶氬瓧涓枃鍚?        String longName = "开犱笁涓?;"
        String maskedLong = SensitiveInfoUtil.maskName(longName);
        assertEquals("开?*", maskedLong);

        // 测试绌哄鍚?        assertEquals(null, SensitiveInfoUtil.maskName(null));
        assertEquals("", SensitiveInfoUtil.maskName(""));
    }

    @Test
    public void testMaskBankCard() {
        // 测试閾惰鍗″彿
        String bankCard = "6222021234567890123";
        String masked = SensitiveInfoUtil.maskBankCard(bankCard);
        assertEquals("6222**********0123", masked);

        // 测试鐭摱琛屽崱鍙?        String shortBankCard = "6222021234";
        String maskedShort = SensitiveInfoUtil.maskBankCard(shortBankCard);
        assertEquals("6222021234", maskedShort);

        // 测试绌洪摱琛屽崱鍙?        assertEquals(null, SensitiveInfoUtil.maskBankCard(null));
        assertEquals("", SensitiveInfoUtil.maskBankCard(""));
    }

    @Test
    public void testAutoMaskByFieldName() {
        // 测试鎵嬫満鍙峰瓧娈?        String mobileMasked = SensitiveInfoUtil.autoMaskByFieldName("13812345678", "mobile");
        assertEquals("138****5678", mobileMasked);

        // 测试韬唤璇佸瓧娈?        String idCardMasked = SensitiveInfoUtil.autoMaskByFieldName("110101199001011234", "idCard");
        assertEquals("1101**********1234", idCardMasked);

        // 测试密码瀛楁
        String passwordMasked = SensitiveInfoUtil.autoMaskByFieldName("password123", "password");
        assertEquals("***********", passwordMasked);

        // 测试非敏感字段
        String normalMasked = SensitiveInfoUtil.autoMaskByFieldName("normalValue", "normalField");
        assertEquals("normalValue", normalMasked);
    }

    @Test
    public void testMaskAddress() {
        // 测试地址
        String address = "鍖椾含甯傛捣娣€鍖轰腑鍏虫潙澶ц1鍙?";
        String masked = SensitiveInfoUtil.maskAddress(address);
        assertEquals("鍖椾含甯傛捣娣€********", masked);

        // 测试鐭湴鍧€
        String shortAddress = "鍖椾含";
        String maskedShort = SensitiveInfoUtil.maskAddress(shortAddress);
        assertEquals("鍖椾含", maskedShort);

        // 测试绌哄湴鍧€
        assertEquals(null, SensitiveInfoUtil.maskAddress(null));
        assertEquals("", SensitiveInfoUtil.maskAddress(""));
    }

    @Test
    public void testMaskBySensitiveLevel() {
        // 测试鏅€氱骇鍒?        String normalMasked = SensitiveInfoUtil.maskBySensitiveLevel("110101199001011234", "normal", "idCard");
        assertEquals("1101**********1234", normalMasked);

        // 测试楂樼骇鍒?        String highMasked = SensitiveInfoUtil.maskBySensitiveLevel("110101199001011234", "high", "idCard");
        assertEquals("1101**********1234", highMasked);

        // 测试鍏抽敭绾у埆
        String criticalMasked = SensitiveInfoUtil.maskBySensitiveLevel("110101199001011234", "critical", "idCard");
        assertTrue(criticalMasked.startsWith("11") && criticalMasked.endsWith("34"));
        assertTrue(criticalMasked.contains("*"));
    }
}
