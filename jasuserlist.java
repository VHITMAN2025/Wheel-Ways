package com.example.navigation;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
//import { getAuth } from "firebase/auth";


public class jasuserlist extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<jasuser> list1;
    FirebaseFirestore firestore;
    CollectionReference db;
    DocumentReference df;
    CyclesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total1);

        recyclerView=findViewById(R.id.total_view);
        firestore = FirebaseFirestore.getInstance();
        db = firestore.collection("cycledata");
        list1=new ArrayList<>();
        df = firestore.collection("cycledata").document();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adapter=new CyclesAdapter(list1);
        recyclerView.setAdapter(adapter);
        // Initialize RecyclerView
// Retrieve cycles from Firestore
        df.collection("cycledata").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        jasuser cycle = document.toObject(jasuser.class);
                        list1.add(cycle);
                    }
                    // Initialize and set up RecyclerView adapter
                    // MyAdapter1 adapter = new MyAdapter1(list1);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(jasuserlist.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



}
 //adapter.notifyDataSetChanged();
