package it.unipi.lsmd.MyAnime.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "admins")
public class Admin {
    @Id
    private String id;
    private String username;
    private String email;
    @Field("password_salt")
    private String passwordSalt;
    @Field("password_hash")
    private String passwordHash;
}