package com.majestyinc.querious_;

import android.app.Application;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ProfileViewholder extends RecyclerView.ViewHolder {

    TextView textViewName,textViewProfession,viewUserprofile;
    TextView namell,vp_ll,namefollower,vpfollower,professionFollower;

    ImageView imageView,iv_ll,iv_follower,imageViewChat;
    CardView cardView;
    LinearLayout cardView2;

    public ProfileViewholder(@NonNull View itemView) {
        super(itemView);
    }

    public void setProfile(FragmentActivity fragmentActivity, String name, String uid, String prof,
                           String url){

        cardView = itemView.findViewById(R.id.cardview_profile);
        textViewName = itemView.findViewById(R.id.tv_name_profile);
        textViewProfession = itemView.findViewById(R.id.tv_profession_profile);
        viewUserprofile = itemView.findViewById(R.id.viewUser_profile);
        imageView = itemView.findViewById(R.id.profile_imageview);



        Picasso.get().load(url).into(imageView);
        textViewProfession.setText(prof);
        textViewName.setText(name);

    }

    public void setProfileInchat(Application fragmentActivity, String name, String uid, String prof,
                                 String url){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

         imageViewChat = itemView.findViewById(R.id.iv_ch_item);
        TextView nametvChat = itemView.findViewById(R.id.name_ch_item_tv);
//        TextView proftv = itemView.findViewById(R.id.ch_itemprof_tv);
        cardView2 = itemView.findViewById(R.id.cardView_chat);


            Picasso.get().load(url).into(imageViewChat);
            nametvChat.setText(name);
//            proftv.setText(prof);



    }

    public void setLikeduser(Application fragmentActivity, String name, String uid, String prof,
                             String url){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();


        vp_ll = itemView.findViewById(R.id.vp_ll);
        namell = itemView.findViewById(R.id.name_ll);
        iv_ll = itemView.findViewById(R.id.iv_ll);

        Picasso.get().load(url).into(iv_ll);
        namell.setText(name);


    }

    public void setFollower( Application application, String name, String url,
                             String profesion){

        iv_follower = itemView.findViewById(R.id.iv_follower);
        namefollower = itemView.findViewById(R.id.name_follower);
        vpfollower = itemView.findViewById(R.id.vp_follower);

        Picasso.get().load(url).into(iv_follower);
        namefollower.setText(name);




    }



}
