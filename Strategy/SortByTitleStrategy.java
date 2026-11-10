package Strategy;

import Model.Photo;
import java.util.Comparator;
import java.util.List;

public class SortByTitleStrategy implements SortingStrategy {
    @Override
    public void sort(List<Photo> photos) {
        photos.sort(Comparator.comparing(Photo::getTitle,String.CASE_INSENSITIVE_ORDER));
        System.out.println("Photos are sorted by title (A-Z)");
    }
}