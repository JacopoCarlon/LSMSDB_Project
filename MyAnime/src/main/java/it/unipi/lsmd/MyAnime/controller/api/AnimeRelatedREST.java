package it.unipi.lsmd.MyAnime.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.unipi.lsmd.MyAnime.model.query.AnimeRelated;
import it.unipi.lsmd.MyAnime.repository.AnimeRepoNeo4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AnimeRelatedREST {
    @Autowired
    AnimeRepoNeo4j animeRepoNeo4j;

    @GetMapping("/api/animeRelated")
    public @ResponseBody String animeRelated(@RequestParam("title") String title){
        try {
            List<AnimeRelated> animes = animeRepoNeo4j.findRelatedAnime(title);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(animes);
        } catch (Exception e){
            e.printStackTrace();
            return "{\"error\": \"An error occurred while converting the data.\"}";
        }
    }
}
