package com.gonzalocl.tracking4;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;

import java.io.File;

public class TrackingInProgress extends Activity {

    private TrackingApp trackingApp;
    private File csvFile;
    private File kmlFile;
    private CSVWriter csvWriter;
    private KMLWriter kmlWriter;

    private boolean updateUI;

    private float totalDistance = 0;
    private Location lastLocation = null;
    private float speedSum = 0;
    private int recordCount = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.traking_in_progress);

        trackingApp = TrackingApp.getTracking();
        csvFile = trackingApp.getCsvFile();
        kmlFile = trackingApp.getKmlFile();
        csvWriter = new CSVWriter(csvFile);
        kmlWriter = new KMLWriter(kmlFile);

        updateUI = true;

        final TextView displayDistance = findViewById(R.id.displayDistance);
        final TextView displayTime = findViewById(R.id.displayTime);
        final TextView displayAverageSpeed = findViewById(R.id.displayAverageSpeed);
        final TextView displaySpeed = findViewById(R.id.displaySpeed);

        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    kmlWriter.newrec(location.getLatitude(), location.getLongitude());
                    csvWriter.newrec(
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getAltitude(),
                            location.getAccuracy(),
                            location.getTime()
                    );

                    if (lastLocation != null) {
                        totalDistance += location.distanceTo(lastLocation)/1000;
                    }
                    recordCount++;
                    speedSum += location.getSpeed()*3.6;

                    if (updateUI) {
                        displayDistance.setText(String.format("%.2f km", totalDistance));
                        displaySpeed.setText(String.format("%d km/h", (int) (location.getSpeed()*3.6)));
                        displayAverageSpeed.setText(String.format("%.2f km/h", speedSum/recordCount));
                    }

                    lastLocation = location;
                }
            }
        };

        trackingApp.getFusedLocationProviderClient().requestLocationUpdates(
                trackingApp.getLocationRequest(),
                locationCallback,
                null
        );

        Button btnStop = findViewById(R.id.button_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trackingApp.getFusedLocationProviderClient().removeLocationUpdates(locationCallback);

                csvWriter.fin();
                kmlWriter.fin();

                Intent intent = new Intent(v.getContext(), ConfigActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI = true;
        // TODO update ui now
        // TODO update location now
        // TODO unset update location in batch
        // TODO set update timer
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateUI = false;
        // TODO set update location in batch
        // TODO unset update timer
    }
}
