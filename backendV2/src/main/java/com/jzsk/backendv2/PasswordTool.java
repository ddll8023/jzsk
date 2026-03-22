package com.jzsk.backendv2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Scanner;

public class PasswordTool {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter password: ");
        String raw = scanner.nextLine();
        String hash = new BCryptPasswordEncoder().encode(raw);
        System.out.println(hash);
        scanner.close();
    }
}
