package com.example.vehicle;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.vehicle.content.DataUtils;
import com.example.vehicle.tasks.GetVehicleImage;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VehicleDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VehicleDetailFragment extends Fragment {
    public HashMap<String, String> car;
    ProgressDialog progressDialog;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VehicleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VehicleDetailFragment newInstance(String param1, String param2) {
        VehicleDetailFragment fragment = new VehicleDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
        if (getArguments().containsKey("selectedCar")) {
            car = (HashMap<String, String>) getArguments().getSerializable("selectedCar");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.vehicle_detail, container, false);
        //Shows the detail info in a TextView
        if (car != null) {
            ((TextView) root.findViewById(R.id.txtPrice)).setText(car.get("price"));
            ((TextView) root.findViewById(R.id.txtMakeModel)).setText(car.get("vehicle_make") +
                    " - " + car.get("model"));
            ((TextView) root.findViewById(R.id.txtLastUpdate)).setText(car.get("created_at"));
            ImageView imgVehicle = ((ImageView) root.findViewById(R.id.imgVehicle));
            loadProgressBar();
            new GetVehicleImage(getActivity().getApplicationContext(), imgVehicle, progressDialog)
                    .execute(car.get("vehicle_url"));
        }

        return root;
    }

    public static VehicleDetailFragment newInstance(HashMap<String, String> selectedCar) {
        VehicleDetailFragment frg = new VehicleDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("selectedCar", selectedCar);
        frg.setArguments(arguments);
        return frg;
    }

    private void loadProgressBar(){
        progressDialog = new ProgressDialog(getActivity().getApplicationContext());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}