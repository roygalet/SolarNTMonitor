/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PVOutputData;

/**
 *
 * @author royga
 */
public class PVSummarizedData {

    private String date;
    private int outputs;
    private float energyGenerated;
    private float efficiency;
    private float energyExported;
    private float energyUsed;
    private float peakEnergyImport;
    private float offPeakEnergyImport;
    private float shoulderEnergyImport;
    private float highShoulderEnergyImport;
    private String summarizedData;

    public PVSummarizedData(String summarizedData) {
        String[] fields = summarizedData.split(",");
        this.date = fields[0];
        this.outputs = Integer.parseInt(fields[1]);
        this.energyGenerated = Float.parseFloat(fields[2]);
        this.efficiency = Float.parseFloat(fields[3]);
        this.energyExported = Float.parseFloat(fields[4]);
        this.energyUsed = Float.parseFloat(fields[5]);
        this.peakEnergyImport = Float.parseFloat(fields[6]);
        this.offPeakEnergyImport = Float.parseFloat(fields[7]);
        this.shoulderEnergyImport = Float.parseFloat(fields[8]);
        this.highShoulderEnergyImport = Float.parseFloat(fields[9]);
        this.summarizedData = summarizedData;
    }

    public String toString() {
        return this.summarizedData;
    }

    public String getDate() {
        return date;
    }

    public int getOutputs() {
        return outputs;
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

    public String getSummarizedData() {
        return summarizedData;
    }

}
