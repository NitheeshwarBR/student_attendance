package com.nitheeshwar.assignment2;


public class Event {
    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private double latitude;
    private double longitude;

    // Default constructor
    public Event() {
    }

    // Constructor with parameters
    public Event(int id, String name, String startDate, String endDate, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Constructor without id (useful for insertion, as the id will be auto-generated)
    public Event(String name, String startDate, String endDate, double latitude, double longitude) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
