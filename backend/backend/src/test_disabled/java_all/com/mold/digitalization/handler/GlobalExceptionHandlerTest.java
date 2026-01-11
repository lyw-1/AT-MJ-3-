package com.mold.digitalization.handler;

import com.mold.digitalization.dto.ResponseDTO;
import com.mold.digitalization.exception.BusinessException;
import com.mold.process.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 鍏ㄥ眬开傚父澶勭悊鍣ㄦ祴璇曠被
 * 测试鍚勭开傚父鎯呭喌涓嬬殑缁熶竴鍝嶅簲鏍煎紡
 */
@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
public class GlobalExceptionHandlerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    
    @Test
    public void testHandleDigitalizationBusinessException() throws Exception {
        mockMvc.perform(get("/api/test/digitalization-business-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(600))
                .andExpect(jsonPath("$.message").value("鏁板瓧妯″潡涓氬姟开傚父测试"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleProcessBusinessException() throws Exception {
        mockMvc.perform(get("/api/test/process-business-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("娴佺▼妯″潡涓氬姟开傚父测试"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleNullPointerException() throws Exception {
        mockMvc.perform(get("/api/test/null-pointer-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("空指针异常"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/api/test/illegal-argument-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数错误"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(get("/api/test/method-argument-not-valid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数校验失败"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/api/test/method-argument-type-mismatch")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("参数类型错误"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleMissingServletRequestParameterException() throws Exception {
        mockMvc.perform(get("/api/test/missing-request-parameter")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("缺少必要参数"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(get("/api/test/http-message-not-readable")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("请求消息不可读"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleHttpRequestMethodNotSupportedException() throws Exception {
        mockMvc.perform(get("/api/test/http-request-method-not-supported")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(405))
                .andExpect(jsonPath("$.message").value("请求方法不支持"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleHttpMediaTypeNotSupportedException() throws Exception {
        mockMvc.perform(get("/api/test/http-media-type-not-supported")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(415))
                .andExpect(jsonPath("$.message").value("媒体类型不支持"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleNoHandlerFoundException() throws Exception {
        mockMvc.perform(get("/api/test/no-handler-found")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("请求路径不存在"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    
    @Test
    public void testHandleException() throws Exception {
        mockMvc.perform(get("/api/test/generic-exception")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("系统内部错误"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
