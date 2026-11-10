package Decorator;

public class GrayscaleFilterDecorator extends ImageDecorator {
    public GrayscaleFilterDecorator(ImageComponent decoratedImage) {
        super(decoratedImage);
    }

    @Override
    public String render() {
        return decoratedImage.render()+"+[Used filter:Grayscale]";
    }

    @Override
    public double getFileSize() {
        return decoratedImage.getFileSize()-0.1;
    }
}