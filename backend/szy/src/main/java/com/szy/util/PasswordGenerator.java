package com.szy.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 * 用于生成 BCrypt 格式的密码哈希
 * 
 * 使用方法：运行 main 方法即可在控制台输出 BCrypt 密码
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 生成 admin123 的 BCrypt 哈希
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("===========================================");
        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt 哈希: " + encodedPassword);
        System.out.println("===========================================");
        System.out.println();
        System.out.println("请使用以下 SQL 语句更新数据库:");
        System.out.println();
        System.out.println("UPDATE user SET password = '" + encodedPassword + "' WHERE username = 'admin01';");
        System.out.println();
        System.out.println("===========================================");

        // 验证密码是否匹配
        boolean matches = encoder.matches(rawPassword, encodedPassword);
        System.out.println("密码验证结果: " + (matches ? "✓ 匹配成功" : "✗ 匹配失败"));
    }
}
