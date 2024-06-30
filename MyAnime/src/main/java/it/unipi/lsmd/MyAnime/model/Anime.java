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
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "animes")
public class Anime {
    @Id
    private String id;
    private String title;
    private String title_japanese;
    private String type;
    private String source;
    private int episodes;
    private String status;
    private boolean airing;
    private Map aired;
    private String rated;
    private float averageScore;
    private int scoredBy;
    private int watchers;
    private String synopsis;
    private String broadcast;
    private String[] producer;
    private String licensor;
    private String studio;
    private String[] genre;
    private int episodeDuration;
    private String imgURL;
    @Field("most_recent_reviews")
    private Review[] mostRecentReviews;

    public Anime (ObjectId objId, String title, String title_japanese, String type, String source, int episodes, String status, boolean airing, Map aired, String rated, float avarageScore, int scoredBy, int watchers, String synopsis, String broadcast, String[] producer, String licensor, String studio, String[] genre, int EpisodeDuration, String imgURL, Review[] mostRecentReviews) {
        this.id = objId.toString();
        this.title = title;
        this.title_japanese = title_japanese;
        this.type = type;
        this.source = source;
        this.episodes = episodes;
        this.status = status;
        this.airing = airing;
        this.aired = aired;
        this.rated = rated;
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

    public static Anime mapToAnime(org.bson.Document doc) {
        ObjectId id = doc.getObjectId("id");
        String title = doc.getString("title");
        String title_japanese = doc.getString("title_japanese");
        String type = doc.getString("type");
        String source = doc.getString("source");
        int episodes = doc.getInteger("episodes");
        String status = doc.getString("status");
        boolean airing = doc.getBoolean("airing");
        Map aired = doc.getMap("aired"); //probabilmente sbagliato
        String rated = doc.getString("rated");
        float averageScore = doc.getDouble("averageScore").floatValue();
        int scoredBy = doc.getInteger("scoredBy");
        int watchers = doc.getInteger("watchers");
        String synopsis = doc.getString("synopsis");
        String broadcast = doc.getString("broadcast");
        String[] producer = doc.getList("producer", String.class).toArray(new String[0]);
        String licensor = doc.getString("licensor");
        String studio = doc.getString("studio");
        String[] genre = doc.getList("genre", String.class).toArray(new String[0]);
        int episodeDuration = doc.getInteger("episodeDuration");
        String imgURL = doc.getString("imgURL");

        Review[] mostRecentReviews = new Review[0];

        return new Anime(objId, title, title_japanese, type, source, episodes, status, airing, aired, rated, avarageScore, scoredBy, watchers, synopsis, broadcast, producer, licensor, studio, genre, EpisodeDuration, imgURL, mostRecentReviews);
    }
}