package com.majestyinc.querious_;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FileUpload extends AppCompatActivity {

//    Button selectFile,upload;
//    EditText status;
//    Uri pdfUri;
//    String name;
//
//    FirebaseStorage fileStorage;
//    FirebaseDatabase fileDatabase;
//    ProgressDialog progressDialog;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_file_upload);
//
//        selectFile = findViewById(R.id.selectfile);
//        upload = findViewById(R.id.uploadfile);
//        status = findViewById(R.id.status);
//        name = status.getText().toString();
//
//
//
//        fileStorage = FirebaseStorage.getInstance();
//        fileDatabase = FirebaseDatabase.getInstance();
//
//
//        selectFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(ContextCompat.checkSelfPermission(FileUpload.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
//                {
//                    fetchFile();
//                }
//                else {
//                    ActivityCompat.requestPermissions(FileUpload.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
//                }
//            }
//        });
//
//        upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(pdfUri !=null) {
//                    UploadFile(pdfUri);
//                }
//                else
//                {
//                    Toast.makeText(FileUpload.this, "Please select a file", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void UploadFile(Uri pdfUri) {
//
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setProgressStyle(progressDialog.STYLE_HORIZONTAL);
//        progressDialog.setTitle("Uploading File....");
//        progressDialog.setProgress(0);
//        progressDialog.show();
//
//
//        final String fileName = System.currentTimeMillis()+".pdf";
//        final String filename = System.currentTimeMillis()+" ";
//
//
//        StorageReference storageReference = fileStorage.getReference();
//
//        storageReference.child("Files").child(fileName).putFile(pdfUri).
//                addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        String url = taskSnapshot.getStorage().getDownloadUrl().toString();
//
//                        DatabaseReference databaseReference =fileDatabase.getReference("FileName") ;
//
//                       databaseReference.child(filename).setValue(url).
//                               addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Toast.makeText(FileUpload.this, "File Successfully Uploaded", Toast.LENGTH_SHORT).show();
//                                    progressDialog.dismiss();
//                                }
//                                else
//                                    {
//                                        Toast.makeText(FileUpload.this, "File Not Uploaded", Toast.LENGTH_SHORT).show();
//                                    }
//                            }
//                        });
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(FileUpload.this, "File Not Uploaded", Toast.LENGTH_SHORT).show();
//
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//                int currentProgress = (int) (100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
//
//                progressDialog.setProgress(currentProgress);
//
//
//
//
//
//
//            }
//        });
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 9 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
//        {
//            fetchFile();
//        }
//
//        else
//            {
//                Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
//            }
//    }
//
//    private void fetchFile() {
//
//        Intent intent = new Intent();
//        intent.setType("application/pdf");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent,86);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 86 && resultCode == RESULT_OK && data != null)
//        {
//            pdfUri = data.getData();
//
//            status.setText("A file is selected  : " +data.getData().getLastPathSegment());
//        }
//
//        else
//        {
//            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
//        }
//
//
//    }
}
