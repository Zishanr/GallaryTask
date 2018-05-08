package com.example.zishan.gallarytask.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.databinding.PhotoItemBinding;
import com.example.zishan.gallarytask.network.BaseResponse;
import com.example.zishan.gallarytask.network.Photo;
import com.example.zishan.gallarytask.ui.MainActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<Photo> photos;

    public PhotoGridAdapter(ArrayList<Photo> photos, Context context) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public PhotoGridAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PhotoItemBinding photoItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.photo_item, parent, false);
        return new ImageViewHolder(photoItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bindData(photos.get(position));
    }


    @Override
    public int getItemCount() {
        return photos != null ? photos.size() : 0;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private PhotoItemBinding photoItemBinding;
        private Photo photo;

        ImageViewHolder(PhotoItemBinding viewItemBinding) {
            super(viewItemBinding.getRoot());
            photoItemBinding = DataBindingUtil.bind(viewItemBinding.getRoot());
        }

        void bindData(Photo photo) {
            this.photo = photo;
            Picasso.with(context)
                    .load(urlBuilder())
                    .into(photoItemBinding.photoImage);
        }

        private String urlBuilder() {
            StringBuilder builder = new StringBuilder("https://farm");
            builder.append(photo.getFarm());
            builder.append(".staticflickr.com/");
            builder.append(photo.getServer());
            builder.append("/");
            builder.append(photo.getId());
            builder.append("_");
            builder.append(photo.getSecret());
            builder.append(".jpg");

            return builder.toString();

        }
    }
}
