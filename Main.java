import Factory.MediaFile;
import Model.MediaType;
import Model.Photo;
import Model.FilterType;
import Builder.ExportSettings;
import Builder.ExportSettingsBuilder;
import Decorator.ImageComponent;
import Facade.GalleryFacade;
import Observer.AuditLogger;
import Observer.UINotifier;
import Strategy.SortByTitleStrategy;
import Strategy.SortByDateStrategy;
import Strategy.SortByRatingStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("===PHOTO GALLERY APPLICATION INITIALIZATION===");

        GalleryFacade gallery=new GalleryFacade(100);
        gallery.subscribeObserver(new UINotifier("MainDashboard"));
        gallery.subscribeObserver(new AuditLogger());

        MediaFile photo=gallery.importMedia(MediaType.PHOTO,"vacation_sunset.jpg",4.5);
        MediaFile video=gallery.importMedia(MediaType.VIDEO,"family_birthday.mp4",60);
        MediaFile panorama=gallery.importMedia(MediaType.PANORAMA,"mountains_360.jpg",12);
        for (MediaFile media:new MediaFile[]{photo,video,panorama}) {
            if (media!=null) {
                media.display();
            }
        }

        System.out.println("\n---Demonstrating a failed import(not enough space)---");
        MediaFile tooBig=gallery.importMedia(MediaType.VIDEO,"wedding_4k.mp4",80);
        System.out.println("Import result:"+(tooBig==null?"rejected":"accepted"));

        System.out.println("\n---Demonstrating stacked Decorators---");
        ImageComponent image=gallery.openImage("vacation_sunset.jpg",4.5);
        System.out.println("Original:"+image.render()+" | "+image.getFileSize()+" MB");
        image=gallery.applyFilter(image,FilterType.SEPIA);
        image=gallery.applyFilter(image,FilterType.WATERMARK);
        System.out.println("Result:"+image.render());
        System.out.printf("File size:%.2f MB%n",image.getFileSize());

        System.out.println("\n---Demonstrating Sorting Strategies---");
        List<Photo> photos=new ArrayList<>();
        photos.add(new Photo("Sunset",LocalDate.of(2026,6,15),5));
        photos.add(new Photo("Family",LocalDate.of(2026,1,10),4));
        photos.add(new Photo("Mountains",LocalDate.of(2026,10,20),5));
        System.out.println("Before sorting:"+photos);

        gallery.sortPhotos(photos,new SortByDateStrategy());
        System.out.println("After Date Sort:"+photos);
        gallery.sortPhotos(photos,new SortByRatingStrategy());
        System.out.println("After Rating Sort:"+photos);
        gallery.sortPhotos(photos,new SortByTitleStrategy());
        System.out.println("After Title Sort:"+photos);

        System.out.println("\n---Demonstrating Export Builder Pattern---");
        ExportSettings exportSettings=new ExportSettingsBuilder()
                .setResolution("3840x2160")
                .setFormat("PNG")
                .setIncludeWatermark(true)
                .setCompressionQuality(95)
                .setDestinationPath("/secure/gallery/exports")
                .build();
        gallery.exportMedia(photo,exportSettings);

        System.out.println("\n===ALL 6 DESIGN PATTERNS DEMONSTRATED SUCCESSFULLY===");
    }
}
