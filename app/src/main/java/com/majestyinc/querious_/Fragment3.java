package com.majestyinc.querious_;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.StringValue;

public class Fragment3 extends Fragment {

    FirebaseDatabase database;
    FirebaseFirestore firestore;
    DatabaseReference databaseReference,databaseReference1,profileRef,ntRef;
    RecyclerView recyclerView,recyclerView_profile;
    RequestMember requestMember;
    TextView requesttv;
    EditText editText;
    String currentUserId,usertoken;
    NewMember newMember;
    SwipeRefreshLayout refreshLayout;
    CollectionReference users;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment3,container,false);
        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

//        getActivity().getSupportFragmentManager().beginTransaction().show(fragment3).commit();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();



        database = FirebaseDatabase.getInstance();
        firestore = FirebaseFirestore.getInstance();
        databaseReference = database.getReference("Requests").child(currentUserId);
        profileRef = database.getReference("All Users");

        users = firestore.collection("user");

        ntRef = database.getReference("notification").child(currentUserId);
        requestMember = new RequestMember();

        newMember = new NewMember();
        recyclerView_profile = getActivity().findViewById(R.id.recylerview_profile);

        refreshLayout = getActivity().findViewById(R.id.refresh3);


        editText = getActivity().findViewById(R.id.search_f3);

        recyclerView_profile.setHasFixedSize(true);


        recyclerView_profile.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView = getActivity().findViewById(R.id.recylerview_requestf3);
        requesttv = getActivity().findViewById(R.id.requeststv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        //   MediaController mediaController;
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment3).commitNow();
                refreshLayout.setRefreshing(false);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                search();

            }
        });

    }

    private void search() {

        String query = editText.getText().toString().toUpperCase();
        Query search = profileRef.orderByChild("name").startAt(query).endAt(query+"\uf0ff");

        FirebaseRecyclerOptions<All_UserMmber> options1 =
                new FirebaseRecyclerOptions.Builder<All_UserMmber>()
                        .setQuery(search,All_UserMmber.class)
                        .build();

        FirebaseRecyclerAdapter<All_UserMmber,ProfileViewholder> firebaseRecyclerAdapter1 =
                new FirebaseRecyclerAdapter<All_UserMmber, ProfileViewholder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_UserMmber model) {


                        final String postkey = getRef(position).getKey();

                        holder.setProfile(getActivity(),model.getName(),model.getUid(),model.getProf(),model.getUrl());


                        String  name = getItem(position).getName();
                        String  url = getItem(position).getUrl();
                        String uid = getItem(position).getUid();
                        String prof = getItem(position).getProf();


                        holder.viewUserprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (currentUserId.equals(uid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",uid);
                                    intent.putExtra("pro",prof);

                                    startActivity(intent);
                                }



                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.profile,parent,false);

                        return new ProfileViewholder(view);
                    }
                };


        firebaseRecyclerAdapter1.startListening();
//
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView_profile.setLayoutManager(gridLayoutManager);
        recyclerView_profile.setAdapter(firebaseRecyclerAdapter1);
    }


    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    requesttv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);

                }else {
                    requesttv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirestoreRecyclerOptions<All_UserMmber> options1 =
                new FirestoreRecyclerOptions.Builder<All_UserMmber>()
                        .setQuery(users,All_UserMmber.class)
                        .build();

        FirestoreRecyclerAdapter<All_UserMmber,ProfileViewholder> firebaseRecyclerAdapter1 =
                new FirestoreRecyclerAdapter<All_UserMmber, ProfileViewholder>(options1) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfileViewholder holder, int position, @NonNull All_UserMmber model) {


                        final String postkey = users.document(String.valueOf(position)).getId();

                        holder.setProfile(getActivity(),model.getName(),model.getUid(),model.getProf(),model.getUrl());


                        String  name = getItem(position).getName();
                        String  url = getItem(position).getUrl();
                        String uid = getItem(position).getUid();


                        holder.viewUserprofile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserId.equals(uid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",uid);
                                    startActivity(intent);
                                }
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProfileViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.profile,parent,false);

                        return new ProfileViewholder(view);
                    }
                };


        firebaseRecyclerAdapter1.startListening();
//
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false);
        recyclerView_profile.setLayoutManager(gridLayoutManager);
        recyclerView_profile.setAdapter(firebaseRecyclerAdapter1);


        FirebaseRecyclerOptions<RequestMember> options =
                new FirebaseRecyclerOptions.Builder<RequestMember>()
                        .setQuery(databaseReference,RequestMember.class)
                        .build();

        FirebaseRecyclerAdapter<RequestMember,RequestViewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<RequestMember, RequestViewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull RequestViewholder holder, @SuppressLint("RecyclerView") int positions, @NonNull RequestMember model) {


                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String currentUserId = user.getUid();
                        final String postkey = getRef(positions).getKey();

                        holder.setRequest(getActivity(),model.getName(),model.getUrl(),model.getProfession()
                                ,model.getBio(),model.getPrivacy(),model.getEmail(),model.getFollowers(),model.getWebsite(),model.getUserid());

                        String uid = getItem(positions).getUserid();
                        String name = getItem(positions).getName();
                        String bio = getItem(positions).getBio();
                        String email = getItem(positions).getEmail();
                        String privacy = getItem(positions).getPrivacy();
                        String url = getItem(positions).getUrl();
                        String website = getItem(positions).getWebsite();
                        String age = getItem(positions).getProfession();




                        holder.button2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = getItem(positions).getName();
                                decline(name);
                            }
                        });
                        holder.button1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String uid = getItem(positions).getUserid();
                                databaseReference1 = database.getReference("followers").child(currentUserId);
                                requestMember.setName(name);

                                requestMember.setUserid(uid);
                                requestMember.setUrl(url);
                                requestMember.setProfession(age);
                                String id = databaseReference1.push().getKey();
                                databaseReference1.child(uid).setValue(requestMember);
                                databaseReference.child(currentUserId).child(uid).removeValue();

                                Toast.makeText(getActivity(), "Accepted", Toast.LENGTH_SHORT).show();
                                sendNotification(currentUserId,name);
                                decline(name);


                                // handling request notification

                                newMember.setName(name);
                                newMember.setUid(uid);
                                newMember.setUrl(url);
                                newMember.setSeen("no");
                                newMember.setText("Started Following you ");

                                ntRef.child(uid+"f").setValue(newMember);


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public RequestViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.request_item,parent,false);

                        return new RequestViewholder(view);
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void decline(String name) {

        Query query = databaseReference.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();
                }
                //   Toast.makeText(getActivity(), "Removed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ///
            }
        });
    }

    private void sendNotification(String currentUserId, String name){

        FirebaseDatabase.getInstance().getReference().child(currentUserId).child("token")
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
                        new FcmNotificationsSender(usertoken,"Querious",name+" Started Following you",
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
