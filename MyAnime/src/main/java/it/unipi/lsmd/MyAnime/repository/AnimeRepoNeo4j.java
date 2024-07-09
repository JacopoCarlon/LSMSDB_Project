package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.repository.Neo4j.AnimeNeo4jInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

@Repository
public class AnimeRepoNeo4j {
    @Autowired
    private AnimeNeo4jInterface animeNeo4jInterface;
    @Autowired
    private Neo4jClient neo4jClient;
}
