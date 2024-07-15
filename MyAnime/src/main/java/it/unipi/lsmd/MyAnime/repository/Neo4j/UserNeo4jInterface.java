package it.unipi.lsmd.MyAnime.repository.Neo4j;

import it.unipi.lsmd.MyAnime.model.UserNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;


public interface UserNeo4jInterface extends Neo4jRepository<UserNode, String> {

    @Query("MERGE (u:User {username: $username})")
    void createUser(String username);

    // cancella l'utente e tutte le relazioni associate ad esso
    @Query("MATCH (u:User {username: $username})-[r]-() " +
            "DELETE u, r")
    void deleteUser(String username);

    @Query("MATCH (u1:User {username: $user1}), (u2:User {username: $user2}) " +
            "OPTIONAL MATCH (u1)-[f:FOLLOWS]-(u2) " +
            "WITH u1, u2, f, CASE WHEN f IS NULL THEN 'CREATED' ELSE 'EXISTING' END AS status " +
            "MERGE (u1)-[:FOLLOWS]-(u2) " +
            "RETURN status")
    String addFollowing(String user1, String user2);

    @Query("MATCH (u1:User {username: $user1})-[r:FOLLOWS]-(u2:User {username: $user2}) " +
            "DELETE r")
    void removeFollowing(String user1, String user2);

    @Query("MATCH (u:User {username: $username}), (a:Anime {title: $animeTitle}) " +
            "MERGE (u)-[w:WATCHES]->(a) " +
            "WITH u, w, a, CASE WHEN w.status IS NULL THEN true ELSE false END AS isNew " +
            "LIMIT(1)" +
            "SET w.status = $status " +
            "SET w.episodes = $episodes " +
            "RETURN CASE WHEN isNew = true THEN 'CREATED' ELSE 'EXISTING' END")
    String addWatches(String username, String animeTitle, int status, int episodes);

    @Query("MATCH (u:User {username: $username})-[r:WATCHES]->(a:Anime {title: $animeTitle}) " +
            "DELETE r")
    void removeWatches(String username, String animeTitle);

    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]-(following) " +
            "RETURN COUNT(following)")
    int countFollowing(String username);

    @Query("MATCH (followers)-[:FOLLOWS]-(u:User {username: $username}) " +
            "RETURN COUNT(followers)")
    int countFollowers(String username);

    @Query("MATCH (u:User {username: $username})-[:FOLLOWS]-(following:User) " +
            "RETURN following")
    List<UserNode> findFollowingOfUsername(String username);
    @Query("MATCH (follower:User)-[:FOLLOWS]-(u:User {username: $username}) " +
            "RETURN follower")
    List<UserNode> findFollowersOfUsername(String username);
}