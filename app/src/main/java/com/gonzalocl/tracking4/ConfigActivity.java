package com.gonzalocl.tracking4;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.config);

        Button btnStart = findViewById(R.id.button_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText rate = findViewById(R.id.rate);
                if ( rate.getText().toString().equals("") ) {
                    TrackingApp.getTracking().setUpdateRate(Integer.parseInt(getText(R.string.default_rate).toString()));
                } else {
                    TrackingApp.getTracking().setUpdateRate(Integer.parseInt(rate.getText().toString()));
                }

                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(ConfigActivity.this, getText(R.string.err_storage_not_available).toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                TrackingApp.getTracking().setFusedLocationProviderClient(LocationServices.getFusedLocationProviderClient(ConfigActivity.this));
                LocationRequest locationRequest = LocationRequest.create();
                TrackingApp.getTracking().setLocationRequest(locationRequest);
                locationRequest.setInterval(TrackingApp.getTracking().getUpdateRate()*1000);
                locationRequest.setFastestInterval(TrackingApp.getTracking().getUpdateRate()*1000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);

                SettingsClient settingsClient = LocationServices.getSettingsClient(ConfigActivity.this);
                Task<LocationSettingsResponse> settingsResponseTask = settingsClient.checkLocationSettings(settingsBuilder.build());

                settingsResponseTask.addOnSuccessListener(ConfigActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        File tracks_dir;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            tracks_dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getText(R.string.tracks_dir).toString());
                        } else {
                            tracks_dir = new File(Environment.getExternalStoragePublicDirectory(getText(R.string.documents_dir).toString()), getText(R.string.tracks_dir).toString());
                        }
                        if (tracks_dir.mkdirs()) {
                            Toast.makeText(ConfigActivity.this, getText(R.string.tracks_dir_success).toString(), Toast.LENGTH_LONG).show();
                        }

                        Date now = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
                        String fileName = dateFormat.format(now);

                        File csvFile = new File(tracks_dir, fileName + ".csv");
                        File kmlFile = new File(tracks_dir, fileName + ".kml");

                        if (csvFile.exists() || kmlFile.exists() || csvFile.isDirectory() || kmlFile.isDirectory()) {
                            int c = 0;
                            while (csvFile.exists() || kmlFile.exists() || csvFile.isDirectory() || kmlFile.isDirectory()) {
                                c++;
                                csvFile = new File(tracks_dir, fileName + "." + c + ".csv");
                                kmlFile = new File(tracks_dir, fileName + "." + c + ".kml");
                            }
                            fileName = fileName + "." + c;
                        }

                        try {
                            if (!csvFile.createNewFile()) {
                                Toast.makeText(ConfigActivity.this, getText(R.string.err_failed_csv).toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (IOException e) {
                            Toast.makeText(ConfigActivity.this, getText(R.string.err_failed_csv).toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            return;
                        }

                        try {
                            if (!kmlFile.createNewFile()) {
                                Toast.makeText(ConfigActivity.this, getText(R.string.err_failed_kml).toString(), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (IOException e) {
                            Toast.makeText(ConfigActivity.this, getText(R.string.err_failed_kml).toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                            return;
                        }

                        TrackingApp.getTracking().setCsvFile(csvFile);
                        TrackingApp.getTracking().setKmlFile(kmlFile);

                        Intent intent = new Intent(ConfigActivity.this, TrackingInProgress.class);
                        startActivity(intent);
                    }
                });

                settingsResponseTask.addOnFailureListener(ConfigActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {
                            try {
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(ConfigActivity.this, 25);
                            } catch (IntentSender.SendIntentException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

            }
        });

    }
}
