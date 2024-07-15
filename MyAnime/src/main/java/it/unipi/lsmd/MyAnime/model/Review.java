package it.unipi.lsmd.MyAnime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {
    @Id
    private ObjectId id;
    private String username;
    @Field("anime_id")
    private ObjectId animeID;
    private String text;
    private int score;
    @Field("anime_title")
    private String animeTitle;
    @Field("review_time")
    private Instant timestamp;

    @Transient
    private String printableDate;


    public Review(String username, ObjectId animeID, int score, String text, Instant timestamp, String animeTitle) {
        this.username = username;
        this.animeID = animeID;
        this.score = score;
        this.text = text;
        this.animeTitle = animeTitle;
        this.timestamp = timestamp;
    }

    public void setPrintableDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:MM");
            printableDate = formatter.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            printableDate = null;
        }
    }
}