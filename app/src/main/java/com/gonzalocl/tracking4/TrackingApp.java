package com.gonzalocl.tracking4;

public class TrackingApp {

    private static final TrackingApp tracking = new TrackingApp();

    private int update_rate;

    private TrackingApp() {

    }

    public static TrackingApp getTracking() {
        return tracking;
    }

    public int getUpdate_rate() {
        return update_rate;
    }

    public void setUpdate_rate(int update_rate) {
        this.update_rate = update_rate;
    }
}
