package com.majestyinc.querious_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class writeActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    Postmember postmember;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference write,allpost;
    StorageReference storageReference;
    public static String url,name;
    UploadTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        editText = findViewById(R.id.writeup);
        button = findViewById(R.id.btn_writeup);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        postmember = new Postmember();

        write = database.getReference("WriteUp").child(currentuid);
        allpost = database.getReference("All posts");



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postWriteUp();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentuid = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("user").document(currentuid);

        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()) {
                            name = task.getResult().getString("name");
                            url = task.getResult().getString("url");


                        } else {
                            Toast.makeText(writeActivity.this, "error", Toast.LENGTH_SHORT).show();

                        }

                    }
                });



    }


    public void postWriteUp() {


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentuid = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        final String writeup = editText.getText().toString();

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String savedate = currentdate.format(cdate.getTime());

        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
        final String savetime = currenttime.format(ctime.getTime());


        final String time = savedate + ":" + savetime;


                    if (!TextUtils.isEmpty(writeup)) {


                        postmember.setName(name);
                        postmember.setUrl(url);
                        postmember.setWriteup(writeup);
                        postmember.setTime(time);
                        postmember.setUid(currentuid);
                        postmember.setType("text");


                        String id = write.push().getKey();
                        write.child(id).setValue(postmember);

                        String id1 = allpost.push().getKey();
                        allpost.child(id1).setValue(postmember);

                        Toast.makeText(writeActivity.this, "Posted", Toast.LENGTH_SHORT).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(writeActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, 2000);


                    }else {
                        Toast.makeText(writeActivity.this, "Please fill all Fields", Toast.LENGTH_SHORT).show();
                    }




    }
}

