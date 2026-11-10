package Factory;

public class VideoMedia implements MediaFile {
    private final String fileName;
    public VideoMedia(String fileName) {
        this.fileName=fileName;
    }

    @Override
    public void display() {
        System.out.println("Video file playback:"+fileName);
    }

    @Override
    public String getType() {
        return "VIDEO";
    }
}