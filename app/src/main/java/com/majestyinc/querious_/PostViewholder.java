package com.majestyinc.querious_;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Collections;

public class PostViewholder extends RecyclerView.ViewHolder {

    ImageView imageViewprofile,iv_post;
    TextView tv_name,tv_desc,tv_likes,tv_comment,tv_time,tv_nameprofile,Writeup;
    ImageButton likebtn,menuoptions,commentbtn;
    DatabaseReference likesref,commentref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int likescount,commentcount;




    public PostViewholder(@NonNull View itemView) {
        super(itemView);
    }

    public void SetPost(FragmentActivity activity, String name, String url,String postUri,String writeup,String time,
                        String uid,String type,String desc) {

        SimpleExoPlayer exoPlayer =  new SimpleExoPlayer.Builder(activity).build();
        imageViewprofile = itemView.findViewById(R.id.ivprofile_item);
        iv_post = itemView.findViewById(R.id.iv_post_item);
        tv_desc = itemView.findViewById(R.id.tv_desc_post);
        commentbtn = itemView.findViewById(R.id.commentbutton_posts);
        likebtn = itemView.findViewById(R.id.likebutton_posts);
        tv_likes = itemView.findViewById(R.id.tv_likes_post);
        menuoptions = itemView.findViewById(R.id.morebutton_posts);
        tv_time = itemView.findViewById(R.id.tv_time_post);
        tv_nameprofile = itemView.findViewById(R.id.tv_name_post);
        Writeup = itemView.findViewById(R.id.writeup);



        PlayerView playerView = itemView.findViewById(R.id.exoplayer_item_post);

        switch (type)
        {
            case "iv":
                Picasso.get().load(url).into(imageViewprofile);
                Picasso.get().load(postUri).into(iv_post);
                tv_desc.setText(desc);
                tv_time.setText(time);
                tv_nameprofile.setText(name);
                tv_desc.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.INVISIBLE);
                iv_post.setVisibility(View.VISIBLE);
                Writeup.setVisibility(View.GONE);
                if(exoPlayer!=null && exoPlayer.isPlaying()){
                exoPlayer.pause();
                }
                break;

            case "text":
                iv_post.setVisibility(View.GONE);
                playerView.setVisibility(View.GONE);
                tv_desc.setVisibility(View.GONE);
                menuoptions.setVisibility(View.VISIBLE);
                tv_time.setText(time);
                tv_nameprofile.setText(name);
                Writeup.setText(writeup);
                Writeup.setVisibility(View.VISIBLE);
                Picasso.get().load(url).into(imageViewprofile);
                if(exoPlayer!=null && exoPlayer.isPlaying()){
                    exoPlayer.pause();
                }
                break;

            case "vv":
                iv_post.setVisibility(View.GONE);
                tv_desc.setText(desc);
                tv_time.setText(time);
                tv_nameprofile.setText(name);
                tv_desc.setVisibility(View.VISIBLE);
                Picasso.get().load(url).into(imageViewprofile);
                Writeup.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);



                try {
                     exoPlayer = new SimpleExoPlayer.Builder(activity).build();
                    playerView.setPlayer(exoPlayer);
                    MediaItem mediaItem = MediaItem.fromUri(postUri);
                    exoPlayer.addMediaItems(Collections.singletonList(mediaItem));
                    exoPlayer.prepare();
                    exoPlayer.setPlayWhenReady(false);
                } catch (Exception e) {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
                }

                break;

        }

//        if (type.equals("iv")) {
//
//            Picasso.get().load(url).into(imageViewprofile);
//            Picasso.get().load(postUri).into(iv_post);
//            tv_desc.setText(desc);
//            tv_time.setText(time);
//            tv_nameprofile.setText(name);
//            playerView.setVisibility(View.INVISIBLE);
//            Writeup.setVisibility(View.GONE);
//        }
//
//        else if (type.equals("text")){
//            iv_post.setVisibility(View.GONE);
//            playerView.setVisibility(View.GONE);
//            tv_desc.setVisibility(View.GONE);
//            menuoptions.setVisibility(View.VISIBLE);
//            tv_time.setText(time);
//            tv_nameprofile.setText(name);
//            Writeup.setText(writeup);
//            Picasso.get().load(url).into(imageViewprofile);
//        }
//
//        else if (type.equals("vv")) {
//            iv_post.setVisibility(View.GONE);
//            tv_desc.setText(desc);
//            tv_time.setText(time);
//            tv_nameprofile.setText(name);
//            Picasso.get().load(url).into(imageViewprofile);
//            Writeup.setVisibility(View.GONE);
//            playerView.setVisibility(View.VISIBLE);
//
//
//            try {
//                SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(activity).build();
//                playerView.setPlayer(simpleExoPlayer);
//                MediaItem mediaItem = MediaItem.fromUri(postUri);
//                simpleExoPlayer.addMediaItems(Collections.singletonList(mediaItem));
//                simpleExoPlayer.prepare();
//                simpleExoPlayer.setPlayWhenReady(false);
//            } catch (Exception e) {
//                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show();
//            }
//        }


    }

    public void likeschecker(final String postkey) {
        likebtn = itemView.findViewById(R.id.likebutton_posts);


        likesref = database.getReference("post likes");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postkey).hasChild(uid)) {
                    likebtn.setImageResource(R.drawable.ic_like);
                    likescount = (int) snapshot.child(postkey).getChildrenCount();
                    if(snapshot.child(postkey).getChildrenCount()==1)
                    {
                        tv_likes.setText(Integer.toString(likescount) + " Like");
                    }
                    else {
                        tv_likes.setText(Integer.toString(likescount) + " Likes");
                    }
                } else {
                     likebtn.setImageResource(R.drawable.ic_dislike);
                     likescount = (int) snapshot.child(postkey).getChildrenCount();
                    if(snapshot.child(postkey).getChildrenCount()==1)
                    {
                        tv_likes.setText(Integer.toString(likescount)+" Like");
                    }
                    else
                    {
                        tv_likes.setText(Integer.toString(likescount)+" Likes");
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void commentchecker(final String postkey) {

        tv_comment = itemView.findViewById(R.id.tv_comment_post);


        commentref = database.getReference("Comments").child(postkey);

        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                commentcount = (int) snapshot.getChildrenCount();
                if(snapshot.getChildrenCount() ==1)
                {
                    tv_comment.setText(Integer.toString(commentcount)+" Comment");
                }
                else{
                tv_comment.setText(Integer.toString(commentcount)+" Comments");
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

}