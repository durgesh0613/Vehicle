package com.example.vehicle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vehicle.tasks.GetVehicleImage;

import java.util.HashMap;


public class VehicleDetailActivity extends AppCompatActivity {

    public HashMap<String, String> car;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_detail);

        //Toolbar code comes here
        //Code showing the back arrow button in the action bar
        car = (HashMap<String, String>)getIntent().getSerializableExtra("selectedCar");
        if (car != null) {
            ((TextView) findViewById(R.id.txtPrice)).setText(car.get("price") + " USD");
            ((TextView) findViewById(R.id.txtMakeModel)).setText(car.get("vehicle_make") +
                    " - " + car.get("model"));
            ((TextView) findViewById(R.id.txtLastUpdate)).setText(car.get("created_at"));
            ((TextView) findViewById(R.id.vehicle_detail)).setText(car.get("veh_description"));
            ImageView imgVehicle = ((ImageView) findViewById(R.id.imgVehicle));
            loadProgressBar();
            new GetVehicleImage(getApplicationContext(), imgVehicle, progressDialog)
                    .execute(car.get("image_url"));
        }

    }

    private void loadProgressBar(){
        progressDialog = new ProgressDialog(VehicleDetailActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}