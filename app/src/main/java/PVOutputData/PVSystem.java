package PVOutputData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by royga on 5/11/2016.
 */
public class PVSystem {

    private String name;
    private long size;
    private int postCode;
    private String orientation;
    private int outputs;
    private String lastOutput;
    private int systemID;
    private String panel;
    private String inverter;
    private float distance;
    private float latitude;
    private float longitude;
    private HashMap dailyData;
    private HashMap monthlyData;
    private HashMap yearlyData;
    private String systemData;

    public PVSystem(String systemData, PVAccountSettings mySettings) {
        String[] fields = systemData.split(",");
        this.name = fields[0];
        this.size = Long.parseLong(fields[1]);
        this.postCode = Integer.parseInt(fields[2]);
        this.orientation = fields[3];
        this.outputs = Integer.parseInt(fields[4]);
        this.lastOutput = fields[5];
        this.systemID = Integer.parseInt(fields[6]);
        this.panel = fields[7];
        this.inverter = fields[8];
        this.distance = Float.parseFloat(fields[9]);
        this.latitude = Float.parseFloat(fields[10]);
        this.longitude = Float.parseFloat(fields[11]);
        dailyData = new HashMap();
        monthlyData = new HashMap();
        yearlyData = new HashMap();
        this.systemData = systemData;
    }

    public void retrieveDailyData(PVAccountSettings mySettings) {
        String url = "http://pvoutput.org/service/r2/getoutput.jsp";
        url = url.concat("?sid=").concat(String.valueOf(mySettings.getSystemID()));
        url = url.concat("&key=").concat(mySettings.getKey());
        url = url.concat("&sid1=").concat(String.valueOf(this.systemID));
        System.out.println(url);
        try {
            URL pvURL = new URL(url);
            URLConnection urlConnection = pvURL.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(pvURL.openStream()));

            String inputLine;

            dailyData.clear();
            while ((inputLine = bufferedReader.readLine()) != null) {
                String[] lines = inputLine.split(";");
                for (int index = 0; index < lines.length; index++) {
                    dailyData.put((lines[index].split(","))[0], new PVDailyData(lines[index]));
                    System.out.println(lines[index]);
                }
            }
            bufferedReader.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retrieveMonthlyData(PVAccountSettings mySettings) {
        String url = "http://pvoutput.org/service/r2/getoutput.jsp";
        url = url.concat("?sid=").concat(String.valueOf(mySettings.getSystemID()));
        url = url.concat("&key=").concat(mySettings.getKey());
        url = url.concat("&sid1=").concat(String.valueOf(this.systemID));
        url = url.concat("&a=m");
        System.out.println(url);
        try {
            URL pvURL = new URL(url);
            URLConnection urlConnection = pvURL.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(pvURL.openStream()));

            String inputLine;

            monthlyData.clear();
            while ((inputLine = bufferedReader.readLine()) != null) {
                String[] lines = inputLine.split(";");
                for (int index = 0; index < lines.length; index++) {
                    monthlyData.put((lines[index].split(","))[0], new PVSummarizedData(lines[index]));
                    System.out.println(lines[index]);
                }
            }
            bufferedReader.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void retriveYearlyData(PVAccountSettings mySettings) {
        String url = "http://pvoutput.org/service/r2/getoutput.jsp";
        url = url.concat("?sid=").concat(String.valueOf(mySettings.getSystemID()));
        url = url.concat("&key=").concat(mySettings.getKey());
        url = url.concat("&sid1=").concat(String.valueOf(this.systemID));
        url = url.concat("&a=y");
        System.out.println(url);
        try {
            URL pvURL = new URL(url);
            URLConnection urlConnection = pvURL.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(pvURL.openStream()));

            String inputLine;

            yearlyData.clear();
            while ((inputLine = bufferedReader.readLine()) != null) {
                String[] lines = inputLine.split(";");
                for (int index = 0; index < lines.length; index++) {
                    yearlyData.put((lines[index].split(","))[0], new PVSummarizedData(lines[index]));
                    System.out.println(lines[index]);
                }
            }
            bufferedReader.close();

        } catch (MalformedURLException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            Logger.getLogger(PVSystemsCollection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return this.systemData;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public int getPostCode() {
        return postCode;
    }

    public String getOrientation() {
        return orientation;
    }

    public int getOutputs() {
        return outputs;
    }

    public String getLastOutput() {
        return lastOutput;
    }

    public int getSystemID() {
        return systemID;
    }

    public String getPanel() {
        return panel;
    }

    public String getInverter() {
        return inverter;
    }

    public float getDistance() {
        return distance;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public HashMap getDailyData() {
        return dailyData;
    }

    public HashMap getMonthlyData() {
        return monthlyData;
    }

    public HashMap getYearlyData() {
        return yearlyData;
    }

    public String getSystemData() {
        return systemData;
    }

}
