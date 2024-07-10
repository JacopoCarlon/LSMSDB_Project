package it.unipi.lsmd.MyAnime.controller.api;

import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoNeo4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

/* 
Handle sinup using informations required at the signupPage.html:
-   name
-   surname
-   username
-   password (pwd double insert consistency is managed in signupPage.js)
-   birthday
-   email



*/



@RestController
public class SignupREST {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;
    @Autowired
    UserRepoNeo4j userRepoNeo4j;

    @PostMapping("/api/signup")
    @Transactional
    public @ResponseBody String signup(
            @RequestParam("name")       String name,
            @RequestParam("surname")    String surname,
            @RequestParam("username")   String username,
            @RequestParam("password")   String password,
            @RequestParam("birthday")   String birthday,
            @RequestParam("sex")        String sex,
            @RequestParam("email")      String email
    ) {
        try{
            // can user be inserted ?
            Instant this_instant = Instant.now(); 
            //  insertUser(String name, String surname, String username, String password, String birthDate, String email, String gender, Instant joinDate, int statsEpisodes)
            int o_insertMDB = userRepoMongoDB.insertUser(name, surname, username, password, birthday, email, sex, this_instant, 0);
            if (o_insertMDB!=0){
                // Failed insert in MongoDB
                return buildOutcomeResponse(o_insertMDB);
            }
            // correctly inserted in MongoDB, will now try to insert in Neo4jDB

            int o_insertN4j = userRepoNeo4j.insertUser(username);
            if (o_insertN4j!=0){
                // Failed insert in Neo4j DB
                return "{\"outcome_code\": 6}";
            }

            // User did register succesfully on both databases
            return "{\"outcome_code\": 0}";

        }   catch (Exception e) {
            // Generic Error
            e.printStackTrace();
            return "{\"outcome_code\": 5}"; 
        }

    }




    private String buildOutcomeResponse(int outcomeCode) {
        // Failed insert in MongoDB cases : 
        switch (outcomeCode) {
            case 1:
                // Username already exists
                return "{\"outcome_code\": 1}"; 
            case 2:
                // Email already exists
                return "{\"outcome_code\": 2}"; 
            case 3:
                // Error while connecting to MongoDB
                return "{\"outcome_code\": 3}"; 
            case 4:
                // Other MongoDB error
                return "{\"outcome_code\": 4}"; 
            default:
                // Unhandled error
                return "{\"outcome_code\": 5}"; 
        }
    }



}

