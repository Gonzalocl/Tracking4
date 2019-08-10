package com.gonzalocl.tracking4;

import java.io.File;

public class TrackingApp {

    private static final TrackingApp tracking = new TrackingApp();

    private int updateRate;

    private File csvFile;
    private File kmlFile;

    private TrackingApp() {

    }

    public static TrackingApp getTracking() {
        return tracking;
    }

    public int getUpdateRate() {
        return updateRate;
    }

    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
    }

    public File getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(File csvFile) {
        this.csvFile = csvFile;
    }

    public File getKmlFile() {
        return kmlFile;
    }

    public void setKmlFile(File kmlFile) {
        this.kmlFile = kmlFile;
    }
}
