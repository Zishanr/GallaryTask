package com.example.zishan.gallarytask.ui;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.example.zishan.gallarytask.R;
import com.example.zishan.gallarytask.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements ShowSearchView {

    private ActivityMainBinding activityMainBinding;
    private SearchView searchView;
    public static final String TAG = MainActivity.class.getSimpleName();
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new GridImageFragment(), TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.options_menu_main_search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    ((GridImageFragment) getSupportFragmentManager().findFragmentByTag(TAG)).userSerachQuery(query);

                    View view = MainActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return ((GridImageFragment) getSupportFragmentManager().findFragmentByTag(TAG)).setGridSpan(item);
    }

    @Override
    public void hideOrShowSearchView(Boolean hideOrShow) {
        if (searchView != null && menu != null) {
            if (hideOrShow)
                menu.setGroupVisible(R.id.main_menu_group, hideOrShow);
            else
                menu.setGroupVisible(R.id.main_menu_group, hideOrShow);

        }
    }

    @Override
    public void showSearchText(Boolean showSearch) {
        if (showSearch)
            activityMainBinding.tvNoRecordFound.setVisibility(View.VISIBLE);
        else
            activityMainBinding.tvNoRecordFound.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar(Boolean shoowProgress) {
        if (shoowProgress)
            activityMainBinding.progreesBar.setVisibility(View.VISIBLE);
        else
            activityMainBinding.progreesBar.setVisibility(View.GONE);
    }
}
