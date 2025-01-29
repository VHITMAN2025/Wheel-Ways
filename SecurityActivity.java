package com.example.navigation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.navigation.ui.ProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SecurityActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView logout_button;
    Button cycle_data,data_monitor,cycle_return_status,cycle_requests;
    FirebaseAuth firebaseAuth;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        firebaseAuth = FirebaseAuth.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        cycle_data=findViewById(R.id.cycle_data_button);
        data_monitor=findViewById(R.id.data_monitor_btn);
        cycle_return_status = findViewById(R.id.cycle_return_btn);
        cycle_requests = findViewById(R.id.cycle_requests_button);

        cycle_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(SecurityActivity.this,bi_cycle_data.class);
                startActivity(in);

            }
        });
        logout_button = findViewById(R.id.security_logout);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(SecurityActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        data_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SecurityActivity.this, History_Activity.class);
                startActivity(i);
            }
        });
        cycle_return_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SecurityActivity.this, Return_Activity_Security.class);
                startActivity(i);
            }
        });
        cycle_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SecurityActivity.this,Cycle_Requests_Security_Activity.class);
                startActivity(i);
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
                Intent intent = new Intent(SecurityActivity.this, Feedback_Activity.class);
                startActivity(intent);
                break;
            case R.id.nav_about:
                Intent intent1 = new Intent(SecurityActivity.this, About_Activity.class);
                startActivity(intent1);
                break;
            case R.id.nav_profile:
                Intent intent3 = new Intent(SecurityActivity.this, ProfileActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;}
}