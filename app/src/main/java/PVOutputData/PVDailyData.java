/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solarntmonitor;

import java.util.Date;

/**
 *
 * @author royga
 */
public class PVDailyData {

    private String date;
    private float energyGenerated;
    private float efficiency;
    private float energyExported;
    private float energyUsed;
    private float peakPower;
    private String peakTime;
    private String condition;
    private float minimumTemperature;
    private float maximumTemperature;
    private float peakEnergyImport;
    private float offPeakEnergyImport;
    private float shoulderEnergyImport;
    private float highShoulderEnergyImport;
    private String dailyData;

    public PVDailyData(String dailyData) {
        String [] fields = dailyData.split(",");
        this.date  = fields[0];
        this.energyGenerated  = Float.parseFloat(fields[1]);
        this.efficiency  = Float.parseFloat(fields[2]);
        this.energyExported  = Float.parseFloat(fields[3]);
        this.energyUsed  = Float.parseFloat(fields[4]);
        this.peakPower  = Float.parseFloat(fields[5]);
        this.peakTime  = fields[6];
        this.condition  = fields[7];
        this.minimumTemperature  = Float.parseFloat(fields[8]);
        this.maximumTemperature  = Float.parseFloat(fields[9]);
        this.peakEnergyImport  = Float.parseFloat(fields[10]);
        this.offPeakEnergyImport  = Float.parseFloat(fields[11]);
        this.shoulderEnergyImport  = Float.parseFloat(fields[12]);
        this.highShoulderEnergyImport  = Float.parseFloat(fields[13]);
    
        this.dailyData = dailyData;
    }

    public float getMaximumTemperature() {
        return maximumTemperature;
    }

    public float getPeakEnergyImport() {
        return peakEnergyImport;
    }

    public float getOffPeakEnergyImport() {
        return offPeakEnergyImport;
    }

    public float getShoulderEnergyImport() {
        return shoulderEnergyImport;
    }

    public float getHighShoulderEnergyImport() {
        return highShoulderEnergyImport;
    }

    @Override
    public String toString() {
        return dailyData;
    }

    public String getDate() {
        return date;
    }

    public float getEnergyGenerated() {
        return energyGenerated;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getEnergyExported() {
        return energyExported;
    }

    public float getEnergyUsed() {
        return energyUsed;
    }

    public float getPeakPower() {
        return peakPower;
    }

    public String getPeakTime() {
        return peakTime;
    }

    public String getCondition() {
        return condition;
    }

    public float getMinimumTemperature() {
        return minimumTemperature;
    }
    
    
}
