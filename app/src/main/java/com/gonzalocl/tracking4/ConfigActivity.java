package com.gonzalocl.tracking4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.config);

        Button btn_start = findViewById(R.id.button_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(v.getContext(), getText(R.string.err_storage_not_available).toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                File tracks_dir;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    tracks_dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), getText(R.string.tracks_dir).toString());
                } else {
                    tracks_dir = new File(Environment.getExternalStoragePublicDirectory(getText(R.string.documents_dir).toString()), getText(R.string.tracks_dir).toString());
                }
                if (tracks_dir.mkdirs()) {
                    Toast.makeText(v.getContext(), getText(R.string.tracks_dir_success).toString(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(v.getContext(), getText(R.string.err_failed_csv).toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), getText(R.string.err_failed_csv).toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }

                try {
                    if (!kmlFile.createNewFile()) {
                        Toast.makeText(v.getContext(), getText(R.string.err_failed_kml).toString(), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (IOException e) {
                    Toast.makeText(v.getContext(), getText(R.string.err_failed_kml).toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    return;
                }

                TrackingApp.getTracking().setCsvFile(csvFile);
                TrackingApp.getTracking().setKmlFile(kmlFile);

                Intent intent = new Intent(v.getContext(), TrackingInProgress.class);

                EditText rate = findViewById(R.id.rate);
                if ( rate.getText().toString().equals("") ) {
                    TrackingApp.getTracking().setUpdateRate(Integer.parseInt(getText(R.string.default_rate).toString()));
                } else {
                    TrackingApp.getTracking().setUpdateRate(Integer.parseInt(rate.getText().toString()));
                }

                startActivity(intent);
            }
        });

    }
}
