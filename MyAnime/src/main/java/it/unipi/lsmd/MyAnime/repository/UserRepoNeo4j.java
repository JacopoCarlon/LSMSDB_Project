package it.unipi.lsmd.MyAnime.repository;

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

    public String addFollow(String follower, String target){
        try {
            return userNeo4jInterface.addFollow(follower, target);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public int removeFollow(String follower, String target){
        try {
            userNeo4jInterface.removeFollow(follower, target);
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

    public Integer addWatches(String username, String animeTitle, Integer status){
        try {
            return userNeo4jInterface.addWatches(username, animeTitle, status);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public Integer getWatchedEpisodesOfAnime(String username, String animeTitle) {
        try {
            return userNeo4jInterface.getWatchedEpisodesOfAnime(username, animeTitle);
        } catch (DataAccessException dae) {
            dae.printStackTrace();
            return null;
        }
    }

    public int setWatchedEpisodesOfAnime(String username, String animeTitle, Integer watchedEpisodes){
        try {
            userNeo4jInterface.setWatchedEpisodesOfAnime(username, animeTitle, watchedEpisodes);
            return 0;
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException) {
                dae.printStackTrace();
                return 6;
            } else {
                dae.printStackTrace();
                return 7;
            }
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

    public List<UserNode> findFollowedByUsername(String username){
        try {
            return userNeo4jInterface.findFollowedByUsername(username);
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

    public ArrayList<AnimeWatched> findWatchedAnime(String username, Integer status){
        // If status is null, each list is loaded
        String cypherQuery = "MATCH (a:Anime)<-[w:WATCHES]-(u:User {username: $username}) " +
                "WHERE ($status IS NULL) OR (w.status = $status)" +
                "RETURN a.title AS title, a.imgURL AS imgURL, toInteger(w.status) AS status, toInteger(w.watched_episodes) AS watchedEpisodes";
        return AnimeWatched.getAnimeWatched(neo4jClient, cypherQuery, username, status);
    }

    public ArrayList<UserNode> getSuggestedUsersToFollow(String username){
        try {
            return findSuggestedUsersToFollow(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    // dato un utente, restituisce gli utenti consigliati da seguire in base a anime visti in comune
    private ArrayList<UserNode> findSuggestedUsersToFollow(String username) {
        String cypherQuery = "MATCH (user:User {username: $username})-[w1:WATCHES]->(anime:Anime) " +
                "WHERE w1.status <> 4 " +    // se status == 4, l'anime è stato abbandonato, quindi non va considerato tra i gusti dell'utente
                "MATCH (otherUser:User)-[w2:WATCHES]->(anime) " +
                "WHERE NOT (user)-[:FOLLOWS]->(otherUser) AND user <> otherUser AND w2.status <> 4 " +
                "WITH otherUser, COUNT(*) AS recommendationSimilarity " +
                "RETURN otherUser.username AS username " +
                "ORDER BY recommendationSimilarity DESC " +
                "LIMIT 10";

        return UserNode.getUserNode(neo4jClient, username, cypherQuery);
    }
}
