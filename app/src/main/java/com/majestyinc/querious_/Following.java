package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Following extends AppCompatActivity {

    RecyclerView recyclerView;
    String userid;
    DatabaseReference databaseReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        recyclerView = findViewById(R.id.rv_following);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(Following.this));
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            userid = bundle.getString("u");

        }else {
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

        databaseReference = database.getReference("following").child(userid);




    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<All_UserMmber> options1 =
                new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                        .setQuery(databaseReference,All_UserMmber.class)
                        .build();

        FirebaseRecyclerAdapter<All_UserMmber,ProfileViewholder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewholder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_UserMmber model) {

                        final String postkey = getRef(position).getKey();



                        String  name = getItem(position).getName();
                        String  url = getItem(position).getUrl();
                        String currentuserid = getItem(position).getUid();




                        holder.setFollower(getApplication(),model.getName(),model.getUrl(),model.getProf());

                        if(id.equals(currentuserid)) {
                            holder.vpfollower.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Following.this, MainActivity.class);
                                    intent.putExtra("n", name);
                                    intent.putExtra("u", url);
                                    intent.putExtra("uid", currentuserid);
                                    startActivity(intent);
                                }
                            });
                        }

                        else {


                            holder.vpfollower.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(Following.this, ShowUser.class);
                                    intent.putExtra("n", name);
                                    intent.putExtra("u", url);
                                    intent.putExtra("uid", currentuserid);
                                    startActivity(intent);
                                }
                            });
                        }
                        }




                    @NonNull
                    @Override
                    public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.follower_layout,parent,false);

                        return new ProfileViewholder(view);
                    }
                };


        firebaseRecyclerAdapter1.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter1);
//
    }
}
