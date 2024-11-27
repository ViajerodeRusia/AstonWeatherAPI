package com.weather.jakarta.hello.entity;

public class WeatherRecord {
    private int id;
    private int cityId;
    private double temperature;
    private int humidity;
    private int pressure;
    private String description;
    private String recordedAt; // You might want to use a Date or LocalDateTime type

    // Constructors
    public WeatherRecord() {
    }

    public WeatherRecord(int cityId, double temperature, int humidity, int pressure, String description) {
        this.cityId = cityId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }
}

