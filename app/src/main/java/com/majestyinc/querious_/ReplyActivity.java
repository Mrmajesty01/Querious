package com.majestyinc.querious_;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReplyActivity extends AppCompatActivity {



    String uid,question,post_key,key,name_result;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference ,reference2;
    AnswerMember member;
    DatabaseReference Allquestions,ntref;
    String name,url,time,usertoken;
    NewMember newMember;

    TextView nametv,questiontv,tvreply;
    RecyclerView recyclerView;
    ImageView imageViewQue;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference votesref;
    FirebaseFirestore fr = FirebaseFirestore.getInstance();
    DocumentReference votes, allQuest;
    String url_replier,name_replier,uid_replier;

    Button send;

    Boolean votechecker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        nametv = findViewById(R.id.name_reply_tv);
        questiontv = findViewById(R.id.que_reply_tv);
        imageViewQue = findViewById(R.id.iv_que_user);
        tvreply = findViewById(R.id.et_reply);
        send = findViewById(R.id.btn_reply);
        newMember = new NewMember();
        member = new AnswerMember();

        recyclerView = findViewById(R.id.rv_ans);
        recyclerView.setLayoutManager(new LinearLayoutManager(ReplyActivity.this));





        Bundle extra = getIntent().getExtras();
        if (extra != null){
            uid = extra.getString("uid");
            post_key = extra.getString("postkey");
            question = extra.getString("q");
            // key = extra.getString("key");
        }else {
            Toast.makeText(this, "opps", Toast.LENGTH_SHORT).show();
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();


        Allquestions = database.getReference("AllQuestions").child(post_key).child("Answers");
        votesref = database.getReference("votes");
        ntref = database.getReference("notification").child(uid);



        reference = db.collection("user").document(uid);
        reference2 = db.collection("user").document(currentuid);


        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                saveAnswer();

            }
        });






    }

    private void notification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nc = new NotificationChannel("n","n", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(nc);

            NotificationCompat.Builder  builder = new NotificationCompat.Builder(this,"n")
                    .setContentText("Querious")
                    .setSmallIcon(R.drawable.qrnot)
                    .setAutoCancel(true)
                    .setContentText(name_result+" Replied to your Question");

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder.build());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // question user refernce
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String urll = task.getResult().getString("url");
                            name_result = task.getResult().getString("name");
                            Picasso.get().load(urll).into(imageViewQue);
                            questiontv.setText(question);
                            nametv.setText(name_result);
                            url = task.getResult().getString("url");
                            name = task.getResult().getString("name");
                        }else {
                            Toast.makeText(ReplyActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        // refernce for replying user
        reference2.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                             url_replier = task.getResult().getString("url");
                            name_replier = task.getResult().getString("name");
                            uid_replier = task.getResult().getString("uid");

                        }else {
                            Toast.makeText(ReplyActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });





        FirebaseRecyclerOptions<AnswerMember> options =
                new FirebaseRecyclerOptions.Builder<AnswerMember>()
                        .setQuery(Allquestions.orderByChild("time"),AnswerMember.class)
                        .build();

        FirebaseRecyclerAdapter<AnswerMember,AnsViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AnswerMember, AnsViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AnsViewholder holder, int position, @NonNull final AnswerMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final String postkey = getRef(position).getKey();
                        final String userid = getItem(position).getUid();
                        final String answer = getItem(position).getAnswer();
                        final String timee = getItem(position).getTime();
                        final String Ref = getRef(position).getKey();


                        holder.setAnswer(getApplication(),model.getName(),model.getAnswer()
                                ,model.getUid(),model.getTime(),model.getUrl());



                        holder.nameReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(ReplyActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(ReplyActivity.this,ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });

                        holder.imageViewReply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(ReplyActivity.this,MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(ReplyActivity.this,ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });



                        if (!userid.equals(currentUserid))
                        {
                            holder.delRepy.setVisibility(View.INVISIBLE);
                        }


                        holder.delRepy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(userid.equals(currentUserid)){
                                    Query query = Allquestions.child(Ref);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                                                dataSnapshot1.getRef().removeValue();
//                                                votesref.child("upvotes").child(postkey).child(currentUserid).removeValue();
//                                                votesref.child("downvotes").child(postkey).child(currentUserid).removeValue();

                                                Toast.makeText(ReplyActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                }

                            }
                        });

                        holder.upvoteChecker(postkey);
                        holder.upVote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                votechecker = true;
                                votesref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (votechecker.equals(true)){
                                            if (snapshot.child("upvotes").child(postkey).hasChild(currentUserid)){
                                                votesref.child("upvotes").child(postkey).child(currentUserid).removeValue();
                                                votechecker = false;
                                            }else {
                                                votesref.child("upvotes").child(postkey).child(currentUserid).setValue(true);
                                                votesref.child("downvotes").child(postkey).child(currentUserid).removeValue();


                                                votechecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });


                        holder.downVote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                votechecker = true;
                                votesref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (votechecker.equals(true)){
                                            if (snapshot.child("downvotes").child(postkey).hasChild(currentUserid)){
                                                votesref.child("downvotes").child(postkey).child(currentUserid).removeValue();

                                                votechecker = false;
                                            }else {
                                                votesref.child("downvotes").child(postkey).child(currentUserid).setValue(true);
                                                votesref.child("upvotes").child(postkey).child(currentUserid).removeValue();

                                                votechecker = false;
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
                    public AnsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.ans_layout,parent,false);

                        return new AnsViewholder(view);



                    }
                };
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void saveAnswer(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        String answer = tvreply.getText().toString();
        if (!TextUtils.isEmpty(answer)){

            Calendar cdate = Calendar.getInstance();
            SimpleDateFormat currentdate = new SimpleDateFormat("yyyy/MM/dd");
            final  String savedate = currentdate.format(cdate.getTime());

            Calendar ctime = Calendar.getInstance();
            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm");
            final String savetime = currenttime.format(ctime.getTime());

             String time = savedate +" "+"at"+" "+ savetime;

            member.setAnswer(answer);
            member.setTime(time);
            member.setName(name_replier);
            member.setUid(uid_replier);
            member.setUrl(url_replier);

            String id = Allquestions.push().getKey();
            Allquestions.child(id).setValue(member);


            if(userid.equals(uid))
            {

            }
            else {
                newMember.setName(name_replier);
                newMember.setText("Replied To your Question: " + answer);
                newMember.setSeen("no");
                newMember.setUid(uid_replier);
                newMember.setUrl(url_replier);


                String key = ntref.push().getKey();
                ntref.child(key).setValue(newMember);

                sendNotification(uid, name_replier, answer);
            }
            Toast.makeText(this, "Submitted", Toast.LENGTH_SHORT).show();
            tvreply.setText("");
        }else {

            Toast.makeText(this, "Please  write answer", Toast.LENGTH_SHORT).show();
        }



    }
    private void sendNotification(String uid, String name_replier, String answer){

        FirebaseDatabase.getInstance().getReference().child(uid).child("token")
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
                        new FcmNotificationsSender(usertoken,"Querious",name_replier+" Replied To your Question: " + answer,
                                getApplicationContext(),ReplyActivity.this);

                notificationsSender.SendNotifications();

            }
        },3000);

    }

}