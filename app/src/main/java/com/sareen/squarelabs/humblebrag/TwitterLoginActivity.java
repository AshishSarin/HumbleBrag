/*This activity is used for login the user*/


package com.sareen.squarelabs.humblebrag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class TwitterLoginActivity extends AppCompatActivity
{



    private TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);


        // initializing the twiiter login button
        loginButton = (TwitterLoginButton)
                findViewById(R.id.button_login_twitter);

        // setting up the callbacks for login button
        loginButton.setCallback(new Callback<TwitterSession>()
        {
            @Override
            public void success(Result<TwitterSession> result)
            {
                // Login is successful
                // Close this activity
                finish();
            }

            @Override
            public void failure(TwitterException exception)
            {
                //TODO do something on failure
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed()
    {
        // This is done so that user can exit app
        // by pressing back button from login activity
        moveTaskToBack(true);
        super.onBackPressed();
    }
}
