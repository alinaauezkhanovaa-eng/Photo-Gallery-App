package Builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExportSettingsBuilderTest {
    @Test
    @DisplayName("Should build ExportSettings with customized parameters!")
    void testBuilderCustomParameters() {
        ExportSettings settings=new ExportSettingsBuilder()
                .setFormat("PNG")
                .setResolution("3840x2160")
                .setCompressionQuality(80)
                .setIncludeWatermark(true)
                .build();

        assertEquals("PNG",settings.getFormat());
        assertEquals("3840x2160",settings.getResolution());
        assertEquals(80,settings.getCompressionQuality());
        assertTrue(settings.isIncludeWatermark());
    }

    @Test
    @DisplayName("Should use sensible defaults,when nothing is configured!")
    void testBuilderDefaults() {
        ExportSettings settings=new ExportSettingsBuilder().build();
        assertEquals("JPEG",settings.getFormat());
        assertEquals("1920x1080",settings.getResolution());
        assertEquals(90,settings.getCompressionQuality());
        assertFalse(settings.isIncludeWatermark());
    }

    @Test
    @DisplayName("Should throw exception,when invalid compression quality is provided!")
    void testInvalidCompressionQualityThrowsException() {
        assertThrows(IllegalArgumentException.class,() ->
                new ExportSettingsBuilder().setCompressionQuality(150));
    }

    @Test
    @DisplayName("Should throw exception for malformed resolution!")
    void testInvalidResolutionThrowsException() {
        assertThrows(IllegalArgumentException.class,() ->
                new ExportSettingsBuilder().setResolution("big"));
    }

    @Test
    @DisplayName("Should throw exception for unsupported format!")
    void testInvalidFormatThrowsException() {
        assertThrows(IllegalArgumentException.class,() ->
                new ExportSettingsBuilder().setFormat("EXE"));
    }
}