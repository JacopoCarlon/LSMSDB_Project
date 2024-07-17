package it.unipi.lsmd.MyAnime.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import it.unipi.lsmd.MyAnime.model.Anime;
import it.unipi.lsmd.MyAnime.model.User;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoMongoDB;
import it.unipi.lsmd.MyAnime.repository.UserRepoMongoDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchREST {
    @Autowired
    AnimeRepoMongoDB animeRepoMongoDB;
    @Autowired
    UserRepoMongoDB userRepoMongoDB;

    @GetMapping("/api/search")
    public @ResponseBody String search(@RequestParam("term") String term,
                                       @RequestParam("category") String category,
                                       @RequestParam(required = false) List<String> genreList,
                                       @RequestParam(required = false) List<String> yearList,
                                       @RequestParam(required = false) List<String> typeList,
                                       @RequestParam(required = false) List<String> statusList,
                                       @RequestParam(required = false) List<String> ratingList,
                                       @RequestParam(required = false) String sortList){

        if(category.equals("anime")){
            System.out.println("Searching in anime");
            System.out.println(term);
            System.out.println(category);
            System.out.println(genreList);
            System.out.println(yearList);
            System.out.println(typeList);
            System.out.println(statusList);
            System.out.println(ratingList);
            System.out.println(sortList);
            List<Anime> animes = animeRepoMongoDB.findAnime(term, genreList, yearList, typeList, statusList, ratingList, sortList);
            System.out.println("Animes");
            for(Anime anime : animes){
                System.out.println(anime);
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
                return objectMapper.writeValueAsString(animes);
            } catch (Exception e){
                e.printStackTrace();
                return "{\"error\": \"An error occurred while converting the data.\"}";
            }

        }
        else if(category.equals("user")){
            List<User> users = userRepoMongoDB.findUser(term);
            try {
                /*for(User user : users){
                    user.setPrintableBirthDate();
                    user.setPrintableJoinDate();
                }
                System.out.println(users);
                String js = new Gson().toJson(users);
                System.out.println(js);
                return js;*/
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                objectMapper.registerModule(new JavaTimeModule());
                return objectMapper.writeValueAsString(users);
                //return ;
            } catch (Exception e){
                e.printStackTrace();
                return "{\"error\": \"An error occurred while converting the data.\"}";
            }
        }
        else
            return "Invalid category";
    }
}
