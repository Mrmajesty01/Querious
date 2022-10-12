package com.majestyinc.querious_;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class ShowStory extends AppCompatActivity implements StoriesProgressView.StoriesListener {


    int counter = 0;
    ImageView imageViewSHowStory,imageViewUrl;
    TextView textView,tv_view,tvcap,time;
    RequestMember member;



    ImageButton deletebtn;

    List<String> posturi;
    List<String>url;
    List<String>username;
    List<String>caption;
    List<Long>timeEnd;
    List<String>timeUpload;
    List<String>viewws;
    String userid;
    String currentUid;


    StoriesProgressView storiesProgressView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference story,storyRef;
    DatabaseReference reference,viewRef,views;
    String id;
    DocumentReference users;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String nameViewer, urlViewer;



    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_story);


        currentUid = user.getUid();

        viewRef = database.getReference("views");
        imageViewSHowStory = findViewById(R.id.iv_storyview);
        imageViewUrl = findViewById(R.id.iv_ss);
        textView = findViewById(R.id.tv_uname_ss);
        time = findViewById(R.id.time_ss);
        storiesProgressView = findViewById(R.id.stories);
        member = new RequestMember();

        users = db.collection("user").document(currentUid);

        tvcap = findViewById(R.id.tv_cap_st);
        deletebtn = findViewById(R.id.btn_delstory);
        tv_view = findViewById(R.id.tv_views);





        tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowStory.this, ViewedBy.class);
                intent.putExtra("id",id);
                startActivity(intent);

            }
        });

        View reverse = findViewById(R.id.view_prev);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                storiesProgressView.pause();
                return false;
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        View next = findViewById(R.id.view_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesProgressView.skip();

            }
        });
        next.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                storiesProgressView.pause();
                return false;
            }
        });
        next.setOnTouchListener(onTouchListener);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            userid = bundle.getString("u");
        }else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        imageViewUrl.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                if(currentUid.equals(userid)){
                    Intent intent = new Intent(ShowStory.this,MainActivity.class);
                    startActivity(intent);
                }
                else {
                Intent intent = new Intent(ShowStory.this,ShowUser.class);
                intent.putExtra("n",username.get(counter));
                intent.putExtra("u",url.get(counter));
                intent.putExtra("uid",userid);
                startActivity(intent);
            }
            }
        });

        reference = database.getReference("story").child(userid);
        storyRef = db.collection("story");
        story = db.collection("All story");




        if (currentUid.equals(userid)){
            tv_view.setVisibility(View.VISIBLE);
            deletebtn.setVisibility(View.VISIBLE);
        }else {

            tv_view.setVisibility(View.INVISIBLE);
            deletebtn.setVisibility(View.INVISIBLE);
        }


        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                     delete();
                }
                catch (Exception e)
                {
                    Toast.makeText(ShowStory.this, "error: "+e, Toast.LENGTH_SHORT).show();
                }

//                    Query query = reference.orderByChild("timeEnd").equalTo(timeEnd.get(counter));
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
//                                dataSnapshot1.getRef().removeValue();
//
//                                StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(posturi.get(counter));
//                                storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        Toast.makeText(ShowStory.this, "deleted", Toast.LENGTH_SHORT).show();
//                                    }
//                                });

//                            }


//            }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        users.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.getResult().exists())
                {
                    nameViewer = task.getResult().getString("name");
                    urlViewer = task.getResult().getString("url");
                }
            }
        });

        getStories(userid);


    }

    private void getStories(String userid) {

        posturi = new ArrayList<>();
        username = new ArrayList<>();
        url = new ArrayList<>();
        timeEnd = new ArrayList<>();
        caption = new ArrayList<>();
        timeUpload = new ArrayList<>();





        story.orderBy("timeUpload", Query.Direction.ASCENDING).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                posturi.clear();
                url.clear();
                username.clear();
                timeEnd.clear();
                caption.clear();
                timeUpload.clear();



                for (QueryDocumentSnapshot document : task.getResult()){
                    StoryMember member = document.toObject(StoryMember.class);

                    id = document.getId();


                    posturi.add(member.getPostUri());
                    url.add(member.getUrl());
                    username.add(member.getName());
                    timeEnd.add(member.getTimeEnd());
                    timeUpload.add(member.getTimeUpload());
                    caption.add(member.getCaption());
                    views(currentUid);

                }

                storiesProgressView.setStoriesCount(posturi.size());
                storiesProgressView.setStoryDuration(5000L);
                storiesProgressView.setStoriesListener(ShowStory.this);
                storiesProgressView.startStories(counter);

                Picasso.get().load(posturi.get(counter)).into(imageViewSHowStory);
                Picasso.get().load(url.get(counter)).into(imageViewUrl);
                textView.setText(username.get(counter));
                tvcap.setText(caption.get(counter));
                time.setText(timeUpload.get(counter));


                ViewCount();



            }

        });

    }

    @Override
    public void onNext() {

        ViewCount();

        Picasso.get().load(posturi.get(++counter)).into(imageViewSHowStory);
        tvcap.setText(caption.get(counter));
        time.setText(timeUpload.get(counter));





    }

    @Override
    public void onPrev() {

        if ((counter-1) <0)return;
        Picasso.get().load(posturi.get(--counter)).into(imageViewSHowStory);
        tvcap.setText(caption.get(counter));
        time.setText(timeUpload.get(counter));

        ViewCount();




    }

    @Override
    public void onComplete() {

        finish();
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();


    }

    @Override
    protected void onPause() {
        storiesProgressView.pause();
        super.onPause();



    }

    private void views(String currentUid){


        if(userid.equals(currentUid))
        {

        }
        else
        {

            member.setName(nameViewer);
            member.setUrl(urlViewer);
            member.setUserid(currentUid);


            viewRef.child(id).child(userid).child(currentUid).setValue(member);
        }
    }
    private void ViewCount(){

        String timee = time.getText().toString();

         viewRef = database.getReference("views").child(id).child(currentUid);
        viewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                int viewCount = (int) snapshot.getChildrenCount();
                if(snapshot.getChildrenCount() ==1)
                {
                    tv_view.setText(viewCount+" View");
                }
                else
                {
                    tv_view.setText(viewCount+" Views");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    public void delete()
    {


        story.whereEqualTo("timeUpload",timeUpload.get(counter)).whereEqualTo("uid",currentUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        document.getReference().delete();
                        Toast.makeText(ShowStory.this, "deleted", Toast.LENGTH_SHORT).show();
                        finish();

                        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(posturi.get(counter));
                        storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(ShowStory.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }}
                else
                {

                    Toast.makeText(ShowStory.this, "error ", Toast.LENGTH_SHORT).show();

                }

            }
        });


        viewRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                    if (snapshot.child(id).hasChild(currentUid)) {
                        viewRef.child(id).child(currentUid).removeValue();

                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}