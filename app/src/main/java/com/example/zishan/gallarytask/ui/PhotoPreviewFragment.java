package com.example.zishan.gallarytask.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.databinding.FragmentPreviewBinding;
import com.example.zishan.gallarytask.network.Photo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoPreviewFragment extends Fragment {

    private static final String EXTRA_PHOTO_ITEM = "animal_item";
    private static final String EXTRA_TRANSITION_NAME = "transition_name";
    private FragmentPreviewBinding fragmentViewPagerBinding;

    public static PhotoPreviewFragment newInstance(Photo photo, String transitionName) {
        PhotoPreviewFragment photoPreviewFragment = new PhotoPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PHOTO_ITEM, photo);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
        photoPreviewFragment.setArguments(bundle);
        return photoPreviewFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentViewPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview, container, false);
        return fragmentViewPagerBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Photo photo = (Photo) getArguments().getSerializable(EXTRA_PHOTO_ITEM);
        String transitionName = getArguments().getString(EXTRA_TRANSITION_NAME);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentViewPagerBinding.photoPreviewImage.setTransitionName(transitionName);
        }

        Picasso.with(getContext())
                .load(urlBuilder(photo))
                .noFade()
                .into(fragmentViewPagerBinding.photoPreviewImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        startPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        startPostponedEnterTransition();
                    }
                });
    }

    private String urlBuilder(Photo photo) {
        StringBuilder builder = new StringBuilder("https://farm");
        builder.append(photo.getFarm());
        builder.append(".staticflickr.com/");
        builder.append(photo.getServer());
        builder.append("/");
        builder.append(photo.getId());
        builder.append("_");
        builder.append(photo.getSecret());
        builder.append("_m");
        builder.append(".jpg");

        return builder.toString();

    }
}
