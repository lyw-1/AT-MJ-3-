package com.mold.digitalization.controller;

import com.mold.digitalization.exception.BusinessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Collections;

/**
 * 开傚父测试控制鍣? * 鐢ㄤ簬测试鍏ㄥ眬开傚父澶勭悊鍣ㄧ殑鍚勭开傚父鎯呭喌
 */
@RestController
@RequestMapping("/api/test")
public class ExceptionTestController {

    /**
     * 瑙﹀彂鏁板瓧妯″潡涓氬姟开傚父
     */
    @GetMapping("/digitalization-business-exception")
    public void triggerDigitalizationBusinessException() {
        throw new BusinessException(BusinessException.ErrorCode.BUSINESS_ERROR.getCode(), "鏁板瓧妯″潡涓氬姟开傚父测试");
    }
    
    /**
     * 瑙﹀彂娴佺▼妯″潡涓氬姟开傚父
     */
    @GetMapping("/process-business-exception")
    public void triggerProcessBusinessException() {
        throw new BusinessException(400, "娴佺▼妯″潡涓氬姟开傚父测试");
    }
    
    /**
     * 瑙﹀彂绌烘寚閽堝紓甯?     */
    @GetMapping("/null-pointer-exception")
    public void triggerNullPointerException() {
        String str = null;
        str.length(); // 杩欎細鎶涘嚭NullPointerException
    }
    
    /**
     * 瑙﹀彂闈炴硶鍙傛暟开傚父
     */
    @GetMapping("/illegal-argument-exception")
    public void triggerIllegalArgumentException(@RequestParam(required = false) String param) {
        if (param == null || param.isEmpty()) {
            throw new IllegalArgumentException("鍙傛暟错误");
        }
    }
    
    /**
     * 瑙﹀彂方法鍙傛暟鏍￠獙开傚父
     */
    @GetMapping("/method-argument-not-valid")
    public void triggerMethodArgumentNotValidException() throws MethodArgumentNotValidException {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勬牎楠屾鏋惰Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        // 构造一个最小可用的 BindingResult 和 MethodParameter
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "obj");
        bindingResult.addError(new ObjectError("obj", "参数校验失败"));
        MethodParameter methodParameter = new MethodParameter(getDummyMethod(), 0);
        throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
    
    /**
     * 瑙﹀彂方法鍙傛暟绫诲瀷涓嶅尮閰嶅紓甯?     */
    @GetMapping("/method-argument-type-mismatch")
    public void triggerMethodArgumentTypeMismatchException() {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勭被鍨嬭浆鎹㈣Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        throw new org.springframework.beans.TypeMismatchException("test", Integer.class);
    }
    
    /**
     * 瑙﹀彂缂哄皯璇锋眰鍙傛暟开傚父
     */
    @GetMapping("/missing-request-parameter")
    public void triggerMissingServletRequestParameterException() throws org.springframework.web.bind.MissingServletRequestParameterException {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勫弬鏁拌В鏋愯Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        throw new org.springframework.web.bind.MissingServletRequestParameterException("requiredParam", String.class.getSimpleName());
    }
    
    /**
     * 瑙﹀彂HTTP娑堟伅涓嶅彲璇诲紓甯?     */
    @GetMapping("/http-message-not-readable")
    public void triggerHttpMessageNotReadableException() {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勬秷鎭浆鎹㈣Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        throw new org.springframework.http.converter.HttpMessageNotReadableException("请求消息不可读", (Throwable) null);
    }
    
    /**
     * 瑙﹀彂HTTP璇锋眰方法涓嶆敮鎸佸紓甯?     */
    @GetMapping("/http-request-method-not-supported")
    public void triggerHttpRequestMethodNotSupportedException() throws org.springframework.web.HttpRequestMethodNotSupportedException {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勮姹傛槧灏勮Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        throw new org.springframework.web.HttpRequestMethodNotSupportedException("POST", new String[]{"GET"});
    }
    
    /**
     * 瑙﹀彂HTTP濯掍綋绫诲瀷涓嶆敮鎸佸紓甯?     */
    @GetMapping("/http-media-type-not-supported")
    public void triggerHttpMediaTypeNotSupportedException() throws org.springframework.web.HttpMediaTypeNotSupportedException {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勫唴瀹瑰崗鍟嗚Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        throw new org.springframework.web.HttpMediaTypeNotSupportedException(MediaType.APPLICATION_XML, Collections.emptyList());
    }
    
    /**
     * 瑙﹀彂鏃犲鐞嗗櫒开傚父
     */
    @GetMapping("/no-handler-found")
    public void triggerNoHandlerFoundException() throws org.springframework.web.servlet.NoHandlerFoundException {
        // 杩欎釜开傚父閫氬父鐢盨pring鐨勮姹傛槧灏勮Е鍙戯紝杩欓噷鎵嬪姩鎶涘嚭
        throw new org.springframework.web.servlet.NoHandlerFoundException("GET", "/non-existent-path", new HttpHeaders());
    }

    // 提供一个用于构造 MethodParameter 的虚拟方法
    private void dummyMethod(String param) {}

    private java.lang.reflect.Method getDummyMethod() {
        try {
            return ExceptionTestController.class.getDeclaredMethod("dummyMethod", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 瑙﹀彂閫氱敤开傚父
     */
    @GetMapping("/generic-exception")
    public void triggerGenericException() {
        throw new RuntimeException("系统鍐呴儴错误");
    }
}
