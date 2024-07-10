package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.repository.Neo4j.UserNeo4jInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoNeo4j {

    @Autowired
    private UserNeo4jInterface userNeo4jInterface;
    @Autowired
    private Neo4jClient neo4jClient;

    public int insertUser(String username){
        try {
            userNeo4jInterface.createUser(username);
            return 0; // Inserimento riuscito
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                // Gestione specifica per errori di connessione al database
                dae.printStackTrace();
                return 1;
            } else {
                // Gestione generica per altri errori di database
                dae.printStackTrace();
                return 2;
            }
        }
    }



    

}
