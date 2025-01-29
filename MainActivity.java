package com.example.navigation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.navigation.ui.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Toolbar toolbar;
   Button button;
   Button button1,button2;
   CollectionReference cyclesRef,employeeReqRef;
   FirebaseFirestore db;
   ImageView imageButton;
   ImageView logout_button;
   DocumentReference df,employeeRef;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       firebaseAuth = FirebaseAuth.getInstance();
       user = firebaseAuth.getCurrentUser();
       db = FirebaseFirestore.getInstance();
       cyclesRef = db.collection("cycledata");
       employeeReqRef = db.collection("employeeRequests");
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        button=findViewById(R.id.return_cycle);
        button1=findViewById(R.id.request_cycle);
        button2 = findViewById(R.id.check_availability);
        employeeRef = db.collection("employeeRequests").document(user.getUid());
        String employeeId = user.getUid();
        df = db.collection("employeeRequests").document(employeeId);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("notification","notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        imageButton = findViewById(R.id.scanner_image);
        logout_button = findViewById(R.id.employee_logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
});
        FirebaseMessaging.getInstance().setNotificationDelegationEnabled(true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "Notified Yet", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
                Log.i("MyError",e.getMessage());
            }
        });

        // Check if notifications are enabled for the app
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            // Notifications are not enabled, prompt the user to enable them
            Toast.makeText(this, "Please enable notifications for this app", Toast.LENGTH_SHORT).show();

            // Open app settings to allow the user to enable notifications
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(android.net.Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, ""+user.getUid(), Toast.LENGTH_SHORT).show();
                Intent in=new Intent(MainActivity.this, Request_Activity.class);
                startActivity(in);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,Availability_Activity.class);
        startActivity(intent);
    }
});

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentReference AssignedRef = db.collection("cycleAssignments").document(employeeId);
                AssignedRef.update("assigned_cycle_id","").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG,"Cycle deleted from assigned cycle Assignments");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,e.getMessage());
                    }
                });
                DocumentReference empDocRef = db.collection("employeeRequests").document(employeeId);
                empDocRef.update("status","NoRequest").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG,"NO request Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"NO request Failed",e);
                    }
                });
                // Assuming you have a "cycles" collection in Firestore to store cycle data
                cyclesRef = db.collection("cycledata");

              df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                      String actual_employee_id = documentSnapshot.getString("emp_id");
                      passingEmployee(actual_employee_id);
                  }


                  private void passingEmployee(String actual_employee_id) {
                      cyclesRef.whereEqualTo("assignedto", employeeId)
                              .whereEqualTo("status", "unavailable") // Assuming you want to get only assigned cycles
                              .limit(1) // Limit to one cycle (optional)
                              .get()
                              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                  @Override
                                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                      if (task.isSuccessful()) {
                                          if (!task.getResult().isEmpty()) {
                                              // Get the cycle document assigned to the employee
                                              DocumentSnapshot cycleDoc = task.getResult().getDocuments().get(0);
                                              String cycleId = cycleDoc.getId();
                                              String actual_cyc_id = cycleDoc.getString("id");
                                              Toast.makeText(MainActivity.this, "cyc id"+actual_cyc_id, Toast.LENGTH_SHORT).show();
                                              returnAssignedCycle(cycleId, employeeId, actual_cyc_id,actual_employee_id);
                                              // Now you have the cycle ID assigned to the employee
                                              // You can use it as needed
                                              Log.d(TAG, "Cycle ID assigned to employee " + employeeId + ": " + cycleId);
                                              Toast.makeText(MainActivity.this, "Cycle with ID: " + cycleId + " returned successfully", Toast.LENGTH_SHORT).show();
                                          } else {
                                              // No assigned cycle found
                                              Log.d(TAG, "No assigned cycle found for employee ID: " + employeeId);
                                              Toast.makeText(MainActivity.this, "No assigned cycle found for employee ID: " + employeeId, Toast.LENGTH_SHORT).show();
                                          }
                                      } else {
                                          // Query failed
                                          Log.e(TAG, "Error querying cycles for employee ID: " + employeeId, task.getException());
                                          Toast.makeText(MainActivity.this, "Error querying cycles: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                      }
                                  }
                              });

                  }
              });

// Query for the cycle document assigned to the employee

                }


                private void returnAssignedCycle(String cycleId,String employeeId,String actual_cycle_id,String actual_employee_id) {
                    // Assuming you have a "cycles" collection in Firestore to store cycle data

                    //CollectionReference cyclesRef = db.collection("cycledata");

                    // Get the reference to the cycle document based on its ID
                    DocumentReference cycleDocRef = db.collection("cycledata").document(cycleId);
                    imageButton.setOnClickListener(v -> {
                        scancode(cycleId);
                        //Toast.makeText(this, "Cycle Unlocked", Toast.LENGTH_SHORT).show();
                    });

                    // Update cycle data to mark it as available
                    cycleDocRef.update("status", "available",
                                    "assignedto",null)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Cycle returned successfully
                                        Date returnedDate = new Date();
                                        storeReturnedCycle(cycleId,employeeId,actual_cycle_id,actual_employee_id);
                                        Notification.Builder builder = new Notification.Builder(MainActivity.this, "notification");
                                        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24);
                                        builder.setContentTitle("Wheel ways");
                                        builder.setContentText("Bi-cycle returned successfully");
                                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                        notificationManager.notify(1, builder.build());
                                        FirebaseFirestore.getInstance().collection("history")
                                                .whereEqualTo("cycleId", cycleId)
                                                .whereEqualTo("employeeId", employeeId)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                            document.getReference().update("returnDate",returnedDate);
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Handle failure
                                                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        Log.d(TAG, "Cycle " + cycleId + " returned and available again");
                                        Toast.makeText(MainActivity.this, "Cycle Returned Successfully", Toast.LENGTH_SHORT).show();

                                        // Handle success, notify user, update UI, etc.
                                    } else {
                                        // Failed to return cycle
                                        Log.w(TAG, "Error returning cycle", task.getException());
                                        // Handle failure, show error message, etc.
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            private void storeReturnedCycle(String cycleId, String employeeId, String actual_cycle_id, String actual_employee_id) {
                CollectionReference returnedCyclesRef = db.collection("returnedCycles");
                Map<String, Object> returnedCycleData = new HashMap<>();
                returnedCycleData.put("cycleId", cycleId);
                returnedCycleData.put("employeeId", employeeId);
                returnedCycleData.put("actual_cycle_id", actual_cycle_id);
                returnedCycleData.put("returned", true);
                returnedCycleData.put("actual_employee_id", actual_employee_id);

                returnedCyclesRef.add(returnedCycleData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Returned cycle details added successfully
                                Log.d(TAG, "Returned cycle details added to returnedCycles collection: " + documentReference.getId());
                                Toast.makeText(MainActivity.this, "Returned cycle details added to returnedCycles collection", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Failed to add returned cycle details
                                Log.e(TAG, "Error adding returned cycle details to returnedCycles collection", e);
                                Toast.makeText(MainActivity.this, "Error adding returned cycle details to returnedCycles collection", Toast.LENGTH_SHORT).show();
                            }
                        });
            }


        });

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_feedback:
                Intent intent = new Intent(MainActivity.this, Feedback_Activity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                Intent intent1 = new Intent(MainActivity.this, About_Activity.class);
                startActivity(intent1);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

    return true;
    }
    private void scancode(String cycleId) {
        ScanOptions options = new ScanOptions();
        options.setPrompt("scan");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result ->
    {
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String employeeStatus = documentSnapshot.getString("status");
                assert employeeStatus != null;
                if (employeeStatus.equals("approved")){
                    if (result.getContents() != null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Wheel Ways");
                        builder.setMessage("Bi-cycle unlocked");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Cannot unlock cycle at the moment", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    });


}