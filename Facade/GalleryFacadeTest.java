package Facade;

import Builder.ExportSettings;
import Builder.ExportSettingsBuilder;
import Decorator.ImageComponent;
import Factory.MediaFile;
import Model.FilterType;
import Model.MediaType;
import Model.Photo;
import Observer.Observer;
import Strategy.SortByRatingStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GalleryFacadeTest {
    private final List<String> events=new ArrayList<>();
    private final Observer recorder=(event,message)->events.add(event);
    private GalleryFacade facade;

    @BeforeEach
    void setUp() {
        facade=new GalleryFacade(10);
        facade.subscribeObserver(recorder);
    }

    @Test
    @DisplayName("Should import media and notify observers,when there is enough space!")
    void testImportSuccess() {
        MediaFile media=facade.importMedia(MediaType.PHOTO,"a.jpg",4);

        assertNotNull(media);
        assertEquals("PHOTO",media.getType());
        assertEquals(List.of("MEDIA_IMPORTED"),events);
    }

    @Test
    @DisplayName("Should refuse import and notify IMPORT_FAILED,when disk is full!")
    void testImportFailsWhenDiskFull() {
        assertNull(facade.importMedia(MediaType.VIDEO,"big.mp4",50));
        assertEquals(List.of("IMPORT_FAILED"),events);
    }

    @Test
    @DisplayName("Should stack filters through the facade!")
    void testStackedFilters() {
        ImageComponent image=facade.openImage("a.jpg",3.0);
        image=facade.applyFilter(image,FilterType.SEPIA);
        image=facade.applyFilter(image,FilterType.WATERMARK);
        assertTrue(image.render().contains("Sepia"));
        assertTrue(image.render().contains("Watermark"));
        assertEquals(List.of("FILTER_APPLIED","FILTER_APPLIED"),events);
    }

    @Test
    @DisplayName("Should sort photos through the facade using the given strategy!")
    void testSortPhotos() {
        List<Photo> photos=new ArrayList<>();
        photos.add(new Photo("Low",LocalDate.of(2026,1,1),2));
        photos.add(new Photo("High",LocalDate.of(2026,2,1),5));
        facade.sortPhotos(photos,new SortByRatingStrategy());
        assertEquals("High",photos.get(0).getTitle());
        assertEquals(List.of("PHOTOS_SORTED"),events);
    }

    @Test
    @DisplayName("Should export media with builder settings and notify observers!")
    void testExportMedia() {
        MediaFile media=facade.importMedia(MediaType.PHOTO,"a.jpg",1);
        ExportSettings settings=new ExportSettingsBuilder().setFormat("PNG").build();
        events.clear();
        facade.exportMedia(media,settings);
        assertEquals(List.of("MEDIA_EXPORTED"),events);
    }
}