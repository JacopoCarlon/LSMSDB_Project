package it.unipi.lsmd.MyAnime.controller.api;

import com.google.common.hash.Hashing;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;


// !!!!!!!!!! see what is required by adminPage.js in resources static js .


public class AdminPageREST {
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

        if(!Utility.isAdmin(session))
            return "{\"outcome_code\": 1}"; // User is not an admin

        // probably need operations on both mongoDB and neo4j DB.
        //  write results on some data_directory for easy acces on reload of mostPopularPage !!

        /*
            needed : 
            -   clear rankings dir
            -   generate rankings by : (see mostPopularPage btw)
                --  top-rated (all time)    : weighted of average all time, with a give number of minimun votes (avoid ^sandbagging)
                -   average rating all time     == most liked all time
                -   average rating this week    == most liked this week
        */
        return "TODO ...";
        //  ...

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
