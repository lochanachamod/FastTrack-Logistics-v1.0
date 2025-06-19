package model;

import java.time.LocalDate;

public class Shipment {
    private int id;
    private String sender;
    private String receiver;
    private String contents;
    private String status;
    private String currentLocation;
    private LocalDate deliveryDate;
    private String deliverySlot;
    private int driverId;

    public Shipment(int id, String sender, String receiver, String contents, String status,
                    String currentLocation, LocalDate deliveryDate, String deliverySlot, int driverId) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
        this.status = status;
        this.currentLocation = currentLocation;
        this.deliveryDate = deliveryDate;
        this.deliverySlot = deliverySlot;
        this.driverId = driverId;
    }

    // Getters & Setters
    public int getId() { return id; }
    public String getSender() { return sender; }
    public String getReceiver() { return receiver; }
    public String getContents() { return contents; }
    public String getStatus() { return status; }
    public String getCurrentLocation() { return currentLocation; }
    public LocalDate getDeliveryDate() { return deliveryDate; }
    public String getDeliverySlot() { return deliverySlot; }
    public int getDriverId() { return driverId; }

    public void setId(int id) { this.id = id; }
    public void setSender(String sender) { this.sender = sender; }
    public void setReceiver(String receiver) { this.receiver = receiver; }
    public void setContents(String contents) { this.contents = contents; }
    public void setStatus(String status) { this.status = status; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }
    public void setDeliverySlot(String deliverySlot) { this.deliverySlot = deliverySlot; }
    public void setDriverId(int driverId) { this.driverId = driverId; }
}
