package it.unipi.lsmd.MyAnime.repository;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.model.query.ReviewLite;
import it.unipi.lsmd.MyAnime.model.query.UsersPerDate;
import it.unipi.lsmd.MyAnime.repository.MongoDB.UserMongoInterface;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Repository
public class UserRepoMongoDB {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserMongoInterface userMongoInterface;

    public List<User> findUser(String term){
        try {
            Pageable topTen = PageRequest.of(0, 10);
            return userMongoInterface.findUsersByUsernameContaining(term, topTen);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            System.out.println("user: " + username);
            Optional<User> result = userMongoInterface.findByUsername(username);
            System.out.println(result);
            return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public Boolean existsByUsername(String username) {
        try {
            System.out.println("user: " + username);
            Optional<User> result = userMongoInterface.findByUsername(username);
            System.out.println(result);
            return (result.isPresent());
            //  return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public int insertUser(String name, String surname, String username, String password, Instant birthDate, String email, String gender, Instant joinDate, int statsEpisodes) {
        try {
            if (userMongoInterface.existsByUsername(username)) {
                return 1; // Username già esistente
            }

            // Generazione salt
            String salt = Utility.generateSalt();

            // Hashing della password
            String hashedPassword = Hashing.sha256()
                    .hashString(salt + password, StandardCharsets.UTF_8)
                    .toString();

            // Creazione di un nuovo utente
            User newUser = new User(null, username, name, surname, email, hashedPassword, salt, gender, birthDate, joinDate, statsEpisodes, new ReviewLite[0]);

            // Salvataggio del nuovo utente
            userMongoInterface.save(newUser);
            return 0; // Inserimento riuscito

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                // Gestione specifica per errori di connessione al database
                dae.printStackTrace();
                return 3;
            } else {
                // Gestione generica per altri errori di database
                dae.printStackTrace();
                return 4;
            }
        }
    }

    public boolean updateStatsEpisodes(String username, Integer newStat) {
        try {
            Query query = new Query(Criteria.where("username").is(username));
            Update update = new Update().set("stats_episodes", newStat);
            mongoTemplate.updateFirst(query, update, User.class);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public int insertReviewIntoUser(String username, ReviewLite review) {
        try {
            User user = getUserByUsername(username);
            if (user == null) {
                return 1; // User non trovato
            }

            ReviewLite[] oldReviews = user.getMostRecentReviews();
            LinkedList<ReviewLite> mostRecentReviews;
            if (oldReviews != null)
                mostRecentReviews = new LinkedList<>(Arrays.asList(oldReviews));
            else
                mostRecentReviews = new LinkedList<>();

            // remove eventual old review
            String animeTitle = review.getAnimeTitle();
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

            user.setMostRecentReviews(mostRecentReviews.toArray(new ReviewLite[0]));
            userMongoInterface.save(user);
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

    public List<UsersPerDate> getUsersPerDates() {
        try {
            Aggregation usersPerDateAggregation = Aggregation.newAggregation(
                    Aggregation.project()
                            .and("join_date").extractYear().as("year")
                            .and("join_date").extractMonth().as("month"),
                    Aggregation.group("year", "month").count().as("users"),
                    Aggregation.project()
                            .and("_id.year").as("year")
                            .and("_id.month").as("month")
                            .and("users").as("users")
                            .andExclude("_id"),
                    Aggregation.sort(Sort.Direction.ASC, "year", "month")
            );
            AggregationResults<UsersPerDate> results = mongoTemplate.aggregate(
                    usersPerDateAggregation, "users", UsersPerDate.class
            );

            System.out.println("Guarda aggregation result: " + results.getMappedResults().get(0));

            List<UsersPerDate> usersPerDates = results.getMappedResults();

            return usersPerDates;

        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return new ArrayList<>();
        }
    }
}
