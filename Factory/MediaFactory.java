package Factory;

import Model.MediaType;

public class MediaFactory {
    public MediaFile createMedia(MediaType type,String fileName) {
        if (type==null) {
            throw new IllegalArgumentException("Type of the media cannot be null!");
        }

        switch (type) {
            case PHOTO:
                return new PhotoMedia(fileName);
            case VIDEO:
                return new VideoMedia(fileName);
            case PANORAMA:
                return new PanoramaMedia(fileName);
            default:
                throw new IllegalArgumentException("Unknown type of the media:"+type);
        }
    }
}