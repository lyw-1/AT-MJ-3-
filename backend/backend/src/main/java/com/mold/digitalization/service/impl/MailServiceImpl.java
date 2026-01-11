package com.mold.digitalization.service.impl;

import com.mold.digitalization.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * 邮件服务实现类
 * 使用JavaMailSender实现邮件发送功能
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.sender}")
    private String mailSender;

    @Value("${mail.sender.name}")
    private String mailSenderName;

    /**
     * 发送简单文本邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return 是否发送成功
     */
    @Override
    public boolean sendSimpleMail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailSender);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            javaMailSender.send(message);
            log.info("Simple mail sent successfully, to: {}", to);
            return true;
        } catch (Exception e) {
            log.error("Failed to send simple mail, to: {}", to, e);
            return false;
        }
    }

    /**
     * 发送HTML格式邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param content HTML格式的邮件内容
     * @return 是否发送成功
     */
    @Override
    public boolean sendHtmlMail(String to, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(mailSender, mailSenderName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // true表示是HTML格式
            
            javaMailSender.send(message);
            log.info("HTML mail sent successfully, to: {}", to);
            return true;
        } catch (Exception e) {
            log.error("Failed to send HTML mail, to: {}", to, e);
            return false;
        }
    }

    /**
     * 发送密码重置验证码邮件
     * @param to 收件人邮箱
     * @param resetCode 重置验证码
     * @param expiresInMinutes 有效期（分钟）
     * @return 是否发送成功
     */
    @Override
    public boolean sendPasswordResetMail(String to, String resetCode, int expiresInMinutes) {
    String subject = "Mold Process - Password Reset Verification Code";

    // build HTML content
    String content = buildPasswordResetHtmlContent(resetCode, expiresInMinutes);

    return sendHtmlMail(to, subject, content);
    }

    /**
     * 构建密码重置邮件的HTML内容
     * @param resetCode 重置验证码
     * @param expiresInMinutes 有效期（分钟）
     * @return HTML格式的邮件内容
     */
    private String buildPasswordResetHtmlContent(String resetCode, int expiresInMinutes) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>")
                  .append("<html>")
                  .append("<head>")
                  .append("<meta charset=\"UTF-8\">")
                  .append("<style>")
                  .append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }")
                  .append(".container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eee; }")
                  .append(".header { background-color: #f8f9fa; padding: 15px; text-align: center; }")
                  .append(".code { font-size: 24px; font-weight: bold; color: #007bff; padding: 10px; background-color: #f8f9fa; text-align: center; }")
                  .append(".footer { margin-top: 20px; font-size: 12px; color: #666; text-align: center; }")
                  .append("</style>")
                  .append("</head>")
                  .append("<body>")
                  .append("<div class=\"container\">")
                  .append("<div class=\"header\">")
                  .append("<h2>密码重置请求</h2>")
                  .append("</div>")
                  .append("<p>您好：</p>")
                  .append("<p>我们收到了您的密码重置请求，请使用以下验证码完成密码重置操作：</p>")
                  .append("<div class=\"code\">")
                  .append(resetCode)
                  .append("</div>")
                  .append("<p>该验证码将在 ").append(expiresInMinutes).append(" 分钟后过期。</p>")
                  .append("<p>如果您没有发起此请求，请忽略这封邮件。</p>")
                  .append("<div class=\"footer\">")
                  .append("<p>模具数字化系统- 请拿回复邮件</p>")
                  .append("</div>")
                  .append("</div>")
                  .append("</body>")
                  .append("</html>");
        
        return htmlContent.toString();
    }
}
