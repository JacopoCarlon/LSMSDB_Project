package it.unipi.lsmd.MyAnime.model.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsPerDate {
    String year;
    String month;
    int reviews;
}
