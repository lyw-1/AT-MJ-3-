package com.mold.digitalization.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 鏁忔劅操作绾у埆配置绫?
 */
@Configuration
@ConfigurationProperties(prefix = "log.sensitive.level")
@Data
public class SensitiveOperationConfig {
    
    /**
     * 涓寸晫绾у埆鏁忔劅操作鍒楄〃锛堥€楀彿分割锛?
     */
    private String critical = "";
    
    /**
     * 楂樼骇鍒晱鎰熸搷浣滃垪琛紙閫楀彿分割锛?
     */
    private String high = "";
    
    /**
     * 鏅€氱骇鍒晱鎰熸搷浣滃垪琛紙閫楀彿分割锛?
     */
    private String normal = "";
    
    /**
     * 获取涓寸晫绾у埆鏁忔劅操作闆嗗悎
     */
    public Set<String> getCriticalOperations() {
        return parseOperations(critical);
    }
    
    /**
     * 获取楂樼骇鍒晱鎰熸搷浣滈泦鍚?
     */
    public Set<String> getHighOperations() {
        return parseOperations(high);
    }
    
    /**
     * 获取鏅€氱骇鍒晱鎰熸搷浣滈泦鍚?
     */
    public Set<String> getNormalOperations() {
        return parseOperations(normal);
    }
    
    /**
     * 瑙ｆ瀽操作瀛楃涓蹭负闆嗗悎
     */
    private Set<String> parseOperations(String operations) {
        if (operations == null || operations.trim().isEmpty()) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(operations.split(",")));
    }
    
    /**
     * 鏍规嵁操作绫诲瀷获取鏁忔劅绾у埆
     */
    public String getSensitiveLevelByOperation(String operationType) {
        if (operationType == null) {
            return "normal";
        }
        
        String op = operationType.toLowerCase().trim();
        
        if (getCriticalOperations().contains(op)) {
            return "critical";
        } else if (getHighOperations().contains(op)) {
            return "high";
        } else if (getNormalOperations().contains(op)) {
            return "normal";
        }
        
        return "normal"; // 榛樿鏅€氱骇鍒?
    }
}
