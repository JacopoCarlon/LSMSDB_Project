package it.unipi.lsmd.MyAnime.repository.Neo4j;

import it.unipi.lsmd.MyAnime.model.AnimeNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface Anime_Neo4jInterface extends Neo4jRepository<AnimeNode, String> {

    @Query("MATCH (a:Anime) WHERE a.title =~ $animeTitle RETURN a")
    AnimeNode getAnimeByTitle(String animeTitle);
}