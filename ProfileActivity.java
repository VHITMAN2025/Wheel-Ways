package com.example.navigation.ui;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.navigation.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    // Get the user ID of the current user

    // Access Firestore instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView name,roleview,contactview,emailview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        name = findViewById(R.id.profile_name);
        roleview = findViewById(R.id.profile_role);
        contactview = findViewById(R.id.profile_contact);
        emailview = findViewById(R.id.profile_email);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        assert currentUser != null;
        String userId = currentUser.getUid();
        // Assume you have already authenticated the user
// Query Firestore to get user profile data
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Retrieve user profile data
                    String username = documentSnapshot.getString("name");
                    String email = documentSnapshot.getString("email");
                    String contact = documentSnapshot.getString("contact");
                    //String user_id = documentSnapshot.getString("id");
                    String role = documentSnapshot.getString("role");

                    // Display user profile data in UI
                    // For example, set TextView texts
                    name.setText(username);
                    emailview.setText(email);
                    contactview.setText(contact);
                    roleview.setText(role);
                } else {
                    Log.d(TAG, "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error retrieving user profile", e);
            }
        });



    }
}
