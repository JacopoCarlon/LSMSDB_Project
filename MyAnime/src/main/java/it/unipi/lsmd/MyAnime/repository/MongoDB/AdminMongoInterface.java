package it.unipi.lsmd.MyAnime.repository.MongoDB;

import it.unipi.lsmd.MyAnime.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminMongoInterface extends MongoRepository<Admin, String> {
    Optional<Admin> findAdminByUsername(String username);
}
