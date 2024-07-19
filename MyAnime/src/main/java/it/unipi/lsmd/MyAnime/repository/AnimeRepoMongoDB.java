package it.unipi.lsmd.MyAnime.repository;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.query.AnimeOnlyAvgScore;
import it.unipi.lsmd.MyAnime.model.query.AnimeWithWatchers;
import it.unipi.lsmd.MyAnime.model.query.GenreScored;
import it.unipi.lsmd.MyAnime.model.query.ReviewLite;
import it.unipi.lsmd.MyAnime.repository.MongoDB.AnimeMongoInterface;
import it.unipi.lsmd.MyAnime.utilities.Constants;
import it.unipi.lsmd.MyAnime.utilities.Utility;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
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
            List<Anime> result = animeMongoInterface.findByTitle(title);

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
            return filterAnime(term, genre, year, type, status, rating, sortBy);
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
                case "sort-number_of_episodes" -> sort(descending("episodes"));
                default -> sort;
            };
        }
        Bson limit = limit(50);

        matches.add(sort);
        matches.add(limit);

        AggregateIterable<Document> result = collection.aggregate(matches);

        List<Anime> animes = new ArrayList<>();
        //result.forEach(System.out::println);
        result.forEach(doc ->  animes.add(Anime.mapToAnime(doc)));

        mongoClient.close();
        return animes;
    }

    public int insertReviewIntoAnime(ObjectId animeID, ReviewLite review) {
        try {
            Anime anime = getAnimeById(animeID);
            if (anime == null) {
                return 1; // Anime non trovato
            }

            ReviewLite[] oldReviews = anime.getMostRecentReviews();
            LinkedList<ReviewLite> mostRecentReviews;
            if (oldReviews != null)
                mostRecentReviews = new LinkedList<>(Arrays.asList(oldReviews));
            else
                mostRecentReviews = new LinkedList<>();

            // remove eventual old review
            String animeTitle = anime.getTitle();
            String username = review.getUsername();
            for (int i = 0; i < mostRecentReviews.size(); i++) {
                if (mostRecentReviews.get(i).getUsername().equals(username) &&
                        mostRecentReviews.get(i).getAnimeTitle().equals(animeTitle))
                {
                    mostRecentReviews.remove(i);
                    break;
                }
            }

            mostRecentReviews.addFirst(review);
            while (mostRecentReviews.size() > 5) {
                mostRecentReviews.removeLast();
            }

            anime.setMostRecentReviews(mostRecentReviews.toArray(new ReviewLite[0]));
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
        int minReviews = 10;

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

    @Transactional("transactionManager")
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

    public List<AnimeOnlyAvgScore> getAverageScoreFromReviews() {
        try {
            Aggregation averageScoreAggregation = Aggregation.newAggregation(
                    Aggregation.group("anime_id").avg("score").as("avgScore").count().as("scoredBy"),
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
            return new ArrayList<>();
        }
    }

    @Transactional("transactionManager")
    public boolean setAverageScoreFromReviews(List<AnimeOnlyAvgScore> animeAverages) {
        // Prepara le operazioni in batch
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Anime.class);

        try {
            // Aggiungi ogni operazione di aggiornamento al batch
            animeAverages.forEach(animeAverage -> {
                Query query = new Query().addCriteria(Criteria.where("_id").is(animeAverage.get_id()));
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
        if((episodesVal!=null && episodesVal<=0) || (episodeDurationVal!=null && episodeDurationVal<=0)){
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
            // Set mandatory fields for anime
            anime.setTitle(titleVal);
            anime.setSource(sourceVal);
            anime.setImgURL(imgURLVal);

            // Set optional fields for anime
            if(titleJapaneseVal!=null && !titleJapaneseVal.isEmpty()){
                anime.setTitleJapanese(titleJapaneseVal);
            }
            if (type!=null && !type.isEmpty()){
                anime.setType(typeMapper.get(type));
            }
            if (episodesVal!=null){
                anime.setEpisodes(episodesVal);
            }
            if (sliderAiringVal!=null){
                anime.setAiring(sliderAiringVal);
            }
            if (airedInput.get("from")!=null || airedInput.get("to")!=null){
                anime.setAired(airedInput);
            }
            if (rating!=null && !rating.isEmpty()){
                anime.setRating(ratingMapper.get(rating));
            }
            if (backgroundVal!=null && !backgroundVal.isEmpty()){anime.setBackground(backgroundVal);}
            if (broadcastVal!=null && !broadcastVal.isEmpty()){anime.setBroadcast(broadcastVal);}
            if (producerVal!=null && !producerVal.isEmpty()){anime.setProducer(producerVal);}
            if (licensorVal!=null && !licensorVal.isEmpty()){anime.setLicensor(licensorVal);}
            if (studioVal!=null && !studioVal.isEmpty()){anime.setStudio(studioVal);}
            if (genreList != null && !genreList.isEmpty()) {anime.setGenre(genreList.toArray(new String[0]));}
            if (episodeDurationVal!=null){anime.setEpisodeDuration(episodeDurationVal);}

            // System.out.println(anime);
            animeMongoInterface.save(anime);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public List<GenreScored> getGenreScored() {
        try {
            Aggregation genreScoreAggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("score").exists(true)),
                    Aggregation.project("genre").andInclude("score"),
                    Aggregation.unwind("genre"),
                    Aggregation.group("genre")
                            .avg("score").as("avgScore")
                            .max("score").as("maxScore")
                            .min("score").as("minScore"),
                    Aggregation.project()
                            .and("_id").as("genre")
                            .andInclude("avgScore", "maxScore", "minScore")
                            .andExclude("_id"),
                    Aggregation.sort(Sort.by(Sort.Direction.DESC, "avgScore"))
            );
            AggregationResults<GenreScored> results = mongoTemplate.aggregate(
                    genreScoreAggregation, "animes", GenreScored.class
            );


            //System.out.println("Guarda aggregation result: " + results.getMappedResults().get(0));

            List<GenreScored> genreScoreds = results.getMappedResults();

            return genreScoreds;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new ArrayList<>();
        }
    }
}