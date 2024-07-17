package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



@RestController
public class AdminAnimeUploadREST {

    @PostMapping("/api/adminAnimeUpload")
    @Transactional
    public @ResponseBody String signup( HttpSession session,
            @RequestParam("title_val")              String title_val,
            @RequestParam("titleJapanese_val")      String titleJapanese_val,
            @RequestParam("source_val")             String source_val,
            @RequestParam("episodes_val")           Integer episodes_val,
            @RequestParam("slider_airing_val")      Boolean slider_airing_val,
            @RequestParam("aired_input_from_val")   Instant aired_input_from_val,
            @RequestParam("aired_input_to_val")     Instant aired_input_to_val,
            @RequestParam("background_val")         String background_val,
            @RequestParam("broadcast_val")          String broadcast_val,
            @RequestParam("producer_val")           String producer_val,
            @RequestParam("licensor_val")           String licensor_val,
            @RequestParam("studio_val")             String studio_val,
            @RequestParam("EpisodeDuration_val")    Integer EpisodeDuration_val,
            @RequestParam("imgURL_val")             String imgURL_val,
            @RequestParam("type_list")              List<String> type_list,
            @RequestParam("rating_list")            List<String> rating_list,
            @RequestParam("genre_list")             List<String> genre_list
    ){
        // only required are : title_val source_val  imgURL_val

        try {
            if(!Utility.isLogged(session)){
                return "{\"outcome_code\": 1}";     // User not logged in
            }

            // stuff



            // TODO if all good :
            return "{\"outcome_code\": 0}";

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 2}";         // Error while connecting to the database
        }
        // TODO : all the anime upload stuff

    }
}



