package com.tanobel.it_yoga.tis_mobile;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanobel.it_yoga.tis_mobile.util.InternetConnection;
import com.tanobel.it_yoga.tis_mobile.util.RequestPostLumen;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class View_image_adapter extends RecyclerView.Adapter<View_image_adapter.View_image_holder> {
    private RequestPostLumen RP;
    InternetConnection internetCon = new InternetConnection();
    ProgressDialog pDialog;
    String url_image,docno,plant,usage;
    private View_image mFragment;
    public View_image_adapter(View_image mFragment, String docno,String plant,String usage) {
        this.mFragment = mFragment;
        this.docno = docno;
        this.plant = plant;
        this.usage = usage;
        this.url_image = "https://tanobelfood.com/sso/asset/img/logo.png";
    }
    @NonNull
    @Override
    public View_image_adapter.View_image_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_view_image,parent,false);
        View_image_holder vh = new View_image_holder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(View_image_holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
    public class View_image_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ScaleGestureDetector scaleGestureDetector;
        private float mScaleFactor = 1.0f;
        ImageView image;
        public View_image_holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            pDialog = new ProgressDialog(mFragment.getApplicationContext());
            pDialog.setMessage("Please wait....");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            scaleGestureDetector = new ScaleGestureDetector(mFragment.getApplicationContext(), new ScaleListener());
            getImage();
        }

//        @Override
//        public boolean onTouchEvent(MotionEvent motionEvent) {
//            scaleGestureDetector.onTouchEvent(motionEvent);
//            return true;
//        }
        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                mScaleFactor *= scaleGestureDetector.getScaleFactor();
                mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
                image.setScaleX(mScaleFactor);
                image.setScaleY(mScaleFactor);
                return true;
            }
        }
        @Override
        public void onClick(View v) {
            
        }
        public void getImage(){
            if (internetCon.checkConnection(mFragment.getApplicationContext())) {
                //pDialog.show();
                try {
                    JSONObject json = new JSONObject();
                    try {
                        json = new JSONObject();
                        json.put("plant", plant);
                        json.put("docno", docno);
                        json.put("usage", usage);
                        Log.i("JSON nya", json.toString());
                    } catch (JSONException e) {
                        if (pDialog != null) pDialog.dismiss();
                        e.printStackTrace();
                    }
                    RP = new RequestPostLumen("image.view_one", json,mFragment.getApplicationContext());
                    RP.execPostCall(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (pDialog != null) pDialog.dismiss();
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);
                            final String jsonData = response.body().string();
                            mFragment.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject obj = new JSONObject(jsonData);
                                        int status = obj.getInt("success");
                                        String images = obj.getString("image");
                                        if (status == 1){
                                            byte[] decodedBytes = Base64.decode(images, 0);
                                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                                            image.setImageBitmap(decodedByte);
                                        }else{
                                            Toast toast = Toast.makeText(mFragment.getApplicationContext(), "Tidak Ada Image!", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        if (pDialog != null) pDialog.dismiss();
                                    } catch (JSONException e) {
                                        if (pDialog != null) pDialog.dismiss();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    if (pDialog != null) pDialog.dismiss();
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(mFragment.getApplicationContext(), "Tidak terhubung ke internet", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
