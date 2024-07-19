package it.unipi.lsmd.MyAnime.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreScored {
    private String genre;
    private Double avgScore;
    private Double maxScore;
    private Double minScore;

    public void roundAverageScore() {
        this.avgScore = Math.round(this.avgScore * 1000.0) / 1000.0;
    }
}
