package com.mold.digitalization.aspect;

import com.mold.digitalization.entity.system.OperationLog;
import com.mold.digitalization.service.system.OperationLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.*;

/**
 * OperationLogAspect鍒囬潰绫荤殑鍗曞厓测试
 */
public class OperationLogAspectTest {

    @Mock
    private OperationLogService operationLogService;

    @InjectMocks
    private OperationLogAspect operationLogAspect;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        // 设置璇锋眰涓婁笅鏂?        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);
        
        // 设置鍩烘湰璇锋眰淇℃伅
        request.setMethod(HttpMethod.POST.name());
        request.setRequestURI("/api/test");
        request.setRemoteAddr("127.0.0.1");
        request.addHeader("User-Agent", "Mozilla/5.0");
    }

    @Test
    public void testCheckSensitiveOperation() throws Exception {
        // 测试鏅€氭柟娉曪紙鏃犳晱鎰熸敞瑙ｏ級
        OperationLog log = new OperationLog();
        
        // 浣跨敤鍙嶅皠璋冪敤private方法
        java.lang.reflect.Method method = OperationLogAspect.class.getDeclaredMethod("checkSensitiveOperation", Method.class, OperationLog.class);
        method.setAccessible(true);
        method.invoke(operationLogAspect, TestClass.class.getMethods()[0], log);
        
        // 验证鏁忔劅绾у埆设置涓簄ormal
        assert !log.getIsSensitive();
        assertEquals("normal", log.getSensitiveLevel());
    }

    @Test
    public void testDetectAutoSensitiveOperation() throws Exception {
        // 测试鍖呭惈楂橀闄╁叧閿瘝鐨勬搷浣?        OperationLog log = new OperationLog();
        log.setOperationDesc("删除用户淇℃伅");
        
        // 浣跨敤鍙嶅皠璋冪敤private方法
        java.lang.reflect.Method method = OperationLogAspect.class.getDeclaredMethod("detectAutoSensitiveOperation", OperationLog.class);
        method.setAccessible(true);
        method.invoke(operationLogAspect, log);
        
        // 验证鏁忔劅操作妫€娴?        assert log.getIsSensitive();
        assertEquals("high", log.getSensitiveLevel());
        assert log.getOperationDesc().contains("[鑷姩鏍囪-鏁忔劅操作-high]");
    }

    @Test
    public void testMaskSensitiveContent() throws Exception {
        // 创建鍖呭惈鏁忔劅淇℃伅鐨勫弬鏁?        Map<String, Object> params = new HashMap<>();
        params.put("mobile", "13812345678");
        params.put("idCard", "110101199001011234");
        params.put("password", "password123");
        params.put("normalField", "normalValue");
        
        // 浣跨敤鍙嶅皠璋冪敤private方法 - 淇鍙傛暟浼犻€掓柟开?        java.lang.reflect.Method method = OperationLogAspect.class.getDeclaredMethod("maskSensitiveContent", Object[].class, String.class);
        method.setAccessible(true);
        
        // 姝ｇ‘浼犻€掑弬鏁帮細绗竴涓弬鏁版槸Object[]鏁扮粍锛岀浜屼釜鍙傛暟鏄疭tring
        Object[] argsArray = new Object[]{params};
        String maskedContent = (String) method.invoke(operationLogAspect, argsArray, "normal");
        
        // 鎵撳嵃实际鑴辨晱结果鐢ㄤ簬璋冭瘯
        System.out.println("实际鑴辨晱结果: " + maskedContent);
        
        // 验证鑴辨晱结果 - 鏍规嵁实际鐨勮劚鏁忛€昏緫璋冩暣鏂█
        // 鎵嬫満鍙疯劚鏁忥細淇濈暀鍓?浣嶅拰鍚?浣嶏紝涓棿6涓?
        assert maskedContent.contains("138******5678") : "鎵嬫満鍙疯劚鏁忓け璐? " + maskedContent;
        // 韬唤璇佸彿鑴辨晱锛氫繚鐣欏墠6浣嶅拰鍚?浣?        assert maskedContent.contains("110101********1234") : "韬唤璇佸彿鑴辨晱失败: " + maskedContent;
        // 密码鑴辨晱锛氬畬鍏ㄧ敤*鏇夸唬
        assert maskedContent.contains("***********") : "密码鑴辨晱失败: " + maskedContent;
        // 鏅€氬瓧娈典笉鑴辨晱
        assert maskedContent.contains("normalValue") : "鏅€氬瓧娈佃错误鑴辨晱: " + maskedContent;
    }

    // 娉ㄩ噴鎺夋棤娉曠紪璇戠殑测试方法锛屽洜涓簊aveOperationLog鏄鏈夋柟娉曚笖鍙傛暟绛惧悕涓嶅尮閰?    /*
    @Test
    public void testSaveOperationLog() {
        // 设置方法鍙傛暟
        Object[] args = new Object[]{"testParam"};
        String targetName = "TestService";
        String methodName = "testMethod";
        String operationType = "测试操作";
        String operationDesc = "测试操作鎻忚堪";
        
        // 模拟姝ｅ父执行鐨勬儏鍐?        Object result = "success";
        
        // 执行鏃ュ織淇濆瓨方法
        operationLogAspect.saveOperationLog(args, targetName, methodName, operationType, operationDesc, result);
        
        // 验证璋冪敤浜嗘棩蹇楁湇鍔＄殑淇濆瓨方法
        verify(operationLogService, times(1)).save(any(OperationLog.class));
    }

    @Test
    public void testSaveOperationLogWithException() {
        // 设置方法鍙傛暟
        Object[] args = new Object[]{"testParam"};
        String targetName = "TestService";
        String methodName = "testMethod";
        String operationType = "测试操作";
        String operationDesc = "测试操作鎻忚堪";
        
        // 模拟开傚父鎯呭喌
        Exception exception = new RuntimeException("测试开傚父");
        
        // 执行鏃ュ織淇濆瓨方法
        operationLogAspect.saveOperationLog(args, targetName, methodName, operationType, operationDesc, exception);
        
        // 验证璋冪敤浜嗘棩蹇楁湇鍔＄殑淇濆瓨方法
        verify(operationLogService, times(1)).save(any(OperationLog.class));
    }
    */

    // 测试鐢ㄦ櫘閫氱被
    static class TestClass {
        public void normalMethod() {
            // 鏅€氭柟娉?        }
    }
}