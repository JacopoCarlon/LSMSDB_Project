package it.unipi.lsmd.MyAnime.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.model.query.AnimeOnlyAvgScore;
import it.unipi.lsmd.MyAnime.model.query.AnimeWithWatchers;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
import java.util.stream.Collectors;

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

    public boolean existsByTitle(String title){
        try {
            return animeMongoInterface.existsByTitle(title);
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

    @Transactional
    public boolean setWatchersOfAnime(AnimeWithWatchers[] animeList) {
        try {
            BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, Anime.class);

            for (AnimeWithWatchers anime : animeList) {
                Query query = new Query(Criteria.where("title").is(anime.getTitle()));
                Update update = new Update().set("watchers", anime.getWatchers());
                bulkOps.updateOne(query, update);
            }

            bulkOps.execute();
            return true;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public List<AnimeOnlyAvgScore> getAverageScoreForRecentReviews() {
        // Una limitazione importante è che MongoDB non supporta l'aggiornamento di documenti direttamente
        // all'interno di una pipeline di aggregazione. Pertanto, quello che posso fare è calcolare le medie e
        // trovare gli ID necessari in un'unica query, ma poi dovrò eseguire un'operazione di aggiornamento separata.
        // Nello specifico, aggiornerò i documenti degli anime con le medie dei voti calcolate in questa query con
        // il metodo setAverageScoreForRecentReviews().

        try {
            Instant twentyFourHoursAgo = Instant.now().minusSeconds(60 * 60 * 24);

            // Fase 1: Trovo gli ID degli anime con recensioni recenti
            Aggregation recentReviewsAggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("review_time").gte(twentyFourHoursAgo)),
                    Aggregation.group("anime_id"),
                    Aggregation.project("anime_id") // Project the anime_id
            );
            // Execute the aggregation
            AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(
                    recentReviewsAggregation, "reviews", Document.class);

            // Map the results to a list of strings
            List<String> recentAnimeIds = aggregationResults.getMappedResults().stream()
                    .map(document -> document.getString("anime_id"))
                    .collect(Collectors.toList());

            // Fase 2: Calcolo la media degli score per questi anime
            Aggregation averageScoreAggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("anime_id").in(recentAnimeIds)),
                    Aggregation.group("anime_id").avg("score").as("avgScore").sum("username").as("scoredBy"),
                    Aggregation.project("anime_id").andInclude("avgScore", "scoredBy")
            );
            AggregationResults<AnimeOnlyAvgScore> results = mongoTemplate.aggregate(
                    averageScoreAggregation, "reviews", AnimeOnlyAvgScore.class
            );

            List<AnimeOnlyAvgScore> animeAverages = results.getMappedResults();

            // Arrotondo le medie dei voti
            for (AnimeOnlyAvgScore animeAverage : animeAverages) {
                animeAverage.roundAverageScore();
            }

            return animeAverages;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new ArrayList<AnimeOnlyAvgScore>();
        }
    }

    @Transactional
    public boolean setAverageScoreForRecentReviews(List<AnimeOnlyAvgScore> animeAverages) {
        // Prepara le operazioni in batch
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Anime.class);

        try {
            // Aggiungi ogni operazione di aggiornamento al batch
            animeAverages.forEach(animeAverage -> {
                Query query = new Query().addCriteria(Criteria.where("_id").is(animeAverage.getAnimeId()));
                Update update = new Update().set("score", animeAverage.getAvgScore()).set("scored_by", animeAverage.getScoredBy());
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