package com.example.zishan.gallarytask.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.adapter.PhotoGridAdapter;
import com.example.zishan.gallarytask.databinding.GridImageRecyclerviewBinding;
import com.example.zishan.gallarytask.network.BaseCallback;
import com.example.zishan.gallarytask.network.BaseResponse;
import com.example.zishan.gallarytask.network.Photo;
import com.example.zishan.gallarytask.network.RequestController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;

public class GridImageFragment extends Fragment implements PhotoItemClickListener {

    private GridImageRecyclerviewBinding gridImageRecyclerviewBinding;
    private GridLayoutManager gridLayoutManager;
    private PhotoGridAdapter photoGridAdapter;
    private int mCurrentPage;
    private int mTotalPagesAvailable;
    private boolean isLoading;
    private ArrayList<Photo> photoArrayList = new ArrayList<>();
    private String userQuery;
    private Realm realm;
    private ShowSearchView showSearchView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            showSearchView = (ShowSearchView) context;
        } catch (ClassCastException exception) {
            exception.getStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gridImageRecyclerviewBinding = DataBindingUtil.inflate(inflater, R.layout.grid_image_recyclerview, container, false);
        return gridImageRecyclerviewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showSearchView.hideOrShowSearchView(Boolean.TRUE);
        realm = Realm.getDefaultInstance();
        gridImageRecyclerviewBinding.photosRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridImageRecyclerviewBinding.photosRecyclerView.setLayoutManager(gridLayoutManager);
        photoGridAdapter = new PhotoGridAdapter(photoArrayList, getContext(), this);
        gridImageRecyclerviewBinding.photosRecyclerView.setAdapter(photoGridAdapter);

        gridImageRecyclerviewBinding.photosRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mVisibleItemCount = gridLayoutManager.getChildCount();
                int mTotalItemCount = gridLayoutManager.getItemCount();
                int mPastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                if (mCurrentPage < mTotalPagesAvailable && ((mPastVisiblesItems + mVisibleItemCount) >= mTotalItemCount && !isLoading)) {
                    if (photoArrayList != null) {
                        mCurrentPage++;
                        isLoading = true;
                        searchDataForQuery(userQuery, mCurrentPage);
                    }
                }
            }
        });
    }


    private void searchDataForQuery(String query, int mCurrentPage) {
        Call<BaseResponse> responseCall = RequestController.getInstance().createService().searchFlickerImage(Constants.FLICKER_IMAGE_METHOD, Constants.FLICKER_API_KEY,
                query, Constants.PER_PAGE, mCurrentPage, Constants.FORMAT, Constants.NO_JSON_CALLBACK);

        responseCall.enqueue(new BaseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                isLoading = false;
                mTotalPagesAvailable = Integer.parseInt(response.getPhotos().getPages());
                photoArrayList.addAll(response.getPhotos().getPhoto());
                photoGridAdapter.notifyDataSetChanged();

                for (Photo photo : photoArrayList) {
                    photo.setSearchString(userQuery);
                }

                realm.beginTransaction();
                realm.copyToRealmOrUpdate(photoArrayList);
                realm.commitTransaction();
            }

            @Override
            public void onFail(Call<BaseResponse> call, Boolean networkError) {
                isLoading = false;
                if (networkError) {
                    fetchDataFromDB();
                }
            }
        });

    }

    private void fetchDataFromDB() {
        photoArrayList.clear();
        RealmResults<Photo> dbPhotoListData = realm.where(Photo.class)
                .equalTo("searchString", userQuery)
                .findAll();

        photoArrayList.addAll(dbPhotoListData);
        photoGridAdapter.notifyDataSetChanged();
    }

    public void userSerachQuery(String query) {
        photoArrayList.clear();
        mCurrentPage = 1;
        userQuery = query;
        searchDataForQuery(userQuery, mCurrentPage);
    }


    public boolean setGridSpan(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.two:
                gridLayoutManager.setSpanCount(2);
                photoGridAdapter.notifyDataSetChanged();
                return true;
            case R.id.three:
                gridLayoutManager.setSpanCount(3);
                photoGridAdapter.notifyDataSetChanged();
                return true;
            case R.id.four:
                gridLayoutManager.setSpanCount(4);
                photoGridAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPhotoItemClick(int pos, ArrayList<Photo> photoList, ImageView shareImageView) {
        showSearchView.hideOrShowSearchView(Boolean.FALSE);
        Fragment photoViewPagerFragment = PhotoViewPagerFragment.newInstance(pos, photoList);
        getFragmentManager()
                .beginTransaction()
                .addSharedElement(shareImageView, ViewCompat.getTransitionName(shareImageView))
                .replace(R.id.container, photoViewPagerFragment)
                .addToBackStack(null)
                .commit();
    }
}
