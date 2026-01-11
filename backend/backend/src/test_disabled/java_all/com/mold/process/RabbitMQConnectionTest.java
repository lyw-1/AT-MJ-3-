package com.mold.process;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * RabbitMQ连接测试类
 * 验证Spring Boot应用能否正常连接到RabbitMQ
 */
@SpringBootTest
public class RabbitMQConnectionTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testRabbitMQConnection() {
        System.out.println("开始测试RabbitMQ连接...");
        
        // 验证RabbitTemplate是否成功注入
        assertNotNull(rabbitTemplate, "RabbitTemplate注入失败");
        System.out.println("RabbitTemplate注入成功");
        
        try {
            // 发送一个测试消息到默认交换机
            String testMessage = "Hello RabbitMQ from Spring Boot Test";
            rabbitTemplate.convertAndSend("test-exchange", "test.routing.key", testMessage);
            System.out.println("测试消息发送成功: " + testMessage);
            
            // 如果能发送消息，则说明连接正常
            assertTrue(true, "RabbitMQ连接成功");
            System.out.println("RabbitMQ连接测试通过！");
            
        } catch (Exception e) {
            System.err.println("RabbitMQ连接或消息发送失败: " + e.getMessage());
            e.printStackTrace();
            throw e; // 抛出异常使测试失败
        }
    }
}