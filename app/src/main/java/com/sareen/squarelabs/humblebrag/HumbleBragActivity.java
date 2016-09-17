package com.sareen.squarelabs.humblebrag;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;

public class HumbleBragActivity extends AppCompatActivity
{

    private static final String LOG_TAG = HumbleBragActivity.class.getSimpleName();

    private TextView userNameView;

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

        setupViews();

        getSupportActionBar()
                .setElevation(0f);

    }

    private void setupViews()
    {
        userNameView = (TextView)findViewById(R.id.user_name_textview);

        // setting listener for tweet floating action button
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab_tweet);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HumbleBragActivity.this,
                        PostTweetActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // retrieve the active twitter session
        twitterSession =
                Twitter.getSessionManager().getActiveSession();

        if(twitterSession != null)
        {
            // session was valid
            // setting username textview to username
            String userNameStr = twitterSession.getUserName();
            if(userNameStr != null)
            {
                userNameView.setText(userNameStr);
            }
            else
            {
                userNameView.setText("No username");
            }
        }
        else
        {
            // session was not valid or user was not logged in
            // start twitter login activity
            Intent intent = new Intent(this, TwitterLoginActivity.class);
            startActivity(intent);
        }
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

            //restart activity to show login button again
            this.recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // To inflate activity menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_humble_brag, menu);
        return true;
    }
}
