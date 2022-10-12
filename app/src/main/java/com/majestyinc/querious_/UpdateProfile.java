package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;





public class UpdateProfile extends AppCompatActivity {

    EditText etname,etBio,etProfession,etEmail;
    Button button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference;
    DocumentReference documentReference ;
    DatabaseReference AllQuestions,UserQuestions,posts;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseDatabase md = FirebaseDatabase.getInstance();
    All_UserMmber member;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String  currentuid= user.getUid();
        documentReference = db.collection("user").document(currentuid);

        etBio = findViewById(R.id.et_bio_up);
        etname = findViewById(R.id.et_name_up);
        etEmail = findViewById(R.id.et_email_up);
        etProfession = findViewById(R.id.et_profession_up);
        button = findViewById(R.id.btn_up);
        member = new All_UserMmber();
        AllQuestions = database.getReference("AllQuestions");
        UserQuestions = database.getReference("UserQuestions");
        reference = database.getReference("All Users");
        posts = database.getReference("All posts");
        progressBar = findViewById(R.id.pv_updateprofile);

//        String name = documentReference.get().


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();



        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String webResult = task.getResult().getString("web");
                            String url = task.getResult().getString("url");
                            String profResult = task.getResult().getString("prof");


                            etname.setText(nameResult);
                            etBio.setText(bioResult);
                            etEmail.setText(emailResult);
                            etProfession.setText(profResult);
                        }else {
                            Toast.makeText(UpdateProfile.this, "No profile ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void updateProfile() {

        final String name = etname.getText().toString();
        final String bio = etBio.getText().toString();
        final String prof = etProfession.getText().toString();
        final String email =etEmail.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid1= user.getUid();
        final  DocumentReference sDoc = db.collection("user").document(currentuid1);

        final CollectionReference sDoc1 = db.collection("All images");

        final  CollectionReference sDoc2 = db.collection("All posts");

        final CollectionReference sDoc3 = db.collection("All videos");

        final  CollectionReference sDoc4 = db.collection("AllQuestions");

        final  CollectionReference sDoc5 = db.collection("All story");

        final  CollectionReference sDoc6 = db.collection("story");





        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sDoc);




                transaction.update(sDoc, "name",name.toUpperCase());
                transaction.update(sDoc,"prof",prof);
                transaction.update(sDoc,"email",email);
                transaction.update(sDoc,"bio",bio);


                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfile.this, "updated", Toast.LENGTH_SHORT).show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference db1,db2,db3,db4,db5,db6,db7;



                Map<String,Object > profile = new HashMap<>();
                profile.put("name",name.toUpperCase());
                profile.put("prof",prof);


                sDoc1.whereEqualTo("uid",currentuid1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot snapshot: task.getResult())
                        {
                            snapshot.getReference().update(profile);
                        }

                    }
                });


                sDoc2.whereEqualTo("uid",currentuid1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot snapshot: task.getResult())
                        {
                            snapshot.getReference().update(profile);
                        }

                    }
                });



                sDoc3.whereEqualTo("uid",currentuid1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot snapshot: task.getResult())
                        {
                            snapshot.getReference().update(profile);
                        }

                    }
                });


                sDoc4.whereEqualTo("userid",currentuid1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot snapshot: task.getResult())
                        {
                            snapshot.getReference().update(profile);
                        }

                    }
                });


                sDoc5.whereEqualTo("uid",currentuid1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot snapshot: task.getResult())
                        {
                            snapshot.getReference().update(profile);
                        }

                    }
                });


                sDoc6.whereEqualTo("uid",currentuid1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot snapshot: task.getResult())
                        {
                            snapshot.getReference().update(profile);
                        }

                    }
                });







                db4 = database.getReference("All Users");
                Query query3= db4.orderByChild("uid").equalTo(currentuid1);
                query3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().updateChildren(profile)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(UpdateProfile.this, "", Toast.LENGTH_SHORT).show();
                                        }




                                    });
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                db7 = database.getReference("favoriteList").child(currentuid1);
                Query query6= db7.orderByChild("userid").equalTo(currentuid1);
                query6.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            dataSnapshot.getRef().updateChildren(profile)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(UpdateProfile.this, "", Toast.LENGTH_SHORT).show();
                                        }




                                    });
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                             onBackPressed();
                    }
                },2000);



            }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });

//        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//                double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                progressBar.setProgress((int) progress);
//                button.setText(progress +" % Done");
//            }
//        });




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
