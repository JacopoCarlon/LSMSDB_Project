package it.unipi.lsmd.MyAnime.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.unipi.lsmd.MyAnime.model.query.ReviewLite;
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
    private Double averageScore;
    @Field("scored_by")
    private int scoredBy;
    @Field("members")
    private int watchers;
    private String background;
    private String broadcast;
    private String producer;
    private String licensor;
    private String studio;
    private String[] genre;
    @Field("duration_min")
    private int episodeDuration;
    @Field("picture")
    private String imgURL;
    @Field("most_recent_reviews")
    private ReviewLite[] mostRecentReviews;

    public Anime (ObjectId objId, String title, String titleJapanese, String type, String source, int episodes,  boolean airing, Map aired, String rating, Double averageScore, int scoredBy, int watchers, String background, String broadcast, String producer, String licensor, String studio, String[] genre, int EpisodeDuration, String imgURL, ReviewLite[] mostRecentReviews) {
        this.id = objId;
        this.title = title;
        this.titleJapanese = titleJapanese;
        this.type = type;
        this.source = source;
        this.episodes = episodes;
        this.airing = airing;
        this.aired = aired;
        this.rating = rating;
        this.averageScore = averageScore;
        this.scoredBy = scoredBy;
        this.watchers = watchers;
        this.background = background;
        this.broadcast = broadcast;
        this.producer = producer;
        this.licensor = licensor;
        this.studio = studio;
        this.genre = genre;
        this.episodeDuration = EpisodeDuration;
        this.imgURL = imgURL;
        this.mostRecentReviews = mostRecentReviews;
    }

    public Anime (String title, String titleJapanese, String type, String source, int episodes,  boolean airing, Map aired, String rating, String background, String broadcast, String producer, String licensor, String studio, String[] genre, int EpisodeDuration, String imgURL) {
        this.title = title;
        this.titleJapanese = titleJapanese;
        this.type = type;
        this.source = source;
        this.episodes = episodes;
        this.airing = airing;
        this.aired = aired;
        this.rating = rating;
        this.background = background;
        this.broadcast = broadcast;
        this.producer = producer;
        this.licensor = licensor;
        this.studio = studio;
        this.genre = genre;
        this.episodeDuration = EpisodeDuration;
        this.imgURL = imgURL;
    }

    public static Anime mapToAnime(org.bson.Document doc) {
        Anime anime = new Anime();
        String title = doc.getString("title");
        anime.setTitle(title);
        if (doc.get("score") != null){
            Double score;
            if (doc.get("score").getClass() == java.lang.Integer.class){
                score = doc.getInteger("score").doubleValue();
            } else {
                score = doc.getDouble("score");
            }
            anime.setAverageScore(score);
        }
        String imgURL = doc.getString("picture");
        anime.setImgURL(imgURL);

        return anime;
    }
}