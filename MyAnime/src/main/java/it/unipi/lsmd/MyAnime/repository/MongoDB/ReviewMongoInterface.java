package it.unipi.lsmd.MyAnime.repository.MongoDB;

import it.unipi.lsmd.MyAnime.model.Review;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ReviewMongoInterface extends MongoRepository<Review, String> {
    // NB: in Review abbiamo il tipo di dato ObjectId per animeID
    // convertire l'animeID in ObjectId prima di passarlo come parametro
    boolean existsByAnimeIDAndUsername(ObjectId animeObjectID, String username);
    Optional<Review> findByAnimeIDAndUsername(ObjectId animeObjectID, String username);
    Page<Review> findLimitedReviewsByAnimeID(ObjectId animeObjectID, Pageable pageable);
    List<Review> findReviewsByUsername(String username, Pageable pageable);
}