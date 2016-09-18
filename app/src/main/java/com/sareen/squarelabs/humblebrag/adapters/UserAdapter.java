package com.sareen.squarelabs.humblebrag.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sareen.squarelabs.humblebrag.ItemClickListener;
import com.sareen.squarelabs.humblebrag.R;
import com.sareen.squarelabs.humblebrag.UserData;
import com.sareen.squarelabs.humblebrag.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ashish on 18-09-2016.
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>
{
    // click listener for recycler view item
    private ItemClickListener clickListener;

    // array list containing user data
    private ArrayList<UserData> userList;
    private Context mContext;


    public UserAdapter(ArrayList<UserData> users, Context context)
    {
        this.userList = users;
        mContext = context;
    }


    // this method is called form timeline fragment to set click listener for recycler view items
    public void setClickListener(ItemClickListener itemClickListener)
    {
        this.clickListener = itemClickListener;
    }


    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item_profile_image, parent,false);

        return new UserViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        UserData user = userList.get(position);

        holder.userNameView.setText(user.userName);

        String imageUrl = Utilities.removeNormal(user.userProfileImageUrl);

        // loading profile image
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.error_placeholder)
                .error(R.drawable.error_placeholder)
                .resizeDimen(R.dimen.list_item_user_image, R.dimen.list_item_user_image)
                .centerInside()
                .into(holder.userImageView);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }




    public class UserViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        protected TextView userNameView;
        protected ImageView userImageView;


        public UserViewHolder(View v)
        {
            super(v);
            userNameView = (TextView)v.findViewById(R.id.list_item_user_name);
            userImageView = (ImageView) v.findViewById(R.id.list_item_user_image);
            // setting the click listener for the items
            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }


}

