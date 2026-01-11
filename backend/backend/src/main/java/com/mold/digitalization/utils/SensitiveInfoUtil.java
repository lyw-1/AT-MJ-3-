package com.mold.digitalization.utils;

import org.apache.commons.lang3.StringUtils;
import java.util.regex.Pattern;

/**
 * 鏁忔劅淇℃伅鑴辨晱宸ュ叿绫?
 * 鎻愪緵鍚勭鏁忔劅数据鐨勮劚鏁忓鐞嗘柟娉?
 */
public class SensitiveInfoUtil {

    // 姝ｅ垯琛ㄨ揪开忔ā开?
    private static final Pattern ID_CARD_PATTERN = Pattern.compile("\\d{17}[\\dXx]");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("1[3-9]\\d{9}");
    private static final Pattern BANK_CARD_PATTERN = Pattern.compile("[1-9]\\d{15,18}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[\\w.-]+@[\\w.-]+\\.[\\w]{2,4}");

    /**
     * 鑴辨晱鎵嬫満鍙?
     * 淇濈暀鍓?浣嶅拰鍚?浣嶏紝涓棿鐢?鏇夸唬
     * @param mobile 鎵嬫満鍙?
     * @return 鑴辨晱鍚庣殑鎵嬫満鍙?
     */
    public static String maskMobile(String mobile) {
        if (StringUtils.isBlank(mobile) || mobile.length() != 11) {
            return mobile;
        }
        return StringUtils.left(mobile, 3) + "******" + StringUtils.right(mobile, 4);
    }

    /**
     * 鑴辨晱韬唤璇佸彿
     * 淇濈暀鍓?浣嶅拰鍚?浣嶏紝涓棿鐢?鏇夸唬
     * @param idCard 韬唤璇佸彿
     * @return 鑴辨晱鍚庣殑韬唤璇佸彿
     */
    public static String maskIdCard(String idCard) {
        if (StringUtils.isBlank(idCard) || idCard.length() < 10) {
            return idCard;
        }
        return StringUtils.left(idCard, 6) +
               StringUtils.repeat("*", idCard.length() - 10) +
               StringUtils.right(idCard, 4);
    }

    /**
     * 鑴辨晱密码
     * 瀹屽叏鐢?鏇夸唬
     * @param password 密码
     * @return 鑴辨晱鍚庣殑密码
     */
    public static String maskPassword(String password) {
        if (StringUtils.isBlank(password)) {
            return password;
        }
        return StringUtils.repeat("*", password.length());
    }

    /**
     * 鑴辨晱濮撳悕
     * 淇濈暀濮撴皬锛屽叾浠栫敤*鏇夸唬
     * @param name 濮撳悕
     * @return 鑴辨晱鍚庣殑濮撳悕
     */
    public static String maskName(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        // 澶勭悊澶嶅
        if (name.length() == 2) {
            return StringUtils.left(name, 1) + "*";
        } else if (name.length() > 2) {
            return StringUtils.left(name, 1) + StringUtils.repeat("*", name.length() - 1);
        }
        return name;
    }

    /**
     * 鑴辨晱閾惰鍗″彿
     * 淇濈暀鍓?浣嶅拰鍚?浣嶏紝涓棿鐢?鏇夸唬
     * @param bankCard 閾惰鍗″彿
     * @return 鑴辨晱鍚庣殑閾惰鍗″彿
     */
    public static String maskBankCard(String bankCard) {
        if (StringUtils.isBlank(bankCard) || bankCard.length() < 8) {
            return bankCard;
        }
        // 鏍规嵁閾惰鍗″彿闀垮害鍔ㄦ€佽皟鏁磋劚鏁忛暱搴?
        int length = bankCard.length();
        StringBuilder maskBuilder = new StringBuilder(bankCard.substring(0, 4));
        for (int i = 0; i < length - 8; i++) {
            maskBuilder.append('*');
        }
        maskBuilder.append(bankCard.substring(length - 4));
        return maskBuilder.toString();
    }

        /**
     * 鑴辨晱鍦板潃
     * 淇濈暀鐪佸競锛岄殣钘忚缁嗗湴鍧€
     * @param address 鍦板潃
     * @return 鑴辨晱鍚庣殑鍦板潃
     */
    public static String maskAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return address;
        }
        // 绠€鍗曞疄鐜帮細淇濈暀鍓?涓瓧绗︼紝鍏朵綑鐢?浠ｆ浛
        if (address.length() <= 6) {
            return address;
        }
        return address.substring(0, 6) + StringUtils.repeat('*', Math.min(address.length() - 6, 10));
    }

    /**
     * 鏍规嵁鏁忔劅绾у埆鍔ㄦ€佽劚鏁忎俊鎭?
     * @param content 鍘熷内容
     * @param sensitiveLevel 鏁忔劅绾у埆 (normal/high/critical)
     * @param fieldName 瀛楁鍚嶇О
     * @return 鑴辨晱鍚庣殑内容
     */
    public static String maskBySensitiveLevel(String content, String sensitiveLevel, String fieldName) {
        if (StringUtils.isBlank(content) || StringUtils.isBlank(fieldName)) {
            return content;
        }

        // 鏍规嵁瀛楁鍚嶈嚜鍔ㄩ€夋嫨鑴辨晱绛栫暐
        String maskedContent = autoMaskByFieldName(fieldName, content);

        // 鏍规嵁鏁忔劅绾у埆澧炲己鑴辨晱鏁堟灉
        if ("critical".equals(sensitiveLevel)) {
            // 鍏抽敭绾у埆锛氭洿涓ユ牸鐨勮劚鏁?
            if ("password".equals(fieldName)) {
                return maskPassword(content);
            } else if (fieldName.contains("idCard") || fieldName.contains("idNumber")) {
                // 鍙繚鐣欏墠2浣嶅拰鍚?浣?
                if (content.length() > 4) {
                    return content.substring(0, 2) + StringUtils.repeat('*', content.length() - 4) +
                           content.substring(content.length() - 2);
                }
            }
        } else if ("high".equals(sensitiveLevel)) {
            // 楂樼骇鍒細鏍囧噯鑴辨晱
            // autoMaskByFieldName宸茬粡澶勭悊
        }

        return maskedContent;
    }

    /**
     * 鑴辨晱JSON瀛楃涓蹭腑鐨勬晱鎰熶俊鎭?
     * @param jsonString JSON瀛楃涓?
     * @param sensitiveLevel 鏁忔劅绾у埆
     * @return 鑴辨晱鍚庣殑JSON瀛楃涓?
     */
    public static String maskJsonSensitiveInfo(String jsonString, String sensitiveLevel) {
        if (StringUtils.isBlank(jsonString)) {
            return jsonString;
        }

        String result = jsonString;

        // 鑴辨晱甯歌鏁忔劅瀛楁
    String[] sensitiveFields = {"password", "idCard", "idNumber", "mobile", "phone", "tel", "bankCard", "creditCard"};

        for (String field : sensitiveFields) {
            // 绠€鍗曠殑姝ｅ垯鏇挎崲锛屽疄闄呭簲鐢ㄤ腑鍙兘闇€瑕佹洿澶嶆潅鐨凧SON瑙ｆ瀽
            // 杩欓噷浠呬綔涓虹ず渚?
            String regex = "(\"" + field + "\"\\s*:\\s*\")([^\"]+)(\")";
            result = result.replaceAll(regex, "$1" + maskBySensitiveLevel("$2", sensitiveLevel, field) + "$3");
        }

        return result;
    }

    /**
     * 鏍规嵁瀛楁绫诲瀷鑷姩鑴辨晱
     * @param fieldName 瀛楁鍚?
     * @param fieldValue 瀛楁鍊?
     * @return 鑴辨晱鍚庣殑鍊?
     */
    public static String autoMaskByFieldName(String fieldName, String fieldValue) {
        if (StringUtils.isBlank(fieldName) || StringUtils.isBlank(fieldValue)) {
            return fieldValue;
        }

        // 杞负灏忓啓杩涜鍖归厤
        String lowerFieldName = fieldName.toLowerCase();

        if (lowerFieldName.contains("mobile") || lowerFieldName.contains("phone") || lowerFieldName.contains("telephone")) {
            return maskMobile(fieldValue);
        } else if (lowerFieldName.contains("idcard") || lowerFieldName.contains("id_card") || lowerFieldName.contains("identity")) {
            return maskIdCard(fieldValue);
        } else if (lowerFieldName.contains("password") || lowerFieldName.contains("pwd")) {
            return maskPassword(fieldValue);
        } else if (lowerFieldName.contains("name") && !lowerFieldName.contains("user") && !lowerFieldName.contains("login")) {
            return maskName(fieldValue);
        } else if (lowerFieldName.contains("bank") && lowerFieldName.contains("card")) {
            return maskBankCard(fieldValue);
        }
        // email瀛楁宸茬Щ闄わ紝涓嶅啀澶勭悊
        // else if (lowerFieldName.contains("email")) {
        //     return maskEmail(fieldValue);
        // }

        return fieldValue;
    }
}
