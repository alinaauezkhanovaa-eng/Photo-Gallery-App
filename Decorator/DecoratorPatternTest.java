package Decorator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DecoratorPatternTest {

    @Test
    @DisplayName("Should properly decorate Basic Image with Sepia filter!")
    void testSepiaFilterDecorator() {
        ImageComponent baseImage=new BasicImage("landscape.jpg",3.0);
        ImageComponent sepiaImage=new SepiaFilterDecorator(baseImage);

        assertTrue(sepiaImage.render().contains("[Used filter:Sepia]"));
        assertEquals(3.2,sepiaImage.getFileSize(),0.001);
    }

    @Test
    @DisplayName("Should apply Grayscale filter!")
    void testGrayscaleFilterDecorator() {
        ImageComponent image=new GrayscaleFilterDecorator(new BasicImage("a.jpg",3.0));
        assertTrue(image.render().contains("[Used filter:Grayscale]"));
        assertEquals(2.9,image.getFileSize(),0.001);
    }

    @Test
    @DisplayName("Should stack several decorators on one image!")
    void testStackedDecorators() {
        ImageComponent image=new WatermarkDecorator(
                new SepiaFilterDecorator(new BasicImage("a.jpg",3.0)),"(c) Me");
        String rendered=image.render();
        assertTrue(rendered.contains("Sepia"));
        assertTrue(rendered.contains("Watermark:(c) Me"));
        assertEquals(3.25,image.getFileSize(),0.001);
    }
}