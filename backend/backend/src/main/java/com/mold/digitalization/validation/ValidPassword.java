package com.mold.digitalization.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 鑷畾涔夊瘑鐮佸己搴﹂獙璇佹敞瑙?
 * 鐢ㄤ簬验证密码鏄惁绗﹀悎瀹夊叏瑕佹眰
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    
    // 错误淇℃伅
    String message() default "密码不符合规则：长度需在8-20之间，且仅允许字母和数字，并需同时包含字母与数字";
    
    // 鏄惁鍏佽为空
    boolean nullable() default false;
    
    // 鍒嗙粍
    Class<?>[] groups() default {};
    
    // 杞借嵎
    Class<? extends Payload>[] payload() default {};
}
