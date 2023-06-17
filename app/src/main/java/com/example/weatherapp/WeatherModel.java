package com.example.weatherapp;

public class WeatherModel {

    private double temp;
    private String icon;
    private double wSpeed;
    private String time;
    private String pod;

    public WeatherModel(double temp, String icon, double wSpeed, String time, String pod) {
        this.temp = temp;
        this.icon = icon;
        this.wSpeed = wSpeed;
        this.time = time;
        this.pod = pod;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getwSpeed() {
        return wSpeed;
    }

    public void setwSpeed(double wSpeed) {
        this.wSpeed = wSpeed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
