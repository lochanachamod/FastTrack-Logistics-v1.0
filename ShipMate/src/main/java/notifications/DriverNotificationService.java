package notifications;

import model.Driver;

public interface DriverNotificationService {
    void notifyDriver(Driver driver, String message);
}
