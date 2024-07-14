package it.unipi.lsmd.MyAnime.repository;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

import it.unipi.lsmd.MyAnime.model.Review;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.MongoDB.UserMongoInterface;
import it.unipi.lsmd.MyAnime.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepoMongoDB {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserMongoInterface userMongoInterface;

    public List<User> find5User(String term){
        try {
            Pageable topFive = PageRequest.of(0, 5);
            return userMongoInterface.findUsersByUsernameContaining(term, topFive);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    public User getUserByUsername(String username) {
        try {
            Optional<User> result = userMongoInterface.findByUsername(username);
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
            Optional<User> result = userMongoInterface.findByUsername(username);
            return (result.isPresent());
            //  return result.orElse(null);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
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
            User newUser = new User(null, username, name, surname, email, salt, hashedPassword, gender, birthDate, joinDate, statsEpisodes, new Review[0]);

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

    public boolean insertReviewIntoUser(String username, Review review) {
        try {
            addReviewIntoUser(username, review);
            return true;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return false;
        }
    }

    public void addReviewIntoUser(String username, Review review) {
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().push("mostRecentReviews").atPosition(0).each(review);
        mongoTemplate.updateFirst(query, update, User.class);
    }
}
