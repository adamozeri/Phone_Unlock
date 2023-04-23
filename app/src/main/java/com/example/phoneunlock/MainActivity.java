package com.example.phoneunlock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText main_TF_password;
    private LinearLayout main_LL_images;
    private MaterialButton main_BTN_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        main_BTN_submit.setOnClickListener(view -> {
            try {
                clicked();
            } catch (Settings.SettingNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void clicked() throws Settings.SettingNotFoundException {
        for (int i = 0; i < main_LL_images.getChildCount(); i++) {
            ((ShapeableImageView) main_LL_images.getChildAt(i)).setImageResource(R.drawable.secure);
        }
        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        String time = s.format(new Date());
        if(checkSecurityFilters() && main_TF_password.getText().toString().equals(time)){
            for (int i = 0; i < main_LL_images.getChildCount(); i++) {
                ((ShapeableImageView) main_LL_images.getChildAt(i)).setImageResource(R.drawable.unlocked);
            }
        }
    }

    private void findViews() {
        this.main_TF_password = findViewById(R.id.main_TF_password);
        this.main_BTN_submit = findViewById(R.id.main_BTN_submit);
        this.main_LL_images = findViewById(R.id.main_LL_images);
    }

    private boolean checkSecurityFilters() throws Settings.SettingNotFoundException {
        return isAirplaneModeOn() && isLocationOn() && isSilentMode() && isScreenRotationLocked();
    }

    private boolean isAirplaneModeOn(){
        return Settings.System.getInt(getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }


    private boolean isLocationOn(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isSilentMode(){
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    private boolean isScreenRotationLocked() throws Settings.SettingNotFoundException {
        return Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION) == 0;
    }
}