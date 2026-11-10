package Observer;

import java.util.ArrayList;
import java.util.List;

public class GalleryNotificationService implements Subject {
    private final List<Observer> observers=new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        if (observer!=null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event,String message) {
        for (Observer observer:observers) {
            observer.update(event,message);
        }
    }
}