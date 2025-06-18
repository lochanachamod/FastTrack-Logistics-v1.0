package notifications;

import model.Driver;

public class DriverSMSService implements DriverNotificationService {
    @Override
    public void notifyDriver(Driver driver, String message) {
        System.out.printf("📲  [SMS] To %s (#%d): %s%n",
                driver.getName(), driver.getId(), message);
        // TODO wire Twilio / GSM modem here
    }
}
