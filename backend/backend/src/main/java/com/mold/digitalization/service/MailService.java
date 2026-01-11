package com.mold.digitalization.service;

/**
 * 閭欢鍙戦€佹湇鍔℃帴鍙?
 * 鎻愪緵鍙戦€佸悇绉嶇被鍨嬮偖浠剁殑鑳藉姏
 */
public interface MailService {

    /**
     * 鍙戦€佺畝鍗曟枃鏈偖浠?
     * @param to 鏀朵欢浜洪偖绠?
     * @param subject 閭欢涓婚
     * @param content 閭欢内容
     * @return 鏄惁鍙戦€佹垚鍔?
     */
    boolean sendSimpleMail(String to, String subject, String content);

    /**
     * 鍙戦€丠TML鏍煎紡閭欢
     * @param to 鏀朵欢浜洪偖绠?
     * @param subject 閭欢涓婚
     * @param content HTML鏍煎紡鐨勯偖浠跺唴瀹?
     * @return 鏄惁鍙戦€佹垚鍔?
     */
    boolean sendHtmlMail(String to, String subject, String content);

    /**
     * 鍙戦€佸瘑鐮侀噸缃獙璇佺爜閭欢
     * @param to 鏀朵欢浜洪偖绠?     * @param resetCode 閲嶇疆验证鐮?     * @param expiresInMinutes 鏈夋晥鏈燂紙鍒嗛挓锛?     * @return 鏄惁鍙戦€佹垚鍔?     */
    boolean sendPasswordResetMail(String to, String resetCode, int expiresInMinutes);
}
