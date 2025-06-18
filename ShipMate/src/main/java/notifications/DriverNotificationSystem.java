package notifications;

import model.Driver;
import java.util.ArrayList;
import java.util.List;

public class DriverNotificationSystem {
    private List<DriverNotificationService> notificationServices;

    public DriverNotificationSystem() {
        this.notificationServices = new ArrayList<>();
    }

    public void addNotificationService(DriverNotificationService service) {
        notificationServices.add(service);
    }

    public void sendNotification(Driver driver, String message) {
        for (DriverNotificationService service : notificationServices) {
            service.notifyDriver(driver, message);
        }
    }

    public void clearServices() {
    }
}
