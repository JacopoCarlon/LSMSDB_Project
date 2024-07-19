package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.model.query.ReviewsPerDate;
import it.unipi.lsmd.MyAnime.repository.MongoDB.ReviewMongoInterface;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepoMongoDB {
    @Autowired
    private ReviewMongoInterface reviewMongoInterface;
    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean removeByAnimeIDAndUsername(ObjectId animeID, String username) {
        try {
            reviewMongoInterface.removeByAnimeIDAndUsername(animeID , username);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public boolean insertReview(int score, String text, ObjectId animeID, String username, Instant timestamp, String animeTitle) {
        try {
            Review review = new Review(username, animeID, score, text, timestamp, animeTitle);
            reviewMongoInterface.save(review);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public List<Review> getReviewsByAnimeID(ObjectId animeObjectId) {
        try {
            PageRequest pageable = PageRequest.of(0, 500);
            return reviewMongoInterface.findLimitedReviewsByAnimeIDOrderByTimestampDesc(animeObjectId, pageable).getContent();
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    /*
    public List<Review> getReviewsByAnimeTitle(String animeTitle) {
        try {
            PageRequest pageable = PageRequest.of(0, 500);
            return reviewMongoInterface.findLimitedReviewsByAnimeTitle(animeTitle, pageable).getContent();
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
    */

    public List<Review> getReviewsByUsername( String username) {
        try {
            PageRequest pageable = PageRequest.of(0, 500);
            return reviewMongoInterface.findLimitedReviewsByUsernameOrderByTimestampDesc(username, pageable) ;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<ReviewsPerDate> getReviewsPerDates() {
        try {
            Aggregation usersPerDateAggregation = Aggregation.newAggregation(
                    Aggregation.project()
                            .and("review_time").extractYear().as("year")
                            .and("review_time").extractMonth().as("month"),
                    Aggregation.group("year", "month").count().as("reviews"),
                    Aggregation.project()
                            .and("_id.year").as("year")
                            .and("_id.month").as("month")
                            .and("reviews").as("reviews")
                            .andExclude("_id"),
                    Aggregation.sort(Sort.Direction.ASC, "year", "month")
            );
            AggregationResults<ReviewsPerDate> results = mongoTemplate.aggregate(
                    usersPerDateAggregation, "reviews", ReviewsPerDate.class
            );

            System.out.println("Guarda aggregation result: " + results.getMappedResults().get(0));

            List<ReviewsPerDate> reviewsPerDates = results.getMappedResults();

            return reviewsPerDates;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new ArrayList<>();
        }
    }
}
