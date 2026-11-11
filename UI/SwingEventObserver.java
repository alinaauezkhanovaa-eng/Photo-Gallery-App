package UI;

import Observer.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Observer pattern in the GUI: the window subscribes to the facade and every gallery event
 * is shown in the live event feed and in the status line.
 */
public class SwingEventObserver implements Observer {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DefaultTableModel model;
    private final Consumer<String[]> onEvent;

    public SwingEventObserver(DefaultTableModel model, Consumer<String[]> onEvent) {
        this.model = model;
        this.onEvent = onEvent;
    }

    @Override
    public void update(String event, String message) {
        Runnable r = () -> {
            model.insertRow(0, new Object[]{LocalTime.now().format(TIME), event, message});
            onEvent.accept(new String[]{event, message});
        };
        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
        } else {
            SwingUtilities.invokeLater(r);
        }
    }
}
