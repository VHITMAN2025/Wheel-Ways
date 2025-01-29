package com.example.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    EditText signupName, signupId, signupPassword, signupEmail, signupContact;
    TextView LoginRedirect;
    Button signupButton;
    RadioGroup signupRadioGroup;
    RadioButton radioButton;
    FirebaseAuth fAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);

        signupName = findViewById(R.id.signup_name);
        signupId = findViewById(R.id.signup_id);
        signupPassword = findViewById(R.id.signup_password);
        signupContact = findViewById(R.id.signup_contact);
        signupEmail = findViewById(R.id.signup_email);
        signupRadioGroup = findViewById(R.id.radioSelection);
        LoginRedirect = findViewById(R.id.LoginRedirect);
        signupButton = findViewById(R.id.signup_button);
        fAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        // Inside your main activity or relevant activity
        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d("FCM Token", token);
                        employeeFcmToken(token);
                        // Now you have the FCM token, you can store it in Firestore or use it as needed.
                    } else {
                        Log.e("FCM Token", "Failed to get token", task.getException());
                    }
                });
*/


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                    // Now you can use the token as needed, such as storing it in SharedPreferences or sending it to the server
                                    // For example, you can store it in SharedPreferences
                                   /* SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("employee_token", token);
                                    editor.apply();*/
                                    employeeFcmToken();
                                }
                            });


        LoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void employeeFcmToken() {

        int radioId = signupRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        String radio = radioButton.getText().toString();
        fAuth.createUserWithEmailAndPassword(signupEmail.getText().toString(), signupPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                FirebaseUser user = fAuth.getCurrentUser();
                Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                DocumentReference df = firestore.collection("users").document(user.getUid());
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("name", signupName.getText().toString());
                userInfo.put("id", signupId.getText().toString());
                userInfo.put("password", signupPassword.getText().toString());
                userInfo.put("contact", signupContact.getText().toString());
                userInfo.put("email", signupEmail.getText().toString());
                userInfo.put("role", radioButton.getText().toString());


             //   userInfo.put("token",token);
                df.set(userInfo);
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
