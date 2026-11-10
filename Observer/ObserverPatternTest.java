package Observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ObserverPatternTest {
    static class RecordingObserver implements Observer {
        final List<String> events=new ArrayList<>();

        @Override
        public void update(String event,String message) {
            events.add(event);
        }
    }

    private GalleryNotificationService service;
    private RecordingObserver first;
    private RecordingObserver second;

    @BeforeEach
    void setUp() {
        service=new GalleryNotificationService();
        first=new RecordingObserver();
        second=new RecordingObserver();
    }

    @Test
    @DisplayName("Should notify every registered observer!")
    void testAllObserversNotified() {
        service.registerObserver(first);
        service.registerObserver(second);
        service.notifyObservers("MEDIA_IMPORTED","a.jpg");
        assertEquals(List.of("MEDIA_IMPORTED"),first.events);
        assertEquals(List.of("MEDIA_IMPORTED"),second.events);
    }

    @Test
    @DisplayName("Should not notify observer after it was removed!")
    void testRemovedObserverNotNotified() {
        service.registerObserver(first);
        service.registerObserver(second);
        service.removeObserver(first);
        service.notifyObservers("FILTER_APPLIED","a.jpg");
        assertTrue(first.events.isEmpty());
        assertEquals(1,second.events.size());
    }

    @Test
    @DisplayName("Should ignore duplicate registration and null observers!")
    void testDuplicateAndNullRegistration() {
        service.registerObserver(first);
        service.registerObserver(first);
        service.registerObserver(null);
        service.notifyObservers("EVENT","msg");
        assertEquals(1,first.events.size());
    }
}