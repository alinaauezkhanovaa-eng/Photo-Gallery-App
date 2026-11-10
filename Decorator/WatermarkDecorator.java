package Decorator;

public class WatermarkDecorator extends ImageDecorator {
    private final String text;

    public WatermarkDecorator(ImageComponent decoratedImage,String text) {
        super(decoratedImage);
        this.text=text;
    }

    @Override
    public String render() {
        return decoratedImage.render()+"+[Watermark:"+text+"]";
    }

    @Override
    public double getFileSize() {
        return decoratedImage.getFileSize()+0.05;
    }
}