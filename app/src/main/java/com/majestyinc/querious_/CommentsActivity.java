package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommentsActivity extends AppCompatActivity {

    ImageView usernameImageview;
    TextView usernameTextview,description;
    Button commentsBtn;
    EditText commentsEdittext;
    public static String url,name,post_key,userid,bundleuid,desc,type;
    DatabaseReference Commentref,userCommentref,likesref,ntref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference dr;
    String name_model,name_result,age_result,Url,Urrl,uid,bio_result,web_result,email_result,usertoken,urll;
    RecyclerView recyclerView;
    Boolean likeChecker = false;
    DocumentReference docomment,douser,dolikes,donotif;
    NewMember newMember;
    CommentsMember commentsMember;
    CollectionReference collectionReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        commentsMember = new CommentsMember();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        documentReference = db.collection("user").document(userid);

        newMember = new NewMember();
        recyclerView = findViewById(R.id.recycler_view_comments);


        recyclerView.setHasFixedSize(true);
        //   MediaController mediaController;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentsBtn = findViewById(R.id.btn_comments);
        usernameImageview = findViewById(R.id.imageviewUser_comment);
        usernameTextview = findViewById(R.id.name_comments_tv);
        description = findViewById(R.id.desc);

        commentsEdittext = findViewById(R.id.et_comments);
        Bundle extras = getIntent().getExtras();

        if (extras != null){
            url = extras.getString("url");
            name = extras.getString("name");
            post_key = extras.getString("postkey");
            bundleuid = extras.getString("uid");
            desc = extras.getString("cap");
            type = extras.getString("type");
            if(type.equals("text") || type.equals("iv") || type.equals("vv"))
            {
                description.setVisibility(View.GONE);
            }


        }else
        {

        }



        Commentref = database.getReference("Comments").child(post_key);

        docomment = firebaseFirestore.collection("All Posts").document("Comments");

        collectionReference = firebaseFirestore.collection("All Posts");

        likesref = database.getReference("comment likes");

        dolikes = firebaseFirestore.collection("comment likes").document();

        userCommentref = database.getReference("User Posts").child(userid);

        douser = firebaseFirestore.collection("User Posts").document(userid);

        ntref = database.getReference("notification").child(bundleuid);

        donotif = firebaseFirestore.collection("notification").document(bundleuid);

        dr = firebaseFirestore.collection("All Posts").document("Comments");


        commentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Picasso.get().load(url).into(usernameImageview);
        usernameTextview.setText(name);
        description.setText(desc);




        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            name_result = task.getResult().getString("name");
                            age_result = task.getResult().getString("age");
                            bio_result = task.getResult().getString("bio");
                            email_result = task.getResult().getString("email");
                            web_result = task.getResult().getString("website");
                            Urrl = task.getResult().getString("url");
                            uid = task.getResult().getString("uid");
                        }
                    }
                });


        FirebaseRecyclerOptions<CommentsMember> options =
                new FirebaseRecyclerOptions.Builder<CommentsMember>()
                        .setQuery(Commentref.orderByChild("time"),CommentsMember.class)
                        .build();

        FirebaseRecyclerAdapter<CommentsMember,CommentsViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<CommentsMember, CommentsViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentsViewholder holder, int position, @NonNull CommentsMember model) {


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserId = user.getUid();
                        final String postkey =getRef(position).getKey();
                        String time = getItem(position).getTime();
                        String namee = getItem(position).getUsername();
                        String useridd = getItem(position).getUid();
                        urll =getItem(position).getUrl();

                        holder.setComment(getApplication(),model.getComment(),model.getTime(),model.getUrl(),model.getUsername(),model.getUid());

                        holder.LikeChecker(postkey);


                        holder.imageViewComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (useridd.equals(currentUserId)) {
                                    Intent intent = new Intent(CommentsActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(CommentsActivity.this,ShowUser.class);
                                    intent.putExtra("n",namee);
                                    intent.putExtra("u",urll);
                                    intent.putExtra("uid",useridd);
                                    startActivity(intent);
                                }


                            }
                        });




                        holder.nameComment.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (useridd.equals(currentUserId)) {
                                    Intent intent = new Intent(CommentsActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }
                                else {
                                        Intent intent = new Intent(CommentsActivity.this,ShowUser.class);
                                        intent.putExtra("n",namee);
                                        intent.putExtra("u",urll);
                                        intent.putExtra("uid",useridd);
                                        startActivity(intent);
                                    }


                                }
                        });


                        if (!useridd.equals(currentUserId))
                        {
                            holder.delete.setVisibility(View.INVISIBLE);
                        }

                        holder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(useridd.equals(currentUserId)){

                                Commentref.child(postkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                            dataSnapshot1.getRef().removeValue();

                                            Toast.makeText(CommentsActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });



                            }

                            }
                        });
                        holder.likebutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                likeChecker = true;

                                likesref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (likeChecker.equals(true)){
                                            if (snapshot.child(postkey).hasChild(currentUserId)){
                                                likesref.child(postkey).child(currentUserId).removeValue();
                                                likeChecker = false;

                                            }else {
                                                likesref.child(postkey).child(currentUserId).setValue(true);
                                                likeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CommentsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.comments_item,parent,false);

                        return new CommentsViewholder(view);
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }


    private void comment() {

        Calendar callfordate = Calendar.getInstance();
        SimpleDateFormat currentdate = new
                SimpleDateFormat("yyyy/MM/dd");
        final  String savedate = currentdate.format(callfordate.getTime());


        Calendar callfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new
                SimpleDateFormat("HH:mm");
        final  String savetime = currenttime.format(callfortime.getTime());

          String time = savedate+" "+ "at" +" "+savetime;
        String comment = commentsEdittext.getText().toString();
        if (!TextUtils.isEmpty(comment))
        {
            commentsMember.setComment(comment);
            commentsMember.setUsername(name_result);
            commentsMember.setUid(uid);
            commentsMember.setTime(time);
            commentsMember.setUrl(Urrl);


            String pushkey = Commentref.push().getKey();
            Commentref.child(pushkey).setValue(commentsMember);

            commentsEdittext.setText("");


            if(userid.equals(bundleuid))
            {

            }
            else {
                newMember.setName(name_result);
                newMember.setUid(uid);
                newMember.setUrl(Urrl);
                newMember.setSeen("no");
                newMember.setText("Commented on your post: " + comment);


                String key = ntref.push().getKey();
                ntref.child(key).setValue(newMember);
                sendNotification(bundleuid, name_result, comment);

                Toast.makeText(this, "Commented", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Please write comment", Toast.LENGTH_SHORT).show();
        }

    }

    private void sendNotification(String bundleuid, String name_result, String comment){

        FirebaseDatabase.getInstance().getReference().child(bundleuid).child("token")
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
                        new FcmNotificationsSender(usertoken,"Querious",name_result+" Commented on your post: " + comment,
                                getApplicationContext(),CommentsActivity.this);

                notificationsSender.SendNotifications();

            }
        },3000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}