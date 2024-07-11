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
@Node("Anime")
public class AnimeNode {
    @Id @GeneratedValue
    private String id;

    private String title;
    private String imgURL;


    public AnimeNode(String title, String imgURL) {
        this.title = title;
        this.imgURL = imgURL;
    }

    public static ArrayList<AnimeNode> getAnimeNode(Neo4jClient neo4jClient, String username, String cypherQuery) {
        return (ArrayList<AnimeNode>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(AnimeNode.class)
                .mappedBy((typeSystem, record) -> {
                    String title = record.get("title").asString();
                    String imgURL = record.get("imgURL").asString();
                    return new AnimeNode(title, imgURL);
                }).all();
    }
}