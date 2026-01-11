package com.mold.digitalization.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 轻量重定向：兼容常见的 Springdoc 路径，将 /swagger-ui/index.html 映射到 Knife4j 的 /doc.html。
 * 保持原有 Springfox/Knife4j 方案不变，仅提供用户友好的入口。
 */
@Controller
public class SwaggerRedirectController {

    /**
     * 重定向常见的 Swagger UI 入口到 Knife4j 文档。
     */
    @GetMapping({"/swagger-ui/index.html", "/swagger-ui", "/swagger-ui/", "/swagger-ui.html"})
    public String redirectToDoc() {
        return "redirect:/doc.html";
    }
}
