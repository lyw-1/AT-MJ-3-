package com.mold.digitalization.service;

import java.util.Set;

/**
 * Redis服务接口
 * 鎻愪緵Redis缂撳瓨操作鐨勫熀纭€方法
 */
public interface RedisService {

    /**
     * 设置缂撳瓨
     * @param key 閿?     * @param value 鍊?     * @param expireTime 杩囨湡鏃堕棿(绉?
     */
    void set(String key, String value, long expireTime);

    /**
     * 获取缂撳瓨
     * @param key 閿?     * @return 鍊?     */
    String get(String key);

    /**
     * 删除缂撳瓨
     * @param key 閿?     */
    void delete(String key);

    /**
     * 妫€鏌ラ敭鏄惁瀛樺湪
     * @param key 閿?     * @return 鏄惁瀛樺湪
     */
    boolean hasKey(String key);

    /**
     * 设置缂撳瓨(涓嶈缃繃鏈熸椂闂?
     * @param key 閿?     * @param value 鍊?     */
    void set(String key, String value);

    /**
     * 设置缂撳瓨杩囨湡鏃堕棿
     * @param key 閿?     * @param expireTime 杩囨湡鏃堕棿(绉?
     * @return 鏄惁设置成功
     */
    boolean expire(String key, long expireTime);
    
    /**
     * 鍚戦泦鍚堟坊鍔犲厓绱?     * @param key 閿?     * @param values 瑕佹坊鍔犵殑鍊?     * @return 添加成功鐨勫厓绱犱釜鏁?     */
    Long sAdd(String key, String... values);
    
    /**
     * 鏍规嵁妯″紡鏌ユ壘閿?     * @param pattern 閿ā开?     * @return 鍖归厤鐨勯敭闆嗗悎
     */
    Set<String> keys(String pattern);
}
