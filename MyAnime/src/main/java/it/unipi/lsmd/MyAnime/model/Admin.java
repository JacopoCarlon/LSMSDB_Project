package it.unipi.lsmd.MyAnime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "admins")
public class Admin {
    @Id
    private String id;
    private String username;
    private String email;
    private String passwordSalt;
    private String passwordHash;
}