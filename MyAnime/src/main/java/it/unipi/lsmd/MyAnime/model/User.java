package it.unipi.lsmd.MyAnime.model;

import it.unipi.lsmd.MyAnime.model.query.ReviewLite;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;
    private String username;
    private String name;
    private String surname;
    private String email;
    @Field("password_hash")
    private String passwordHash;
    @Field("password_salt")
    private String passwordSalt;
    private String gender;
    @Field("birth_date")
    private Instant birthDate;
    @Field("join_date")
    private Instant joinDate;
    @Field("stats_episodes")
    private int statsEpisodes;
    @Field("most_recent_reviews")
    private ReviewLite[] mostRecentReviews;
}