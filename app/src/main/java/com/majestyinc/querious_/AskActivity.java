package com.majestyinc.querious_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String[] status = {"Choose category","General","Tech","Health","Education","Cars","Food","Sports","News","Politics","Business","Fashion","Beauty","Lifestyle","Travel"};
    EditText editText;
    ImageButton button;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference AllQuestions;
    static DatabaseReference UserQuestions;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference, allQuest;
    QuestionMember member;
    String name,url,privacy,uid,cat_value;
    Spinner spinner;
    TextView textView;
    LinearLayout linearLayout;







    public boolean isNetworkConnected(){
        boolean connected = false;

        try {

            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ninfo = cm.getActiveNetworkInfo();
            connected = ninfo != null && ninfo.isAvailable() && ninfo.isConnected();
            return connected;


        }

        catch (Exception e){

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return connected;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        linearLayout = findViewById(R.id.ll_ask);
        editText = findViewById(R.id.ask_et_question);
        button = findViewById(R.id.btn_submit);
        documentReference = db.collection("user").document(currentUserid);





        textView = findViewById(R.id.tv_cat);
        spinner = findViewById(R.id.spinner_cat);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,status);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);

        AllQuestions = database.getReference("AllQuestions");
        UserQuestions = database.getReference("UserQuestions").child(currentUserid);

        allQuest = db.collection("AllQuestions").document();



        member = new QuestionMember();

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {

                String question = editText.getText().toString();

                Calendar cdate = Calendar.getInstance();
                SimpleDateFormat currentdate = new SimpleDateFormat("yyyy/MM/dd");
                final  String savedate = currentdate.format(cdate.getTime());

                Calendar ctime = Calendar.getInstance();
                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm");
                final String savetime = currenttime.format(ctime.getTime());


                String time = savedate +" "+"at"+" "+ savetime;


                if ((question.length() == 0) || cat_value.equals("Choose category")) {

                    Toast.makeText(AskActivity.this, "Please ask a question", Toast.LENGTH_SHORT).show();
                }else if (!isNetworkConnected()) {
                    showSnackbar();

                }else {


                    member.setQuestion(question);
                    member.setName(name);
                    member.setPrivacy(privacy);
                    member.setUrl(url);
                    member.setUserid(uid);
                    member.setTime(time);
                    member.setCategory(cat_value.toLowerCase());
//                    String id = UserQuestions.push().getKey();
//
//                    UserQuestions.child(id).setValue(member);



//                    member.setKey(id);
//                    String child = AllQuestions.push().getKey();
//                    AllQuestions.child(child).setValue(member);
                    allQuest.set(member);
                    Toast.makeText(AskActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();

                        }
                    },2000);

                }


            }
        });


    }

    private void showSnackbar() {

        Snackbar.make(linearLayout,"Not Connected",Snackbar.LENGTH_LONG)
                .setAction("Turn On", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                })
                .setActionTextColor(getResources().getColor(R.color.retrycolor))
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            name = task.getResult().getString("name");
                            url = task.getResult().getString("url");
                            privacy = task.getResult().getString("privacy");
                            uid = task.getResult().getString("uid");

                        }else {
                            Toast.makeText(AskActivity.this, "Error", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        cat_value = parent.getSelectedItem().toString();
        textView.setText(cat_value);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
