package it.unipi.lsmd.MyAnime.model.query;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;
import static it.unipi.lsmd.MyAnime.utilities.Utility.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeWatched {
    private String title;
    private String imgURL;
    private Integer statusNumber;
    private Integer watchedEpisodes;

    public static ArrayList<it.unipi.lsmd.MyAnime.model.query.AnimeWatched> getAnimeWatched(Neo4jClient neo4jClient, String cypherQuery, String username, Integer status) {
        return (ArrayList<AnimeWatched>) neo4jClient
                .query(cypherQuery)
                .bind(username).to("username")
                .bind(status).to("status")
                .fetchAs(it.unipi.lsmd.MyAnime.model.query.AnimeWatched.class)
                .mappedBy((typeSystem, record) -> {
                    String title = record.get("title").asString();
                    String imgURL = record.get("imgURL").asString();
                    int statusNumber = record.get("status").asInt();
                    int watchedEpisodes = record.get("watchedEpisodes").asInt();
                    return new it.unipi.lsmd.MyAnime.model.query.AnimeWatched(title, imgURL, statusNumber, watchedEpisodes);
                }).all();
    }
}