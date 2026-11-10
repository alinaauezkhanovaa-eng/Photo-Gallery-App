package Observer;

public class UINotifier implements Observer {
    private final String componentName;
    public UINotifier(String componentName) {
        this.componentName=componentName;
    }

    @Override
    public void update(String event,String message) {
        System.out.println("["+componentName+"UI Alert] Event:"+event+" | Message:"+message);
    }
}