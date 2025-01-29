package com.example.navigation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import io.reactivex.rxjava3.annotations.NonNull;

public class Return_Activity_Security extends AppCompatActivity {
TextView returned_emp_id,returned_cycle_id;
CheckBox damagedCheckbox,undamagedCheckbox;
Button submit;
FirebaseFirestore db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cyclereturn);
        returned_emp_id = findViewById(R.id.returned_emp_id);
        returned_cycle_id = findViewById(R.id.returned_cycle_id);
        submit = findViewById(R.id.sub_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Return_Activity_Security.this, "Cycle Condition Updated SuccessFully", Toast.LENGTH_SHORT).show();
            }
        });
       db = FirebaseFirestore.getInstance();
        // Reference to the Firestore collection for returned cycles
        CollectionReference returnedCyclesRef = db.collection("returnedCycles");

// Query the Firestore collection to fetch the returned cycles
        returnedCyclesRef.whereEqualTo("returned",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        // Extract cycle ID and employee ID from the document
                        String doc_id = document.getId();
                        String cycleId = document.getString("cycleId");
                        String employeeId = document.getString("actual_employee_id");
                        String i_want_cycle_id = document.getString("actual_cycle_id");
                        String actual_cycle_id = document.getString("actual_cycle_id");


                        // Display the retrieved IDs in EditText fields
                        returned_cycle_id.setText(i_want_cycle_id);
                        returned_emp_id.setText(employeeId);
                        CheckBox damageCheckbox = findViewById(R.id.damagedCheckBox);
                        CheckBox undamagedCheckbox = findViewById(R.id.undamagedCheckBox);

                        damageCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Update cycle condition to damaged when checkbox is checked
                                if (isChecked) {
                                    updateCycleCondition(cycleId, true,doc_id);
                                    undamagedCheckbox.setChecked(false); // Uncheck the other checkbox
                                }
                            }
                        });

                        undamagedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                // Update cycle condition to undamaged when checkbox is checked
                                if (isChecked) {
                                    updateCycleCondition(cycleId, false,doc_id);
                                    damageCheckbox.setChecked(false); // Uncheck the other checkbox
                                }
                            }
                        });

                        // If you want to handle multiple returned cycles, you can append the IDs to the EditText fields
                        //cycleIdEditText.append(cycleId + "\n");
                        //employeeIdEditText.append(employeeId + "\n");
                    }
                } else {
                    Log.e(TAG, "Error getting returned cycles: ", task.getException());
                    Toast.makeText(Return_Activity_Security.this, "Error getting returned cycles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateCycleCondition(String cycleId, boolean isDamaged,String document_id) {
        // Reference to the "cycles" collection
        CollectionReference cyclesRef = db.collection("cycledata");
        CollectionReference returnedCycleRef = db.collection("returnedCycles");

        // Update the cycle condition based on the checkbox selection
        String condition = isDamaged ? "damaged" : "undamaged";

        // Update the cycle document with the new condition
        cyclesRef.document(cycleId)
                .update("condition", condition)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Cycle condition updated successfully
                        Log.d(TAG, "Cycle condition updated to " + condition);
                      //  Toast.makeText(Return_Activity_Security.this, "Cycle condition updated to " + condition, Toast.LENGTH_SHORT).show();
                        returnedCycleRef.document(document_id).update("returned",false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG,"removed from returned");
                                Toast.makeText(Return_Activity_Security.this, "Cycle removed", Toast.LENGTH_SHORT).show();
                            }
                        });
                       // removeCycleFromReturnedCycles(cycleId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to update cycle condition
                        Log.e(TAG, "Error updating cycle condition", e);
                        //Toast.makeText(Return_Activity_Security.this, "Error updating cycle condition", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    /*private void removeCycleFromReturnedCycles(String cycleId) {
        // Reference to the "returnedCycles" collection
        CollectionReference returnedCyclesRef = db.collection("returnedCycles");
        DocumentReference documentRef = returnedCyclesRef.document(cycleId);
        // Delete the document from the "returnedCycles" collection
        documentRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document deleted successfully
                        Log.d(TAG, "Cycle removed from returnedCycles collection");
                        Toast.makeText(Return_Activity_Security.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to delete document
                        Log.e(TAG, "Error removing cycle from returnedCycles collection", e);
                        Toast.makeText(Return_Activity_Security.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }*/

}