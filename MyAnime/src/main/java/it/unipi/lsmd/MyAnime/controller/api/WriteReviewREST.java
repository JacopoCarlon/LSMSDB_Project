package it.unipi.lsmd.MyAnime.controller.api;


import it.unipi.lsmd.MyAnime.model.query.ReviewLite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
public class WriteReviewREST {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    ReviewRepoMongoDB reviewRepoMongoDB;

    @PostMapping("/api/writeReview")
    @Transactional("transactionManager")
    public @ResponseBody String writeReview(HttpSession session,
                                      @RequestParam("score") int score,
                                      @RequestParam("text") String text,
                                      @RequestParam("animeTitle") String animeTitle,
                                      @RequestParam("username") String username) {

        // DEBUG
        System.out.println("text: " + text);
        System.out.println("animeTitle: " + animeTitle);
        System.out.println("username: " + username);
        System.out.println("score: " + score);

        try {
            if(!Utility.isLogged(session))
                return "{\"outcome_code\": 1}";     // User not logged in
            if(!Utility.getUsername(session).equals(username))
                return "{\"outcome_code\": 2}";     // Usernames don't match


            Anime anime;
            if(!animeRepoMongoDB.existsByTitle(animeTitle))
                return "{\"outcome_code\": 3}";     // Anime doesn't exist
            else
                anime = animeRepoMongoDB.getAnimeByTitle(animeTitle);

            if(anime == null)
                return "{\"outcome_code\": 3}";     // Anime doesn't exist


            if(score < 1 || score > 10)
                return "{\"outcome_code\": 4}";     // Score out of range

            // remove eventual old review
            if (!reviewRepoMongoDB.removeByAnimeIDAndUsername(anime.getId(), username)) {
                return "{\"outcome_code\": 6}";
            }

            Instant timestamp = Instant.now();
            boolean outcomeInsertIntoReview = reviewRepoMongoDB.insertReview(score, text, anime.getId(), username, timestamp, anime.getTitle());
            if(!outcomeInsertIntoReview)
                return "{\"outcome_code\": 6}";     // Error while writing the review into the collection reviews

            // insert della light review nella collection users (aggiornamento reviewedAnimes)
            ReviewLite reviewLite = new ReviewLite(username, anime.getTitle(), score, timestamp);

            // public boolean insertReviewIntoUser(String username, Review review)
            int outcomeInsertIntoUser = userRepoMongoDB.insertReviewIntoUser(username, reviewLite);
            if(outcomeInsertIntoUser == 1) {
                return "{\"outcome_code\": 7}";     // User not found
            } else if (outcomeInsertIntoUser == 2) {
                return "{\"outcome_code\": 9}";     // Violation of uniqueness constraint
            } else if (outcomeInsertIntoUser == 3) {
                return "{\"outcome_code\": 10}";     // Violation of data integrity
            } else if (outcomeInsertIntoUser == 4) {
                return "{\"outcome_code\": 11}";     // Other exceptions related to data access
            }

            // insert della review nella collection animes (aggiornamento lastReviews)
            int outcomeInsertIntoAnime = animeRepoMongoDB.insertReviewIntoAnime(anime.getId(), reviewLite);
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
