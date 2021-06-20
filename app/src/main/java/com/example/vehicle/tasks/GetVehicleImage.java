package com.example.vehicle.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

//Reference: tutorialspoint.com
public class GetVehicleImage extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Context context;
    ProgressDialog progressDialog;

    public GetVehicleImage(Context context, ImageView imageView, ProgressDialog progressDialog) {
        this.imageView=imageView;
        this.context = context;
        this.progressDialog = progressDialog;
    }
    protected Bitmap doInBackground(String... urls) {
        String imageURL=urls[0];
        Bitmap bimage=null;
        try {
            InputStream in=new java.net.URL(imageURL).openStream();
            bimage= BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimage;
    }
    protected void onPostExecute(Bitmap result) {
        if(result!=null) {
            imageView.setImageBitmap(result);
        }else{
            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
        }
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
