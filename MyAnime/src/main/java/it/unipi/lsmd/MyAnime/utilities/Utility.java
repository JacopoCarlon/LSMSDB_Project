package it.unipi.lsmd.MyAnime.utilities;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.SecureRandom;
import java.util.HashMap;


public class Utility {
    public static boolean isLogged(HttpSession session) {
        Object logged = session.getAttribute("logged");
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
            int v = saltBytes[i];
            hexString[i * 2] = HEX_ARRAY[(v >>> 4) & 0x0F];
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

    public static String statusFromInt(int statusInt) {
        switch (statusInt) {
            case 1:
                return "Currently Watching";
            case 2:
                return "Completed";
            case 3:
                return "On Hold";
            case 4:
                return "Dropped";
            default:
                return "Plan To Watch";
        }
    }

    public static HashMap<String,String> ratingMapper() {
        HashMap<String, String> ratingMapper = new HashMap<>();
        ratingMapper.put("rating-g", "G - All Ages");
        ratingMapper.put("rating-pg", "PG - Children");
        ratingMapper.put("rating-pg_13", "PG 13 - Teens 13 and Older");
        ratingMapper.put("rating-r", "R - 17+, Violence & Profanity");
        ratingMapper.put("rating-r+", "R+ - Profanity & Mild Nudity");
        ratingMapper.put("rating-rx", "Rx - Hentai");
        return ratingMapper;
    }

    public static String yearMapping(String year) {
        if (year != null && year.startsWith("year-") && year.length() > 5) {
            return year.substring(5);
        }
        return null;
    }

    public static HashMap<String,String> typeMapper() {
        HashMap<String, String> ratingMapper = new HashMap<>();
        ratingMapper.put("type-movie", "Movie");
        ratingMapper.put("type-music", "Music");
        ratingMapper.put("type-ona", "ONA");
        ratingMapper.put("type-ova", "OVA");
        ratingMapper.put("type-special", "Special");
        ratingMapper.put("type-tv", "TV");
        return ratingMapper;
    }

    public static HashMap<String,String> genreMapper(){
        HashMap<String, String> genreMapper = new HashMap<>();
        genreMapper.put("genre-action", "Action");
        genreMapper.put("genre-adventure", "Adventure");
        genreMapper.put("genre-cars", "Cars");
        genreMapper.put("genre-comedy", "Comedy");
        genreMapper.put("genre-dementia", "Dementia");
        genreMapper.put("genre-demons", "Demons");
        genreMapper.put("genre-drama", "Drama");
        genreMapper.put("genre-ecchi", "Ecchi");
        genreMapper.put("genre-fantasy", "Fantasy");
        genreMapper.put("genre-game", "Game");
        genreMapper.put("genre-harem", "Harem");
        genreMapper.put("genre-hentai", "Hentai");
        genreMapper.put("genre-historical", "Historical");
        genreMapper.put("genre-horror", "Horror");
        genreMapper.put("genre-josei", "Josei");
        genreMapper.put("genre-kids", "Kids");
        genreMapper.put("genre-magic", "Magic");
        genreMapper.put("genre-martial_arts", "Martial Arts");
        genreMapper.put("genre-mecha", "Mecha");
        genreMapper.put("genre-military", "Military");
        genreMapper.put("genre-music", "Music");
        genreMapper.put("genre-mystery", "Mystery");
        genreMapper.put("genre-parody", "Parody");
        genreMapper.put("genre-police", "Police");
        genreMapper.put("genre-psychological", "Psychological");
        genreMapper.put("genre-romance", "Romance");
        genreMapper.put("genre-samurai", "Samurai");
        genreMapper.put("genre-school", "School");
        genreMapper.put("genre-sci-fi", "Sci-Fi");
        genreMapper.put("genre-seinen", "Seinen");
        genreMapper.put("genre-shoujo", "Shoujo");
        genreMapper.put("genre-shoujo_ai", "Shoujo Ai");
        genreMapper.put("genre-shounen", "Shounen");
        genreMapper.put("genre-shounen_ai", "Shounen Ai");
        genreMapper.put("genre-slice_of_life", "Slice of Life");
        genreMapper.put("genre-space", "Space");
        genreMapper.put("genre-sports", "Sports");
        genreMapper.put("genre-super_power", "Super Power");
        genreMapper.put("genre-supernatural", "Supernatural");
        genreMapper.put("genre-thriller", "Thriller");
        genreMapper.put("genre-vampire", "Vampire");
        genreMapper.put("genre-yaoi", "Yaoi");
        genreMapper.put("genre-yuri", "Yuri");
        return genreMapper;
    }
}
