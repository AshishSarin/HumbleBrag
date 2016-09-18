package com.sareen.squarelabs.humblebrag.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.sareen.squarelabs.humblebrag.R;
import com.sareen.squarelabs.humblebrag.Utilities;
import com.sareen.squarelabs.humblebrag.fragments.TimeLineFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;

public class HumbleBragActivity extends AppCompatActivity
    implements TimeLineFragment.OnTimeLineFragmentListener
{

    private static final String LOG_TAG = HumbleBragActivity.class.getSimpleName();

    // Tag for timeline fragment
    private static final String TIMELINE_FEAG_TAG = "timeline_frag";

    // Request code returned by login activity
    private static final int LOGIN_ACTIVITY = 525;


    // This filed contains twitter session
    private TwitterSession twitterSession;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "NFFLUKH1aBnaJPF3utimsxjCB";
    private static final String TWITTER_SECRET = "696BVC8LpueiMaDuZBgeaGi3bgRJjV4eeTmVxNxj0SBt48RR2b";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humble_brag);

        // setting up the twitter kit
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        // retrieve the active twitter session
        twitterSession =
                Twitter.getSessionManager().getActiveSession();

        if(twitterSession != null)
        {
            // session was valid
            // adding timeline fragment to activity
            if(savedInstanceState == null)
            {
                addTimeLineFragment();
            }
        }
        else
        {
            // session was not valid or user was not logged in
            // start twitter login activity
            Intent intent = new Intent(this, TwitterLoginActivity.class);
            startActivityForResult(intent, LOGIN_ACTIVITY);
        }


        // removing elevation from action bar
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
        {
            getSupportActionBar()
                    .setElevation(0f);
        }

        setupViews();

    }

    // adding timelinefragment to activity layout
    // This fragment contains the users profiles images
    // who are singled out on twitter handle "@HumbleBrag"
    private void addTimeLineFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_layout, new TimeLineFragment(), TIMELINE_FEAG_TAG).commit();
    }


    private void setupViews()
    {

        // setting listener for tweet floating action button
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab_tweet);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HumbleBragActivity.this,
                        PostTweetActivity.class);
                startActivity(intent);

                // adding animation
                // PostTweetActivity will slide up and HumbleBragActivity will zoom out
                overridePendingTransition(R.anim.slide_up, R.anim.zoom_out);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGIN_ACTIVITY)
        {
            if(resultCode == RESULT_OK)
            {
                // login was success
                // add timeline fragment
                addTimeLineFragment();
            }
        }

    }

    // To inflate activity menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_humble_brag, menu);
        return true;
    }

    // Handle when a menu option is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int itemId = item.getItemId();
        if(itemId == R.id.action_logout)
        {
            // logout from twitter
            Twitter.getSessionManager()
                    .clearActiveSession();

            // remove user timeline data from adapter
            TimeLineFragment tf = (TimeLineFragment) getSupportFragmentManager()
                    .findFragmentByTag(TIMELINE_FEAG_TAG);
            tf.clearAdapter();


            // removing the fragment
            getSupportFragmentManager().beginTransaction()
                    .remove(tf)
                    .commit();

            //restart activity to show login button again
            this.recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // click listener for recycler view item click
    @Override
    public void onImageClick(ArrayList<String> imageUrlList, int pos)
    {
        // start FullScreenImageActivity
        Intent intent = new Intent(this, FullScreenImageActivity.class);

        // sending image path url list to activity
        intent.putStringArrayListExtra(Utilities.IMAGE_URL_LIST, imageUrlList);

        // sending position of item selected
        intent.putExtra(Utilities.SELECTED_POS, pos);
        startActivity(intent);
    }
}
