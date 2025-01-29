package com.example.navigation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Admin_Approval extends AppCompatActivity {

    private static final String TAG = "AdminActivity";
    private FirebaseFirestore db;
    private TextView requestsTextView;
    private LinearLayout requestsLinearLayout;
    TextView employee_id,colour,location;
    Button approveButton,rejectButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_requests_admin);
     //   requestsLinearLayout = findViewById(R.id.requestsLinearLayout);
        db = FirebaseFirestore.getInstance();
      // requestsTextView = findViewById(R.id.requestsTextView);
         employee_id = findViewById(R.id.Tid);
         colour = findViewById(R.id.Tcolour);
         location = findViewById(R.id.Tlocation);
         approveButton = findViewById(R.id.bAccept);
         rejectButton = findViewById(R.id.bReject);
        // Retrieve cycle requests
        retrieveCycleRequests();
    }

    private void retrieveCycleRequests() {
        CollectionReference requestsRef = db.collection("employeeRequests");
        requestsRef.whereEqualTo("status","pending").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    StringBuilder requests = new StringBuilder();
                    for (DocumentSnapshot document : task.getResult()) {
                        EmployeeRequest request = document.toObject(EmployeeRequest.class);
                        String requestId = document.getId();

                        // Display request details
                        assert request != null;
                       // emp_id.setText(jasuser.getEmp_id());
                        employee_id.setText(request.getEmp_id());
                        colour.setText(request.getCycle_color());
                        location.setText(request.getCycle_location());

                       /* requests.append("Request ID: ").append(requestId)
                                .append(", Employee ID: ").append(request.getEmp_id())
                                .append(", Cycle_color: ").append(request.getCyc_color())
                                .append(", Location: ").append(request.getCyc_location())
                                .append(", Status: ").append(request.getStatus())
                                .append("\n");*/

                        // Display approve and reject buttons
                       // Button approveButton = new Button(Admin_Approval.this);
                       // approveButton.setText("Approve");
                        approveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                approveRequest(requestId, request.getEmp_id());
                            }
                        });

                       // Button rejectButton = new Button(Admin_Approval.this);
                      //  rejectButton.setText("Reject");
                        rejectButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rejectRequest(requestId);
                            }
                        });

                      //  requestsLinearLayout.addView(approveButton);
                      //  requestsLinearLayout.addView(rejectButton);
                    }
                   // requestsTextView.setText(requests.toString());
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void approveRequest(String requestId, String employeeId) {
        // Update request status to "Approved" in Firestore
        DocumentReference requestRef = db.collection("employeeRequests").document(requestId);
        Map<String,Object> map = new HashMap<>();
        map.put("isAdminApproved",true);
        requestRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(Admin_Approval.this, "Request Accepted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Admin_Approval.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rejectRequest(String requestId) {

                db.collection("employeeRequests")
                        .whereEqualTo("status", "pending")
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        // Get the first pending request
                                        DocumentReference requestRef = task.getResult().getDocuments().get(0).getReference();
                                        // Delete the request
                                        requestRef.delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(Admin_Approval.this, "Request rejected", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("EmployeeRequests", "Failed to reject request", e);
                                                        Toast.makeText(Admin_Approval.this, "Failed to reject request", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(Admin_Approval.this, "No pending requests", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.e("EmployeeRequests", "Error getting requests", task.getException());
                                    Toast.makeText(Admin_Approval.this, "Error getting requests", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

    private void allocateCycleToEmployee(String employeeId) {
        // Here you can implement the logic to allocate a cycle to the employee
        // For example, you can update the cycle data in Firestore to mark it as allocated
    }
}
