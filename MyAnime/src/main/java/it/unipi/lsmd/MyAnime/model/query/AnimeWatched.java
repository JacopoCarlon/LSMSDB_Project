package it.unipi.lsmd.MyAnime.model.query;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;
@Data
@NoArgsConstructor
public class AnimeWatched {
    private String title;
    private String imgURL;
    private String status;


    public AnimeWatched(String title, String imgURL, String status) {
        this.title = title;
        this.imgURL = imgURL;
        this.status = status;
    }

    public static ArrayList<it.unipi.lsmd.MyAnime.model.query.AnimeWatched> getAnimeWatched(Neo4jClient neo4jClient, String cypherQuery, String username) {
        return (ArrayList<AnimeWatched>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .fetchAs(it.unipi.lsmd.MyAnime.model.query.AnimeWatched.class)
                .mappedBy((typeSystem, record) -> {
                    String title = record.get("title").asString();
                    String imgURL = record.get("imgURL").asString();
                    String status = record.get("status").asString();
                    return new it.unipi.lsmd.MyAnime.model.query.AnimeWatched(title, imgURL, status);
                }).all();
    }
}