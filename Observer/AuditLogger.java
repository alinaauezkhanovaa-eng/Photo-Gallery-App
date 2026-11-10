package Observer;

public class AuditLogger implements Observer {
    @Override
    public void update(String event,String message) {
        System.out.println("[AUDIT LOG] Event logged:"+event+"-Details:"+message);
    }
}