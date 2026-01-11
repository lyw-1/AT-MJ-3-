package com.mold.digitalization.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mold.digitalization.handler.GlobalExceptionHandler;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

/**
 * 为控制器切片测试提供统一的 MockMvc 构建工厂。
 * 统一注册 UTF-8 编码过滤器与消息转换器，避免中文乱码与 JSON 序列化差异。
 */
public final class MockMvcFactory {

    private MockMvcFactory() {}

    /**
     * 使用 standaloneSetup 构建 MockMvc，并统一注册 UTF-8 与 JSON 转换器。
     * 可选传入一个或多个 @ControllerAdvice 以启用全局异常处理。
     */
    public static MockMvc standalone(Object controller, ObjectMapper objectMapper, Object... controllerAdvices) {
        StandaloneMockMvcBuilder builder = MockMvcBuilders.standaloneSetup(controller)
                .addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .setMessageConverters(
                        new StringHttpMessageConverter(StandardCharsets.UTF_8),
                        new MappingJackson2HttpMessageConverter(objectMapper)
                );

        if (controllerAdvices != null && controllerAdvices.length > 0) {
            builder.setControllerAdvice(controllerAdvices);
        }
        // 默认注册全局异常处理，确保统一错误返回结构
        else {
            builder.setControllerAdvice(new GlobalExceptionHandler());
        }

        return builder.build();
    }

    /**
     * 简化重载：默认注册 GlobalExceptionHandler。
     */
    public static MockMvc standalone(Object controller, ObjectMapper objectMapper) {
        return standalone(controller, objectMapper, new GlobalExceptionHandler());
    }
}
