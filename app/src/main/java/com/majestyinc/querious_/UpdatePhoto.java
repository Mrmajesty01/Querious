package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class UpdatePhoto extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    ImageView imageView;
    UploadTask uploadTask;
    ProgressBar progressBar;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    String currentuid,myurl = "";
    Button button;
    private  final static int  PICK_IMAGE = 1;

    Uri imageuri,url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_photo);

        imageView = findViewById(R.id.iv_updatephoto);
        button = findViewById(R.id.btn_updatephoto);
        progressBar = findViewById(R.id.pv_updatephoto);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentuid = user.getUid();


        storageReference = FirebaseStorage.getInstance().getReference("Profile images");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImage();
            }
        });
    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {

            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null) {
                imageuri = data.getData();

                Picasso.get().load(imageuri).into(imageView);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error"+e, Toast.LENGTH_SHORT).show();
        }


    }
    private void updateImage() {
        final StorageReference reference = storageReference.child(System.currentTimeMillis()+ "."+getFileExt(imageuri));
        uploadTask = reference.putFile(imageuri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw  task.getException();
                }

                return reference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {

                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();

                    myurl = downloadUri.toString();

                    final DocumentReference sDoc = db.collection("user").document(currentuid);

                    final  CollectionReference sDoc1 = db.collection("All images");

                    final  CollectionReference sDoc2 = db.collection("All posts");

                    final CollectionReference sDoc3 = db.collection("All videos");

                    final  CollectionReference sDoc4 = db.collection("AllQuestions");

                    final  CollectionReference sDoc5 = db.collection("All story");

                    final  CollectionReference sDoc6 = db.collection("story");





                    db.runTransaction(new Transaction.Function<Void>() {
                        @Override
                        public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                            DocumentSnapshot snapshot = transaction.get(sDoc);


                            transaction.update(sDoc, "url",myurl );

                            // Success
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UpdatePhoto.this, "updated", Toast.LENGTH_SHORT).show();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference db1,db2,db3,db4,db5,db6,db7,db8,db9;



                            Map<String,Object > profile = new HashMap<>();
                            profile.put("url",downloadUri.toString());


                            sDoc1.whereEqualTo("uid",currentuid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(QueryDocumentSnapshot snapshot: task.getResult())
                                    {
                                        snapshot.getReference().update(profile);
                                    }

                                }
                            });


                            sDoc2.whereEqualTo("uid",currentuid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(QueryDocumentSnapshot snapshot: task.getResult())
                                    {
                                        snapshot.getReference().update(profile);
                                    }

                                }
                            });



                            sDoc3.whereEqualTo("uid",currentuid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(QueryDocumentSnapshot snapshot: task.getResult())
                                    {
                                        snapshot.getReference().update(profile);
                                    }

                                }
                            });


                            sDoc4.whereEqualTo("userid",currentuid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(QueryDocumentSnapshot snapshot: task.getResult())
                                    {
                                        snapshot.getReference().update(profile);
                                    }

                                }
                            });


                            sDoc5.whereEqualTo("uid",currentuid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(QueryDocumentSnapshot snapshot: task.getResult())
                                    {
                                        snapshot.getReference().update(profile);
                                    }

                                }
                            });


                            sDoc6.whereEqualTo("uid",currentuid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    for(QueryDocumentSnapshot snapshot: task.getResult())
                                    {
                                        snapshot.getReference().update(profile);
                                    }

                                }
                            });







                            db4 = database.getReference("All Users");
                            Query query3= db4.orderByChild("uid").equalTo(currentuid);
                            query3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(UpdatePhoto.this, "", Toast.LENGTH_SHORT).show();
                                                    }




                                                });
                                    }
                                }



                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            db7 = database.getReference("favoriteList").child(currentuid);
                            Query query6= db7.orderByChild("userid").equalTo(currentuid);
                            query6.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().updateChildren(profile)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(UpdatePhoto.this, "", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(UpdatePhoto.this, "failed", Toast.LENGTH_SHORT).show();
                                }
                            });



                }

            }
        });

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                button.setText(progress +" % Done");
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}