package com.mold.digitalization.annotation;

import java.lang.annotation.*;

/**
 * 敏感操作注解
 * 用于标记需要特殊审计的敏感操作
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveOperation {
    
    /**
     * 敏感操作级别
     * 默认为普通敏感操作
     */
    String level() default "normal"; // normal, high, critical
    
    /**
     * 敏感操作描述
     */
    String description() default "敏感操作";
    
    /**
     * 是否记录详细参数
     * true: 记录完整参数（注意脱敏）
     * false: 简化参数记录
     */
    boolean recordDetail() default true;
}
