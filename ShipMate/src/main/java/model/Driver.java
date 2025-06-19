package model;

public class Driver {
    private int id;
    private String name;
    private String phone;
    private String schedule;

    public Driver(int id, String name, String phone, String schedule) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.schedule = schedule;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
}