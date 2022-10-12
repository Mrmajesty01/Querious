package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class YourQues extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DatabaseReference AllQuestions,UserQuestions,fav_questions ;
    CollectionReference userques;
    DocumentReference userq;
    String currentUserid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your);

        recyclerView = findViewById(R.id.rv_yourque);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserid = user.getUid();

        AllQuestions = database.getReference("AllQuestions");
        UserQuestions = database.getReference("UserQuestions").child(currentUserid);

        userques =  firestore.collection("AllQuestions");
        userq =  firestore.collection("AllQuestions").document();


        fav_questions = database.getReference("favoriteList").child(currentUserid);

        FirestoreRecyclerOptions<QuestionMember> options =
                new FirestoreRecyclerOptions.Builder<QuestionMember>()
                        .setQuery(userques.orderBy("time", com.google.firebase.firestore.Query.Direction.ASCENDING).whereEqualTo("userid",currentUserid),QuestionMember.class)
                        .build();

        FirestoreRecyclerAdapter<QuestionMember,Viewholder_Question> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<QuestionMember, Viewholder_Question>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder_Question holder, int position, @NonNull final QuestionMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = userques.document(String.valueOf(position)).getId();

                        final  String postky =  getSnapshots().getSnapshot(position).getId();



                        holder.setitemdelete(getApplication(),model.getName(),model.getUrl(),model.getUserid(),model.getKey()
                                ,model.getQuestion(),model.getPrivacy(),model.getTime());

                        holder. RepliesChecker(postky);

                        final String uid = getItem(position).getUserid();
                        final String que = getItem(position).getQuestion();

                        final  String time = getItem(position).getTime();
                        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete(time);
                            }
                        });

                        holder.cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(YourQues.this,ReplyActivity.class);
                                intent.putExtra("uid",uid);
                                intent.putExtra("q",que);
                                intent.putExtra("postkey",postky);
                                //  intent.putExtra("key",privacy);
                                startActivity(intent);

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public Viewholder_Question onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.yourque_item,parent,false);

                        return new Viewholder_Question(view);



                    }
                };
        firestoreRecyclerAdapter.startListening();

        recyclerView.setAdapter(firestoreRecyclerAdapter);


    }

    void delete(String time){


                userques.whereEqualTo("time",time).whereEqualTo("userid",currentUserid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                                Toast.makeText(YourQues.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        } else {

                        }
                    }
                });




        Query query2 = fav_questions.orderByChild("time").equalTo(time);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(YourQues.this, "Deleted", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }}

