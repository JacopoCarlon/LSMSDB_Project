package it.unipi.lsmd.MyAnime.model.query;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class ReviewLite {
    private String username;
    @Field("anime_title")
    private String animeTitle;
    private Integer score;
    @Field("review_time")
    private Instant timestamp;

    @Transient
    private String printableDate;

    public ReviewLite (String username, String animeTitle, Integer score, Instant timestamp) {
        this.username = username;
        this.animeTitle = animeTitle;
        this.score = score;
        this.timestamp = timestamp;
    }
    public void setPrintableDate() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            printableDate = formatter.format(timestamp.atZone(ZoneId.of("UTC")));
        } catch (Exception e) {
            e.printStackTrace();
            printableDate = null;
        }
    }
}
