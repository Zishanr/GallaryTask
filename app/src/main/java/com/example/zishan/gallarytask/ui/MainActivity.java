package com.example.zishan.gallarytask.ui;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.adapter.PhotoGridAdapter;
import com.example.zishan.gallarytask.databinding.ActivityMainBinding;
import com.example.zishan.gallarytask.network.BaseCallback;
import com.example.zishan.gallarytask.network.BaseResponse;
import com.example.zishan.gallarytask.network.Photo;
import com.example.zishan.gallarytask.network.RequestController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private GridLayoutManager gridLayoutManager;
    private PhotoGridAdapter photoGridAdapter;
    private int mCurrentPage;
    private int mTotalPagesAvailable;
    private boolean isLoading;
    private ArrayList<Photo> photoArrayList;
    private String userQuery;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initUI();
        realm = Realm.getDefaultInstance();
    }

    private void initUI() {
        photoArrayList = new ArrayList<>();
        activityMainBinding.photosRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        activityMainBinding.photosRecyclerView.setLayoutManager(gridLayoutManager);
        photoGridAdapter = new PhotoGridAdapter(photoArrayList, MainActivity.this);
        activityMainBinding.photosRecyclerView.setAdapter(photoGridAdapter);

        activityMainBinding.photosRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // SearchWidget Added.
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.options_menu_main_search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    photoArrayList.clear();
                    mCurrentPage = 1;
                    userQuery = query;
                    searchDataForQuery(userQuery, mCurrentPage);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
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
                realm.copyToRealmOrUpdate(photoArrayList); // Persist unmanaged objects
                realm.commitTransaction();

            }

            @Override
            public void onFail(Call<BaseResponse> call, Boolean networkError) {
                isLoading = false;

                if (networkError) {
                    searchDataData();
                }
            }
        });

    }

    private void searchDataData() {
        photoArrayList.clear();
        RealmResults<Photo> result2 = realm.where(Photo.class)
                .equalTo("searchString", userQuery)
                .findAll();

        photoArrayList.addAll(result2);
        photoGridAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
