package com.mold.digitalization.config;

import com.mold.digitalization.entity.User;
import com.mold.digitalization.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * 生产环境：启动时确保admin用户存在且密码正确
 */
@Component
@Profile({"prod", "dev"})
public class AdminUserInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting AdminUserInitializer...");
        try {
            // 检查admin用户是否存在
            logger.info("Checking if admin user exists...");
            User adminUser = userService.getUserByUsername("admin");
            if (adminUser != null) {
                logger.info("Admin user found, checking password...");
                // 强制重置admin用户密码，确保密码为"admin123456"
                logger.info("Admin user found, resetting password to 'admin123456'...");
                boolean resetResult = userService.resetPassword(adminUser.getId(), "admin123456");
                if (resetResult) {
                    logger.info("Admin user password has been reset to 'admin123456'");
                } else {
                    logger.error("Failed to reset admin user password");
                }
            } else {
                logger.info("Admin user not found, creating new admin user...");
                // 如果admin用户不存在，创建新的admin用户
                User newAdmin = new User();
                newAdmin.setUsername("admin");
                newAdmin.setPassword("admin123456"); // 密码会在createUser方法中被加密
                newAdmin.setRealName("系统管理员");
                newAdmin.setPhone("13800138000");
                newAdmin.setEmail("admin@example.com");
                newAdmin.setStatus(1); // 启用状态
                boolean createResult = userService.createUser(newAdmin);
                if (createResult) {
                    logger.info("Admin user has been created with password 'admin123456'");
                } else {
                    logger.error("Failed to create admin user");
                }
            }
        } catch (Exception e) {
            logger.error("Error in AdminUserInitializer: " + e.getMessage(), e);
        }
    }
}