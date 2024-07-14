package it.unipi.lsmd.MyAnime.controller.api;

import com.google.common.hash.Hashing;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Constants;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


// !!!!!!!!!! see what is required by adminPage.js in resources static js .

@RestController
public class AdminPageREST {

    @Autowired
    private UserRepoMongoDB userRepoMongoDB;
    @Autowired
    private AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    private ReviewRepoMongoDB reviewRepoMongoDB;
    // TODO :
    // need to connect and calculate admin data for updates and rankings !!!

    @PostMapping("/api/admin/calculateAdminStats")
    public @ResponseBody String calculateAdminStats(HttpSession session){
        if(!Utility.isAdmin(session)){
            return "{\"outcome_code\": 1}";     // User is not an admin
        }
        
        // try catch method on (neo4j?)DB
        
        /*
            needed : 
            -   dailyLikesOnAnime   // from neo4j (?)
            -   dailyReviews        // from neo4j (?)
        */
        return "TODO ...";
        //  ...
    }





    @PostMapping("/api/admin/calculateRankings")
    public @ResponseBody String calculateRankings(HttpSession session){
        // chiamata una volta a settimana

        if(!Utility.isAdmin(session)) {
            return "{\"outcome_code\": 1}"; // User is not an admin
        }

        try {
            Utility.clearRankingsDirectory(Constants.folderName_QueryResults);
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 12}";
        }

        try {
            System.out.println("LOG --> Start to calculate rankings");

            List<Anime> rankingAnimeByScoreAllTime = animeRepoMongoDB.getAnimeByScoreAllTime();
            System.out.println("LOG --> Found " + rankingAnimeByScoreAllTime.size() + " albums with at least 15 reviews");
            if(rankingAnimeByScoreAllTime.isEmpty())
                return "{\"outcome_code\": 2}";
            Utility.writeToFile(rankingAnimeByScoreAllTime, Constants.fileName_RankingAnimeByScoreAllTime, Constants.folderName_QueryResults);
            System.out.println("LOG --> Success calculation 1: " + Constants.fileName_RankingAnimeByScoreAllTime);

            System.out.println("LOG --> End rankings calculation successfully");
            return "{\"outcome_code\": 0}"; // Update successful

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                throw dae;
            }
            dae.printStackTrace();
            return "{\"outcome_code\": 10}"; // Connection database error
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"outcome_code\": 11}"; // File writing error
        }

        // probably need operations on both mongoDB and neo4j DB.
        //  write results on some data_directory for easy access on reload of mostPopularPage !!

        /*
            needed : 
            -   clear rankings dir
            -   generate rankings by : (see mostPopularPage btw)
                --  top-rated (all time)    : weighted of average all time, with a give number of minimun votes (avoid ^sandbagging)
                -   average rating all time     == most liked all time
                -   average rating this week    == most liked this week

        return "TODO ...";
        */

    }




    
    // daily upadtes :
    @PostMapping("/api/admin/updateNewLikes")
    @Transactional
    public @ResponseBody String updateNewLikesAndAvgRatings(HttpSession session){
        if(!Utility.isAdmin(session)){
            return "{\"outcome_code\": 1}";         // User is not an admin
        }
        // try catch method on MongoDB

        /*
            needed : 
            -   update review average   // from neo4j (?)
            -   dailyReviews            // from neo4j (?)
        */
        return "TODO ...";
        //  ...
    }


}
