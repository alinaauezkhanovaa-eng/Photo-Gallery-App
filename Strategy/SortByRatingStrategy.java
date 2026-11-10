package Strategy;

import Model.Photo;
import java.util.Comparator;
import java.util.List;

public class SortByRatingStrategy implements SortingStrategy {
    @Override
    public void sort(List<Photo> photos) {
        photos.sort(Comparator.comparingInt(Photo::getRating).reversed()
                .thenComparing(Comparator.comparing(Photo::getDate).reversed()));
        System.out.println("Photos are sorted by the rating");
    }
}