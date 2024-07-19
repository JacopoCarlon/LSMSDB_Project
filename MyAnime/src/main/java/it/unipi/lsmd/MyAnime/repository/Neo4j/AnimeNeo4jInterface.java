package it.unipi.lsmd.MyAnime.repository.Neo4j;

import it.unipi.lsmd.MyAnime.model.AnimeNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface AnimeNeo4jInterface extends Neo4jRepository<AnimeNode, String> {
    AnimeNode getAnimeByTitle(String animeTitle);

    boolean existsByTitle(String animeTitle);

    //@Query("MATCH (a:Anime) WHERE a.title = $animeTitle RETURN a")
    //AnimeNode getAnimeByExactTitle(String animeTitle);

    @Query("MERGE (a:Anime {title: $title, imgURL: $imgURL})")
    void createAnime(String title, String imgURL);

    @Query("MATCH (a1:Anime {title: $anime1}), (a2:Anime {title: $anime2}) " +
            "OPTIONAL MATCH (a1)-[irt:IS_RELATED_TO]->(a2) " +
            "WITH a1, a2, irt, CASE WHEN irt IS NULL THEN 'CREATED' ELSE 'EXISTING' END AS status " +
            "MERGE (a1)-[:IS_RELATED_TO {relation_type: $type}]->(a2)" +
            "MERGE (a2)-[:IS_RELATED_TO {relation_type: $typeInverse}]->(a1)" +
            "RETURN status")
    String addRelated(String anime1, String anime2, String type, String typeInverse);
}