package it.unipi.lsmd.MyAnime.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimeWithWatchers {
    private String title;
    private String imgURL;
    private Integer watchers;

    public static ArrayList<AnimeWithWatchers> getAnimeWithWatchers(String cypherQuery, Neo4jClient neo4jClient) {
        return (ArrayList<AnimeWithWatchers>) neo4jClient
                .query(cypherQuery)
                .fetchAs(AnimeWithWatchers.class)
                .mappedBy((typeSystem, record) -> {
                    String title = record.get("title").asString();
                    String imgURL = record.get("imgURL").asString();
                    Integer watchers = record.get("watchers").asInt();
                    return new AnimeWithWatchers(title, imgURL, watchers);
                }).all();
    }
}
