package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoNeo4j;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
            @RequestParam(required = false)         String titleJapanese_val,
            @RequestParam("source_val")             String source_val,
            @RequestParam(required = false)         Integer episodes_val,
            @RequestParam(required = false)         Boolean slider_airing_val,
            @RequestParam(required = false)         String aired_input_from_val,
            @RequestParam(required = false)         String aired_input_to_val,
            @RequestParam(required = false)         String background_val,
            @RequestParam(required = false)         String broadcast_val,
            @RequestParam(required = false)         String producer_val,
            @RequestParam(required = false)         String licensor_val,
            @RequestParam(required = false)         String studio_val,
            @RequestParam(required = false)         Integer EpisodeDuration_val,
            @RequestParam("imgURL_val")             String imgURL_val,
            @RequestParam(required = false)         List<String> type_list,
            @RequestParam(required = false)         List<String> rating_list,
            @RequestParam(required = false)         List<String> genre_list,
            @RequestParam(required = false)         List<String> relations_list
    ){

        if(title_val==null || title_val.isEmpty()){
            return "{\"outcome_code\": 1}";
        }
        if(imgURL_val==null || imgURL_val.isEmpty()){
            return "{\"outcome_code\": 1}";
        }
        if(source_val==null || source_val.isEmpty()){
            return "{\"outcome_code\": 1}";
        }
        if(genre_list==null || genre_list.isEmpty()){
            return "{\"outcome_code\": 1}";
        }
        String type_val = (type_list!=null && !type_list.isEmpty()) ? type_list.get(0) : null;
        String rating_val = (rating_list!=null && !rating_list.isEmpty()) ? rating_list.get(0) : null;
        try {
            if(!Utility.isLogged(session) || !Utility.isAdmin(session)){
                return "{\"outcome_code\": 2}";     // User not logged in nor admin
            }

            String[] schemes = {"http","https"};
            UrlValidator urlValidator = new UrlValidator(schemes);
            if (!urlValidator.isValid(imgURL_val)) {
                return "{\"outcome_code\": 3}";
            }

            if (relations_list != null) {
                for (int i = 0; i < relations_list.size(); i += 3) {
                    if (!animeRepoNeo4j.existsByTitle(relations_list.get(i)) || title_val.equals(relations_list.get(i))) {
                        return "{\"outcome_code\": 4}";    // Invalid relation title
                    }
                }
            }
            if(!animeRepoMongoDB.insertAnime(title_val,titleJapanese_val,source_val,episodes_val,slider_airing_val,aired_input_from_val,aired_input_to_val,background_val,broadcast_val,producer_val,licensor_val, studio_val, EpisodeDuration_val, imgURL_val, type_val, rating_val, genre_list)){
                return "{\"outcome_code\": 5}";     // Anime not added to mongodb
            }

            if(!animeRepoNeo4j.insertAnime(title_val,imgURL_val)){
                return "{\"outcome_code\": 6}";     // Anime not added to Neo4j
            }

            if(relations_list!=null && !animeRepoNeo4j.insertAnimeRelations(title_val, relations_list)){
                return "{\"outcome_code\": 6}";     // Error while adding relations to Neo4j
            }

            return "{\"outcome_code\": 0}";

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 7}";         // Error while connecting to the database
        }
    }
}



