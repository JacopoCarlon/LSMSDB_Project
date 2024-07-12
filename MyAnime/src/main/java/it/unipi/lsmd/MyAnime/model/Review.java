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

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String username;
    private String animeID;
    private int score;
    private String text;
    private Instant timestamp;

    @Transient
    private String printableDate;


    public Review(String username, String animeID, int score, String text, Instant timestamp) {
        this.username = username;
        this.animeID = animeID;
        this.score = score;
        this.text = text;
        this.timestamp = timestamp;
    }

    public void setPrintableDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
            printableDate = formatter.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
            printableDate = null;
        }
    }
}