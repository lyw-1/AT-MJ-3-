package com.mold.digitalization.validation;

import com.mold.digitalization.utils.PasswordStrengthChecker;
import org.springframework.util.StringUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 密码开哄害验证鍣?
 * 实现ValidPassword娉ㄨВ鐨勯獙璇侀€昏緫
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {
    
    private boolean nullable;
    
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
    }
    
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // 濡傛灉鍏佽为空锛屼笖密码为空锛屽垯验证閫氳繃
        if (nullable && !StringUtils.hasText(password)) {
            return true;
        }
        
        // 濡傛灉密码涓嶄负绌猴紝妫€鏌ュ叾开哄害
        if (StringUtils.hasText(password)) {
            PasswordStrengthChecker.PasswordStrengthResult result =
                    PasswordStrengthChecker.checkPasswordStrength(password);

            if (!result.isMeetsSecurityRequirements()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(result.getMessage())
                       .addConstraintViolation();
                return false;
            }

            return true;
        }
        
    // 密码为空涓斾笉鍏佽为空锛岄獙璇佸け璐?
    return false;
    }
}
