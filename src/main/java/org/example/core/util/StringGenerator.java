package org.example.core.util;

import org.example.core.config.TestEnv;

import java.security.SecureRandom;

public class StringGenerator {
    private static final String HEX = "0123456789ABCDEF";
    private static final String ALPHANUM = buildAlphaNum('A', 'Z', '0', '9');
    private static final String INVALID_CHARS = buildUpLowNumString();
    private static final SecureRandom RND = new SecureRandom();
    
    private final TestEnv env;

    public StringGenerator(TestEnv env) {
        this.env = env;
    }

    public static String generateToken(String alphabet, int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(alphabet.charAt(RND.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    public static String randomString(int length) {
        return generateToken(INVALID_CHARS, length);
    }

    public String validToken() {
        return generateToken(ALPHANUM, env.getTokenValidLength());
    }

    public String invalidTokenWithSymbols() {
        return generateToken(INVALID_CHARS, env.getTokenValidLength());
    }

    public String invalidLongToken() {
        return generateToken(ALPHANUM, 33);
    }

    public String invalidShortToken() {
        return generateToken(ALPHANUM, 31);
    }

    private static String buildAlphaNum(char first, char last, char numFirst, char numLast) {
        StringBuilder sb = new StringBuilder();
        sb.append(buildString(first, last));
        sb.append(buildString(numFirst, numLast));
        return sb.toString();
    }

    private static String buildString(char first, char last) {
        StringBuilder sb = new StringBuilder();
        for (char c = first; c <= last; c++) sb.append(c);
        return sb.toString();
    }

    private static String buildUpLowNumString() {
        StringBuilder sb = new StringBuilder();
        sb.append(buildString('A', 'Z'));
        sb.append(buildString('a', 'z'));
        sb.append(buildString('А', 'Я'));
        sb.append(buildString('а', 'я'));
        sb.append(buildString('0', '9'));
        sb.append("!@#$%^&*()_+-=[]{}|;:'\\\",.<>?/`~");
        return sb.toString();
    }

//    public static String validToken() {
//        StringBuilder sb = new StringBuilder(32);
//        for (int i = 0; i < 32; i++) sb.append(ALPHANUM.charAt(RND.nextInt(ALPHANUM.length())));
//        return sb.toString();
//    }
//
//    public static String invalidLongToken() {
//        StringBuilder sb = new StringBuilder(33);
//        for (int i = 0; i < 33; i++) sb.append(HEX.charAt(RND.nextInt(HEX.length())));
//        return sb.toString();
//    }
//
//    public static String invalidShortToken() {
//        StringBuilder sb = new StringBuilder(31);
//        for (int i = 0; i < 31; i++) sb.append(HEX.charAt(RND.nextInt(HEX.length())));
//        return sb.toString();
//    }
}
