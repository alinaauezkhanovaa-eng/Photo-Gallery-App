package Strategy;

import Model.Photo;
import java.util.Comparator;
import java.util.List;

public class SortByDateStrategy implements SortingStrategy {
    @Override
    public void sort(List<Photo> photos) {
        photos.sort(Comparator.comparing(Photo::getDate).reversed());
        System.out.println("Photos are sorted by the date (first of all new ones).");
    }
}