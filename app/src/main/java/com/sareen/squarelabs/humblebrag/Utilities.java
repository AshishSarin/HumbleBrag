package com.sareen.squarelabs.humblebrag;

/**
 * Created by Ashish on 18-09-2016.
 */
public class Utilities
{
    // Public Constant
    public static final String IMAGE_URL_LIST = "imageUrls";
    public static final String SELECTED_POS = "selected_pos";
    public static final String TWITTER_HANDLE = "HumbleBrag";   // twitter handle to fetch  data from

    // removing "_normal" from image urls to get profile image of orginal size
    public static String removeNormal(String profileImageUrlHttps)
    {
        String img = profileImageUrlHttps.replaceAll("_normal", "");
        return img;
    }
}
