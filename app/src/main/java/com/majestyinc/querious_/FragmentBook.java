package com.majestyinc.querious_;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentBook extends Fragment  {

//    FloatingActionButton fab;
//    RecyclerView recyclerView;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragmentbook, container, false);
//        return view;
//    }
//
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FileName");
//
//        databaseReference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                String fileName = snapshot.getKey();
//
//                String url = snapshot.getValue().toString();
//
//                ( (MyAdapter) recyclerView.getAdapter()).update(fileName,url);
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//
//        fab = getActivity().findViewById(R.id.tofileupload);
//        recyclerView = getActivity().findViewById(R.id.files);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        MyAdapter myAdapter = new MyAdapter(recyclerView,getActivity(),new ArrayList<>(),new ArrayList<>());
//        recyclerView.setAdapter(myAdapter);
//
//        fab.setOnClickListener(this);
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tofileupload:
//                Intent intent = new Intent(getActivity(), FileUpload.class);
//                startActivity(intent);
//                break;
//        }
//    }
}

