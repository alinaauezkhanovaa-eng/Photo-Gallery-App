package Factory;

public class PhotoMedia implements MediaFile {
    private final String fileName;
    public PhotoMedia(String fileName) {
        this.fileName=fileName;
    }

    @Override
    public void display() {
        System.out.println("Reflection of the photo:"+fileName);
    }

    @Override
    public String getType() {
        return "PHOTO";
    }
}