package com.majestyinc.querious_;

import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static org.jitsi.meet.sdk.JitsiMeetActivityDelegate.onBackPressed;

public class Fragment1 extends Fragment implements View.OnClickListener{

    ImageView imageView;
    TextView nameEt,profEt,bioEt,emailEt,followertv,followingtv,posttv,newtv;
    ImageButton imageButtonEdit,imageButtonMenu;
    DocumentReference reference;
    FirebaseFirestore firestore;
    CardView followers,following;
    TextView btnsendmessage;
    Uri imageUri;
    String url,userid;
    private static int PICK_IMAGE=1;
    int followerno,postno,followingno,newcount;
    SwipeRefreshLayout refreshLayout;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;



    RequestMember requestMember;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CollectionReference posts,q3,q4,q5;
    All_UserMmber fmember;
    Boolean likechecker,commentchecker = false;
    DatabaseReference likeref,likelist,Commentref;
    NewMember newMember;

    FirebaseAuth mAuth;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference db1,db2,db3,ntRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1,container,false);
        return view;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
//        getActivity().getSupportFragmentManager().beginTransaction().show(fragment1).commit();



        isNetworkConnected();
         FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         userid = user.getUid();


        fmember = new All_UserMmber();

        recyclerView = getActivity().findViewById(R.id.rv_posts);
        recyclerView.setHasFixedSize(true);

        newMember = new NewMember();
        firestore = FirebaseFirestore.getInstance();

        likeref = database.getReference("post likes");

        Commentref = database.getReference("Comments");

        posts = firestore.collection("All posts");


        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);



        reference = firestore.collection("user").document(userid);

        mAuth= FirebaseAuth.getInstance();

        db1 = database.getReference("followers").child(userid);
        db2 = database.getReference("User Posts").child(userid);
        db3 = database.getReference("following").child(userid);

        followers = getActivity().findViewById(R.id.followers_cardview);
        following = getActivity().findViewById(R.id.following_cardview);


        q3 = firestore.collection("All images");
        q4 = firestore.collection("All videos");
        q5 = firestore.collection("WriteUp");

        ntRef = database.getReference("notification").child(userid);

        imageView = getActivity().findViewById(R.id.iv_f1);
        nameEt = getActivity().findViewById(R.id.tv_name_f1);
        profEt = getActivity().findViewById(R.id.tv_prof_f1);
        bioEt = getActivity().findViewById(R.id.tv_bio_f1);
        emailEt = getActivity().findViewById(R.id.tv_email_f1);
        followertv = getActivity().findViewById(R.id.tv_followers_f1);
        followingtv = getActivity().findViewById(R.id.following_f1);
        posttv = getActivity().findViewById(R.id.postsNo_tv);

        newtv = getActivity().findViewById(R.id.tv_newf1);
        refreshLayout = getActivity().findViewById(R.id.refresh1);

        btnsendmessage = getActivity().findViewById(R.id.btn_sendmessage_showuser);

        imageButtonEdit = getActivity().findViewById(R.id.ib_edit_f1);
        imageButtonMenu = getActivity().findViewById(R.id.ib_menu_f1);


        imageButtonMenu.setOnClickListener(this);
        imageButtonEdit.setOnClickListener(this);
        imageView.setOnClickListener(this);
        btnsendmessage.setOnClickListener(this);

        following.setOnClickListener(this);
        followers.setOnClickListener(this);

        followertv.setOnClickListener(this);
        newtv.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment1).commitNow();
                refreshLayout.setRefreshing(false);

            }
        });


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_edit_f1:
                Intent intent = new Intent(getActivity(),UpdateProfile.class);
                startActivity(intent);
                break;
            case R.id.ib_menu_f1:
                showBottomSheet();

                break;
            case R.id.iv_f1:
                Intent intent1 = new Intent(getActivity(),ImageActivity.class);
                startActivity(intent1);
                break;


            case R.id.btn_sendmessage_showuser:
                Intent in = new Intent(getActivity(),ChatActivity.class);
                startActivity(in);
                break;


            case R.id.tv_newf1:
                Intent intent3 = new Intent(getActivity(),NotificationActivity.class);
                startActivity(intent3);
                changeSeen();
                break;


            case R.id.followers_cardview:
                Intent follower = new Intent(getActivity(),FollowerActivity.class);
                follower.putExtra("u",userid);
                startActivity(follower);

                break;

            case R.id.following_cardview:
                Intent following = new Intent(getActivity(),Following.class);
                following.putExtra("u",userid);
                startActivity(following);

                break;


        }
    }

    private void showBottomSheet() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_menu);

        ImageView logout,privacy,settings,delete;

        logout = dialog.findViewById(R.id.logout_profile);
        privacy = dialog.findViewById(R.id.privacy_profile);
        delete = dialog.findViewById(R.id.del_profile);
        settings = dialog.findViewById(R.id.settings_profile);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to Logout")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                mAuth.signOut();

                                FirebaseDatabase.getInstance().getReference("Token").child(userid).child("token").removeValue();
                                startActivity(new Intent(getActivity(),LoginActivity.class));

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete Profile")
                        .setMessage("Are you sure to delete?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //   StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl()

                                deleteImage();
                                reference.delete()

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Toast.makeText(getActivity(), "Profile deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                                Toast.makeText(getActivity(), "Profile delete failed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create();
                builder.show();



            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),PrivacyActivity.class));

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(),SettingsActivity.class));

            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void deleteImage() {

        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()) {

                            String Url = task.getResult().getString("url");
                            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(Url);
                            reference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                        }
                                    });

                        } else {

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                    }

                });
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

            Toast.makeText(getActivity(), "error"+e, Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.getResult().exists()) {

                            String nameResult = task.getResult().getString("name");
                            String bioResult = task.getResult().getString("bio");
                            String emailResult = task.getResult().getString("email");
                            String webResult = task.getResult().getString("web");
                            url = task.getResult().getString("url");
                            String profResult = task.getResult().getString("prof");

                            Picasso.get().load(url).into(imageView);
                            nameEt.setText(nameResult);
                            bioEt.setText(bioResult);
                            emailEt.setText(emailResult);
                            profEt.setText(profResult);


                        } else {
                            Intent intent = new Intent(getActivity(), CreateProfile.class);
                            startActivity(intent);
                        }
                    }
                });


        Query query = ntRef.orderByChild("seen").equalTo("no");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    newcount = (int) snapshot.getChildrenCount();
                    newtv.setText(Integer.toString(newcount) + " New");
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followerno = (int) snapshot.getChildrenCount();
                followertv.setText(Integer.toString(followerno));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        db2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postno = (int) snapshot.getChildrenCount();
                posttv.setText(Integer.toString(postno));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        db3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingno = (int) snapshot.getChildrenCount();
                followingtv.setText(Integer.toString(followingno));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirestoreRecyclerOptions<Postmember> options =
                new FirestoreRecyclerOptions.Builder<Postmember>()
                        .setQuery(posts.whereEqualTo("uid", userid).orderBy("time", com.google.firebase.firestore.Query.Direction.ASCENDING), Postmember.class)
                        .build();

        FirestoreRecyclerAdapter<Postmember, PostViewholder> firestoreRecyclerAdapter =
                new FirestoreRecyclerAdapter<Postmember, PostViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull PostViewholder holder, int position, @NonNull final Postmember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();


                        final String postkey = getSnapshots().getSnapshot(position).getId();
                        holder.SetPost(getActivity(), model.getName(), model.getUrl(), model.getPostUri(), model.getWriteup(), model.getTime()
                                , model.getUid(), model.getType(), model.getDesc());


                        final String url = getItem(position).getPostUri();
                        final String name = getItem(position).getName();
                        final String urll = getItem(position).getUrl();
                        final String time = getItem(position).getTime();
                        final String type = getItem(position).getType();
                        final String userid = getItem(position).getUid();
                        final String desc = getItem(position).getDesc();
                        final String writeup = getItem(position).getWriteup();


                        holder.likeschecker(postkey);
                        holder.commentchecker(postkey);


                        holder.menuoptions.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showDialog(name, url, time, userid, type, postkey, writeup, desc);
                            }
                        });

                        holder.tv_nameprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(getActivity(), ShowUser.class);
                                    intent.putExtra("n", name);
                                    intent.putExtra("u", urll);
                                    intent.putExtra("uid", userid);
                                    startActivity(intent);
                                }


                            }
                        });

                        holder.imageViewprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    startActivity(intent);

                                } else {
                                    Intent intent = new Intent(getActivity(), ShowUser.class);
                                    intent.putExtra("n", name);
                                    intent.putExtra("u", urll);
                                    intent.putExtra("uid", userid);
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

                                        if (likechecker.equals(true)) {
                                            if (snapshot.child(postkey).hasChild(currentUserid)) {
                                                likeref.child(postkey).child(currentUserid).removeValue();
                                                likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                                likelist.removeValue();


                                                likechecker = false;
                                            } else {

                                                likeref.child(postkey).child(currentUserid).setValue(true);
                                                likelist = database.getReference("like list").child(postkey);
                                                fmember.setName(name);
                                                fmember.setUid(currentUserid);
                                                fmember.setUrl(url);
                                                likelist.child(currentUserid).setValue(fmember);


                                                likechecker = false;


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
                                Intent intent = new Intent(getActivity(), ShowLikedUser.class);
                                intent.putExtra("p", postkey);
                                intent.putExtra("u", urll);
                                intent.putExtra("uid", userid);
                                startActivity(intent);
                            }
                        });


                        holder.commentbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), CommentsActivity.class);
                                intent.putExtra("postkey", postkey);
                                intent.putExtra("name", name);
                                intent.putExtra("url", urll);
                                intent.putExtra("uid", userid);
                                intent.putExtra("cap", desc);
                                intent.putExtra("type", type);
                                startActivity(intent);
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public PostViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.post_layout, parent, false);

                        return new PostViewholder(view);


                    }
                };
        firestoreRecyclerAdapter.startListening();

        recyclerView.setAdapter(firestoreRecyclerAdapter);


    }

    private void changeSeen(){

        Map<String,Object > profile = new HashMap<>();
        profile.put("seen","yes");

        ntRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    dataSnapshot.getRef().updateChildren(profile)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

    void showDialog(String name,String url,String time,String userid,String type,String postkey,String writeup,String desc) {

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


        if (type.equals("text")) {
            download.setVisibility(View.GONE);
            copyurl.setVisibility(View.GONE);
            share.setVisibility(View.GONE);

        }


        if (userid.equals(currentUserid)) {
            delete.setVisibility(View.VISIBLE);
            report.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
        } else if (!userid.equals(currentUserid)) {
            report.setVisibility(View.VISIBLE);
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        } else {
            delete.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                captionEt.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                if (type.equals("text")) {
                    captionEt.setText(writeup);
                } else {
                    captionEt.setText(desc);
                }

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        String edit = captionEt.getText().toString();
                        if (type.equals("text")) {


                            posts.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().update("writeup", edit);
                                            Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            posts.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            document.getReference().update("desc", edit);
                                            Toast.makeText(getActivity(), "Edited", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        }
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                if (type.equals("iv") || type.equals("vv")) {

                    q3.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


                    db2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                db2.child(postkey).removeValue();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                    q4.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    posts.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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


                            if (likechecker.equals(true)) {
                                if (snapshot.child(postkey).hasChild(currentUserid)) {
                                    likeref.child(postkey).child(currentUserid).removeValue();
                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                    likelist.removeValue();

                                    likechecker = false;
                                }
                            } else {


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Commentref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (commentchecker.equals(true)) {
                                if (snapshot.hasChild(postkey)) {
                                    Commentref.child(postkey).removeValue();
                                    commentchecker = false;

                                }
                            } else {

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
                } else {
                    posts.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                    q5.whereEqualTo("time", time).whereEqualTo("uid", userid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                            if (likechecker.equals(true)) {
                                if (snapshot.child(postkey).hasChild(currentUserid)) {
                                    likeref.child(postkey).child(currentUserid).removeValue();
                                    likelist = database.getReference("like list").child(postkey).child(currentUserid);
                                    likelist.removeValue();

                                    likechecker = false;
                                }

                            } else {


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    Commentref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {


                            if (commentchecker.equals(true)) {
                                if (snapshot.hasChild(postkey)) {
                                    Commentref.child(postkey).removeValue();
                                    commentchecker = false;
                                }
                            } else {

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    db2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                db2.child(postkey).removeValue();
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

                        if (type.equals("iv")) {

                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading image....");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + System.currentTimeMillis() + ".jpg");
                            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                            Toast.makeText(getActivity(), "Downloading", Toast.LENGTH_SHORT).show();


                        } else {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                                    DownloadManager.Request.NETWORK_MOBILE);
                            request.setTitle("Download");
                            request.setDescription("Downloading video....");
                            request.allowScanningByMediaScanner();
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + System.currentTimeMillis() + ".mp4");
                            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
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
                        .setPermissions(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();


                dialog.dismiss();


            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sharetext = name + "\n" + "\n" + url;
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, sharetext);
                intent.setType("text/plain");
                startActivity(intent.createChooser(intent, "share via"));

                dialog.dismiss();

            }
        });

        copyurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ClipboardManager cp = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("String", url);
                cp.setPrimaryClip(clip);
                clip.getDescription();
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

}
