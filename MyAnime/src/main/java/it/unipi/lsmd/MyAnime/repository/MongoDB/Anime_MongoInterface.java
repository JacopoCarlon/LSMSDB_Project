package it.unipi.lsmd.MyAnime.repository.MongoDB;

import it.unipi.lsmd.MyAnime.model.Anime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Anime_MongoInterface extends MongoRepository<Anime, String> {
    boolean existsById(String id);
    boolean existsByTitle(String title);
    Optional<Anime> findById(String id);

    @Query(value = "{ 'title': ?0 }")
    List<Anime> findByTitle(String title);

    @Query(value = "{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Anime> findAnimeByTitleContaining(String term, Pageable pageable);

    @Query(value = "{}", sort = "{ 'averageScore': -1 }")
    List<Anime> findAnimeSortedByRating(Pageable pageable);

    @Query(value = "{}", sort = "{ 'watchers': -1 }")
    List<Anime> findAnimeSortedByWatchers(Pageable pageable);
}