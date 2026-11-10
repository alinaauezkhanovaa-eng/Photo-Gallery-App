package Strategy;

import Model.Photo;
import java.util.List;

public class GalleryContext {
    private SortingStrategy sortingStrategy;

    public void setSortingStrategy(SortingStrategy sortingStrategy) {
        this.sortingStrategy=sortingStrategy;
    }

    public void executeSort(List<Photo> photos) {
        if (sortingStrategy==null) {
            throw new IllegalStateException("Sorting Strategy is not set!");
        }
        sortingStrategy.sort(photos);
    }
}