package com.theironyard.telematics;

public class VehicleInfo {
    private int vin;
    private double odometer;
    private double consumption;
    private double lastOilChange;
    private double engineSize;

    public int getVin() { return vin; }

    public void setVin(int vin) { this.vin = vin; }

    public double getOdometer() { return odometer; }

    public void setOdometer(double odometer) { this.odometer = odometer; }

    public double getConsumption() { return consumption; }

    public void setConsumption(double consumption) { this.consumption = consumption; }

    public double getLastOilChange() { return lastOilChange; }

    public void setLastOilChange(double lastOilChange) { this.lastOilChange = lastOilChange; }

    public double getEngineSize() { return engineSize; }

    public void setEngineSize(double engineSize) { this.engineSize = engineSize; }
}