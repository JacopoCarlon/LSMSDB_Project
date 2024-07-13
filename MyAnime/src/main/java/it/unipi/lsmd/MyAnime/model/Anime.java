package it.unipi.lsmd.MyAnime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.Map;

import it.unipi.lsmd.MyAnime.model.Review;

@Data
@NoArgsConstructor
@Document(collection = "animes")
public class Anime {
    @Id
    private ObjectId id;
    private String title;
    @Field(name = "title_japanese")
    private String titleJapanese;
    private String type;
    private String source;
    private int episodes;
    private boolean airing;
    private Map aired;
    private String rating;
    @Field("score")
    private float averageScore;
    @Field("scored_by")
    private int scoredBy;
    @Field("members")
    private int watchers;
    @Field("background")
    private String synopsis;
    private String broadcast;
    private String producer;
    private String licensor;
    private String studio;
    private String[] genre;
    @Field("duration_min")
    private int episodeDuration;
    @Field("picture")
    private String imgURL;
    private Review[] mostRecentReviews;

    public Anime (ObjectId objId, String title, String titleJapanese, String type, String source, int episodes,  boolean airing, Map aired, String rating, float avarageScore, int scoredBy, int watchers, String synopsis, String broadcast, String producer, String licensor, String studio, String[] genre, int EpisodeDuration, String imgURL, Review[] mostRecentReviews) {
        this.id = objId;
        this.title = title;
        this.titleJapanese = titleJapanese;
        this.type = type;
        this.source = source;
        this.episodes = episodes;
        this.airing = airing;
        this.aired = aired;
        this.rating = rating;
        this.averageScore = avarageScore;
        this.scoredBy = scoredBy;
        this.watchers = watchers;
        this.synopsis = synopsis;
        this.broadcast = broadcast;
        this.producer = producer;
        this.licensor = licensor;
        this.studio = studio;
        this.genre = genre;
        this.episodeDuration = EpisodeDuration;
        this.imgURL = imgURL;
        this.mostRecentReviews = mostRecentReviews;
    }

}