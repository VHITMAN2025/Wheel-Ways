package com.example.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Request_Activity extends AppCompatActivity {

    private FirebaseFirestore db;
    EditText emp_id, cycle_color, cycle_location;
    Button button;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DocumentReference RequestsRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        emp_id = findViewById(R.id.employee_id);
        cycle_color = findViewById(R.id.cycle_color);
        cycle_location = findViewById(R.id.location);
        button = findViewById(R.id.request_button);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        RequestsRef = db.collection("employeeRequests").document(user.getUid());

        RequestsRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    String status = "NoRequest";
                    Map<String,Object> map = new HashMap<>();
                    map.put("status",status);
                    RequestsRef.set(map);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Request_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String empid = emp_id.getText().toString();
                String cycleColor = cycle_color.getText().toString();
                String location = cycle_location.getText().toString();
                String current_id = user.getUid();
                String status = "pending";
                    db.collection("cycleAssignments").document(user.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        String assignedCycle = documentSnapshot.getString("assigned_cycle_id");
                                        if (assignedCycle != null && !assignedCycle.isEmpty()) {
                                            // Employee already has an assigned cycle, cannot make another request
                                            Toast.makeText(Request_Activity.this, "You already have an assigned cycle. Return it before making another request.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // No assigned cycle, proceed with sending the request
                                            getFCMToken(empid, cycleColor, location, current_id, status);
                                        }
                                    } else {
                                        // No document found for the employee, proceed with sending the request
                                        getFCMToken(empid, cycleColor, location, current_id, status);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Failed to retrieve cycle assignment data, show error message
                                    Toast.makeText(Request_Activity.this, "Failed to check assigned cycle: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                EmployeeRequest employeeRequest = new EmployeeRequest(empid, cycleColor, location, status, current_id);
               /* db.collection("employeeRequests")
                        .add(employeeRequest)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                getFCMToken(empid,cycleColor,location,current_id,status);
                                Toast.makeText(Request_Activity.this, "Cycle request sent successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Request_Activity.this, "Failed to send cycle request", Toast.LENGTH_SHORT).show();
                            }
                        });
*/

              /*  db.collection("employeeRequests")
                        .add(employeeRequest)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                getFCMToken(empid,cycleColor,location,current_id,status);
                                Toast.makeText(Request_Activity.this, "Cycle request sent successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Request_Activity.this, "Failed to send cycle request", Toast.LENGTH_SHORT).show();
                            }
                        });
                        */
            }
        });
    }

    void getFCMToken(String empid,String cycleColor, String location ,String current_id,String status) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if ((task.isSuccessful())) {
                    String empToken = task.getResult();
                    DocumentReference requestRef = db.collection("employeeRequests").document(user.getUid());
                    Map<String, Object> data = new HashMap<>();
                    data.put("Emp_Token", empToken);
                    data.put("emp_id", empid);
                    data.put("cycle_color", cycleColor);
                    data.put("cycle_location",location);
                    data.put("current_id", current_id);
                    data.put("status",status);

                    // Assuming you want to associate the token with the user making the request

                    requestRef.set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Success
                                    storeRequestInCycleAssignments(current_id);
                                    //Toast.makeText(Request_Activity.this, "Cycle request sent successfully", Toast.LENGTH_SHORT).show();
                                    //finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Request_Activity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.d("PORA",e.getMessage());
                                }
                            });
                }
            }
        });
    }
    void storeRequestInCycleAssignments(String current_id) {
        // Prepare data for storing in cycleAssignments collection
        Map<String, Object> assignmentData = new HashMap<>();
        assignmentData.put("employee_id", current_id);
        assignmentData.put("assigned_cycle_id", ""); // Initially empty, to be updated later when a cycle is assigned

        // Store request in cycleAssignments collection
        db.collection("cycleAssignments").document(current_id)
                .set(assignmentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Success, request stored in both collections
                        Toast.makeText(Request_Activity.this, "Cycle request sent successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to store request in cycleAssignments collection
                        Toast.makeText(Request_Activity.this, "Failed to store request in cycleAssignments: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
