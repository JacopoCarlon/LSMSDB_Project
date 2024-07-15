package it.unipi.lsmd.MyAnime.repository.MongoDB;

import it.unipi.lsmd.MyAnime.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMongoInterface extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);

    @Query(value = "{ 'username': { $regex: ?0 } }" , fields = "{'username': 1}")
    List<User> findUsersByUsernameContaining(String term, Pageable pageable);
}