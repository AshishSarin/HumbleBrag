package com.sareen.squarelabs.humblebrag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sareen.squarelabs.humblebrag.DividerItemDecoration;
import com.sareen.squarelabs.humblebrag.ItemClickListener;
import com.sareen.squarelabs.humblebrag.R;
import com.sareen.squarelabs.humblebrag.UserData;
import com.sareen.squarelabs.humblebrag.Utilities;
import com.sareen.squarelabs.humblebrag.activities.HumbleBragActivity;
import com.sareen.squarelabs.humblebrag.adapters.UserAdapter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by Ashish on 18-09-2016.
 */


public class TimeLineFragment extends Fragment
    implements ItemClickListener
{
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private Button retry_button;

    private UserAdapter adapter;


    // this user list will contain user data
    // and data is fed to adapter through this.
    private ArrayList<UserData> userList;

    public void clearAdapter()
    {
        userList.clear();
        adapter.notifyDataSetChanged();
    }


    // interface for communicating with parent activity
    public interface OnTimeLineFragmentListener
    {
        void onImageClick(ArrayList<String> list, int pos);
    }
    OnTimeLineFragmentListener mCallback;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof HumbleBragActivity)
        {
            HumbleBragActivity activity = (HumbleBragActivity)context;
            try
            {
                mCallback = (OnTimeLineFragmentListener) activity;
            }
            catch (ClassCastException e)
            {
                throw new ClassCastException(activity.toString()
                        + " must implement OnTimeLineListenerFragment");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        setupViews(v);      // setting the view
        fetchData();        // fetching data from twitter
        return v;
    }

    private void setupViews(View v)
    {
        // initializing the retry button
        retry_button = (Button)v.findViewById(R.id.button_retry);
        retry_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // removing retry button from screen once clicked
                retry_button.setVisibility(View.GONE);
                // retry is called to again try to fetch data
                retry();
            }
        });
        // when starting fragment, we don't want to show retry button
        retry_button.setVisibility(View.GONE);

        // showing the progress bar
        progressBar = (ProgressBar)v.findViewById(R.id.progress_timeline);


        // setting up the recycler view
        recyclerView = (RecyclerView)v.findViewById(R.id.recView_timeline);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        /* setting up the adapter for recyclerView*/
        // initalizing the user list and adapter
        userList = new ArrayList<UserData>();
        adapter = new UserAdapter(userList, getActivity());

        // This method is called for setting the item click listener for recyclerview
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        // adding the divider in recylerview
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
    }



    @Override
    public void onClick(View view, int position) {

        ArrayList<String> listUrl = new ArrayList<String>();
        for(int i=0;i<userList.size();i++)
        {
            listUrl.add(userList.get(i).userProfileImageUrl);
        }
        mCallback.onImageClick(listUrl, position);
    }

    private void retry()
    {
        fetchData();
    }

    private void fetchData()
    {
        // making progress bar visible
        progressBar.setVisibility(View.VISIBLE);

        // getting status service
        StatusesService service =
                TwitterCore.getInstance().getApiClient().getStatusesService();

        // calling userTimeline for fetching recent tweets from "@humblebrag" timeline
        Call<List<Tweet>> tweets = service.userTimeline
                (       null,
                        Utilities.TWITTER_HANDLE,       // user screen name
                        100,                            // no of tweets to fetch
                        null,
                        null,
                        null,
                        null,
                        null,
                        null

                );

        // sending the asynchronous request
        tweets.enqueue(new Callback<List<Tweet>>()
        {
            @Override
            public void success(Result<List<Tweet>> result)
            {
                // tweet list contains all the tweets from timeline
                List<Tweet> tweetList = result.data;

                // adding people retweeted by twitter handle
                for(int i=0;i<tweetList.size(); i++)
                {
                    // checking if it was retweet
                    if(tweetList.get(i).retweetedStatus != null)
                    {
                        // it was a retweet
                        // adding original author of tweet to userList

                        Tweet orgTweet = tweetList.get(i).retweetedStatus;  // getting original tweet
                        String userName = orgTweet.user.name;               // getting userName
                        String userScreenName = "@" + orgTweet.user.screenName;   // getting original tweet author
                        String userImageUrl = orgTweet.user.profileImageUrl;    // getting profile image url of author
                        String userTweet = orgTweet.text;                       // getting the tweet
                        String time = tweetList.get(i).createdAt;
                        Log.e("Tweeted at: ", time);
                        userList.add(new UserData(userName,userScreenName, userImageUrl, userTweet));     // creating a new user data and adding it to list

                    }


                    // removing the progress bar
                    progressBar.setVisibility(View.GONE);

                    // notify the adapter about new data
                    adapter.notifyDataSetChanged();

                }

            }


            @Override
            public void failure(TwitterException exception)
            {
                // profile image fetching failed
                // removing progress bar
                progressBar.setVisibility(View.GONE);

                // retry button is shown
                retry_button.setVisibility(View.VISIBLE);
            }
        });
    }
}
