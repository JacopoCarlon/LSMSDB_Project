package it.unipi.lsmd.MyAnime.utilities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipi.lsmd.MyAnime.model.Anime;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.List;


public class Utility {
    public static boolean isLogged(HttpSession session) {
        Object logged = session.getAttribute("is_logged");
        return logged != null && logged.equals(true);
    }

    public static boolean isAdmin(HttpSession session) {
        Object admin = session.getAttribute("is_admin");
        return admin != null && admin.equals(true);
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

    public static void clearRankingsDirectory(String folderName) throws IOException {
        String currentDir = System.getProperty("user.dir");
        String subFolderPath = currentDir + File.separator + folderName;
        File subFolder = new File(subFolderPath);

        if (subFolder.exists() && subFolder.isDirectory()) {
            File[] files = subFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.delete()) {
                        throw new IOException("Failed to delete " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    public static void writeToFile(Object data, String fileName, String folderName) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String json = objectMapper.writeValueAsString(data);

        String currentDir = System.getProperty("user.dir");
        String subFolderPath = currentDir + File.separator + folderName;
        File subFolder = new File(subFolderPath);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }

        Path filePath = Paths.get(subFolder.getPath() + File.separator + fileName);
        Files.write(filePath, json.getBytes(), StandardOpenOption.CREATE);
    }
}
