package Decorator;

public abstract class ImageDecorator implements ImageComponent {
    protected final ImageComponent decoratedImage;

    public ImageDecorator(ImageComponent decoratedImage) {
        this.decoratedImage=decoratedImage;
    }

    @Override
    public String render() {
        return decoratedImage.render();
    }

    @Override
    public double getFileSize() {
        return decoratedImage.getFileSize();
    }
}