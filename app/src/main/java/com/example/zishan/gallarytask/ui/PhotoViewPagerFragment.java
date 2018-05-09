package com.example.zishan.gallarytask.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.adapter.PhotoViewPagerAdapter;
import com.example.zishan.gallarytask.databinding.FragmentViewPagerBinding;
import com.example.zishan.gallarytask.network.Photo;
import com.example.zishan.gallarytask.util.Constants;

import java.util.ArrayList;

public class PhotoViewPagerFragment extends Fragment {

    private FragmentViewPagerBinding fragmentViewPagerBinding;

    public static PhotoViewPagerFragment newInstance(int currentItem, ArrayList<Photo> photos) {
        PhotoViewPagerFragment photoViewPagerFragment = new PhotoViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_INITIAL_ITEM_POS, currentItem);
        bundle.putSerializable(Constants.EXTRA_PHOTOS, photos);
        photoViewPagerFragment.setArguments(bundle);
        return photoViewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        setSharedElementReturnTransition(null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentViewPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false);
        return fragmentViewPagerBinding.getRoot();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItem = 0;
        ArrayList<Photo> photos;
        if (getArguments() != null) {
            currentItem = getArguments().getInt(Constants.EXTRA_INITIAL_ITEM_POS);
            photos = (ArrayList<Photo>) getArguments().getSerializable(Constants.EXTRA_PHOTOS);
            PhotoViewPagerAdapter photoViewPagerAdapter = new PhotoViewPagerAdapter(getChildFragmentManager(), photos);

            ViewPager viewPager = fragmentViewPagerBinding.photoViewPager;
            viewPager.setAdapter(photoViewPagerAdapter);
            viewPager.setCurrentItem(currentItem);
        }
    }
}