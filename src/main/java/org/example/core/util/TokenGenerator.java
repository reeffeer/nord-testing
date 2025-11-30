package org.example.core.util;

import java.security.SecureRandom;

public class TokenGenerator {
    private static final String HEX = "0123456789ABCDEF";
    private static final SecureRandom RND = new SecureRandom();

    public static String validToken() {
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) sb.append(HEX.charAt(RND.nextInt(HEX.length())));
        return sb.toString();
    }
}
