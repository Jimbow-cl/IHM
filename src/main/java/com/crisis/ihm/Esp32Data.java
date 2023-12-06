package com.crisis.ihm;

public class Esp32Data {
    private long temperature;

    private long humidity;

    public Esp32Data(long temperature, long humidity) {

        this.temperature = temperature;
        this.humidity = humidity;
    }

    public long getHumidity() {
        return humidity;
    }

    public long getTemperature() {
        return temperature;
    }

}
