package Model;

import java.time.LocalDate;

public class Photo {
    private String title;
    private LocalDate date;
    private int rating;

    public Photo(String title,LocalDate date,int rating) {
        this.title=title;
        this.date=date;
        this.rating=rating;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Photo{title='"+title+"',date="+date+",rating="+rating+"}";
    }
}