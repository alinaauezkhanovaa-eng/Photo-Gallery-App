package Decorator;

public class BasicImage implements ImageComponent {
    private final String fileName;
    private final double sizeMb;

    public BasicImage(String fileName,double sizeMb) {
        this.fileName=fileName;
        this.sizeMb=sizeMb;
    }

    @Override
    public String render() {
        return "Reflection of the original photo:"+fileName;
    }

    @Override
    public double getFileSize() {
        return sizeMb;
    }
}