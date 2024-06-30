package it.unipi.lsmd.MyAnime.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Node("User")
public class UserNode {
    @Id @GeneratedValue
    private String id;

    private String username;


    public AnimeNode(String username) {
        this.username = username;
    }

    public static ArrayList<AnimeNode> getAnimeNode(Neo4jClient neo4jClient, String username, String cypherQuery) {
        return (ArrayList<Album_Neo4j>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(Album_Neo4j.class)
                .mappedBy((typeSystem, record) -> {
                    String usernameNode = record.get("username").asString();
                    return new UserNode(usernameNode);
                }).all();
    }
}