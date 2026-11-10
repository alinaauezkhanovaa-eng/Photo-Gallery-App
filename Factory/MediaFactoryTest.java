package Factory;

import Model.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MediaFactoryTest {
    private MediaFactory mediaFactory;

    @BeforeEach
    void setUp() {
        mediaFactory=new MediaFactory();
    }

    @Test
    @DisplayName("Should create PhotoMedia object for the PHOTO type!")
    void testCreatePhotoMedia() {
        MediaFile media=mediaFactory.createMedia(MediaType.PHOTO,"pic.jpg");
        assertNotNull(media);
        assertEquals("PHOTO",media.getType());
        assertTrue(media instanceof PhotoMedia);
    }

    @Test
    @DisplayName("Should create VideoMedia object for the VIDEO type!")
    void testCreateVideoMedia() {
        MediaFile media=mediaFactory.createMedia(MediaType.VIDEO,"movie.mp4");
        assertNotNull(media);
        assertEquals("VIDEO",media.getType());
        assertTrue(media instanceof VideoMedia);
    }

    @Test
    @DisplayName("Should create PanoramaMedia object for the PANORAMA type!")
    void testCreatePanoramaMedia() {
        MediaFile media=mediaFactory.createMedia(MediaType.PANORAMA,"view.jpg");
        assertNotNull(media);
        assertEquals("PANORAMA",media.getType());
        assertTrue(media instanceof PanoramaMedia);
    }

    @Test
    @DisplayName("Should throw exception,when MediaType is null!")
    void testNullMediaTypeThrowsException() {
        assertThrows(IllegalArgumentException.class,
                ()-> mediaFactory.createMedia(null,"file.jpg"));
    }
}