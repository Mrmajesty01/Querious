package com.majestyinc.querious_;

import static org.webrtc.ContextUtils.getApplicationContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.squareup.picasso.Picasso;

public class Fragment2 extends Fragment implements View.OnClickListener {

    FloatingActionButton fb;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference reference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference,fvrtref,fvrt_listRef;
    RecyclerView recyclerView;
    Boolean fvrtChecker = false;
    ImageView imageView;
    ImageButton ibCat;
    LinearLayoutManager linearLayoutManager;
    CollectionReference allQuest;
    SwipeRefreshLayout refreshLayout;

    QuestionMember member;
    String name,url_pic;
    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();


        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

//        getActivity().getSupportFragmentManager().beginTransaction().show(fragment2).commit();
        recyclerView = getActivity().findViewById(R.id.rv_f2);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        refreshLayout = getActivity().findViewById(R.id.refresh2);

        databaseReference = database.getReference("AllQuestions");
        allQuest = db.collection("AllQuestions");
        member = new QuestionMember();
        fvrtref = database.getReference("favourites");
        fvrt_listRef = database.getReference("favoriteList").child(currentUserid);


        imageView = getActivity().findViewById(R.id.iv_f2);
        fb = getActivity().findViewById(R.id.floatingActionButton);
        reference = db.collection("user").document(currentUserid);
        ibCat = getActivity().findViewById(R.id.btn_catf2);


        fb.setOnClickListener(this);
        imageView.setOnClickListener(this);
        ibCat.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragment2).commitNow();
                refreshLayout.setRefreshing(false);
            }

        });




        FirestoreRecyclerOptions<QuestionMember> options =
                new FirestoreRecyclerOptions.Builder<QuestionMember>()
                        .setQuery(allQuest.orderBy("time", com.google.firebase.firestore.Query.Direction.ASCENDING),QuestionMember.class)
                        .build();

        FirestoreRecyclerAdapter<QuestionMember,Viewholder_Question> firebaseRecyclerAdapter =
                new FirestoreRecyclerAdapter<QuestionMember, Viewholder_Question>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder_Question holder, int position, @NonNull final QuestionMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = allQuest.document(String.valueOf(position)).getId();

                        final  String postky =  getSnapshots().getSnapshot(position).getId();

                        holder.setitem(getActivity(),model.getName(),model.getUrl(),model.getUserid(),model.getKey()
                                ,model.getQuestion(),model.getPrivacy(),model.getTime(),model.getCategory());

                        final String que = getItem(position).getQuestion();
                        final String name = getItem(position).getName();
                        final String url = getItem(position).getUrl();
                        final  String time = getItem(position).getTime();
                        final String privacy = getItem(position).getPrivacy();
                        final String userid = getItem(position).getUserid();

                        holder. RepliesChecker(postky);

                        holder.name_result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });



                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });


                        holder.replybtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),ReplyActivity.class);
                                intent.putExtra("uid",userid);
                                intent.putExtra("q",que);
                                intent.putExtra("postkey",postky);
                                //  intent.putExtra("key",privacy);
                                startActivity(intent);

                            }
                        });

                        holder.favouriteChecker(postkey);
                        holder.fvrt_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                fvrtChecker = true;

                                fvrtref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (fvrtChecker.equals(true)){
                                            if (snapshot.child(postkey).hasChild(currentUserid)){
                                                fvrtref.child(postkey).child(currentUserid).removeValue();
                                                delete(time);
                                                Toast.makeText(getActivity(), "Removed from favourites", Toast.LENGTH_SHORT).show();
                                                fvrtChecker = false;
                                            }else {


                                                fvrtref.child(postkey).child(currentUserid).setValue(true);
                                                member.setName(name);
                                                member.setTime(time);
                                                member.setPrivacy(privacy);
                                                member.setUserid(userid);
                                                member.setUrl(url);
                                                member.setQuestion(que);

                                                //  String id = fvrt_listRef.push().getKey();
                                                fvrt_listRef.child(postkey).setValue(member);
                                                fvrtChecker = false;

                                                Toast.makeText(getActivity(), "Added to favourites", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public Viewholder_Question onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.question_item,parent,false);

                        return new Viewholder_Question(view);



                    }
                };
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    void delete(String time){

        Query query = fvrt_listRef.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_f2:
                BottomSheetF2 bottomSheetF2 = new BottomSheetF2();
                bottomSheetF2.show(getFragmentManager(),"bottom");
                break;
            case R.id.floatingActionButton:
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_catf2:
                showCategorySheet();

                break;

        }
    }

    private void showCategorySheet() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.category_layout);

        TextView tvOld = dialog.findViewById(R.id.tvOld);
        TextView tvTech = dialog.findViewById(R.id.tvTech);
        TextView tvGeneral = dialog.findViewById(R.id.tvGeneral);
        TextView tvPolitics = dialog.findViewById(R.id.tvPolitics);
        TextView tvBusiness = dialog.findViewById(R.id.tvBusiness);
        TextView tvTravel = dialog.findViewById(R.id.tvTravel);
        TextView tvhealth = dialog.findViewById(R.id.tvhealth);
        TextView tvEducation = dialog.findViewById(R.id.tvEducation);
        TextView tvCars = dialog.findViewById(R.id.cars);
        TextView tvFood = dialog.findViewById(R.id.tvFood);
        TextView tvsports = dialog.findViewById(R.id.tvSports);
        TextView tvNews = dialog.findViewById(R.id.tvnews);
        TextView tvfashion = dialog.findViewById(R.id.tvfashion);
        TextView tvBeauty = dialog.findViewById(R.id.tvBeauty);
        TextView tvLifestyle = dialog.findViewById(R.id.tvLifestyle);


        tvTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "tech";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#FFC107"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
            }
        });

        tvOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String category = "old";
                sortQuestion(category);

                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvOld.setTextColor(Color.parseColor("#FFC107"));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,
                        new Fragment2()).commit();

            }
        });
        tvhealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "health";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "general";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "business";
                sortQuestion(category);

                tvOld.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvPolitics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "politics";
                sortQuestion(category);

                tvOld.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "travel";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#FFC107"));
            }
        });
        tvEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "education";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "cars";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#FFC107"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
            }
        });

        tvFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "food";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvsports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "sports";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#FFC107"));
            }
        });

        tvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "news";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#FFC107"));
            }
        });


        tvfashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "fashion";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#FFC107"));
            }
        });
        tvBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "beauty";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#FFC107"));
            }
        });
        tvLifestyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = "lifestyle";
                sortQuestion(category);
                tvOld.setTextColor(Color.parseColor("#000000"));
                tvTech.setTextColor(Color.parseColor("#000000"));
                tvGeneral.setTextColor(Color.parseColor("#000000"));
                tvPolitics.setTextColor(Color.parseColor("#000000"));
                tvBusiness.setTextColor(Color.parseColor("#000000"));
                tvTravel.setTextColor(Color.parseColor("#000000"));
                tvhealth.setTextColor(Color.parseColor("#000000"));
                tvEducation.setTextColor(Color.parseColor("#000000"));
                tvCars.setTextColor(Color.parseColor("#000000"));
                tvFood.setTextColor(Color.parseColor("#000000"));
                tvsports.setTextColor(Color.parseColor("#000000"));
                tvNews.setTextColor(Color.parseColor("#000000"));
                tvfashion.setTextColor(Color.parseColor("#000000"));
                tvBeauty.setTextColor(Color.parseColor("#000000"));
                tvLifestyle.setTextColor(Color.parseColor("#FFC107"));
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Bottomanim;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void sortQuestion(String category) {


        Query search = databaseReference.orderByChild("category").startAt(category).endAt(category+"\uf0ff");


        FirestoreRecyclerOptions<QuestionMember> options =
                new FirestoreRecyclerOptions.Builder<QuestionMember>()
                        .setQuery(allQuest.orderBy("category").startAt(category).endAt(category+"\uf0ff"),QuestionMember.class)
                        .build();

        FirestoreRecyclerAdapter<QuestionMember,Viewholder_Question> firebaseRecyclerAdapter =
                new FirestoreRecyclerAdapter<QuestionMember, Viewholder_Question>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull Viewholder_Question holder, int position, @NonNull final QuestionMember model) {

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = allQuest.document(String.valueOf(position)).getId();

                        holder.setitem(getActivity(),model.getName(),model.getUrl(),model.getUserid(),model.getKey()
                                ,model.getQuestion(),model.getPrivacy(),model.getTime(),model.getCategory());

                        final String que = getItem(position).getQuestion();
                        final String name = getItem(position).getName();
                        final String url = getItem(position).getUrl();
                        final  String time = getItem(position).getTime();
                        final String privacy = getItem(position).getPrivacy();
                        final String userid = getItem(position).getUserid();





                        holder.name_result.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });

                        holder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (currentUserid.equals(userid)) {
                                    Intent intent = new Intent(getActivity(),MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Intent intent = new Intent(getActivity(),ShowUser.class);
                                    intent.putExtra("n",name);
                                    intent.putExtra("u",url);
                                    intent.putExtra("uid",userid);
                                    startActivity(intent);
                                }


                            }
                        });



                        holder.replybtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(),ReplyActivity.class);
                                intent.putExtra("uid",userid);
                                intent.putExtra("q",que);
                                intent.putExtra("postkey",postkey);
                                //  intent.putExtra("key",privacy);
                                startActivity(intent);

                            }
                        });

                        holder.favouriteChecker(postkey);
                        holder.fvrt_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                fvrtChecker = true;

                                fvrtref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        if (fvrtChecker.equals(true)){
                                            if (snapshot.child(postkey).hasChild(currentUserid)){
                                                fvrtref.child(postkey).child(currentUserid).removeValue();
                                                delete(time);
                                                Toast.makeText(getActivity(), "Removed from favourite", Toast.LENGTH_SHORT).show();
                                                fvrtChecker = false;
                                            }else {


                                                fvrtref.child(postkey).child(currentUserid).setValue(true);
                                                member.setName(name);
                                                member.setTime(time);
                                                member.setPrivacy(privacy);
                                                member.setUserid(userid);
                                                member.setUrl(url);
                                                member.setQuestion(que);

                                                //  String id = fvrt_listRef.push().getKey();
                                                fvrt_listRef.child(postkey).setValue(member);
                                                fvrtChecker = false;

                                                Toast.makeText(getActivity(), "Added to favourite", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public Viewholder_Question onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.question_item,parent,false);

                        return new Viewholder_Question(view);



                    }
                };
        firebaseRecyclerAdapter.startListening();

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        reference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.getResult().exists()){
                            String url = task.getResult().getString("url");
                            name = task.getResult().getString("name");
                            url_pic = task.getResult().getString("url");

                            Picasso.get().load(url).into(imageView);
                        }else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }

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


}