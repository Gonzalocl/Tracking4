package com.gonzalocl.tracking4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TrackingInProgress extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.traking_in_progress);

        Button btnStop = findViewById(R.id.button_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO remove updates
                Intent intent = new Intent(v.getContext(), ConfigActivity.class);
                // TODO end csv kml
                startActivity(intent);
            }
        });
    }
}
