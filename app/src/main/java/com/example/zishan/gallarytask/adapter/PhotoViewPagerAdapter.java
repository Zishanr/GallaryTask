package com.example.zishan.gallarytask.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.zishan.gallarytask.network.Photo;
import com.example.zishan.gallarytask.ui.PhotoPreviewFragment;

import java.util.ArrayList;

public class PhotoViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Photo> photos;

    public PhotoViewPagerAdapter(FragmentManager fm, ArrayList<Photo> photos) {
        super(fm);
        this.photos = photos;
    }

    @Override
    public Fragment getItem(int position) {
        Photo photo = photos.get(position);
        return PhotoPreviewFragment.newInstance(photo, photo.getId());
    }

    @Override
    public int getCount() {
        return photos != null ? photos.size() : 0;
    }

}