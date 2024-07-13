package it.unipi.lsmd.MyAnime.controller.api;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.transaction.annotation.Transactional;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.ReviewRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import jakarta.servlet.http.HttpSession;
import java.time.Instant;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.Review;

@Controller
public class WriteReviewREST {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    ReviewRepoMongoDB reviewRepoMongoDB;

    @PostMapping("/api/writeReview")
    @Transactional
    public @ResponseBody String writeReview(HttpSession session,
                                      @RequestParam("rating") int rating,
                                      @RequestParam("text") String text,
                                      @RequestParam("animeID") ObjectId animeID,
                                      @RequestParam("username") String username) {

        try {
            if(!Utility.isLogged(session))
                return "{\"outcome_code\": 1}";     // User not logged in
            if(!Utility.getUsername(session).equals(username))
                return "{\"outcome_code\": 2}";     // Usernames don't match
            if(!animeRepoMongoDB.existsById(animeID))
                return "{\"outcome_code\": 3}";     // Anime doesn't exist
            if(rating < 1 || rating > 5)
                return "{\"outcome_code\": 4}";     // Rating out of range
            if(reviewRepoMongoDB.existsByAnimeIDAndUsername(animeID, username))
                return "{\"outcome_code\": 5}";     // User has already written a review for this anime


            Anime anime = animeRepoMongoDB.getAnimeById(animeID);
            if(anime == null)
                return "{\"outcome_code\": 3}";     // Anime doesn't exist

        // insert della review nella collection reviews
        // public boolean insertReview(int score, String text, String animeId, String username, Instant timestamp)
        //  TODO : verify consinstency inserts !!!

            boolean outcomeInsertIntoReview = reviewRepoMongoDB.insertReview(rating, text, anime.getId(), username, Instant.now(), anime.getTitle());
            if(!outcomeInsertIntoReview)
                return "{\"outcome_code\": 6}";     // Error while writing the review into the collection reviews

        // insert della review nella collection users (aggiornamento reviewedAnimes)
        //  public Review(String username, String animeId, int score, String text, Instant timestamp)
            Review reviewedAnime = new Review(username, anime.getId(), rating, text, Instant.now(), anime.getTitle());

            //  public boolean insertReviewIntoUser(String username, Review review)
            boolean outcomeInsertIntoUser = userRepoMongoDB.insertReviewIntoUser(username, reviewedAnime);
            if(!outcomeInsertIntoUser)
                return "{\"outcome_code\": 7}";     // Error while writing the review into the collection users

        // insert della review nella collection animes (aggiornamento lastReviews)
        //  public int insertReviewIntoAnime(String animeID, String username, int score, String text, Instant timestamp)
            int outcomeInsertIntoAnime = animeRepoMongoDB.insertReviewIntoAnime(anime.getId(), username, rating, text, Instant.now());
            if (outcomeInsertIntoAnime == 1) {
                return "{\"outcome_code\": 8}";     // Anime not found
            } else if (outcomeInsertIntoAnime == 2) {
                return "{\"outcome_code\": 9}";     // Violation of uniqueness constraint
            } else if (outcomeInsertIntoAnime == 3) {
                return "{\"outcome_code\": 10}";     // Violation of data integrity
            } else if (outcomeInsertIntoAnime == 4) {
                return "{\"outcome_code\": 11}";     // Other exceptions related to data access
            }

        // Se tutto è andato a buon fine, ritorna un json con outcome_code = 0
            return "{\"outcome_code\": 0}";         // Review successfully written

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            return "{\"outcome_code\": 12}";         // Error while connecting to the database
        }
    }
}
