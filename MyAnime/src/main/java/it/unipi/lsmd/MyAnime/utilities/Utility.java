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

    public static String generateSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }
}
