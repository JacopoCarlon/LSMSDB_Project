package it.unipi.lsmd.MyAnime.model.query;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;

import java.util.ArrayList;
@Data
@NoArgsConstructor
public class AnimeRelated {

    private String title;
    private String imgURL;
    private String relationship;


    public AnimeRelated(String title, String imgURL, String relationship) {
        this.title = title;
        this.imgURL = imgURL;
        this.relationship = relationship;
    }

    public static ArrayList<it.unipi.lsmd.MyAnime.model.query.AnimeRelated> getAnimeRelated(Neo4jClient neo4jClient, String cypherQuery, String title) {
        return (ArrayList<AnimeRelated>) neo4jClient
                .query(cypherQuery)
                .bind(title).to("title")
                .fetchAs(it.unipi.lsmd.MyAnime.model.query.AnimeRelated.class)
                .mappedBy((typeSystem, record) -> {
                    String titleRelated = record.get("titleRelated").asString();
                    String imgURL = record.get("imgURL").asString();
                    String relationship = record.get("relationship").asString();
                    return new it.unipi.lsmd.MyAnime.model.query.AnimeRelated(titleRelated, imgURL, relationship);
                }).all();
    }
}
