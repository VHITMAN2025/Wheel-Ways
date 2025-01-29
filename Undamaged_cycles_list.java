package com.example.navigation;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class Undamaged_cycles_list extends AppCompatActivity {
    FirebaseFirestore firestore;
    CollectionReference cyclesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cycles_list);
        firestore = FirebaseFirestore.getInstance();
        cyclesRef = firestore.collection("cycledata");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve cycles from Firestore
        cyclesRef.whereEqualTo("condition","undamaged").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Cycle> cyclesList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Extract cycle data from the document
                        String cycleNumber = document.getString("id");
                        String cycleColor = document.getString("colour");
                        String location = document.getString("location");

                        // Create a Cycle object
                        Cycle cycle = new Cycle(cycleNumber, cycleColor, location);
                        cyclesList.add(cycle);
                    }

                    // Initialize and set up RecyclerView adapter
                    CyclesAdapter adapter = new CyclesAdapter(cyclesList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    Toast.makeText(Undamaged_cycles_list.this, "Error getting documents", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}