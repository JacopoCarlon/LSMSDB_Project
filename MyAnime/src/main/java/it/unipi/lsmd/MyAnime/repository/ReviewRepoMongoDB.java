package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.repository.MongoDB.ReviewMongoInterface;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepoMongoDB {
    @Autowired
    private ReviewMongoInterface reviewMongoInterface;
    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean existsByAnimeIDAndUsername(String animeID, String username) {
        try {
            return reviewMongoInterface.existsByAnimeIDAndUsername(new ObjectId(animeID) , username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean insertReview(int score, String text, String animeID, String username, Instant timestamp) {
        try {
            Review review = new Review(username, animeID, score, text, timestamp);
            reviewMongoInterface.save(review);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByAnimeID(String animeID) {
        try {
            ObjectId animeObjectId = new ObjectId(animeID);
            PageRequest pageable = PageRequest.of(0, 500);
            return reviewMongoInterface.findLimitedReviewsByAnimeID(animeObjectId, pageable).getContent();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Review getReviewByAnimeIDAndUsername(String animeID, String username) {
        try {
            ObjectId animeObjectId = new ObjectId(animeID);
            Optional<Review> result = reviewMongoInterface.findByAnimeIDAndUsername(animeObjectId, username);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }
}
