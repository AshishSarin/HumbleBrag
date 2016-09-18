package com.sareen.squarelabs.humblebrag.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.sareen.squarelabs.humblebrag.R;
import com.sareen.squarelabs.humblebrag.Utilities;
import com.sareen.squarelabs.humblebrag.adapters.FullScreenImageAdapter;

import java.util.ArrayList;

/**
 * Created by Ashish on 18-09-2016.
 * This activity show selected image in full screen with pan and zoom
 */
public class FullScreenImageActivity extends AppCompatActivity
{
    private ViewPager viewPager;

    // adapter for view pager
    private FullScreenImageAdapter imageAdapter;

    //List containing image urls
    private ArrayList<String> imageUrlList;

    // initial position to start the view pager on
    private int intialPos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);

        viewPager = (ViewPager)findViewById(R.id.main_pager);
        viewPager.setOffscreenPageLimit(10);


        imageUrlList = getIntent().getStringArrayListExtra(Utilities.IMAGE_URL_LIST);
        intialPos = getIntent().getIntExtra(Utilities.SELECTED_POS, 0);

        imageAdapter = new FullScreenImageAdapter(this,
                imageUrlList);

        viewPager.setAdapter(imageAdapter);

        // moving view pager to current selected item
        viewPager.setCurrentItem(intialPos);
    }
}
