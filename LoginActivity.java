package com.example.navigation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginbutton;
    TextView SignupRedirect;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;
    String User;
    DatabaseReference reference, reference1;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);
        firestore = FirebaseFirestore.getInstance();
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);
        loginbutton = findViewById(R.id.loginbutton);
        SignupRedirect = findViewById(R.id.signupredirect);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("users");
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@io.reactivex.rxjava3.annotations.NonNull Task<String> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String fcmToken = task.getResult();
                            // Pass the FCM token to the security activity
                            Intent intent = new Intent(LoginActivity.this,Cycle_Requests_Security_Activity.class);
                            intent.putExtra("token", fcmToken);
                        } else {
                            // Failed to retrieve FCM token
                            Log.e(TAG, "Failed to get FCM token", task.getException());
                            Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        fAuth = FirebaseAuth.getInstance();
       // User = fAuth.getCurrentUser().getUid();
        SignupRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","onClick"+loginEmail.getText().toString());
                String textEmail = loginEmail.getText().toString();
                String textPassword = loginPassword.getText().toString();
                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    loginEmail.setError("Email is Required");
                    loginEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    loginPassword.setError("Password is Required");
                    loginPassword.requestFocus();
                } else {
                    loginUser(textEmail, textPassword);
                }
            }
        });
    }

    private void loginUser(String textEmail, String textPassword) {
        fAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Logged in Successfull", Toast.LENGTH_SHORT).show();
                checkUserRole(Objects.requireNonNull(authResult.getUser()).getUid(),textEmail,textPassword);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole(String uid,String textPassword,String textEmail) {
        DocumentReference df = firestore.collection("users").document(uid);
        df.get().addOnSuccessListener(documentSnapshot -> {
        Log.d("TAG","onSuccess"+documentSnapshot.getData());
        String role= documentSnapshot.getString("role");
            if (role != null) {
                switch (role) {
                    case "Admin":
                        Intent intent1 = new Intent(LoginActivity.this, AdminActivity.class);
                        startActivity(intent1);
                        saveUserLoginDetails(textEmail,textPassword,role);
                        break;
                    case "Security":
                        Intent intent2 = new Intent(LoginActivity.this, SecurityActivity.class);
                        startActivity(intent2);
                        saveUserLoginDetails(textEmail,textPassword,role);
                        break;
                    default:
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        saveUserLoginDetails(textEmail,textPassword,role);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Save user login details in SharedPreferences
    private void saveUserLoginDetails(String userId,String userPwd,String userRole) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        // Assuming you have obtained user details after successful login
        // Replace with actual user role

        // Save user details
        editor.putString("userId", userId);
        editor.putString("userRole", userRole);
        editor.putString("userPwd",userPwd);
        editor.putBoolean("isLoggedIn", true);
        // Commit changes
        editor.apply();
    }

}
