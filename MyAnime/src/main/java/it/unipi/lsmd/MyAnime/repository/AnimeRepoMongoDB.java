package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.repository.MongoDB.AnimeMongoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Repository
public class AnimeRepoMongoDB {

    @Autowired
    private AnimeMongoInterface animeMongoInterface;

    @Autowired
    private MongoTemplate mongoTemplate;

    public boolean existsById(String id){
        try {
            return animeMongoInterface.existsById(id);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public Anime getAnimeById(String id){
        try {
            Optional<Anime> result = animeMongoInterface.findById(id);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Anime getAnimeByTitle(String title){
        try {
            List<Anime> result = animeMongoInterface.findByTitle(title);
            if (result.isEmpty())
                return null;
            else if (result.size() > 1){
                System.err.println("Multiple anime found with the same title");
                return result.get(0);
            }
            else
                return result.get(0);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<Anime> find5Anime(String term){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return animeMongoInterface.findAnimeByTitleContaining(term, topFive);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public int insertReviewIntoAnime(String animeID, String username, int score, String text, Instant timestamp) {
        try {
            Anime anime = getAnimeById(animeID);
            if (anime == null) {
                return 1; // Anime non trovato
            }

            LinkedList<Review> mostRecentReviews = new LinkedList<>(Arrays.asList(anime.getMostRecentReviews()));
            mostRecentReviews.addFirst(new Review(username, animeID,score, text, timestamp));
            while (mostRecentReviews.size() > 5) {
                mostRecentReviews.removeLast();
            }

            anime.setMostRecentReviews(mostRecentReviews.toArray(new Review[0]));
            animeMongoInterface.save(anime);
            return 0; // Successo

        } catch (DuplicateKeyException e) {
            e.printStackTrace();
            return 2; // Violazione del vincolo di unicità
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return 3; // Violazione dell'integrità dei dati
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                throw dae;
            }
            dae.printStackTrace();
            return 4; // Altre eccezioni relative all'accesso ai dati
        }
    }

    @Transactional
    public boolean setAverageScoreForRecentReviews(List<Anime> animeList) {
        // Prepara le operazioni in batch
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Anime.class);

        try {
            // Aggiungi ogni operazione di aggiornamento al batch
            animeList.forEach(anime -> {
                Query query = new Query().addCriteria(Criteria.where("_id").is(anime.getId()));
                Update update = new Update().set("averageScore", anime.getAverageScore());
                bulkOps.updateOne(query, update);
            });

            // Esegui tutte le operazioni in batch
            bulkOps.execute();
            return true;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }
}