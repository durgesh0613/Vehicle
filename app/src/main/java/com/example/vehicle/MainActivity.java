package com.example.vehicle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vehicle.content.SongUtils;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());

        RecyclerView rv = findViewById(R.id.vehicle_list);
        rv.setAdapter(new SimpleItemRecyclerViewAdapter(SongUtils.SONG_ITEMS));


        //is the container layout available? If so, set mTwoPane to true.
        if (findViewById(R.id.vehicle_detail_container) != null) {
            mTwoPane = true;
        }
    }

    class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private List<SongUtils.Song> mValues;

        SimpleItemRecyclerViewAdapter(List<SongUtils.Song> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.vehicle_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mContentView.setText(mValues.get(position).song_title);

            holder.mView.setOnClickListener(v -> {
                if (mTwoPane) {
                    int selectedSong = holder.getAdapterPosition();
                    VehicleDetailFragment frg = VehicleDetailFragment.newInstance(selectedSong);
                    getSupportFragmentManager().beginTransaction().replace(R.id.vehicle_detail_container, frg)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, VehicleDetailActivity.class);
                    intent.putExtra(SongUtils.SONG_ID_KEY, holder.getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final View mView;
            final TextView mIdView;
            final TextView mContentView;
            SongUtils.Song mItem;

            ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = view.findViewById(R.id.id);
                mContentView = view.findViewById(R.id.content);
            }
        }
    }

}