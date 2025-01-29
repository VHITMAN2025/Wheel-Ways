package com.example.navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in
        if (isUserLoggedIn()) {
            // User is already logged in, navigate to appropriate activity based on role
            String userRole = getUserRole();
            if ("Admin".equals(userRole)) {
                startActivity(new Intent(this, AdminActivity.class));
            } else if ("Security".equals(userRole)) {
                startActivity(new Intent(this, SecurityActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }
            finish(); // Finish the current activity
        } else {
            // User is not logged in, navigate to login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // Finish the current activity
        }
    }

    // Check if user is logged in
    private boolean isUserLoggedIn() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getBoolean("isLoggedIn", false);
    }

    // Get user role
    private String getUserRole() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getString("userRole", "");
    }

}
