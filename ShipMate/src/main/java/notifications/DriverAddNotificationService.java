package notifications;

import model.Driver;

public class DriverAppNotificationService implements DriverNotificationService {
    @Override
    public void notifyDriver(Driver driver, String message) {
        System.out.printf("🔔  [In-App] To %s (#%d): %s%n",
                driver.getName(), driver.getId(), message);
        // TODO push via Firebase / Socket / REST
    }
}
