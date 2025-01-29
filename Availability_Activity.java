package com.example.navigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Availability_Activity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseFirestore db;
    private TextView availableCountTextView;
    private TextView unavailableCountTextView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Handler handler;
    Runnable checkCycleRunnable;
    Button notify;
    private static final int CHECK_INTERVAL = 30000; // Interval in milliseconds (e.g., 30 seconds)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
firebaseAuth = FirebaseAuth.getInstance();
user = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        availableCountTextView = findViewById(R.id.not_issued_count);
        unavailableCountTextView = findViewById(R.id.issued_count);
        notify = findViewById(R.id.notifybtn);
        handler = new Handler();
        // Call methods to get the count of available and unavailable cycles
        getCountOfCycles("available", availableCountTextView);
        getCountOfCycles("unavailable", unavailableCountTextView);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("notification","notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Initialize runnable to check for available cycles


        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionReference waitingEmployeesRef = db.collection("waitingEmployees");

                // Create a new document with employee information
                String employeeId = user.getUid();
                Map<String, Object> employeeInfo = new HashMap<>();
                employeeInfo.put("employeeId", employeeId);
                employeeInfo.put("timestamp", FieldValue.serverTimestamp());

                // Add the document to the collection
                waitingEmployeesRef.add(employeeInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String emp_id = documentReference.getId();
                                startCheckingCycles(emp_id);
                                Log.d(TAG, "Employee added to waiting list: " + documentReference.getId());
                                // Show a message to the employee that they will be notified when a cycle becomes available
                                Toast.makeText(Availability_Activity.this, "You will be notified when a cycle becomes available", Toast.LENGTH_SHORT).show();
                                //checkForAvailableCycles(emp_id);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error adding employee to waiting list", e);
                                // Show an error message to the employee
                                Toast.makeText(Availability_Activity.this, "Failed to add to waiting list", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop checking for available cycles when the activity is destroyed
        stopCheckingCycles();
    }

    private void startCheckingCycles(String emp_id) {
        // Post the initial execution of the runnable
        handler.post(checkCycleRunnable);
        checkForAvailableCycles(emp_id);
    }

    private void stopCheckingCycles() {
        // Remove any pending executions of the runnable
        handler.removeCallbacks(checkCycleRunnable);
    }

    // Method to check for available cycles

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

    // Method to periodically check for available cycles and notify waiting employees
    private void checkForAvailableCycles(String c_emp_id) {
        // Reference to the "cycledata" collection
        CollectionReference cyclesRef = db.collection("cycledata");

        // Query for available cycles
        cyclesRef.whereEqualTo("status", "available").whereEqualTo("assignedto","")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Error querying for available cycles", e);
                            return;
                        }

                        // If there are available cycles, notify waiting employees
                        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                            notifyWaitingEmployees(c_emp_id);
                        }
                    }
                });
    }

    // Method to notify waiting employees when cycles become available
    private void notifyWaitingEmployees(String emp_id) {
        // Reference to the "waitingEmployees" collection
        CollectionReference waitingEmployeesRef = db.collection("waitingEmployees");
        Notification.Builder builder = new Notification.Builder(Availability_Activity.this,"notification");
        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24);
        builder.setContentTitle("Wheel ways");
        builder.setContentText("Cycle Awaiting for you Make a request");

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

        // Query all documents in the collection
        waitingEmployeesRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve employee information
                                String employeeId = document.getString("employeeId");

                                // Notify the employee (you can implement this based on your notification mechanism)
                                // For example, you can send a push notification to the employee
                                sendNotificationToEmployee(emp_id);

                                // After notifying the employee, remove them from the waiting list
                                document.getReference().delete();
                            }
                        } else {
                            Log.e(TAG, "Error notifying waiting employees", task.getException());
                        }
                    }
                });
    }    private void sendNotificationToEmployee(String employeeId) {
        // Implement your notification mechanism here
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(employeeId + "@fcm.googleapis.com")
                .setMessageId(Integer.toString(new Random().nextInt(9999)))
                .addData("message", "A cycle is available for you!")
                .build());
    }
}
