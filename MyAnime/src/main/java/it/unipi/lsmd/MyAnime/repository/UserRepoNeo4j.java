package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.model.AnimeNode;
import it.unipi.lsmd.MyAnime.model.UserNode;
import it.unipi.lsmd.MyAnime.model.query.AnimeWatched;
import it.unipi.lsmd.MyAnime.repository.Neo4j.UserNeo4jInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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

    public String addFriendship(String username1, String username2){
        try {
            return userNeo4jInterface.addFollowing(username1, username2);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public int removeFollowing(String username1, String username2){
        try {
            userNeo4jInterface.removeFollowing(username1, username2);
            return 0;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                dae.printStackTrace();
                return 1;
            } else {
                dae.printStackTrace();
                return 2;
            }
        }
    }

    public String addWatches(String username, String animeTitle, int status, int episodes){
        try {
            return userNeo4jInterface.addWatches(username, animeTitle, status, episodes);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public int removeWatches(String username, String animeTitle){
        try {
            userNeo4jInterface.removeWatches(username, animeTitle);
            return 0;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                dae.printStackTrace();
                return 1;
            } else {
                dae.printStackTrace();
                return 2;
            }
        }
    }

    public int countFollowers(String username){
        try {
            return userNeo4jInterface.countFollowers(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                dae.printStackTrace();
                return 1;
            } else {
                dae.printStackTrace();
                return 2;
            }
        }
    }

    public int countFollowing(String username){
        try {
            return userNeo4jInterface.countFollowing(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                dae.printStackTrace();
                return 1;
            } else {
                dae.printStackTrace();
                return 2;
            }
        }
    }

    public List<UserNode> findFollowingOfUsername(String username){
        try {
            return userNeo4jInterface.findFollowingOfUsername(username);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public List<UserNode> findFollowersOfUsername(String username){
        try {
            return userNeo4jInterface.findFollowersOfUsername(username);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public ArrayList<AnimeWatched> findWatchedAnime(String username){
        String cypherQuery = "MATCH (u:User {username: $username})-[w:WATCHES]->(a:Anime)" +
                "RETURN a.title AS title, a.imgURL AS imgURL, w.status AS status, w.episodes AS episodes";

        return AnimeWatched.getAnimeWatched(neo4jClient, cypherQuery, username);
    }

}
