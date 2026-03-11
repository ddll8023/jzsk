package com.szy.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码工具类
 */
public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // 生成 Jzsk@123456 的 BCrypt 密码
        String rawPassword = "Jzsk@123456";
        String encodedPassword = encoder.encode(rawPassword);

        System.out.println("原始密码: " + rawPassword);
        System.out.println("BCrypt哈希: " + encodedPassword);
        System.out.println();
        System.out.println("SQL更新语句:");
        System.out.println("UPDATE user SET password = '" + encodedPassword + "' WHERE username = 'admin01';");
    }
}