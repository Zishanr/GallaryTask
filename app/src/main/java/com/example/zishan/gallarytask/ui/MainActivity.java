package com.example.zishan.gallarytask.ui;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.adapter.PhotoGridAdapter;
import com.example.zishan.gallarytask.databinding.ActivityMainBinding;
import com.example.zishan.gallarytask.network.BaseCallback;
import com.example.zishan.gallarytask.network.BaseResponse;
import com.example.zishan.gallarytask.network.RequestController;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private GridLayoutManager gridLayoutManager;
    private PhotoGridAdapter photoGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        activityMainBinding.photosRecyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        activityMainBinding.photosRecyclerView.setLayoutManager(gridLayoutManager);

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
                    searchDataForQuery(query);
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

    private void searchDataForQuery(String query) {
        Call<BaseResponse> responseCall = RequestController.getInstance().createService().searchFlickerImage(Constants.FLICKER_IMAGE_METHOD, Constants.FLICKER_API_KEY,
                query, Constants.PER_PAGE, 1, Constants.FORMAT, Constants.NO_JSON_CALLBACK);

        responseCall.enqueue(new BaseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (photoGridAdapter == null) {
                    photoGridAdapter = new PhotoGridAdapter(response.getPhotos().getPhoto(), MainActivity.this);
                    activityMainBinding.photosRecyclerView.setAdapter(photoGridAdapter);
                } else {
                    photoGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call) {

            }
        });

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
