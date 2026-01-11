package com.mold.digitalization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 鍩虹Controller绫?
 * 鎻愪緵閫氱敤鐨凜ontroller功能鍜屽搷搴斿鐞?
 */
public abstract class BaseController {
    
    /**
     * 成功鍝嶅簲
     * @param data 鍝嶅簲数据
     * @param <T> 数据绫诲瀷
     * @return 鍝嶅簲实体
     */
    protected <T> ResponseEntity<T> success(T data) {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
    
    /**
     * 成功鍝嶅簲锛堝瓧绗︿覆娑堟伅锛?
     * @param message 鍝嶅簲娑堟伅
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<String> success(String message) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    
    /**
     * 成功鍝嶅簲锛屾棤数据
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<Void> success() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * 创建成功鍝嶅簲
     * @param data 鍝嶅簲数据
     * @param <T> 数据绫诲瀷
     * @return 鍝嶅簲实体
     */
    protected <T> ResponseEntity<T> created(T data) {
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }
    
    /**
     * 鍙傛暟错误鍝嶅簲
     * @param message 错误娑堟伅
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<String> badRequest(String message) {
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * 鏈巿鏉冨搷搴?
     * @param message 错误娑堟伅
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<String> unauthorized(String message) {
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * 绂佹访问鍝嶅簲
     * @param message 错误娑堟伅
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<String> forbidden(String message) {
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }
    
    /**
     * 璧勬簮涓嶅瓨鍦ㄥ搷搴?
     * @param message 错误娑堟伅
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<String> notFound(String message) {
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
    
    /**
     * 璧勬簮涓嶅瓨鍦ㄥ搷搴旓紙娉涘瀷鐗堟湰锛?
     * @param message 错误娑堟伅
     * @param <T> 数据绫诲瀷
     * @return 鍝嶅簲实体
     */
    protected <T> ResponseEntity<T> notFoundGeneric(String message) {
        // 鐢变簬娉涘瀷闄愬埗锛岃繖閲岃繑鍥炵┖鍝嶅簲浣擄紝浣嗙姸鎬佺爜涓篘OT_FOUND
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    /**
     * 服务鍣ㄩ敊璇搷搴?
     * @param message 错误娑堟伅
     * @return 鍝嶅簲实体
     */
    protected ResponseEntity<String> internalServerError(String message) {
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
