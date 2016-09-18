package com.sareen.squarelabs.humblebrag.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sareen.squarelabs.humblebrag.R;
import com.sareen.squarelabs.humblebrag.Utilities;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashish on 18-09-2016.
 */

// pager adapter for full screen view pager
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity activity;
    private ArrayList<String> imagePaths;
    private LayoutInflater inflater;

    // constructor
    public FullScreenImageAdapter(Activity activity,
                                  ArrayList<String> imagePaths)
    {
        this.activity = activity;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount()
    {
        return this.imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        ImageView imgDisplay;

        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.fullscreen_layout, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);

        String url = Utilities.removeNormal(imagePaths.get(position));
        Picasso.with(activity)
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.error_placeholder)
                .error(R.drawable.error_placeholder)
                .resizeDimen(R.dimen.list_item_user_image, R.dimen.list_item_user_image)
                .centerInside()
                .into(imgDisplay);


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        ((ViewPager) container).removeView((LinearLayout) object);

    }
}
