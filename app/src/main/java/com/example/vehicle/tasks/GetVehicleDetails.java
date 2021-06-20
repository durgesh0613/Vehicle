package com.example.vehicle.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicle.MainActivity;
import com.example.vehicle.content.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GetVehicleDetails extends AsyncTask<Void, Void, Void> {


    private ProgressDialog progressDialog;
    private Context context;
    private ArrayList<String> vehicleList;
    private ArrayList<Integer> vehicleIds;
    private ArrayList<HashMap<String, String>> vehicleDetails;
    private ViewGroup component;
    private String apiUrl;
    private String type;
    private MainActivity mainActivity;

    public GetVehicleDetails(ProgressDialog progressDialog, Context context, ArrayList<String> vehicleList, ArrayList<Integer> vehicleIds, ViewGroup component, String apiUrl, String type, ArrayList<HashMap<String, String>> vehicleDetails, MainActivity mainActivity) {
        this.progressDialog = progressDialog;
        this.context = context;
        this.vehicleList = vehicleList;
        this.component = component;
        this.apiUrl = apiUrl;
        this.type = type;
        this.vehicleIds = vehicleIds;
        this.vehicleDetails = vehicleDetails;
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (!type.equalsIgnoreCase("details")) {
            ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, vehicleList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((Spinner) component).setAdapter(adapter);
        } else {
            ((RecyclerView) component).setAdapter(this.mainActivity.returnAdapter(vehicleDetails));
            if (vehicleDetails.isEmpty()) {
                Toast.makeText(context, "The URL returned no results", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        if (type.equalsIgnoreCase("details")) {
            gatherDetails();
            return null;
        }
        HttpHandler sh = new HttpHandler();
        String jsonString = sh.makeServiceCall(apiUrl);

        if (jsonString != null) {
            try {
                JSONArray names = new JSONArray(jsonString);

                for (int i = 0; i < names.length(); i++) {
                    JSONObject d = names.getJSONObject(i);
                    String id = d.getString("id");
                    String name = d.getString(type);

                    vehicleIds.add(Integer.parseInt(id));
                    vehicleList.add(name);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private void gatherDetails() {
        HttpHandler sh = new HttpHandler();
        String jsonString = sh.makeServiceCall(apiUrl);

        if (jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray lists = jsonObject.getJSONArray("lists");

                for (int i = 0; i < lists.length(); i++) {
                    JSONObject d = lists.getJSONObject(i);
                    HashMap<String, String> vehicle = new HashMap<>();

                    vehicle.put("color", d.getString("color"));
                    vehicle.put("created_at", d.getString("created_at"));
                    vehicle.put("id", d.getString("id"));
                    vehicle.put("image_url", d.getString("image_url"));
                    vehicle.put("mileage", d.getString("mileage"));
                    vehicle.put("model", d.getString("model"));
                    vehicle.put("price", d.getString("price"));
                    vehicle.put("veh_description", d.getString("veh_description"));
                    vehicle.put("vehicle_make", d.getString("vehicle_make"));
                    vehicle.put("vehicle_url", d.getString("vehicle_url"));
                    vehicle.put("vin_number", d.getString("vin_number"));

                    vehicleDetails.add(vehicle);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}