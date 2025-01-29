package com.example.navigation;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class History_Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseFirestore firestore;
    private CollectionReference historyRef;
    private RecyclerView recyclerView;
    private HistoryAdapter historyadapter;
    private List<History> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);

        // Initialize Firestore and collection reference
        firestore = FirebaseFirestore.getInstance();
        historyRef = firestore.collection("history");

        // Initialize RecyclerView and set layout manager
        recyclerView = findViewById(R.id.history_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize historyList
        historyList = new ArrayList<>();

        // Initialize adapter and set it to RecyclerView
        historyadapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(historyadapter);

        // Retrieve history data from Firestore
        fetchHistoryData();
    }

    private void fetchHistoryData() {
        historyRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Clear existing historyList
                    historyList.clear();

                    // Iterate through each document in the result set
                    for (DocumentSnapshot document : task.getResult()) {
                        // Convert document to History object and add to historyList
                        History history = document.toObject(History.class);
                        historyList.add(history);
                    }

                    // Notify adapter of data change
                    historyadapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Error getting history documents", task.getException());
                }
            }
        });
    }
}
