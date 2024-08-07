package it.unipi.lsmd.MyAnime.repository.MongoDB;

import it.unipi.lsmd.MyAnime.model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewMongoInterface extends MongoRepository<Review, String> {
    void removeByAnimeIDAndUsername(ObjectId animeObjectID, String username);
    //Optional<Review> findByAnimeIDAndUsername(ObjectId animeObjectID, String username);
    Page<Review> findLimitedReviewsByAnimeIDOrderByTimestampDesc(ObjectId animeObjectID, Pageable pageable);
    //Page<Review> findLimitedReviewsByAnimeTitle(String animeTitle, Pageable pageable);
    List<Review> findLimitedReviewsByUsernameOrderByTimestampDesc(String username, Pageable pageable);
}