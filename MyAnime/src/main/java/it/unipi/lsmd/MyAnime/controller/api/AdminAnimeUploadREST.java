package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoNeo4j;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.validator.routines.UrlValidator;
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

    private final AnimeRepoMongoDB animeRepoMongoDB;
    private final AnimeRepoNeo4j animeRepoNeo4j;

    public AdminAnimeUploadREST(AnimeRepoMongoDB animeRepoMongoDB, AnimeRepoNeo4j animeRepoNeo4j) {
        this.animeRepoMongoDB = animeRepoMongoDB;
        this.animeRepoNeo4j = animeRepoNeo4j;
    }

    @PostMapping("/api/adminAnimeUpload")
    @Transactional("transactionManager")
    public @ResponseBody String adminAnimeUpload( HttpSession session,
            @RequestParam("title_val")              String title_val,
            @RequestParam(required = false)      String titleJapanese_val,
            @RequestParam("source_val")             String source_val,
            @RequestParam(required = false)           Integer episodes_val,
            @RequestParam(required = false)      Boolean slider_airing_val,
            @RequestParam(required = false)   String aired_input_from_val,
            @RequestParam(required = false)     String aired_input_to_val,
            @RequestParam(required = false)         String background_val,
            @RequestParam(required = false)          String broadcast_val,
            @RequestParam(required = false)           String producer_val,
            @RequestParam(required = false)           String licensor_val,
            @RequestParam(required = false)             String studio_val,
            @RequestParam(required = false)    Integer EpisodeDuration_val,
            @RequestParam("imgURL_val")             String imgURL_val,
            @RequestParam(required = false)              String type_val,
            @RequestParam(required = false)            String rating_val,
            @RequestParam(required = false)             List<String> genre_list,
            @RequestParam(required = false)             List<String> relations_list
    ){
        // only required are : title_val source_val  imgURL_val
        System.out.println("DBG -> inizio di animeAdd con parametri : ");
        System.out.println(title_val);
        System.out.println(titleJapanese_val);
        System.out.println(source_val);
        System.out.println(imgURL_val);
        System.out.println(type_val);
        System.out.println(rating_val);
        System.out.println(genre_list);
        System.out.println(episodes_val);
        System.out.println(slider_airing_val);
        System.out.println(aired_input_from_val);
        System.out.println(aired_input_to_val);
        System.out.println(background_val);
        System.out.println(broadcast_val);
        System.out.println(producer_val);
        System.out.println(licensor_val);
        System.out.println(studio_val);
        System.out.println(EpisodeDuration_val);

        if(title_val==null || title_val.isEmpty()){
            return "{\"outcome_code\": 3}";
        }
        if(imgURL_val==null || imgURL_val.isEmpty()){
            return "{\"outcome_code\": 3}";
        }
        if(source_val==null || source_val.isEmpty()){
            return "{\"outcome_code\": 3}";
        }
        try {
            if(!Utility.isLogged(session) || !Utility.isAdmin(session)){
                return "{\"outcome_code\": 1}";     // User not logged in nor admin
            }

            String[] schemes = {"http","https"};
            UrlValidator urlValidator = new UrlValidator(schemes);
            if (!urlValidator.isValid(imgURL_val)) {
                return "{\"outcome_code\": 3}";
            }

            if(!animeRepoMongoDB.insertAnime(title_val,titleJapanese_val,source_val,episodes_val,slider_airing_val,aired_input_from_val,aired_input_to_val,background_val,broadcast_val,producer_val,licensor_val, studio_val, EpisodeDuration_val, imgURL_val, type_val, rating_val, genre_list)){
                return "{\"outcome_code\": 4}";     // Anime not added to mongodb
            }

            if(!animeRepoNeo4j.insertAnime(title_val,imgURL_val)){
                return "{\"outcome_code\": 4}";     // Anime not added to Neo4j
            }

            if(relations_list!=null && !animeRepoNeo4j.insertAnimeRelations(title_val, relations_list)){
                return "{\"outcome_code\": 4}";     // Error while adding relations to Neo4j
            }

            return "{\"outcome_code\": 0}";

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 2}";         // Error while connecting to the database
        }
    }
}



