/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package solarntmonitor;

/**
 *
 * @author royga
 */
public class PVSummarizedData {
    private String date;
    private float energyGerated;
    private float efficiency;
    private float energyExported;
    private float energyUsed;
    private float peakEnergyImport;
    private float offPeakEnergyImport;
    private float shoulderEnergyImport;
    private float highShoulderEnergyImport;
    private String summarizedData;

    public PVSummarizedData(String summarizedData) {
        String [] fields = summarizedData.split(",");
        this.date  = fields[0];
        this.energyGerated  = Float.parseFloat(fields[1]);
        this.efficiency  = Float.parseFloat(fields[2]);
        this.energyExported  = Float.parseFloat(fields[3]);
        this.energyUsed  = Float.parseFloat(fields[4]);
        this.peakEnergyImport  = Float.parseFloat(fields[5]);
        this.offPeakEnergyImport  = Float.parseFloat(fields[6]);
        this.shoulderEnergyImport  = Float.parseFloat(fields[7]);
        this.highShoulderEnergyImport  = Float.parseFloat(fields[8]);
        this.summarizedData = summarizedData;
    }

    public String toString(){
        return this.summarizedData;
    }
}
