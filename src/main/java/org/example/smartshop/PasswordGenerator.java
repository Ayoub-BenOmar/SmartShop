package org.example.smartshop;

import org.example.smartshop.util.PasswordUtil;

public class PasswordGenerator {

    public static void main(String[] args) {
        String rawPassword = "1230";
        String hashed = PasswordUtil.hash(rawPassword);
        System.out.println("Hashed password: " + hashed);
    }
}
