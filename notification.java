package com.example.navigation;

import androidx.appcompat.app.AppCompatActivity;

        import android.annotation.SuppressLint;
        import android.app.Notification;
        import android.app.NotificationChannel;
        import android.os.Build;
        import android.os.Bundle;
        import android.app.NotificationManager;
        import android.content.Context;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;


public class notification extends AppCompatActivity {
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);
        button=findViewById(R.id.notifybtn);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("notification","notification",NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

// Create a notification
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification.Builder builder = new Notification.Builder(notification.this,"notification");
                builder.setSmallIcon(R.drawable.baseline_circle_notifications_24);
                builder.setContentTitle("Wheel ways");
                builder.setContentText("sorry for for inconvienece.We will let u know when the bi-cycle is available");

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1, builder.build());
            }
        });

    }
}
