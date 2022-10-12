package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowUser extends AppCompatActivity {


    TextView nametv,emailtv,requesttv,biot,proft;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference databaseReference,databaseReference1,databaseReference2,followingRef,followingdel,postnoref,db1,db2,ntref,nref1;
    TextView button,followers_tv,following_tv,posts_tv;
    CardView followers_cv,following_cv,posts_cd;
    String url,name,proff,email,privacy,p,website,bio,userid,usertoken;
    RequestMember requestMember;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CollectionReference posts,q3,q4,q5;
    All_UserMmber fmember;
    Boolean likechecker,commentchecker = false;
    DatabaseReference likeref,likelist,Commentref;
    String name_result;
    String uidreq,namereq,urlreq,professionreq;
    NewMember newMember;

    SwipeRefreshLayout refreshLayout;
    TextView sendmessage;
    int postNo ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference,documentReference1;

    int followercount,postiv,postvv;
    int followingcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        database = FirebaseDatabase.getInstance();

        sendmessage = findViewById(R.id.btn_sendmessage_showuserr);
        requestMember = new RequestMember();
        fmember = new All_UserMmber();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();


        recyclerView = findViewById(R.id.rv_posts);
        recyclerView.setHasFixedSize(true);

        newMember = new NewMember();



        refreshLayout = findViewById(R.id.refresh_show_user);
        nametv = findViewById(R.id.name_tv_showprofile);
        biot = findViewById(R.id.bio_tv_showprofile);
        proft = findViewById(R.id.prof_tv_showprofile);
        emailtv = findViewById(R.id.email_tv_showProfile);
        imageView = findViewById(R.id.imageView_showprofile);
        button = findViewById(R.id.btn_requestshowprofile);
        requesttv = findViewById(R.id.tv_requestshowprofile);

        followers_tv = findViewById(R.id.followerNo_tv);
        following_tv = findViewById(R.id.followingNo_tv);
        posts_tv = findViewById(R.id.postsNo_tv);
        followers_cv = findViewById(R.id.followers_cardview);
        following_cv = findViewById(R.id.following_cardview);
        posts_cd =findViewById(R.id.posts_cardview);

        likeref = database.getReference("post likes");

        Commentref = database.getReference("Comments");

        posts = db.collection("All posts");
        q3 = db.collection("All images");
        q4 = db.collection("All videos");
        q5 = db.collection("WriteUp");

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            url = extras.getString("u");
            name = extras.getString("n");
            userid = extras.getString("uid");
            proff = extras.getString("pro");
        }else {
            //   Toast.makeText(this, "Privact account", Toast.LENGTH_SHORT).show();
        }

        databaseReference = database.getReference("Requests").child(userid);
        databaseReference1 = database.getReference("followers").child(userid);
        followingRef = database.getReference("following");
        documentReference = db.collection("user").document(userid);
        postnoref = database.getReference("User Posts").child(userid);
        followingdel = database.getReference("following").child(currentUserId);
        databaseReference2  = database.getReference("followers");
        documentReference1 = db.collection("user").document(currentUserId);
        db1 = database.getReference("All images").child(userid);
        db2 = database.getReference("All videos").child(userid);

        ntref = database.getReference("notification").child(currentUserId);
        nref1 = database.getReference("notification").child(userid);

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowUser.this,MessageActivity.class);
                intent.putExtra("n",name);
                intent.putExtra("u",url);
                intent.putExtra("uid",userid);
                startActivity(intent);
            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = button.getText().toString();
                if (status.equals("Follow")){
                    follow();
                }else if (status.equals("Requested")){
                    delRequest();
                }else if (status.equals("Following")){
                    unFollow();
                }

            }
        });

        followers_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowUser.this,FollowerActivity.class);
                intent.putExtra("u",userid);
                startActivity(intent);
            }
        });

        following_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowUser.this,Following.class);
                intent.putExtra("u",userid);
                startActivity(intent);
            }
        });

    }

    private void delRequest() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();
        databaseReference.child(currentUserId).removeValue();
        button.setText("Follow ");
    }

    @Override
    protected void onStart() {
        super.onStart();

        postnoref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                postNo = (int)snapshot.getChildrenCount();
                posts_tv.setText(Integer.toString(postNo));
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            String name_result = task.getResult().getString("name");
                            String age_result = task.getResult().getString("prof");
                            String bio_result = task.getResult().getString("bio");
                            String email_result = task.getResult().getString("email");
                            String web_result = task.getResult().getString("web");
                            String Url = task.getResult().getString("url");
                            p = task.getResult().getString("privacy");


                            if (p.equals("Public")){
                                biot.setText(bio_result);
                                nametv.setText(name_result);
                                proft.setText(age_result);
                                emailtv.setText(email_result);
                                Picasso.get().load(Url).into(imageView);
                                requesttv.setVisibility(View.GONE);
                            }else {

                                String u = button.getText().toString();
                                if (u.equals("Following")){
                                    biot.setText(bio_result);
                                    nametv.setText(name_result);
                                    proft.setText(age_result);
                                    emailtv.setText(email_result);
                                    Picasso.get().load(Url).into(imageView);
                                    requesttv.setVisibility(View.GONE);
                                }else {
                                    biot.setText("*****************");
                                    nametv.setText(name_result);
                                    proft.setText("*****************");
                                    emailtv.setText("*****************");
                                    Picasso.get().load(Url).into(imageView);
                                    requesttv.setVisibility(View.VISIBLE);
                                }

                            }





                        }else {
                            Toast.makeText(ShowUser.this, "No Profile exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        documentReference1.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            namereq = task.getResult().getString("name");
                            professionreq = task.getResult().getString("prof");
                            urlreq = task.getResult().getString("url");


                        }else {
                            //  Toast.makeText(ShowUser.this, "No Profile exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });



        // refernce for following
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    followercount = (int)snapshot.getChildrenCount();
                    followers_tv.setText(Integer.toString(followercount));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(userid).exists()){
                    followingcount = (int)snapshot.child(userid).getChildrenCount();
                    following_tv.setText(Integer.toString(followingcount));


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(currentUserId)){
                    button.setText("Requested");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(userid).hasChild(currentUserId)){
                    button.setText("Following");
                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        FirestoreRecyclerOptions<Postmember> options =
                new FirestoreRecyclerOptions.Builder<Postmember>()
                        .setQuery(posts.whereEqualTo("uid",userid).orderBy("time", com.google.firebase.firestore.Query.Direction.ASCENDING),Postmember.class)
                        .build();

        FirestoreRecyclerAdapter<Postmember,PostViewholder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<Postmember, PostViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewholder holder, int position, @NonNull final Postmember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = getSnapshots().getSnapshot(position).getId();
                        holder.SetPost(ShowUser.this,model.getName(),model.getUrl(),model.getPostUri(),model.getWriteup(),model.getTime()
                                ,model.getUid(),model.getType(),model.getDesc());



                        final String url = getItem(position).getPostUri();
                        final String name = getItem(position).getName();
                        final String urll = getItem(position).getUrl();
                        final  String time = getItem(position).getTime();
                        final String type = getItem(position).getType();
                        final String userid = getItem(position).getUid();
                        final String  desc = getItem(position).getDesc();
                        final String writeup = getItem(position).getWriteup();

                        holder.likeschecker(postkey);
                        holder.commentchecker(postkey);

                        holder.menuoptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDialog(name,url,time,userid,type,postkey,writeup,desc);
                            }
                        });

                        holder.tv_nameprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(ShowUser.this,MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(ShowUser.this,ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",urll);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });

                        holder.imageViewprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(ShowUser.this,MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(ShowUser.this,ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",urll);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });
                        holder.likebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                likechecker = true;

                                likeref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (likechecker.equals(true)){
                                            if (snapshot.child(postkey).hasChild(currentUserid)){
                                                likeref.child(postkey).child(currentUserid).removeValue();
                                                likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                                likelist.removeValue();

                                                ntref.child(currentUserid+"l").removeValue();

                                                likechecker = false;
                                            }else {

                                                likeref.child(postkey).child(currentUserid).setValue(true);
                                                likelist = database.getReference("like list").child(postkey);
                                                fmember.setName(namereq);
                                                fmember.setUid(currentUserid);
                                                fmember.setUrl(urlreq);
                                                likelist.child(currentUserid).setValue(fmember);

                                                newMember.setName(namereq);
                                                newMember.setUid(currentUserid);
                                                newMember.setUrl(urlreq);
                                                newMember.setSeen("no");
                                                newMember.setText("Liked Your Post ");

                                                ntref.child(currentUserid+"l").setValue(newMember);

                                                sendNotification(userid,name_result);
                                                likechecker= false;


                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                        holder.tv_likes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ShowUser.this,ShowLikedUser.class);
                                intent.putExtra("p",postkey);
                                intent.putExtra("u",urll);
                                intent.putExtra("uid",userid);
                                startActivity(intent);
                            }
                        });



                        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ShowUser.this,CommentsActivity.class);
                                intent.putExtra("postkey",postkey);
                                intent.putExtra("name",name);
                                intent.putExtra("url",urll);
                                intent.putExtra("uid",userid);
                                intent.putExtra("cap",desc);
                                intent.putExtra("type",type);
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public PostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_layout,parent,false);

                        return new PostViewholder(view);



                    }
                };
        firestoreRecyclerAdapter.startListening();

        recyclerView.setAdapter(firestoreRecyclerAdapter);













    }

    void follow() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();


        if (p.equals("Public")){
            button.setText("Following");
            requestMember.setUserid(currentUserId);
            requestMember.setProfession(professionreq);
            requestMember.setUrl(urlreq);
            requestMember.setName(namereq);

            databaseReference1.child(currentUserId).setValue(requestMember);



            fmember.setName(name);
            fmember.setProf(proff);
            fmember.setUid(userid);
            fmember.setUrl(url);
            if(!userid.equals(currentUserId)) {
                followingRef.child(currentUserId).child(userid).setValue(fmember);
            }
            newMember.setName(namereq);
            newMember.setUid(currentUserId);
            newMember.setUrl(urlreq);
            newMember.setSeen("no");
            newMember.setText("Started Following you ");

            sendNotification(userid,namereq);
            nref1.child(currentUserId+"f").setValue(newMember);


        }else {

            button.setText("Requested");
            requestMember.setUserid(currentUserId);
            requestMember.setProfession(professionreq);
            requestMember.setUrl(urlreq);
            requestMember.setName(namereq);
            databaseReference.child(currentUserId).setValue(requestMember);
            followingRef.child(currentUserId).child(userid).setValue(fmember);
            requesttv.setText("Wait until your request is accepted");

            sendNotification2(userid,name_result);

        }
    }

    private void sendNotification2(String userid, String name_result) {

        FirebaseDatabase.getInstance().getReference().child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usertoken = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FcmNotificationsSender notificationsSender =
                        new FcmNotificationsSender(usertoken,"Querious",name_result+" Sent You Follow request",
                                getApplicationContext(),ShowUser.this);

                notificationsSender.SendNotifications();

            }
        },3000);

    }

    private void unFollow() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = user.getUid();

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowUser.this);
        builder.setTitle("Unfollow")
                .setMessage("Are you sure to Unfollow?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference1.child(currentUserId).removeValue();
                        followingdel.child(userid).removeValue();
                        ntref.child(currentUserId+"f").removeValue();
                        button.setText("Follow");
                        Toast.makeText(ShowUser.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create();
        builder.show();
    }

    private void sendNotification(String userid,String name_result){

        FirebaseDatabase.getInstance().getReference().child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usertoken = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                FcmNotificationsSender notificationsSender =
                        new FcmNotificationsSender(usertoken,"Querious",name_result+" Started Following you",
                                getApplicationContext(),ShowUser.this);

                notificationsSender.SendNotifications();

            }
        },3000);

    }

    void showDialog(String name,String url,String time,String userid,String type,String postkey,String writeup,String desc){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_options);

        TextView download = dialog.findViewById(R.id.download_tv_post);
        TextView share = dialog.findViewById(R.id.share_tv_post);
        TextView delete = dialog.findViewById(R.id.delete_tv_post);
        TextView copyurl = dialog.findViewById(R.id.copyurl_tv_post);
        TextView edit = dialog.findViewById(R.id.edit_post);
        EditText captionEt = dialog.findViewById(R.id.et_caption);
        Button button = dialog.findViewById(R.id.btn_edit_caption);
        TextView report = dialog.findViewById(R.id.report);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();


        if(type.equals("text"))
        {
            download.setVisibility(View.GONE);
            copyurl.setVisibility(View.GONE);
            share.setVisibility(View.GONE);

        }


        if (userid.equals(currentUserid))
        {
            delete.setVisibility(View.VISIBLE);
            report.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
        }


        else if (!userid.equals(currentUserid)) {
            report.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }

        else
        {
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                captionEt.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                if(type.equals("text"))
                {
                captionEt.setText(writeup);}
                else
                {
                    captionEt.setText(desc);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String edit = captionEt.getText().toString();
                        if(type.equals("text"))
                        {


                            posts.whereEqualTo("time",time ).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().update("writeup",edit);
                                            Toast.makeText(ShowUser.this, "Edited", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                }}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShowUser.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        else {

                            posts.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().update("desc",edit);
                                            Toast.makeText(ShowUser.this, "Edited", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                }}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ShowUser.this, "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }

                    }
                });


            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likechecker = true;
                commentchecker = true;

                if(type.equals("iv") || type.equals("vv")) {

                    q3.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(ShowUser.this, "Deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });


                    postnoref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                postnoref.child(postkey).removeValue();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                    q4.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(ShowUser.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                    posts.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(ShowUser.this, "Deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });

                    likeref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (likechecker.equals(true)){
                                if (snapshot.child(postkey).hasChild(currentUserid)) {
                                    likeref.child(postkey).child(currentUserid).removeValue();
                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                    likelist.removeValue();

                                    ntref.child(currentUserid + "l").removeValue();
                                    likechecker = false;
                                }
                            }else {


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    Commentref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {



                            if (commentchecker.equals(true)){
                                if (snapshot.hasChild(postkey)) {
                                    Commentref.child(postkey).removeValue();
                                    commentchecker = false;

                                }}else {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                    reference.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ShowUser.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    posts.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(ShowUser.this, "Deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });

                    q5.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(ShowUser.this, "Deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });


                    postnoref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                postnoref.child(postkey).removeValue();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    likeref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (likechecker.equals(true)){
                                if (snapshot.child(postkey).hasChild(currentUserid)) {
                                    likeref.child(postkey).child(currentUserid).removeValue();
                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                    likelist.removeValue();

                                    ntref.child(currentUserid + "l").removeValue();
                                    likechecker = false;
                                }

                            }else {


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Commentref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (commentchecker.equals(true)){
                                if (snapshot.hasChild(postkey)) {
                                    Commentref.child(postkey).removeValue();
                                    commentchecker = false;
                                }}else
                            {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                dialog.dismiss();

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        if (type.equals("iv")){

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading image....");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name+System.currentTimeMillis() + ".jpg");
                            DownloadManager manager = (DownloadManager)ShowUser.this.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(ShowUser.this, "Downloading", Toast.LENGTH_SHORT).show();


                        }else {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading video....");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name+System.currentTimeMillis() + ".mp4");
                            DownloadManager manager = (DownloadManager)ShowUser.this.getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(ShowUser.this, "Downloading", Toast.LENGTH_SHORT).show();


                        }

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                        Toast.makeText(ShowUser.this, "No permissions", Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.with(ShowUser.this)
                        .setPermissionListener(permissionListener)
                        .setPermissions(Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();


                dialog.dismiss();


            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sharetext = name +"\n" +"\n"+ url;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT,sharetext);
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent,"share via"));

                dialog.dismiss();

            }
        });

        copyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager cp = (ClipboardManager)ShowUser.this.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("String",url);
                cp.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(ShowUser.this, "", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
