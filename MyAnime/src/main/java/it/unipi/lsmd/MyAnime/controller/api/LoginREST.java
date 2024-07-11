package it.unipi.lsmd.MyAnime.controller.api;

import com.google.common.hash.Hashing;
import it.unipi.lsmd.MyAnime.model.Admin;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.AdminRepoMongoDB;
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
    @Autowired
    AdminRepoMongoDB adminRepoMongoDB;

    @PostMapping("/api/login")
    public @ResponseBody String login (HttpSession session,
                                       @RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       @RequestParam("option") Boolean as_admin                                       
                                       ) {

        try {
            String salt, hash;

            if (as_admin){
                Admin admin = adminRepoMongoDB.getAdminByUsername(username);
                if (admin == null) {
                    // Admin not found
                    return "{\"login_code\": 1}";
                }
                salt = admin.getPasswordSalt();
                hash = admin.getPasswordHash();
            }
            else {
                User user = userRepoMongoDB.getUserByUsername(username);
                if(user == null){
                    //  //  User not found
                    return "{\"login_code\": 1}";
                }
                salt = user.getPasswordSalt();
                hash = user.getPasswordHash();
            }

           String hashedPassword = Hashing.sha256()
                    .hashString(salt + password, StandardCharsets.UTF_8)
                    .toString();

            if(hash.equals(hashedPassword)){
                //  //  Login successful
                session.setAttribute("is_logged", true);
                session.setAttribute("username", username);
                session.setAttribute("is_admin", as_admin);
                return "{\"login_code\": 0}";     
            }
            else {
                //  //  Wrong password
                return "{\"login_code\": 2}";     
            }

        } catch (DataAccessResourceFailureException e) {
            //  //  Error connecting to DB
            e.printStackTrace();
            return "{\"login_code\": 3}";         
        }
    }
}


//  TODO (above)

//  HOW WAS PASSWORD AND SALT GENERATED :
//  (from : users_converter.ipynb)

/*
def hash_salt(password) :
    salt = uuid.uuid4().hex
    concat = str(salt+password)
    hashed = hashlib.sha256( concat.encode() ).hexdigest() 
    return [ hashed , salt ]
*/
/*
extracted['password'] = extracted.apply( 
                lambda row: hash_salt(row['username'])
                , axis=1 )  
*/
