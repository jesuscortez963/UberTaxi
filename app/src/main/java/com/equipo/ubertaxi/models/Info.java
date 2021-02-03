package com.equipo.ubertaxi.models;

public class Info {

    double km;
    double min;

    public Info() {

    }

    public Info(double  km, double min) {
        this.km = km;
        this.min = min;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }
}
