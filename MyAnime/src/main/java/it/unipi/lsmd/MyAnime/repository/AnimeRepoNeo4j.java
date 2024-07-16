package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.model.AnimeNode;
import it.unipi.lsmd.MyAnime.model.query.AnimeRelated;
import it.unipi.lsmd.MyAnime.repository.Neo4j.AnimeNeo4jInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class AnimeRepoNeo4j {
    @Autowired
    private AnimeNeo4jInterface animeNeo4jInterface;
    @Autowired
    private Neo4jClient neo4jClient;

    public ArrayList<AnimeRelated> findRelatedAnime(String title) {
        String cypherQuery = "MATCH (a:Anime)-[r:RELATED_TO]->(b:Anime {title: $title})" +
                "RETURN a.title as title, a.imgURL as imgURL, r.relation_type as relationship";

        return AnimeRelated.getAnimeRelated(neo4jClient, cypherQuery, title);
    }

    public ArrayList<AnimeNode> getSuggestedAnime_ByFollow(String username){
        try {
            return findSuggestedAnime_ByFollow(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    // dato un utente, restituisce gli anime consigliati in base ai gusti degli utenti che segue
    // (anime visti da utenti che segue)
    private ArrayList<AnimeNode> findSuggestedAnime_ByFollow(String username) {
        String cypherQuery = "MATCH (user:User {username: $username})-[:FOLLOWS]->(otherUser:User) " +
                "MATCH (otherUser)-[w1:WATCHES]->(recommendedAnime:Anime) " +
                "WHERE NOT (user)-[:WATCHES]->(recommendedAnime) AND w1.status <> 4 " +     // se status == 4, l'anime è stato abbandonato, quindi non va considerato tra i gusti dell'utente
                "WITH recommendedAnime, COUNT(*) AS recommendationSimilarity " +
                "RETURN recommendedAnime.title AS title, recommendedAnime.imgURL AS imgURL " +
                "ORDER BY recommendationSimilarity " +
                "LIMIT 50";

        return AnimeNode.getAnimeNodeByUsername(neo4jClient, cypherQuery, username);
    }

    public ArrayList<AnimeNode> getSuggestedAnime_ByTaste(String username){
        try {
            return findSuggestedAnime_ByTaste(username);
        } catch (DataAccessException dae) {
            if (dae instanceof DataAccessResourceFailureException)
                throw dae;
            dae.printStackTrace();
            return null;
        }
    }

    // dato un utente, restituisce gli anime consigliati in base ai suoi gusti
    // (anime che piacciono ad utenti che hanno gusti simili)
    private ArrayList<AnimeNode> findSuggestedAnime_ByTaste(String username) {
        String cypherQuery = "MATCH (user:User {username: $username})-[w1:WATCHES]->(anime:Anime) " +
                "WHERE w1.status <> 4 " +    // se status == 4, l'anime è stato abbandonato, quindi non va considerato tra i gusti dell'utente
                "MATCH (otherUser:User)-[w2:WATCHES]->(anime) " +
                "WHERE user <> otherUser AND w2.status <> 4 " +
                "MATCH (otherUser)-[w3:WATCHES]->(recommendedAnime:Anime) " +
                "WHERE NOT (user)-[:WATCHES]->(recommendedAnime) AND w3.status <> 4 " +
                "WITH recommendedAnime, COUNT(*) AS recommendationSimilarity " +
                "RETURN recommendedAnime.title AS title, recommendedAnime.imgURL AS imgURL " +
                "ORDER BY recommendationSimilarity " +
                "LIMIT 50";

        return AnimeNode.getAnimeNodeByUsername(neo4jClient, cypherQuery, username);
    }
}
