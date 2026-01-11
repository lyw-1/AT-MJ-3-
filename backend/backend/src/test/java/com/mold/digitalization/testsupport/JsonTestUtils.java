package com.mold.digitalization.testsupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 测试环境下统一的 ObjectMapper 构建工具。
 * 提供对 Java 8 日期时间类型的支持，并禁用时间戳写出，避免产生 500/400 错误。
 */
public final class JsonTestUtils {

    private JsonTestUtils() {}

    /**
     * 构建并返回测试使用的 ObjectMapper。
     */
    public static ObjectMapper newObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}

