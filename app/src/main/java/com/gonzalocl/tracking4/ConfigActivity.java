package com.gonzalocl.tracking4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.config);

        Button btn_start = findViewById(R.id.button_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TrackingInProgress.class);

                EditText rate = findViewById(R.id.rate);
                if ( rate.getText().toString().equals("") ) {
                    TrackingApp.getTracking().setUpdate_rate(Integer.parseInt(getText(R.string.default_rate).toString()));
                } else {
                    TrackingApp.getTracking().setUpdate_rate(Integer.parseInt(rate.getText().toString()));
                }

                startActivity(intent);
            }
        });

    }
}
