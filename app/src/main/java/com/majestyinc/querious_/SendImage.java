package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.content.ContentResolver;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class SendImage extends AppCompatActivity {

    String urls,receiver_name,sender_uid,receiver_uid,url;
    ImageView imageView;
    Uri imageurl;
    ProgressBar progressBar;
    Button button;
    UploadTask uploadTask;

    TextView textView;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    DatabaseReference rootRef1,rootRef2,chats,chatsR;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String id = user.getUid();

    private  Uri uri;
    MessageMember messageMember;
    All_UserMmber userMmber;
    Postmember receivermember;
    String sender_name, sender_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_image);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            url = bundle.getString("u");
            receiver_name = bundle.getString("n");
            receiver_uid = bundle.getString("uid");
            urls =  bundle.getString("i");
            sender_uid = bundle.getString("suid");
            sender_name = bundle.getString("sn");
            sender_url = bundle.getString("surl");
        }

        else{
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }


        messageMember = new MessageMember();
        storageReference = firebaseStorage.getInstance().getReference("Message Images");
        chats = database.getReference("Chats").child(id);
        chatsR = database.getReference("Chats").child(receiver_uid);


        imageView = findViewById(R.id.iv_sendImage);
        button = findViewById(R.id.btn_sendimage);
        progressBar = findViewById(R.id.pb_sendImage);
        textView = findViewById(R.id.tv_dont);

        userMmber = new All_UserMmber();
        receivermember = new Postmember();


        Picasso.get().load(urls).into(imageView);
        imageurl = Uri.parse(urls);

        rootRef1 = database.getReference("Message").child(sender_uid).child(receiver_uid);
        rootRef2 = database.getReference("Message").child(receiver_uid).child(sender_uid);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImage();
                textView.setVisibility(View.VISIBLE);
            }
        });

    }
    private String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType((contentResolver.getType(uri)));
    }

    private void sendImage() {



            if (imageurl != null) {
                progressBar.setVisibility(View.VISIBLE);
                final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExt(imageurl));
                uploadTask = reference.putFile(imageurl);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            Calendar cdate = Calendar.getInstance();
                            SimpleDateFormat currentdate = new SimpleDateFormat("dd-MMMM-yyyy");
                            final String savedate = currentdate.format(cdate.getTime());

                            Calendar ctime = Calendar.getInstance();
                            SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss");
                            final String savetime = currenttime.format(ctime.getTime());

                            String time = savedate + ":" + savetime;


                            Calendar datee = Calendar.getInstance();
                            SimpleDateFormat currentdatee = new SimpleDateFormat("yyyy/MM/dd");
                            final  String savedatee = currentdatee.format(datee.getTime());

                            Calendar timee = Calendar.getInstance();
                            SimpleDateFormat currenttimee = new SimpleDateFormat("HH:mm:ss");
                            final String savetimee = currenttimee.format(timee.getTime());

                            String tim = savedatee +":"+ savetimee;




                            long deletetime = System.currentTimeMillis();


                            messageMember.setDate(savedate);
                            messageMember.setTime(savetime);
                            messageMember.setImage(downloadUri.toString());
                            messageMember.setReceiveruid(receiver_uid);
                            messageMember.setSenderuid(sender_uid);
                            messageMember.setType("i");
                            messageMember.setDelete(deletetime);

                            userMmber.setName(receiver_name);
                            userMmber.setUrl(url);
                            userMmber.setUid(receiver_uid);
                            userMmber.setTime(tim);
                            userMmber.setSeen("no");

                            chats.child(receiver_uid).setValue(userMmber);

                            receivermember.setName(sender_name);
                            receivermember.setUrl(sender_url);
                            receivermember.setUid(sender_uid);
                            receivermember.setTime(tim);
                            receivermember.setSeen("no");

                            chatsR.child(sender_uid).setValue(receivermember);


                            String id = rootRef1.push().getKey();
                            rootRef1.child(id).setValue(messageMember);

                            String id1 = rootRef2.push().getKey();
                            rootRef2.child(id1).setValue(messageMember);
                            progressBar.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SendImage.this, MessageActivity.class);
                                    intent.putExtra("n", receiver_name);
                                    intent.putExtra("uid", receiver_uid);
                                    intent.putExtra("u", url);
//                                intent.putExtra("i",urls);
                                    startActivity(intent);
                                }
                            }, 2000);

                        }

                    }
                });


            } else {
                Toast.makeText(this, "Please select something", Toast.LENGTH_SHORT).show();
        }


    }
}