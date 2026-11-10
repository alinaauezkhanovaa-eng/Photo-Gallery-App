package Strategy;

import Model.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StrategyPatternTest {
    private GalleryContext galleryContext;
    private List<Photo> photos;

    @BeforeEach
    void setUp() {
        galleryContext=new GalleryContext();
        photos=new ArrayList<>();
        photos.add(new Photo("Old Photo",LocalDate.of(2020,1,1),3));
        photos.add(new Photo("New Photo", LocalDate.of(2026,5,10),5));
        photos.add(new Photo("Mid Photo", LocalDate.of(2024,3,15),4));
    }

    @Test
    @DisplayName("Should correctly sort photos by date(new ones are the first)")
    void testSortByDate() {
        galleryContext.setSortingStrategy(new SortByDateStrategy());
        galleryContext.executeSort(photos);
        assertEquals("New Photo",photos.get(0).getTitle());
        assertEquals("Mid Photo",photos.get(1).getTitle());
        assertEquals("Old Photo",photos.get(2).getTitle());
    }

    @Test
    @DisplayName("Should correctly sort photos by rating(highest first)!")
    void testSortByRating() {
        galleryContext.setSortingStrategy(new SortByRatingStrategy());
        galleryContext.executeSort(photos);
        assertEquals(5,photos.get(0).getRating());
        assertEquals(4,photos.get(1).getRating());
        assertEquals(3,photos.get(2).getRating());
    }

    @Test
    @DisplayName("Should break rating ties by newest date!")
    void testSortByRatingTieBreak() {
        photos.add(new Photo("Newest Five",LocalDate.of(2026,10,1),5));
        galleryContext.setSortingStrategy(new SortByRatingStrategy());
        galleryContext.executeSort(photos);
        assertEquals("Newest Five",photos.get(0).getTitle());
        assertEquals("New Photo",photos.get(1).getTitle());
    }

    @Test
    @DisplayName("Should sort photos by title A-Z!")
    void testSortByTitle() {
        galleryContext.setSortingStrategy(new SortByTitleStrategy());
        galleryContext.executeSort(photos);
        assertEquals("Mid Photo",photos.get(0).getTitle());
        assertEquals("New Photo",photos.get(1).getTitle());
        assertEquals("Old Photo",photos.get(2).getTitle());
    }

    @Test
    @DisplayName("Should throw exception when sorting strategy is not set")
    void testMissingStrategyThrowsException() {
        assertThrows(IllegalStateException.class,()->galleryContext.executeSort(photos));
    }
}