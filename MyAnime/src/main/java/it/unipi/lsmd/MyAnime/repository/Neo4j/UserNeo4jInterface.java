package it.unipi.lsmd.MyAnime.repository.Neo4j;

import it.unipi.lsmd.MyAnime.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;


public interface UserNeo4jInterface extends Neo4jRepository<UserNode, String> {

    @Query("MERGE (u:User {username: $username})")
    void createUser(String username);

    // cancella l'utente e tutte le relazioni associate ad esso
    @Query("MATCH (u:User {username: $username})-[r]" +
            "DELETE DETACH u")
    void deleteUser(String username);

    @Query("MATCH (u1:User {username: $user1}) " +
            "MATCH (u2:User {username: $user2}) " +
            "OPTIONAL MATCH (u1)-[f:FOLLOWS]->(u2) " +
            "WITH u1, u2, f, CASE WHEN f IS NULL THEN 'CREATED' ELSE 'EXISTING' END AS status " +
            "MERGE (u1)-[:FOLLOWS]->(u2) " +
            "RETURN status")
    String addFollow(String user1, String user2);

    @Query("MATCH (u1:User {username: $user1})-[r:FOLLOWS]->(u2:User {username: $user2}) " +
            "DELETE r")
    void removeFollow(String user1, String user2);

    @Query("MATCH (u:User {username: $username}) " +
            "MATCH (a:Anime {title: $animeTitle}) " +
            "OPTIONAL MATCH (u)-[w:WATCHES]->(a) " +
            "WITH a, u, w, CASE WHEN w IS NULL THEN 0 ELSE w.status END AS oldStatus " +
            "MERGE (u)-[ww:WATCHES]->(a) " +
            "SET ww.status = $status " +
            "SET ww.watched_episodes = 0 " +
            "RETURN oldStatus")
    Integer addWatches(String username, String animeTitle, Integer status);

    @Query("MATCH (u:User {username: $username}) " +
            "MATCH (a:Anime {title: $animeTitle}) " +
            "OPTIONAL MATCH (u)-[w:WATCHES]->(a) " +
            "RETURN CASE WHEN w IS NULL THEN NULL ELSE w.watched_episodes END AS watched_episodes")
    Integer getWatchedEpisodesOfAnime(String username, String animeTitle);

    @Query("MATCH (u:User {username: $username})-[w:WATCHES]->(a:Anime {title: $animeTitle})" +
            "SET w.watched_episodes = $watchedEpisodes")
    void setWatchedEpisodesOfAnime(String username, String animeTitle, Integer watchedEpisodes);


    @Query("MATCH (u:User {username: $username})-[r:WATCHES]->(a:Anime {title: $animeTitle}) " +
            "DELETE r")
    void removeWatches(String username, String animeTitle);

    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]->(following) " +
            "RETURN COUNT(following)")
    int countFollowing(String username);

    @Query("MATCH (followers)-[:FOLLOWS]->(u:User {username: $username}) " +
            "RETURN COUNT(followers)")
    int countFollowers(String username);

    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]->(followed:User) " +
            "RETURN followed")
    List<UserNode> findFollowedByUsername(String username);
    @Query("MATCH (follower:User)-[:FOLLOWS]->(u:User {username: $username}) " +
            "RETURN follower")
    List<UserNode> findFollowersOfUsername(String username);
}