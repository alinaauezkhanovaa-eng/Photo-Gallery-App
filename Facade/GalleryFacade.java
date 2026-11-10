package Facade;

import Builder.ExportSettings;
import Decorator.BasicImage;
import Decorator.GrayscaleFilterDecorator;
import Decorator.ImageComponent;
import Decorator.SepiaFilterDecorator;
import Decorator.WatermarkDecorator;
import Factory.MediaFactory;
import Factory.MediaFile;
import Model.FilterType;
import Model.MediaType;
import Model.Photo;
import Observer.GalleryNotificationService;
import Observer.Observer;
import Strategy.GalleryContext;
import Strategy.SortingStrategy;

import java.util.List;

public class GalleryFacade {
    private static final double DEFAULT_CAPACITY_MB=1000;

    private final MediaFactory mediaFactory;
    private final StorageSubsystem storageSubsystem;
    private final GalleryNotificationService notificationService;
    private final GalleryContext sortingContext;

    public GalleryFacade() {
        this(DEFAULT_CAPACITY_MB);
    }

    public GalleryFacade(double storageCapacityMb) {
        this.mediaFactory=new MediaFactory();
        this.storageSubsystem=new StorageSubsystem(storageCapacityMb);
        this.notificationService=new GalleryNotificationService();
        this.sortingContext=new GalleryContext();
    }

    public void subscribeObserver(Observer observer) {
        notificationService.registerObserver(observer);
    }

    public void unsubscribeObserver(Observer observer) {
        notificationService.removeObserver(observer);
    }

    public MediaFile importMedia(MediaType type, String fileName,double sizeMb) {
        System.out.println("\n---Media Import Workflow via Facade---");
        if (!storageSubsystem.checkDiskSpace(sizeMb)) {
            notificationService.notifyObservers("IMPORT_FAILED","Insufficient storage space for:"+fileName);
            return null;
        }
        MediaFile media=mediaFactory.createMedia(type,fileName);
        storageSubsystem.saveToDisk(fileName,sizeMb);
        notificationService.notifyObservers("MEDIA_IMPORTED","Successfully imported:"+fileName);
        return media;
    }

    public ImageComponent openImage(String fileName,double sizeMb) {
        return new BasicImage(fileName,sizeMb);
    }

    public ImageComponent applyFilter(ImageComponent image, FilterType filter) {
        System.out.println("\n---Applying"+filter+"filter via Facade---");
        ImageComponent result;
        switch (filter) {
            case SEPIA:
                result=new SepiaFilterDecorator(image);
                break;
            case GRAYSCALE:
                result=new GrayscaleFilterDecorator(image);
                break;
            case WATERMARK:
                result=new WatermarkDecorator(image,"(c) PhotoGallery");
                break;
            default:
                throw new IllegalArgumentException("Unknown filter:"+filter);
        }
        notificationService.notifyObservers("FILTER_APPLIED",filter +"filter applied");
        return result;
    }

    public void sortPhotos(List<Photo> photos, SortingStrategy strategy) {
        System.out.println("\n---Sorting Workflow via Facade---");
        sortingContext.setSortingStrategy(strategy);
        sortingContext.executeSort(photos);
        notificationService.notifyObservers("PHOTOS_SORTED",
                "Sorted"+photos.size()+"photos with"+strategy.getClass().getSimpleName());
    }

    public void exportMedia(MediaFile media, ExportSettings settings) {
        System.out.println("\n---Export Workflow via Facade---");
        System.out.println("Exporting"+media.getType()+"with"+settings);
        notificationService.notifyObservers("MEDIA_EXPORTED",
                media.getType()+"exported as"+settings.getFormat()+"to"+settings.getDestinationPath());
    }
}