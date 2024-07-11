package it.unipi.lsmd.MyAnime.utilities;


import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;


public class Utility {
    public static boolean isLogged(HttpSession session) {
        Object username = session.getAttribute("username");
        return username != null;
    }

    public static boolean isAdmin(HttpSession session) {
        Object role = session.getAttribute("role");
        return role != null && role.equals("admin");
    }

    public static String getRole(HttpSession session){
        Object role = session.getAttribute("role");
        if(role != null)
            return role.toString();
        else
            return "guest";
    }

    public static String getUsername(HttpSession session){
        Object username = session.getAttribute("username");
        if(username != null)
            return username.toString();
        else
            return "";
    }

    private static final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] saltBytes = new byte[16];
        sr.nextBytes(saltBytes);
        char[] hexString = new char[32];
        for (int i = 0; i < 16; i++) {
            byte v = saltBytes[i];
            hexString[i * 2] = HEX_ARRAY[v >>> 4];
            hexString[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexString);
    }
}
