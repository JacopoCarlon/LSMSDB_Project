package it.unipi.lsmd.MyAnime.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "it.unipi.lsmd.MyAnime.repository")
// Enable the Neo4jRepository support for this package. This allows you to use Spring Data Neo4j repositories.
public class Neo4jConfig {
}
