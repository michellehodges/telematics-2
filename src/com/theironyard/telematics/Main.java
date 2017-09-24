package com.theironyard.telematics;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // make a new VehicleInfo object
        VehicleInfo vh = new VehicleInfo();
        vh.setVin(12345);
        vh.setConsumption(45);
        vh.setEngineSize(3);
        vh.setLastOilChange(30);
        vh.setOdometer(50000);
        // for each field in VehicleInfo, use the scanner to read in values. and populate the object.

        TelematicsService.report(vh);
    }
}