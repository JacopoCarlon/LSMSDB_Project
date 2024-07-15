package it.unipi.lsmd.MyAnime.repository.MongoDB;

import it.unipi.lsmd.MyAnime.model.Anime;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnimeMongoInterface extends MongoRepository<Anime, String> {
    boolean existsById(ObjectId id);
    boolean existsByTitle(String title);
    Optional<Anime> findById(ObjectId id);

    @Query(value = "{'title' : ?0 }")
    List<Anime> findByTitle(String title);

    @Query(value = "{ 'title': { $regex: ?0, $options: 'i' } }", fields = "{ 'title': 1, 'score': 1, 'picture': 1}")
    List<Anime> findAnimeByTitleContaining(String term, Pageable pageable);

    @Query(value = "{}", sort = "{ 'score': -1 }")
    List<Anime> findAnimeSortedByRating(Pageable pageable);

    @Query(value = "{}", sort = "{ 'watchers': -1 }")
    List<Anime> findAnimeSortedByWatchers(Pageable pageable);
}