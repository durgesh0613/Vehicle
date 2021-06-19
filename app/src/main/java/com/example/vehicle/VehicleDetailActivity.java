package com.example.vehicle;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle.content.SongUtils;


public class VehicleDetailActivity extends AppCompatActivity {

    public SongUtils.Song mSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        //Toolbar code comes here
        //Code showing the back arrow button in the action bar
        mSong = SongUtils.SONG_ITEMS.get(getIntent().getIntExtra(SongUtils.SONG_ID_KEY, 0));
        if (mSong != null) {
            ((TextView) findViewById(R.id.vehicle_detail)).setText(mSong.details);
        }

    }
}