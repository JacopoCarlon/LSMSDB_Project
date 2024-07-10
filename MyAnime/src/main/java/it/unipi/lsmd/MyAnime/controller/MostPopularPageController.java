package it.unipi.lsmd.MyAnime.controller;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import it.unipi.lsmd.MyAnime.utilities.Constants;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import it.unipi.lsmd.MyAnime.model.Anime;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MostPopularPageController {


    //  TODO : verify consistency with structure in constants + model + repo + controller
    @RequestMapping("/mostPopularPage")
    public String mostPopularPage(HttpSession session,
                                  Model model) {
        model.addAttribute("logged", (Utility.isLogged(session)) ? true : false);

        try {
                //  list of anime
                List<Anime> rankingAnimeByRating_AllTime = readJsonData(Constants.fileName_RankingAnimeByRating_AllTime, new TypeReference<List<Anime>>() {});
                model.addAttribute("rankingAnimeByRating_AllTime", rankingAnimeByRating_AllTime);

                List<Anime> rankingAnimeByLikes_AllTime = readJsonData(Constants.fileName_RankingAnimeByLikes_AllTime, new TypeReference<List<Anime>>() {});
                model.addAttribute("rankingAnimeByLikes_AllTime", rankingAnimeByLikes_AllTime);

                List<Anime> rankingAnimeByLikes_LastWeek = readJsonData(Constants.fileName_RankingAnimeByLikes_LastWeek, new TypeReference<List<Anime>>() {});
                model.addAttribute("rankingAnimeByLikes_LastWeek", rankingAnimeByLikes_LastWeek);
                

            } catch (IOException e) {
                e.printStackTrace();
                return "error/genericError";
            }

            return "mostPopularPage";
    }

    private <T> List<T> readJsonData(String jsonFileName, TypeReference<List<T>> typeReference) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // Ottieni il percorso della directory corrente del server
        String currentDir = System.getProperty("user.dir");
        // Aggiungi il nome della cartella al percorso
        Path jsonFilePath = Paths.get(currentDir, Constants.folderName_QueryResults, jsonFileName);

        // Verifica che il file esista prima di procedere
        if (!Files.exists(jsonFilePath)) {
            throw new IOException("File not found: " + jsonFilePath.toString());
        }

        // Leggi il contenuto del file JSON
        String jsonContent = new String(Files.readAllBytes(jsonFilePath));

        // Deserializza il contenuto JSON in una lista di oggetti del tipo specificato
        return objectMapper.readValue(jsonContent, typeReference);
    }


}
