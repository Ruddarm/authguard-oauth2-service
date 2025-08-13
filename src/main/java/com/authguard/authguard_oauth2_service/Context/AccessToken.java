package com.authguard.authguard_oauth2_service.Context;

public class AccessToken {
    private static final ThreadLocal<String> tokenHolder = new ThreadLocal<>();

    public static String get() {
        return tokenHolder.get();
    }

    public static void set(String token) {
        tokenHolder.set(token);
    }

    public static void clear() {
        tokenHolder.remove();
    }

}
