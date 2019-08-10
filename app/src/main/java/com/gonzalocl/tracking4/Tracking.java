package com.gonzalocl.tracking4;

public class Tracking {

    private static final Tracking tracking = new Tracking();

    private int update_rate;

    private Tracking() {

    }

    public static Tracking getTracking() {
        return tracking;
    }

    public int getUpdate_rate() {
        return update_rate;
    }

    public void setUpdate_rate(int update_rate) {
        this.update_rate = update_rate;
    }
}
