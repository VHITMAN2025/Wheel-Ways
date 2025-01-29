package com.example.navigation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class scanner extends AppCompatActivity {

    Button btn;
    FirebaseFirestore db;
    DocumentReference df;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        btn = findViewById(R.id.btn_scan);
        db = FirebaseFirestore.getInstance();
        df = db.collection("employeeRequests").document(user.getUid());
        btn.setOnClickListener(v -> {
            scancode();
            Toast.makeText(this, "Cycle Unlocked", Toast.LENGTH_SHORT).show();
        });
    }

    private void scancode() {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(scanner.this);
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
                    Toast.makeText(scanner.this, "Cannot unlock cycle at the moment", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(scanner.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    });

}
