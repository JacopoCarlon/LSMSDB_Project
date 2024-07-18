package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.query.*;
import it.unipi.lsmd.MyAnime.repository.*;
import it.unipi.lsmd.MyAnime.utilities.Constants;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
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
    @Autowired
    private AnimeRepoNeo4j animeRepoNeo4j;
    @Autowired
    private UserRepoNeo4j userRepoNeo4j;

    // TODO :
    // need to connect and calculate admin data for updates and rankings !!!

    @PostMapping("/api/admin/calculateAdminStats")
    @Transactional("transactionManager")
    public @ResponseBody String calculateAdminStats(HttpSession session){
        if(!Utility.isAdmin(session)){
            return "{\"outcome_code\": 1}";     // User is not an admin
        }
        try {
            System.out.println(">> START: calculating admin stats");

            List<GenreScored> genreScoreds = animeRepoMongoDB.getGenreScored();

            for(GenreScored genreScored : genreScoreds){
                System.out.println(genreScored);
            }

            List<UsersPerDate> usersPerDates = userRepoMongoDB.getUsersPerDates();

            for(UsersPerDate usersPerDate : usersPerDates){
                System.out.println(usersPerDate);
            }

            List<ReviewsPerDate> reviewsPerDates = reviewRepoMongoDB.getReviewsPerDates();

            for(ReviewsPerDate reviewsPerDate : reviewsPerDates){
                System.out.println(reviewsPerDate);
            }
            System.out.println(">>> END: All new watchers and average scores updated successfully");
            return "{\"outcome_code\": 0}";             // Update successful

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 10}";            // Error while connecting to the database
        }
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
            System.out.println("LOG --> Found " + rankingAnimeByScoreAllTime.size() + " anime with at least 100 reviews");
            if(rankingAnimeByScoreAllTime.isEmpty())
                return "{\"outcome_code\": 2}";
            Utility.writeToFile(rankingAnimeByScoreAllTime, Constants.fileName_RankingAnimeByScoreAllTime, Constants.folderName_QueryResults);
            System.out.println("LOG --> Success calculation 1: " + Constants.fileName_RankingAnimeByScoreAllTime);

            List<Anime> rankingAnimeByWatchersAllTime = animeRepoMongoDB.getAnimeByWatchersAllTime();
            System.out.println("LOG --> Found " + rankingAnimeByWatchersAllTime.size() + " anime");
            if(rankingAnimeByWatchersAllTime.isEmpty())
                return "{\"outcome_code\": 2}";
            Utility.writeToFile(rankingAnimeByWatchersAllTime, Constants.fileName_RankingAnimeByWatchersAllTime, Constants.folderName_QueryResults);
            System.out.println("LOG --> Success calculation 1: " + Constants.fileName_RankingAnimeByWatchersAllTime);

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
    @PostMapping("/api/admin/updateWatchersAndScores")
    @Transactional("transactionManager")
    public @ResponseBody String updateNewWatchersAndAvgRatings(HttpSession session){
        if(!Utility.isAdmin(session)){
            return "{\"outcome_code\": 1}";         // User is not an admin
        }
        // try catch method on MongoDB
        try {
            System.out.println(">> START: Updating new watchers and average ratings...");

            // aggiorna il numero di watchers degli anime
            ArrayList<AnimeWithWatchers> animeWithWatchers = animeRepoNeo4j.getAnimeWithWatchers();
            if(!animeWithWatchers.isEmpty()){
                boolean outcome1 = animeRepoMongoDB.setWatchersOfAnime(animeWithWatchers.toArray(new AnimeWithWatchers[0]));
                if(!outcome1)
                    return "{\"outcome_code\": 2}";     // Error while updating new likes (for albums)
                else
                    System.out.println(">> New watchers updated successfully");
            }

            // aggiorna gli average score degli anime che hanno ricevuto recensioni
            List<AnimeOnlyAvgScore> newAvgScore = animeRepoMongoDB.getAverageScoreFromReviews();
            System.out.println("> Found " + newAvgScore.size() + " anime with recent reviews");
            if(!newAvgScore.isEmpty()) {
                boolean outcome3 = animeRepoMongoDB.setAverageScoreFromReviews(newAvgScore);
                if (!outcome3)
                    return "{\"outcome_code\": 4}";     // Error while updating average score
                else
                    System.out.println(">> Average score updated successfully");
            }

            System.out.println(">>> END: All new watchers and average scores updated successfully");
            return "{\"outcome_code\": 0}";             // Update successful

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 10}";            // Error while connecting to the database
        }
    }


}
