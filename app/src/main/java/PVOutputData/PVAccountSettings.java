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
public class PVAccountSettings {
    private int systemID;
    private String key;
    private int postCode;

    public PVAccountSettings(int systemID, String key) {
        this.systemID = systemID;
        this.key = key;
    }

    public PVAccountSettings(int systemID, String key, int postCode) {
        this.systemID = systemID;
        this.key = key;
        this.postCode = postCode;
    }
    
    public int getSystemID() {
        return systemID;
    }

    public void setSystemID(int systemID) {
        this.systemID = systemID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }
    
    
}
