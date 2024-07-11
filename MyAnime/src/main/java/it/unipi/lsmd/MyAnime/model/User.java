package it.unipi.lsmd.MyAnime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String passwordSalt;
    private String passwordHash;
    private String gender;
    private String birthDate;
    private Instant joinDate;
    private int statsEpisodes;
    private Review[] mostRecentReviews;
}