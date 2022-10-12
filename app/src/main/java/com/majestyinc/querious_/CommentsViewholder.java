package com.majestyinc.querious_;

import android.app.Application;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CommentsViewholder extends RecyclerView.ViewHolder {


    ImageView imageViewComment;
    TextView nameComment,timeTv,ansTv,tv_likes;
    ImageButton likebutton,delete;
    int likescount ;
    DatabaseReference databaseReference;
    FirebaseDatabase database;



    public CommentsViewholder(@NonNull View itemView) {
        super(itemView);
    }


    public void setComment(Application application, String comment, String time, String url, String username, String uid){

        imageViewComment = itemView.findViewById(R.id.imageView_comment_item);
        nameComment = itemView.findViewById(R.id.tv_name_comment_item);
        timeTv = itemView.findViewById(R.id.tv_time_comment_item);
        ansTv = itemView.findViewById(R.id.tv_comment_item);
        delete = itemView.findViewById(R.id.del_comment);
        delete.setVisibility(View.VISIBLE);


        nameComment.setText(username);
        timeTv.setText(time);
        ansTv.setText(comment);
        Picasso.get().load(url).into(imageViewComment);


    }

    public void LikeChecker(String postkey) {


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("comment likes");
        likebutton = itemView.findViewById(R.id.likebutton_comment_item);

        tv_likes = itemView.findViewById(R.id.tv_like_comment_item);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(postkey).hasChild(currentUserId)){
                    likescount = (int)snapshot.child(postkey).getChildrenCount();
                    likebutton.setImageResource(R.drawable.ic_like);
                    if(snapshot.child(postkey).getChildrenCount()==1)
                    {
                        tv_likes.setText(Integer.toString(likescount)+" Like");
                    }
                    else
                    {
                        tv_likes.setText(Integer.toString(likescount)+" Likes");
                    }


                }else {
                    likescount = (int)snapshot.child(postkey).getChildrenCount();
                    tv_likes.setText(Integer.toString(likescount)+" Likes");
                    if(snapshot.child(postkey).getChildrenCount()==1)
                    {
                        tv_likes.setText(Integer.toString(likescount)+" Like");
                    }
                    else
                    {
                        tv_likes.setText(Integer.toString(likescount)+" Likes");
                    }
                    likebutton.setImageResource(R.drawable.ic_dislike);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}