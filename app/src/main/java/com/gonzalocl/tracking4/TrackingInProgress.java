package com.gonzalocl.tracking4;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.traking_in_progress);

        trackingApp = TrackingApp.getTracking();
        csvFile = trackingApp.getCsvFile();
        kmlFile = trackingApp.getKmlFile();
        csvWriter = new CSVWriter(csvFile);
        kmlWriter = new KMLWriter(kmlFile);


        final LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // TODO
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
}
