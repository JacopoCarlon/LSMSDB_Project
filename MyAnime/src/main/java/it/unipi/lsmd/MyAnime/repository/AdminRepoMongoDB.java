package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.model.Admin;
import it.unipi.lsmd.MyAnime.repository.MongoDB.AdminMongoInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdminRepoMongoDB {
    @Autowired
    private AdminMongoInterface adminMongoInterface;

    public Admin getAdminByUsername(String username) {
        try {
            Optional<Admin> result = adminMongoInterface.findAdminByUsername(username);
            return result.orElse(null);
        }
        catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }
}
