package it.unipi.lsmd.MyAnime.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.repository.MongoDB.AnimeMongoInterface;
import it.unipi.lsmd.MyAnime.utilities.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
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

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Sorts.descending;


@Repository
public class AnimeRepoMongoDB {

    @Autowired
    private AnimeMongoInterface animeMongoInterface;

    @Autowired
    private MongoTemplate mongoTemplate;

    private ConnectionString mongoConnection = new ConnectionString(Constants.mongoDBConnectionUri);

    public boolean existsById(ObjectId id){
        try {
            return animeMongoInterface.existsById(id);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public Anime getAnimeById(ObjectId id){
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
            //  System.out.println("animeByTitle: "+title);
            //  String aaa = title;
            //  System.out.println(title.getClass());
            //  System.out.println(aaa.getClass());
            //  System.out.println("aaa".getClass());
            List<Anime> result = animeMongoInterface.findByTitle(title);
            //  System.out.println(result);
            if (result.isEmpty())
                return null;
            else if (result.size() < 1){
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

    public List<Anime> findAnime(String term){
        try {
            Pageable topTen = PageRequest.of(0, 10);
            return animeMongoInterface.findAnimeByTitleContaining(term, topTen);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public int insertReviewIntoAnime(ObjectId animeID, String username, int score, String text, Instant timestamp) {
        try {
            Anime anime = getAnimeById(animeID);
            if (anime == null) {
                return 1; // Anime non trovato
            }

            LinkedList<Review> mostRecentReviews = new LinkedList<>(Arrays.asList(anime.getMostRecentReviews()));
            mostRecentReviews.addFirst(new Review(username, animeID, score, text, timestamp, anime.getTitle()));
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

    public List<Anime> getAnimeByScoreAllTime() {
        MongoClient mongoClient = MongoClients.create(mongoConnection);
        MongoDatabase database = mongoClient.getDatabase("MyAnimeLibrary");
        MongoCollection<Document> collection = database.getCollection("animes");
        int minReviews = 100;

        Bson match = match(gte("scored_by", minReviews));
        Bson project = project(fields(
                include("title",
                        "score",
                        "picture")
        ));
        Bson sort = sort(descending("score"));
        Bson limit = limit(50);

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                match, project, sort, limit
        ));

        List<Anime> animes = new ArrayList<>();
        result.forEach(doc ->  animes.add(Anime.mapToAnime(doc)));

        mongoClient.close();
        return animes;
    }

    public List<Anime> getAnimeByWatchersAllTime() {
        MongoClient mongoClient = MongoClients.create(mongoConnection);
        MongoDatabase database = mongoClient.getDatabase("MyAnimeLibrary");
        MongoCollection<Document> collection = database.getCollection("animes");

        Bson project = project(fields(
                include("title",
                        "score",
                        "picture")
        ));
        Bson sort = sort(descending("watchers"));
        Bson limit = limit(50);

        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(
                project, sort, limit
        ));

        List<Anime> animes = new ArrayList<>();
        result.forEach(doc ->  animes.add(Anime.mapToAnime(doc)));

        mongoClient.close();
        return animes;
    }
}