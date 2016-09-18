package com.sareen.squarelabs.humblebrag;

/**
 * Created by Ashish on 18-09-2016.
 */

/* This contains the user screen name and url of
* profile image of user*/
public class UserData
{
    public String userName;
    public String userScreenName;
    public String userProfileImageUrl;
    public String userTweet;

    public UserData(String name, String screenName, String url, String tweet)
    {
        userName = name;
        userScreenName = screenName;
        userProfileImageUrl = url;
        userTweet = tweet;
    }
}
