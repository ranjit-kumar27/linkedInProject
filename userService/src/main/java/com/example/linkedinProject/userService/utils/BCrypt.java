package com.example.linkedinProject.userService.utils;

import static org.mindrot.jbcrypt.BCrypt.*;

public class BCrypt {
    public static String hash(String s) {
        return hashpw(s, gensalt());
    }

    public static boolean match(String passwordText, String passwordHash) {
        return checkpw(passwordText, passwordHash);
    }

}
