package com.szy.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具
 */
public class PasswordUtil {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 生成 Jzsk@123456 的加密密码
        String rawPassword = "Jzsk@123456";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("========================================");
        System.out.println("原始密码: " + rawPassword);
        System.out.println("加密后密码: " + encodedPassword);
        System.out.println("========================================");
        System.out.println();
        System.out.println("SQL 更新语句：");
        System.out.println("UPDATE user SET password = '" + encodedPassword + "' WHERE username = 'admin01';");
    }
}