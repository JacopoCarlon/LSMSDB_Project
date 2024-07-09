package it.unipi.lsmd.MyAnime.repository;

import it.unipi.lsmd.MyAnime.repository.Neo4j.UserNeo4jInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepoNeo4j {

    @Autowired
    private UserNeo4jInterface userNeo4jInterface;
    @Autowired
    private Neo4jClient neo4jClient;
}
