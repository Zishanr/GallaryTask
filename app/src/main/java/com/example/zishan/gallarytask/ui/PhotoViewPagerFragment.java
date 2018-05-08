package com.example.zishan.gallarytask.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;

public class PhotoViewPagerFragment extends Fragment {

    private static final String EXTRA_INITIAL_ITEM_POS = "initial_item_pos";
    private static final String EXTRA_PHOTOS = "photos";
    private FragmentViewPagerBinding fragmentViewPagerBinding;

    public static PhotoViewPagerFragment newInstance(int currentItem, ArrayList<Photo> photos) {
        PhotoViewPagerFragment photoViewPagerFragment = new PhotoViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_INITIAL_ITEM_POS, currentItem);
        bundle.putSerializable(EXTRA_PHOTOS, photos);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentViewPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false);
        return fragmentViewPagerBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int currentItem = 0;
        ArrayList<Photo> photos;
        if (getArguments() != null) {
            currentItem = getArguments().getInt(EXTRA_INITIAL_ITEM_POS);
            photos = (ArrayList<Photo>) getArguments().getSerializable(EXTRA_PHOTOS);
            PhotoViewPagerAdapter photoViewPagerAdapter = new PhotoViewPagerAdapter(getChildFragmentManager(), photos);

            ViewPager viewPager = fragmentViewPagerBinding.photoViewPager;
            viewPager.setAdapter(photoViewPagerAdapter);
            viewPager.setCurrentItem(currentItem);
        }
    }
}
