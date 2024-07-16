package it.unipi.lsmd.MyAnime.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimeOnlyAvgScore {
    private String animeId;
    private Double avgScore;
    private Integer scoredBy;

    public void roundAverageScore() {
        this.avgScore = Math.round(this.avgScore * 1000.0) / 1000.0;
    }
}
