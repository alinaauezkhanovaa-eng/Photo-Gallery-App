package Factory;

public class PanoramaMedia implements MediaFile {
    private final String fileName;
    public PanoramaMedia(String fileName) {
        this.fileName=fileName;
    }

    @Override
    public void display() {
        System.out.println("Loading 360° panoramic view for the file:"+fileName);
    }

    @Override
    public String getType() {
        return "PANORAMA";
    }
}