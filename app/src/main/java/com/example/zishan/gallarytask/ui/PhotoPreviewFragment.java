package com.example.zishan.gallarytask.ui;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

    private FragmentPreviewBinding fragmentViewPagerBinding;

    public static PhotoPreviewFragment newInstance(Photo photo, String transitionName) {
        PhotoPreviewFragment photoPreviewFragment = new PhotoPreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.EXTRA_PHOTO_ITEM, photo);
        bundle.putString(Constants.EXTRA_TRANSITION_NAME, transitionName);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentViewPagerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_preview, container, false);
        return fragmentViewPagerBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Photo photo = null;
        String transitionName = null;
        if (getArguments() != null) {
            photo = (Photo) getArguments().getSerializable(Constants.EXTRA_PHOTO_ITEM);
            transitionName = getArguments().getString(Constants.EXTRA_TRANSITION_NAME);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragmentViewPagerBinding.photoPreviewImage.setTransitionName(transitionName);
        }

        if (photo != null) {
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
    }

    private String urlBuilder(Photo photo) {
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