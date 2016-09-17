package com.sareen.squarelabs.humblebrag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import retrofit2.Call;

public class PostTweetActivity extends AppCompatActivity
{



    private EditText edit_tweet;        // EditText with tweet to post
    private ProgressDialog dialog;      // ProgressDialog to show while posting tweet
    private TextView text_char_count;   // TextView showing character count

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tweet);

        setUpViews();
    }


    private void setUpViews()
    {
        edit_tweet = (EditText)findViewById(R.id.edit_tweet);
        text_char_count = (TextView)findViewById(R.id.text_char_count);

        // defining TextWatcher for keeping count of characters in edit_tweet
        final TextWatcher mEditTweetWatcher = new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // setting the text_char_count to current no of chars in edit_tweet
                int remainingChars = 140 - s.length();
                text_char_count.setText(String.valueOf(remainingChars));
                if(remainingChars < 0)
                {
                    text_char_count.setTextColor
                            (ContextCompat.getColor(PostTweetActivity.this, R.color.colorTextError));
                }
                else
                {
                    text_char_count.setTextColor
                            (ContextCompat.getColor(PostTweetActivity.this, R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        };

        // adding TextWatcher to edit_tweet
        edit_tweet.addTextChangedListener(mEditTweetWatcher);
    }





    public void onClickTweet(View view)
    {

        // getting tweet in string
        String tweetStr = edit_tweet.getText().toString();

        // post tweet only if it is not empty
        if(!TextUtils.isEmpty(tweetStr))
        {
            // EditText is not empty
            // Check if char count is exceeding 140 limit
            if(tweetStr.length() > 140)
            {
                Toast.makeText(this, "You have exceeded the 140 character limit",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // showing progress dialog to user
            dialog = ProgressDialog.show
                    (this, getString(R.string.progress_dialog_post_tweet_title),
                            getString(R.string.progress_dialog_post_tweet_message));

            // getting twitter api client and session
            StatusesService statusesService =
                    TwitterCore.getInstance().getApiClient().getStatusesService();


            Call<Tweet> call = statusesService.update
                    (tweetStr, null, false, null,null, null, true, false,null);

            call.enqueue(new Callback<Tweet>()
            {
                @Override
                public void success(Result<Tweet> result)
                {
                    //dismiss the progress dialog
                    dialog.dismiss();
                    //close the activity
                    finish();
                }

                public void failure(TwitterException exception)
                {
                    //dismiss the progress dialog
                    dialog.dismiss();
                    // showing error to user
                    Toast.makeText(PostTweetActivity.this,
                            R.string.error_msg_post_tweet, Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }


    public void onClose(View view)
    {
        // close this activity
        finish();
    }
}
