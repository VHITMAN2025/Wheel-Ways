package com.example.navigation;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class bi_cycle_data extends AppCompatActivity {

    FirebaseFirestore db;
    Button total_cycles_details,available_cycles_details,unavailable_cycle_details,damaged_cycle_details,undamaged_cycle_details;

    TextView availableCountTextView, unavailableCountTextView, undamagedCountTextView,damagedCountTextView, totalCountTextView;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bicycle_data);
        total_cycles_details = findViewById(R.id.total_btn);
        available_cycles_details = findViewById(R.id.free_btn);
        unavailable_cycle_details = findViewById(R.id.unfree_btn);
        damaged_cycle_details = findViewById(R.id.damaged_btn);
        undamaged_cycle_details = findViewById(R.id.undamaged_btn);

        total_cycles_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bi_cycle_data.this, Total_cycles_list.class);
                startActivity(intent);
            }
        });
        available_cycles_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(bi_cycle_data.this, Free_cycles_list.class);
                startActivity(intent1);
            }
        });
        unavailable_cycle_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bi_cycle_data.this, Not_Free_cycles_list.class);
                startActivity(intent);
            }
        });
        damaged_cycle_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bi_cycle_data.this, Damaged_cycles_list.class);
                startActivity(intent);
            }
        });
        undamaged_cycle_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(bi_cycle_data.this, Undamaged_cycles_list.class);
                startActivity(intent);
            }
        });
        db = FirebaseFirestore.getInstance();
        availableCountTextView = findViewById(R.id.unallocated);
        unavailableCountTextView = findViewById(R.id.allocated);
        damagedCountTextView = findViewById(R.id.damaged_count);
        undamagedCountTextView = findViewById(R.id.undamaged_count);
        totalCountTextView = findViewById(R.id.total_count);

        // Call methods to get the count of available and unavailable cycles
        getCountOfCycles("available", availableCountTextView);
        getCountOfCycles("unavailable", unavailableCountTextView);
        getConditionOfCycles("damaged", damagedCountTextView);
        getConditionOfCycles("undamaged", undamagedCountTextView);
        getTotalCycleCount();

    }

    private void getConditionOfCycles(String condition, TextView CountTextView)
    {
        db.collection("cycledata")
                .whereEqualTo("condition", condition)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            CountTextView.setText(String.valueOf(count));
                            Log.d(TAG, "Number of " + condition + " cycles: " + count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getCountOfCycles(String status, TextView countTextView) {
        db.collection("cycledata")
                .whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            countTextView.setText(String.valueOf(count));
                            Log.d(TAG, "Number of " + status + " cycles: " + count);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void getTotalCycleCount() {
        // Reference to the "cycledata" collection
        CollectionReference cycleDataRef = db.collection("cycledata");

        // Query to fetch all documents in the "cycledata" collection
        cycleDataRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Get the count of total number of cycles
                    int cycleCount = task.getResult().size();
                    Log.d(TAG, "Total number of cycles: " + cycleCount);

                    // Display the cycle count in an EditText
                    totalCountTextView.setText(String.valueOf(cycleCount));
                } else {
                    // Failed to fetch cycle data
                    Log.e(TAG, "Error getting cycle data: ", task.getException());
                    Toast.makeText(bi_cycle_data.this, "Error getting cycle data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}