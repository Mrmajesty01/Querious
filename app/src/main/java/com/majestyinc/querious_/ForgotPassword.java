package com.majestyinc.querious_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.react.modules.toast.ToastModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgotPassword extends AppCompatActivity {

    Button fgtbtn;
    EditText fgtet;
    DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseAuth auth;
    String email = fgtet.getText().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fgtbtn = findViewById(R.id.button_fgt);
        fgtet = findViewById(R.id.forgot_pswd);
        auth = FirebaseAuth.getInstance();


    }

    public boolean validateEmail()
    {

        String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.isEmpty())
        {
            Toast.makeText(this, "Field can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!email.matches(emailpattern))
        {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    public void forgotPassword()
    {
        if(!validateEmail())
        {
            auth.sendPasswordResetEmail(fgtet.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(ForgotPassword.this, "check your mail to reset password", Toast.LENGTH_SHORT).show();
                        fgtet.setText("");
                    }
                    else
                    {
                        Toast.makeText(ForgotPassword.this, "enter correct email address", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ForgotPassword.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
