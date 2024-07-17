package it.unipi.lsmd.MyAnime.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.model.query.AnimeOnlyAvgScore;
import it.unipi.lsmd.MyAnime.model.query.AnimeWithWatchers;
import it.unipi.lsmd.MyAnime.repository.MongoDB.AnimeMongoInterface;
import it.unipi.lsmd.MyAnime.utilities.Constants;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.apache.commons.validator.routines.UrlValidator;
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
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Sorts.ascending;
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

    public List<Anime> findAnime(String term, List<String> genre, List<String> year, List<String> type, List<String> status, List<String> rating, String sortBy) {
        try {
            Pageable topTen = PageRequest.of(0, 10);
            return filterAnime(term, genre, year, type, status, rating, sortBy);
            // return animeMongoInterface.findAnimeByTitleContaining(term, topTen);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public List<Anime> filterAnime(String term, List<String> genre, List<String> years, List<String> type, List<String> status, List<String> rating, String sortBy){
        MongoClient mongoClient = MongoClients.create(mongoConnection);
        MongoDatabase database = mongoClient.getDatabase("MyAnimeLibrary");
        MongoCollection<Document> collection = database.getCollection("animes");

        List<Bson> matches = new ArrayList<>();
        if (term!=null && !term.isEmpty()){
            matches.add(match(regex("title", term, "i")));
        }
        if (genre!= null && !genre.isEmpty()){
            HashMap<String, String> genreMapper = Utility.genreMapper();
            genre.replaceAll(genreMapper::get);
            matches.add(match(in("genre", genre)));
        }
        if (years!=null && !years.isEmpty()){
            List<Bson> conditions = new ArrayList<>();
            for (String year: years) {
                String yearStr = Utility.yearMapping(year);
                Instant startYear;
                Instant startOfNextYear;
                if (yearStr.endsWith("s")){
                    String substring = yearStr.substring(0, yearStr.length() - 1);
                    startYear = ZonedDateTime.of(Integer.parseInt(substring), 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
                    startOfNextYear = ZonedDateTime.of(Integer.parseInt(substring)+10, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();

                } else {
                    startYear = ZonedDateTime.of(Integer.parseInt(yearStr), 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
                    startOfNextYear = ZonedDateTime.of(Integer.parseInt(yearStr)+1, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
                }
                conditions.add(and(gte("aired.from",startYear), lt("aired.from", startOfNextYear)));
            }
            matches.add(match(or(conditions)));
        }
        if (type!=null && !type.isEmpty()){
            HashMap<String,String> typeMapper = Utility.typeMapper();
            type.replaceAll(typeMapper::get);
            matches.add(match(in("type", type)));
        }
        if (status!=null && status.size() == 1){
            //  boolean is_airing = status.getFirst().equals("status-on_hold");
            boolean is_airing = status.get(0).equals("status-on_hold");
            matches.add(match(in("airing", is_airing)));
        }
        if (rating!=null && !rating.isEmpty()){
            HashMap<String, String> ratingMapper = Utility.ratingMapper();
            rating.replaceAll(ratingMapper::get);
            matches.add(match(in("rating", rating)));
        }
        Bson sort = sort(descending("score"));
        if (sortBy!=null && !sortBy.isEmpty()){
            sort = switch (sortBy) {
                case "sort-release_date" -> sort(descending("airing.from"));
                case "sort-title_az" -> sort(ascending("title"));
                case "sort-scores" -> sort(descending("score"));
                case "sort-most_watched" -> sort(descending("members"));
                case "sort-number_of_episodes" -> sort(ascending("episodes"));
                default -> sort;
            };
        }
        Bson project = project(fields(
                include("title",
                        "score",
                        "picture")
        ));
        Bson limit = limit(50);

        //matches.add(project);
        matches.add(sort);
        matches.add(limit);

        AggregateIterable<Document> result = collection.aggregate(matches);

        List<Anime> animes = new ArrayList<>();
        //result.forEach(System.out::println);
        result.forEach(doc ->  animes.add(Anime.mapToAnime(doc)));

        mongoClient.close();
        return animes;
    }

    public int insertReviewIntoAnime(ObjectId animeID, String username, int score, String text, Instant timestamp) {
        try {
            Anime anime = getAnimeById(animeID);
            if (anime == null) {
                return 1; // Anime non trovato
            }

            Review[] oldReviews = anime.getMostRecentReviews();
            LinkedList<Review> mostRecentReviews;
            if (oldReviews != null)
                mostRecentReviews = new LinkedList<>(Arrays.asList(oldReviews));
            else
                mostRecentReviews = new LinkedList<>();
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

    public boolean insertAnime(String titleVal, String titleJapaneseVal, String sourceVal, Integer episodesVal, Boolean sliderAiringVal, String airedInputFromVal, String airedInputToVal, String backgroundVal,String broadcastVal, String producerVal, String licensorVal, String studioVal, Integer episodeDurationVal, String imgURLVal, String type, String rating, List<String> genreList) {
        if(existsByTitle(titleVal)){
            System.out.println("anime already exists");
            return false;
        }
        if((episodesVal!=null && episodesVal<0) || (episodeDurationVal!=null && episodeDurationVal<0)){
            System.out.println("episodes or episodeDuration not valid");
            return false;
        }
        try {
            Map airedInput = new HashMap<String, Instant>();
            if (airedInputFromVal != null && !airedInputFromVal.isEmpty()){
                airedInput.put("from", ZonedDateTime.of(Integer.parseInt(airedInputFromVal.substring(0,4)), Integer.parseInt(airedInputFromVal.substring(5,7)), Integer.parseInt(airedInputFromVal.substring(8)), 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
            } else {
                airedInput.put("from", null);
            }
            if (airedInputToVal != null && !airedInputToVal.isEmpty()){
                airedInput.put("to", ZonedDateTime.of(Integer.parseInt(airedInputToVal.substring(0,4)), Integer.parseInt(airedInputToVal.substring(5,7)), Integer.parseInt(airedInputToVal.substring(8)), 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
            } else {
                airedInput.put("to", null);
            }
            HashMap<String,String> typeMapper = Utility.typeMapper();
            HashMap<String, String> genreMapper = Utility.genreMapper();
            if(genreList!=null && !genreList.isEmpty()){
                genreList.replaceAll(genreMapper::get);
            }
            HashMap<String, String> ratingMapper = Utility.ratingMapper();
            Anime anime = new Anime();
            anime.setTitle(titleVal);
            anime.setTitleJapanese(titleJapaneseVal);
            anime.setType((type == null ? null : typeMapper.get(type)));
            anime.setSource(sourceVal);
            if (episodesVal!=null){anime.setEpisodes(episodesVal);}
            anime.setAiring(sliderAiringVal);
            anime.setAired(airedInput);
            anime.setRating((rating == null ? null : ratingMapper.get(rating)));
            anime.setScoredBy(0);
            anime.setWatchers(0);
            if (backgroundVal!=null){anime.setBackground(backgroundVal);}
            if (broadcastVal!=null){anime.setBroadcast(broadcastVal);}
            if (producerVal!=null){anime.setProducer(producerVal);}
            if (licensorVal!=null){anime.setLicensor(licensorVal);}
            if (studioVal!=null){anime.setStudio(studioVal);}
            if (genreList != null) {anime.setGenre(genreList.toArray(new String[0]));}
            if (episodeDurationVal!=null){anime.setEpisodeDuration(episodeDurationVal);}
            anime.setImgURL(imgURLVal);

            System.out.println(anime);
            animeMongoInterface.save(anime);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }
}