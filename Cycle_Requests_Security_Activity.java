package com.example.navigation;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cycle_Requests_Security_Activity extends AppCompatActivity {
    FirebaseFirestore db;
    Button assign;
    CollectionReference cyclesRef;
    CollectionReference requestsRef;
    TextView emp_id, colour, location;

    // Get the reference to the cycle document based on its ID
    //DocumentReference cycleDocRef = cyclesRef.document(cycleId);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycle_requests_security);
        String imp_id = getIntent().getStringExtra("current_id");
        emp_id = findViewById(R.id.Sid);
        colour = findViewById(R.id.Scolour);
        location = findViewById(R.id.Slocation);
        assign = findViewById(R.id.SAccept);
                db = FirebaseFirestore.getInstance();
                requestsRef = db.collection("employeeRequests");


                requestsRef.whereEqualTo("status","pending").whereEqualTo("isAdminApproved",true).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            StringBuilder requests = new StringBuilder();
                            for (DocumentSnapshot document : task.getResult()) {
                                EmployeeRequest request = document.toObject(EmployeeRequest.class);
                                // Display request details
                                String doc_id = document.getId();
                                assert request != null;
                                String EemployeeId = document.getString("current_id");
                                String actual_employee_id = document.getString("emp_id");
                                emp_id.setText(request.getEmp_id());
                                colour.setText(request.getCycle_color());
                                location.setText(request.getCycle_location());
                                assign.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        allocateCycleToEmployee(EemployeeId,doc_id,actual_employee_id);
                                    }
                                });

                                Toast.makeText(Cycle_Requests_Security_Activity.this, ""+EemployeeId, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

            }


    private void allocateCycleToEmployee(String employeeId,String doc_id,String actual_employee_id) {
        // Assuming you have a "cycles" collection in Firestore to store cycle data
        CollectionReference cyclesRef = db.collection("cycledata");

        // Query for available cycles that are not assigned to any employee
        cyclesRef.whereEqualTo("status", "available")
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Get the first available cycle
                            DocumentSnapshot cycleDoc = task.getResult().getDocuments().get(0);
                            String cycleId = cycleDoc.getId();
                            String actual_cycle_id = cycleDoc.getString("id");
                         //   String actual_cycle_id = cycleDoc.getString("id");
                            Map<String, Object> assignmentData = new HashMap<>();
                            assignmentData.put("employee_id", employeeId);
                            assignmentData.put("assigned_cycle_id", cycleId);
                            assignmentData.put("assigned_date", new Date()); // You can add more details if needed

                           DocumentReference CRef = db.collection("cycleAssignments").document(employeeId);
                           CRef.update(assignmentData).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {
                                   Log.i(TAG,"You Made it");
                               }
                           }).addOnFailureListener(new OnFailureListener() {
                               @Override
                               public void onFailure(@NonNull Exception e) {
                                   Log.i(TAG,e.getMessage());
                               }
                           });

                            // Update cycle data to mark it as allocated to the specific employee
                            cycleDoc.getReference().update("status", "unavailable", "assignedto", employeeId)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Cycle allocated successfully
                                                ReturnedCycle_Details returnedCycle_details = new ReturnedCycle_Details(cycleId, employeeId);

                                                Log.d(TAG, "Cycle " + cycleId + " allocated to employee: " + employeeId);
                                                Toast.makeText(Cycle_Requests_Security_Activity.this, "Cycle Assigned SuccessFully", Toast.LENGTH_SHORT).show();


                                                CollectionReference empRef = db.collection("employeeRequests");
                                                DocumentReference empDocRef = empRef.document(employeeId);
                                                empDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        String employeeFCMToken = documentSnapshot.getString("Emp_Token");
                                                        String new_actual_employee_id = documentSnapshot.getString("emp_id");
                                                        sendNotificationToEmployee(employeeFCMToken);
                                                        Log.i("Finally DID it",employeeFCMToken);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d(TAG ,"",e);
                                                    }
                                                });


                                                storeIssuanceDate(cycleId,employeeId,actual_cycle_id,actual_employee_id);
                                                updateEmployeeStatus(doc_id);
                                                // Handle success, notify user, update UI, etc.
                                            } else {
                                                // Failed to allocate cycle
                                                Log.w(TAG, "Error allocating cycle check Inventory", task.getException());
                                                // Handle failure, show error message, etc.
                                            }
                                        }
                                    });
                        } else {
                            // No available cycles found or query failed
                            Log.d(TAG, "No available cycles or query failed");
                            // Handle failure, show error message, etc.
                        }
                    }

                    private void updateEmployeeStatus(String doc_id) {
                        // Reference to the "employees" collection
                        CollectionReference employeesRef = db.collection("employeeRequests");

                        // Update employee status to "approved"
                        employeesRef.document(doc_id).update("status", "approved")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Employee status updated successfully
                                            Log.d(TAG, "Employee status updated to 'approved'");
                                            Toast.makeText(Cycle_Requests_Security_Activity.this, "Employee status updated to 'approved'", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Failed to update employee status
                                            Log.e(TAG, "Error updating employee status", task.getException());
                                            Toast.makeText(Cycle_Requests_Security_Activity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }
    private void storeIssuanceDate(String cycleId, String employeeId, String actual_cycle_id, String actual_employee_id) {
        // Generate the issuance date (current date/time)
        Date issuanceDate = new Date(); // Current date/time

        // Create a new history object
        History history = new History();
        history.setCycleId(cycleId);
        history.setEmployeeId(employeeId);
        history.setActual_cycle_id(actual_cycle_id);
        history.setActual_employee_id(actual_employee_id);
        history.setIssuanceDate(issuanceDate);

        // Add the history object to the "history" collection in Firestore
        FirebaseFirestore.getInstance().collection("history")
                .add(history)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Issuance date stored in history collection: " + documentReference.getId());
                    // Handle success
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error storing issuance date in history collection", e);
                    // Handle failure
                });
    }
    public void sendNotificationToEmployee(String employeeFCMToken) {
        // Construct the FCM message
        Map<String, String> messageData = new HashMap<>();
        messageData.put("title", "Cycle Assigned");
        messageData.put("body", "A cycle has been assigned to you.");

        // Send the FCM message to the specified employee
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(employeeFCMToken)
                .setMessageId(UUID.randomUUID().toString())
                .setData(messageData)
                .build());
    }

    // Method to update return date in history collection


}
