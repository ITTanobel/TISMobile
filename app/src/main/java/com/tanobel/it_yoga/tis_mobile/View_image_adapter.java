package com.tanobel.it_yoga.tis_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class View_image_adapter extends RecyclerView.Adapter<View_image_adapter.View_image_holder> {
    String personImages;
    String url_image;
    private View_image mFragment;
    public View_image_adapter(View_image mFragment, String personImages) {
        this.mFragment = mFragment;
        this.personImages = personImages;
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

        //holder.image.setImageResource(mFragment.getResources().getIdentifier("ic_inspection","drawable",mFragment.getPackageName()));
//        URL url = null;
//        try {
//            url = new URL("https://tanobelfood.com/sso/asset/img/favicon.ico");
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            holder.image.setImageBitmap(bmp);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }
    public class View_image_holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        public View_image_holder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            String url = url_image;
            Picasso.get()
                    .load(url)
                    .into(image);
        }

        @Override
        public void onClick(View v) {
            
        }
    }

}
