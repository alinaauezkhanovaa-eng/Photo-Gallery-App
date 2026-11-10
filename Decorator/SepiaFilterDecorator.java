package Decorator;

public class SepiaFilterDecorator extends ImageDecorator {
    public SepiaFilterDecorator(ImageComponent decoratedImage) {
        super(decoratedImage);
    }

    @Override
    public String render() {
        return decoratedImage.render()+"+[Used filter:Sepia]";
    }

    @Override
    public double getFileSize() {
        return decoratedImage.getFileSize()+0.2;
    }
}