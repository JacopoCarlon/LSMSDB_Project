package it.unipi.lsmd.MyAnime.controller.api;

import com.google.common.hash.Hashing;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
public class LoginREST {
    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @PostMapping("/api/login")
    public @ResponseBody String login (HttpSession session,
                                       @RequestParam("username") String username,
                                       @RequestParam("password") String password) {

        try {
            User user = userRepoMongoDB.getUserByUsername(username);

            if(user == null){
                // TODO return not found
                return "";     // User not found
            }

            String hashedPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();

            if(user.getPassword().equals(hashedPassword)){
                // TODO return success
                return "";     // Login successful
            }
            else {
                // TODO return failure
                return "";     // Wrong password
            }

        } catch (DataAccessResourceFailureException e) {
            e.printStackTrace();
            // TODO return error
            return "";         // Error connecting to DB
        }
    }
}
