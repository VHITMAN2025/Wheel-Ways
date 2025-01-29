package com.example.navigation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Feedback_Activity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitFeedbackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitFeedbackButton = findViewById(R.id.submitFeedbackButton);

        submitFeedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        // Get the rating and comment text
        float rating = ratingBar.getRating();
        String comment = commentEditText.getText().toString().trim();

        // Here you can do whatever you want with the feedback data,
        // such as sending it to a server or storing it locally.

        // For now, we'll just display a toast message with the feedback.
        String feedbackMessage = "Thank You For giving" + rating+" stars for our app \nWe work on your Feedback";
        Toast.makeText(this, feedbackMessage, Toast.LENGTH_LONG).show();
    }
}
