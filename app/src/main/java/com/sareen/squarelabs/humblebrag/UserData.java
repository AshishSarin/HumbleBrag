package com.sareen.squarelabs.humblebrag;

/**
 * Created by Ashish on 18-09-2016.
 */

/* This contains the user screen name and url of
* profile image of user*/
public class UserData
{
    public String userName;
    public String userProfileImageUrl;

    public UserData(String name, String url)
    {
        userName = name;
        userProfileImageUrl = url;
    }
}
