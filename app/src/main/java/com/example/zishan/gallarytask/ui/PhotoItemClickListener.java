package com.example.zishan.gallarytask.ui;

import android.widget.ImageView;

import com.example.zishan.gallarytask.network.Photo;

import java.util.ArrayList;

public interface PhotoItemClickListener {
    void onPhotoItemClick(int pos, ArrayList<Photo> animalItem, ImageView shareImageView);
}
