package com.theironyard.telematics;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelematicsService {
    static void report(VehicleInfo vehicleInfo) {
        // Write the VehicleInfo to a file as json using the VIN as the name of the file and
        // a "json" extension (e.g. "234235435.json"). The file will overwrite any existing files for the same VIN.
        File f = new File(vehicleInfo.getVin() + ".json");
        try {
            // write to a new file >> target File object to write on is 'f'
            FileWriter fw = new FileWriter(f);

            // convert our vehicleInfo to json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(vehicleInfo);

            fw.write(json);
            fw.close();

            // build an arraylist of vehicle info objects
            // we'll get our vehicle info info from all the json files in the current directory
            ArrayList<VehicleInfo> vehicleInfos = new ArrayList<VehicleInfo>();

            // read from all json files
            File file = new File(".");
            for (File currentFile : file.listFiles()) {
                if (currentFile.getName().endsWith(".json")) {
                    // get the json from the file
                    String fileJson = getDataFromFile(currentFile);

                    // convert that json to a VehicleInfo object
                    ObjectMapper om = new ObjectMapper();
                    VehicleInfo vi = om.readValue(fileJson, VehicleInfo.class);

                    // put vi inside of vehicleInfos arrayList.
                    vehicleInfos.add(vi);
                }
            }

            // when we get to this point, we should have a populated arraylist of VehicleInfos

            // vehicleInfos arraylist to HTML
            String htmlSource = "/Users/mischy/Documents/the-iron-yard" +
                    "/JavaProjects/WeeklyProjects/week-9-edited/src/com/theironyard/telematics/index.html";

            File htmlFile = new File(htmlSource);
            String htmlString = getDataFromFile(htmlFile);

            htmlString = htmlString.replace("id=\"$odometerAvg\">[0-9]+.[0-9]{2}</td>", String.format("id=\"$odometerAvg\">%.2f</td>", getAverageOdometer(vehicleInfos)));
            htmlString = htmlString.replace("id=\"$consumptionAvg\">[0-9]+.[0-9]{2}</td>", String.format("id=\"$consumptionAvg\">%.2f</td>", getAverageConsumption(vehicleInfos)));
            htmlString = htmlString.replace("id=\"$lastchangeAvg\">[0-9]+.[0-9]{2}</td>", String.format("id=\"$lastchangeAvg\">%.2f</td>", getAverageLastOilChange(vehicleInfos)));
            htmlString = htmlString.replace("id=\"$enginesizeAvg\">[0-9]+.[0-9]{2}</td>", String.format("id=\"$enginesizeAvg\">%.2f</td>", getAverageEngineSize(vehicleInfos)));

            //Ben - Mike did this part for me (in case youre wondering)
            Pattern pt = Pattern.compile("id=\"body\">[\\S\\s]*</div>");
            Matcher match = pt.matcher(htmlString);
            htmlString = match.replaceFirst(String.format("id=\"body\">%s</div>", createVehicleTable(vehicleInfos)));

            FileWriter htmlFilewriter = new FileWriter(htmlFile);
            htmlFilewriter.write(htmlString);
            htmlFilewriter.close();
            System.out.println(htmlString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDataFromFile(File f) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(f);

        String results = "";

        while (fileScanner.hasNext()) {
            results += fileScanner.nextLine() + "\n";
        }

        return results;
    }

    private static double getAverageOdometer( ArrayList<VehicleInfo> vehicleInfos) {
        int totalVehicles = vehicleInfos.size();
        double totalOdometer = 0;
        double average;

        for (VehicleInfo vehicle : vehicleInfos) {
           totalOdometer += vehicle.getOdometer();

        }
        average = totalOdometer / totalVehicles;

        return average;
    }

    private static double getAverageConsumption( ArrayList<VehicleInfo> vehicleInfos) {
        int totalVehicles = vehicleInfos.size();
        double totalConsumption = 0;
        double average;

        for (VehicleInfo vehicle : vehicleInfos) {
            totalConsumption += vehicle.getConsumption();

        }
        average = totalConsumption / totalVehicles;

        return average;
    }

    private static double getAverageLastOilChange( ArrayList<VehicleInfo> vehicleInfos) {
        int totalVehicles = vehicleInfos.size();
        double totalOilChange = 0;
        double average;

        for (VehicleInfo vehicle : vehicleInfos) {
            totalOilChange += vehicle.getLastOilChange();

        }
        average = totalOilChange / totalVehicles;

        return average;
    }

    private static double getAverageEngineSize( ArrayList<VehicleInfo> vehicleInfos) {
        int totalVehicles = vehicleInfos.size();
        double totalEngineSize = 0;
        double average;

        for (VehicleInfo vehicle : vehicleInfos) {
            totalEngineSize += vehicle.getEngineSize();

        }
        average = totalEngineSize / totalVehicles;

        return average;
    }

    private static String createVehicleTable(ArrayList<VehicleInfo> vehicleInfos) {
        String tableString ="";

        String insertion =  "<tr>\n" +
                            "    <td align=\"center\">%d</td>\n" + //vin
                            "    <td align=\"center\">%.2f</td>\n" + //odometer
                            "    <td align=\"center\">%.2f</td>\n" + //consumption
                            "    <td align=\"center\">%.2f</td align=\"center\">\n" + //lastchange
                            "    <td align=\"center\">%.2f</td>\n" + //enginesize
                            "</tr>\n";

        for (VehicleInfo vehicle : vehicleInfos) {
            tableString += String.format(insertion, vehicle.getVin(), vehicle.getOdometer(), vehicle.getConsumption(), vehicle.getLastOilChange(), vehicle.getEngineSize());
        }

        return tableString;
    }
}