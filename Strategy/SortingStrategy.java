package Strategy;

import Model.Photo;
import java.util.List;

public interface SortingStrategy {
    void sort(List<Photo> photos);
}