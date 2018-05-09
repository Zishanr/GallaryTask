package com.example.zishan.gallarytask.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.databinding.PhotoItemBinding;
import com.example.zishan.gallarytask.network.Photo;
import com.example.zishan.gallarytask.ui.Constants;
import com.example.zishan.gallarytask.ui.GridImageFragment;
import com.example.zishan.gallarytask.ui.PhotoItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<Photo> photos;
    private final PhotoItemClickListener photoItemClickListener;

    public PhotoGridAdapter(ArrayList<Photo> photos, Context context, PhotoItemClickListener photoItemClickListener) {
        this.context = context;
        this.photos = photos;
        this.photoItemClickListener = photoItemClickListener;
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

        void bindData(final Photo photo) {
            this.photo = photo;
            Picasso.with(context)
                    .load(urlBuilder())
                    .into(photoItemBinding.photoImage);

            ViewCompat.setTransitionName(photoItemBinding.photoImage, photo.getId());

            photoItemBinding.photoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoItemClickListener.onPhotoItemClick(getAdapterPosition(), photos, photoItemBinding.photoImage);
                }
            });
        }

        private String urlBuilder() {
            StringBuilder builder = new StringBuilder(Constants.IMAGE_HOST);
            builder.append(photo.getFarm());
            builder.append(Constants.IMAGE_DOMAIN);
            builder.append(photo.getServer());
            builder.append(Constants.SEPERATOR);
            builder.append(photo.getId());
            builder.append(Constants.UNDER_SCORE);
            builder.append(photo.getSecret());
            builder.append(Constants.IMAGE_FORMAT);

            return builder.toString();

        }
    }
}
