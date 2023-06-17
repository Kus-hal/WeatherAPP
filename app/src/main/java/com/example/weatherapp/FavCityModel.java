package com.example.weatherapp;

public class FavCityModel {
    int imgcardBG;
    String city, temperature, condition, windSpeed, imgCondition;

    public int getImgcardBG() {
        return imgcardBG;
    }

    public void setImgcardBG(int imgcardBG) {
        this.imgcardBG = imgcardBG;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getImgCondition() {
        return imgCondition;
    }

    public void setImgCondition(String imgCondition) {
        this.imgCondition = imgCondition;
    }

    public FavCityModel(int imgcardBG, String city, String temperature, String condition, String windSpeed, String imgCondition) {
        this.imgcardBG = imgcardBG;
        this.city = city;
        this.temperature = temperature;
        this.condition = condition;
        this.windSpeed = windSpeed;
        this.imgCondition = imgCondition;
    }
}
