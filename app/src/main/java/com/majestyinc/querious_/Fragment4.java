package com.majestyinc.querious_;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;


import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

public class Fragment4 extends Fragment implements View.OnClickListener{

    ImageButton button;
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference,likeref,storyRef,likelist,referenceDel,ntref,deleteStory;
    Boolean likechecker,commentchecker = false;
    DatabaseReference db1,db2,db3,db4,Commentref,postnoref,viewRef;
    DocumentReference allpost,postref;


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;

    NewMember newMember;
    ReportClass reportClass;
    LinearLayoutManager linearLayoutManager;
    Uri imageUri;
    private  static  final  int PICK_IMAGE = 1;
    RecyclerView recyclerViewstory;
    CollectionReference q1,q2,q3,q4,q5,q6,story,storyref,userStory;
    LinearLayoutManager linearLayoutManagerStory;
    long time,timeend;
    String uid;
    SwipeRefreshLayout refreshLayout;
    String desc,writeup;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentuid = user.getUid();
    TextView tvcs,tvcp,writeUp;
    int lastPosition;
    private InterstitialAd mInterstitialAd;
    String name_result,url_result,uid_result,bio_result,usertoken,currenUid;
    All_UserMmber userMmber;
    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment4,container,false);
        return view;




    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
//        getActivity().getSupportFragmentManager().beginTransaction().show(fragment4).commit();



        recyclerView = getActivity().findViewById(R.id.rv_posts);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        fragment4 = new Fragment4();



        recyclerViewstory = getActivity().findViewById(R.id.rv_storyf4);
        recyclerViewstory.setHasFixedSize(true);
        linearLayoutManagerStory = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManagerStory.setReverseLayout(true);
        linearLayoutManagerStory.setStackFromEnd(true);
        recyclerViewstory.setLayoutManager(linearLayoutManagerStory);
        recyclerViewstory.setItemAnimator(new DefaultItemAnimator());


        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getActivity(),"ca-app-pub-1951396871656386/6225413563", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAd = null;
                    }
                });









        button = getActivity().findViewById(R.id.createpost_f4);
//        reference = database.getReference("All posts");
        likeref = database.getReference("post likes");
        Commentref = database.getReference("Comments");
        storyRef = database.getReference("All story");
        referenceDel = database.getReference("story");
        deleteStory = database.getReference("story");
        viewRef = database.getReference("views");

        reportClass = new ReportClass();




        postref = db.collection("All posts").document();
        storyref = db.collection("All story");
        userStory = db.collection("story");

        story = db.collection("story");







        documentReference = db.collection("user").document(currentuid);



        newMember = new NewMember();

        ntref = database.getReference("notification").child(currentuid);
        db1 = database.getReference("All images").child(currentuid);
        db2 = database.getReference("All videos").child(currentuid);
        db4 = database.getReference("WriteUp").child(currentuid);
        db3 = database.getReference("All posts");

        q1 = db.collection("All posts");
        q2 = db.collection("All story");
        q3 = db.collection("All images");
        q4 = db.collection("All videos");
        q5 = db.collection("WriteUp");
        q6 =  db.collection("story");

        postnoref = database.getReference("User Posts").child(currentuid);



        allpost = db.collection("All posts").document();

        refreshLayout = getActivity().findViewById(R.id.refresh4);









        button.setOnClickListener(this);

        userMmber = new All_UserMmber();

        checkStory(currentuid);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment4).commitNow();
                refreshLayout.setRefreshing(false);
            }
        });





    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.createpost_f4:

                showBottomsheet();
                break;


        }
    }

    private void checkStory(String currentid){

        storyref.whereEqualTo("uid",currentid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {



                if(value!=null)
                   {

                   }
               else
               {
                   story.document(currentid).delete();
               }

               }


        });





    }

    private void showBottomsheet() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.f4_bottomsheet);



        tvcp = dialog.findViewById(R.id.tv_cpf4);
        tvcs = dialog.findViewById(R.id.tv_csf4);
        writeUp = dialog.findViewById(R.id.writeup);

        userStory.whereEqualTo("uid",currenUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    tvcs.setVisibility(GONE);
                }
            }
        });




        tvcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });

        tvcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentstory = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intentstory.setType("image/*");
                startActivityForResult(intentstory,PICK_IMAGE);
            }

        });


        writeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), writeActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    @Override
    public void onStart() {
        super.onStart();


       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currenUid = user.getUid();





        storyref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {




                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()){
                         time = System.currentTimeMillis();
                         timeend = document.getLong("timeEnd");
                         uid = document.getString("uid");
                         int timenow = (int) time;
                         int endtime = (int) timeend;
                         if (timenow >= endtime) {
                             document.getReference().delete();

                                    }
                        }



                    }

                }
                }


        });








        story.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()){
                            time = System.currentTimeMillis();
                            timeend = document.getLong("timeEnd");
                            uid = document.getString("uid");
                            int timenow = (int) time;
                            int endtime = (int) timeend;
                            if (timenow >= endtime) {
                                document.getReference().delete();

                            }
                        }


                    }

                }
            }


        });



        documentReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()){
                            name_result = task.getResult().getString("name");
                            url_result = task.getResult().getString("url");
                            uid_result = task.getResult().getString("uid");

                        }else {

                        }

                    }
                });

        FirestoreRecyclerOptions<Postmember> options =
                new FirestoreRecyclerOptions.Builder<Postmember>()
                        .setQuery(q1.orderBy("time", com.google.firebase.firestore.Query.Direction.ASCENDING),Postmember.class)
                        .build();

        FirestoreRecyclerAdapter<Postmember,PostViewholder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<Postmember, PostViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewholder holder,final int position, @NonNull final Postmember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();


                        final  String postkey = getSnapshots().getSnapshot(position).getId();
                        holder.SetPost(getActivity(),model.getName(),model.getUrl(),model.getPostUri(),model.getWriteup(),model.getTime()
                                ,model.getUid(),model.getType(),model.getDesc());






                        final String url = getItem(position).getPostUri();
                        final String name = getItem(position).getName();
                        final String urll = getItem(position).getUrl();
                        final  String time = getItem(position).getTime();
                        final String type = getItem(position).getType();
                        final String userid = getItem(position).getUid();
                        final String  desc = getItem(position).getDesc();
                        final String writeup = getItem(position).getWriteup();

                        holder.likeschecker(postkey);
                        holder.commentchecker(postkey);







                        holder.menuoptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDialog(name,url,time,userid,type,postkey,writeup,desc,urll);
                            }
                        });

                        holder.tv_nameprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",urll);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });

                        holder.imageViewprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",urll);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });
                        holder.likebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                likechecker = true;

                                likeref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(currentUserid.equals(userid))
                                        {
                                            if (likechecker.equals(true)){
                                                if (snapshot.child(postkey).hasChild(currentUserid)){
                                                    likeref.child(postkey).child(currentUserid).removeValue();
                                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                                    likelist.removeValue();

                                                    ntref.child(currentUserid+"l").removeValue();

                                                    likechecker = false;
                                                }else {

                                                    likeref.child(postkey).child(currentUserid).setValue(true);
                                                    likelist = database.getReference("like list").child(postkey);
                                                    userMmber.setName(name_result);
                                                    userMmber.setUid(currentUserid);
                                                    userMmber.setUrl(url_result);
                                                    likelist.child(currentUserid).setValue(userMmber);

                                                    likechecker = false;


                                                }
                                            }


                                        }
                                        else
                                        {
                                            if (likechecker.equals(true)){
                                                if (snapshot.child(postkey).hasChild(currentUserid)){
                                                    likeref.child(postkey).child(currentUserid).removeValue();
                                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                                    likelist.removeValue();

                                                    ntref.child(currentUserid+"l").removeValue();

                                                    likechecker = false;
                                                }else {

                                                    likeref.child(postkey).child(currentUserid).setValue(true);
                                                    likelist = database.getReference("like list").child(postkey);
                                                    userMmber.setName(name_result);
                                                    userMmber.setUid(currentUserid);
                                                    userMmber.setUrl(url_result);
                                                    likelist.child(currentUserid).setValue(userMmber);

                                                    newMember.setName(name_result);
                                                    newMember.setUid(currentUserid);
                                                    newMember.setUrl(url_result);
                                                    newMember.setSeen("no");
                                                    newMember.setText("Liked Your Post ");


                                                    ntref.child(currentUserid + "l").setValue(newMember);

                                                    sendNotification(userid, name_result);
                                                    likechecker = false;


                                                }
                                            }

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });

                        holder.tv_likes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),ShowLikedUser.class);
                                intent.putExtra("p",postkey);
                                intent.putExtra("u",urll);
                                intent.putExtra("uid",userid);
                                startActivity(intent);
                            }
                        });



                        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),CommentsActivity.class);
                                intent.putExtra("postkey",postkey);
                                intent.putExtra("name",name);
                                intent.putExtra("url",urll);
                                intent.putExtra("uid",userid);
                                intent.putExtra("cap",desc);
                                intent.putExtra("type",type);
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public PostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_layout,parent,false);

                        return new PostViewholder(view);



                    }
                };
        firestoreRecyclerAdapter.startListening();
        recyclerView.setAdapter(firestoreRecyclerAdapter);




        // story firebase adapter



        FirestoreRecyclerOptions<StoryMember> options1 =
                new FirestoreRecyclerOptions.Builder<StoryMember>()
                        .setQuery(q6.orderBy("timeUpload", com.google.firebase.firestore.Query.Direction.DESCENDING),StoryMember.class)
                        .build();

        FirestoreRecyclerAdapter<StoryMember,StoryViewHolder> firebaseRecyclerAdapter =
                new FirestoreRecyclerAdapter<StoryMember, StoryViewHolder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull StoryViewHolder holder, int position, @NonNull final StoryMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        holder.setStory(getActivity(),model.getPostUri(),model.getName(),model.getTimeEnd(),model.getTimeUpload()
                                ,model.getType(),model.getCaption(),model.getUrl(),model.getUid());



                        String userid = getItem(position).getUid();
                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(mInterstitialAd != null){
                                    mInterstitialAd.show(getActivity());

                                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                        @Override
                                        public void onAdDismissedFullScreenContent() {
                                            super.onAdDismissedFullScreenContent();
                                            Intent intent = new Intent(getActivity(),ShowStory.class);
                                            intent.putExtra("u",userid);
                                            startActivity(intent);
                                        }
                                    });

                                }
                                else
                                {
                                    Intent intent = new Intent(getActivity(),ShowStory.class);
                                    intent.putExtra("u",userid);
                                    startActivity(intent);
                                }

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.story_layout,parent,false);


                        return new StoryViewHolder(view);



                    }
                };
        firebaseRecyclerAdapter.startListening();
        recyclerViewstory.setAdapter(firebaseRecyclerAdapter);
    }





    void showDialog(String name,String url,String time,String userid,String type,String postkey,String writeup,String desc,String userUri){

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.post_options);

        TextView download = dialog.findViewById(R.id.download_tv_post);
        TextView share = dialog.findViewById(R.id.share_tv_post);
        TextView delete = dialog.findViewById(R.id.delete_tv_post);
        TextView copyurl = dialog.findViewById(R.id.copyurl_tv_post);
        TextView edit = dialog.findViewById(R.id.edit_post);
        EditText captionEt = dialog.findViewById(R.id.et_caption);
        Button button = dialog.findViewById(R.id.btn_edit_caption);
        TextView report = dialog.findViewById(R.id.report);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();


      if(type.equals("text"))
        {
            download.setVisibility(GONE);
            copyurl.setVisibility(GONE);
            share.setVisibility(GONE);

        }


        if (userid.equals(currentUserid))
        {
            delete.setVisibility(View.VISIBLE);
            report.setVisibility(GONE);
            edit.setVisibility(View.VISIBLE);
        }


        else if (!user.equals(currentUserid)) {
            report.setVisibility(View.VISIBLE);
            delete.setVisibility(GONE);
            edit.setVisibility(GONE);
        }

        else
        {
            delete.setVisibility(GONE);
            edit.setVisibility(GONE);
        }


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showReportsheet(name,url,userid,type,postkey,time,userUri);

            }
        });


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                captionEt.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                if(type.equals("text"))
                {
                    captionEt.setText(writeup);}
                else
                {
                    captionEt.setText(desc);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String edit = captionEt.getText().toString();
                        if(type.equals("text"))
                        {


                            q1.whereEqualTo("time",time ).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().update("writeup",edit);
                                            Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                }
                            }}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        else {

                            q1.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().update("desc",edit);
                                            Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                }}).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }

                    }
                });


            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likechecker = true;
                commentchecker = true;

                if(type.equals("iv") || type.equals("vv")) {

                    q3.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful())
                            {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                    });

                    postnoref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                postnoref.child(postkey).removeValue();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });






                    q4.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete();
                                        Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            });

                q1.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

                    likeref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (likechecker.equals(true)){
                                if (snapshot.child(postkey).hasChild(currentUserid)) {
                                    likeref.child(postkey).child(currentUserid).removeValue();
                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                    likelist.removeValue();

                                    ntref.child(currentUserid + "l").removeValue();
                                    likechecker = false;
                                }
                                }else {


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    Commentref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {



                            if (commentchecker.equals(true)){
                            if (snapshot.hasChild(postkey)) {
                                Commentref.child(postkey).removeValue();
                                commentchecker = false;

                            }}else {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                    reference.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else
                {
                    q1.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });

                    q5.whereEqualTo("time",time).whereEqualTo("uid",userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });


                    postnoref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                postnoref.child(postkey).removeValue();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    likeref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (likechecker.equals(true)){
                                if (snapshot.child(postkey).hasChild(currentUserid)) {
                                    likeref.child(postkey).child(currentUserid).removeValue();
                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                    likelist.removeValue();

                                    ntref.child(currentUserid + "l").removeValue();
                                    likechecker = false;
                                }

                                }else {


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Commentref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (commentchecker.equals(true)){
                            if (snapshot.hasChild(postkey)) {
                                Commentref.child(postkey).removeValue();
                                commentchecker = false;
                            }}else
                                {

                                }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                dialog.dismiss();

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                PermissionListener permissionListener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                        if (type.equals("iv")){

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading image....");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name+System.currentTimeMillis() + ".jpg");
                            DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();


                        }else {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading video....");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,name+System.currentTimeMillis() + ".mp4");
                            DownloadManager manager = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();


                        }

                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {

                        Toast.makeText(getActivity(), "No permissions", Toast.LENGTH_SHORT).show();
                    }
                };
                TedPermission.with(getActivity())
                        .setPermissionListener(permissionListener)
                        .setPermissions(Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();


                dialog.dismiss();


            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sharetext = name +"\n" +"\n"+ url;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT,sharetext);
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent,"share via"));

                dialog.dismiss();

            }
        });

        copyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager cp = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("String",url);
                cp.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }



    private void showReportsheet(String name, String url, String userid,
                                 String type, String postkey, String time, String userUri) {



        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.report_file_post);

        Button cancel = dialog.findViewById(R.id.cancel_report_post);
        Button submitreport = dialog.findViewById(R.id.submit_report_post);

        RadioButton sexualrb = dialog.findViewById(R.id.sexualTv_report);
        RadioButton violentrb = dialog.findViewById(R.id.violenTv_report);
        RadioButton hatefulrb = dialog.findViewById(R.id.hatefulTv_report);
        RadioButton harassmentrb = dialog.findViewById(R.id.harassmentTv_report);
        RadioButton childrb = dialog.findViewById(R.id.childTv_report);
        RadioButton infringesrb = dialog.findViewById(R.id.infringesTv_report);
        RadioButton spamrb = dialog.findViewById(R.id.spamTv_report);
        RadioButton terrorismrb = dialog.findViewById(R.id.terrorismTv_report);

        DatabaseReference reportRefPost = database.getReference("Report Post");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });


        submitreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sexualrb.isChecked()){

                    reportClass.setIssue("Sexual Content");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);


                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });



                }else if (violentrb.isChecked()){

                    reportClass.setIssue("Violent of repulsive ");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);


                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }else if (hatefulrb.isChecked()){

                    reportClass.setIssue("Hateful of abusive content");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);

                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }else if (childrb.isChecked()){

                    reportClass.setIssue("Child Abuse");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);


                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }else if (infringesrb.isChecked()){

                    reportClass.setIssue("Infringes my rights");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);


                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }else if (terrorismrb.isChecked()){

                    reportClass.setIssue(" Promotes Terrorism ");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);


                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }else if (spamrb.isChecked()){

                    reportClass.setIssue("Spam or misleading");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);

                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });
                }else if (harassmentrb.isChecked()){

                    reportClass.setIssue("Harassment Content");
                    reportClass.setName(name);
                    reportClass.setTime(time);
                    reportClass.setType(type);
                    reportClass.setUid(userid);
                    reportClass.setUrl(url);
                    reportClass.setUseruri(userUri);

                    String key = reportRefPost.push().getKey();
                    reportRefPost.child(key).setValue(reportClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Reported Successfully", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }



            }
        });






        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {

            if (requestCode == PICK_IMAGE || resultCode == RESULT_OK ||
                    data != null || data.getData() != null) {
                imageUri = data.getData();

                String url = imageUri.toString();
                Intent intent = new Intent(getActivity(),StoryActivity.class);
                intent.putExtra("u",url);
                startActivity(intent);
            }else {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){


        }


    }

    private void sendNotification(String userid, String name_result){

        FirebaseDatabase.getInstance().getReference("Token").child(userid).child("token")
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
                        new FcmNotificationsSender(usertoken,"Querious",name_result+" Liked Your post ",
                                getContext(),getActivity());

                notificationsSender.SendNotifications();

            }
        },3000);

    }

    public boolean isNetworkConnected(){
        boolean connected = false;

        try {

            ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ninfo = cm.getActiveNetworkInfo();
            connected = ninfo != null && ninfo.isAvailable() && ninfo.isConnected();
            return connected;


        }

        catch (Exception e){

            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return connected;
    }


}

