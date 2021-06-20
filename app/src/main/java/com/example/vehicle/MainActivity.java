package com.example.vehicle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicle.content.DataUtils;
import com.example.vehicle.tasks.GetVehicleDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane = false;

    private static String makeUrl = "https://thawing-beach-68207.herokuapp.com/carmakes";

    private static String modelUrl = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";

    private static String detailsUrl = "https://thawing-beach-68207.herokuapp.com/cars/";

    ArrayList<String> vehicleMakeList = new ArrayList<>();
    ArrayList<String> vehicleModelList = new ArrayList<>();
    ArrayList<Integer> vehicleMakeIds = new ArrayList<>();
    ArrayList<Integer> vehicleModelIds = new ArrayList<>();
    ArrayList<HashMap<String, String>> vehicleDetails = new ArrayList<>();

    private ProgressDialog progressDialog;

    private Spinner vehicleMake;
    private Spinner vehicleModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        RecyclerView rv = findViewById(R.id.vehicle_list);


        //is the container layout available? If so, set mTwoPane to true.
        if (findViewById(R.id.vehicle_detail_container) != null) {
            mTwoPane = true;
        }

        vehicleMake = findViewById(R.id.spinnerMake);
        vehicleModel = findViewById(R.id.spinnerModel);

        vehicleMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleModelList.clear();
                vehicleModelIds.clear();
                Integer selectedId = vehicleMakeIds.get(position);
                loadProgressBar();
                new GetVehicleDetails(progressDialog, getApplicationContext(), vehicleModelList,
                        vehicleModelIds, vehicleModel, modelUrl + selectedId, "model", null).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicleDetails.clear();
                loadProgressBar();
                Integer makeId = vehicleMakeIds.get(vehicleMake.getSelectedItemPosition());
                Integer modelId = vehicleModelIds.get(position);
                String zipCode = "92603";
                new GetVehicleDetails(progressDialog, getApplicationContext(), vehicleModelList,
                        vehicleModelIds, rv, detailsUrl + makeId + "/" + modelId
                        + "/" + zipCode  , "details", vehicleDetails).execute();
                //rv.setAdapter(new SimpleItemRecyclerViewAdapter(vehicleDetails));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        loadProgressBar();
        new GetVehicleDetails(progressDialog, getApplicationContext(), vehicleMakeList,
                vehicleMakeIds, vehicleMake, makeUrl, "vehicle_make", null).execute();
    }

    private void loadProgressBar(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public RecyclerView.Adapter returnAdapter(ArrayList<HashMap<String, String>> vehicleDetails){
        return new SimpleItemRecyclerViewAdapter(vehicleDetails);
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private List<HashMap<String, String>> details;

        SimpleItemRecyclerViewAdapter(List<HashMap<String, String>> items) {
            details = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vehicle_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mItem = details.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            String make = holder.mItem.get("vehicle_make");
            String model = holder.mItem.get("model");
            String id = holder.mItem.get("id");
            holder.mContentView.setText(make + model + " - " + id );

            holder.mView.setOnClickListener(v -> {
                int selectedCar = holder.getAdapterPosition();
                if (mTwoPane) {
                    VehicleDetailFragment frg = VehicleDetailFragment.newInstance(details.get(selectedCar));
                    getSupportFragmentManager().beginTransaction().replace(R.id.vehicle_detail_container, frg)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, VehicleDetailActivity.class);
                    intent.putExtra("selectedCar", details.get(selectedCar));
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return details.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mContentView;
            HashMap<String, String> mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }

}