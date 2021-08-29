package com.example.likemind;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.Timer;

import helper.SQLiteHandler;
import helper.SessionManager;

public class MainActivity extends AppCompatActivity {

    private View about_me_fragment;
    private View events_map_fragment;

    private SessionManager session;
    private SQLiteHandler db;

//    private boolean doubleBackToExitPressedOnce = false;
    private long onRecentBackPressedTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button log_out_BTN = findViewById(R.id.log_out_BTN);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        about_me_fragment = findViewById(R.id.about_me_fragment);
        events_map_fragment = findViewById(R.id.events_map_fragment);

        about_me_fragment.setVisibility(View.VISIBLE);
        events_map_fragment.setVisibility(View.GONE);

        TabLayout menuTabLayout = findViewById(R.id.menuTabLayout);

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String login = user.get("login");

        // Logout button click event
        log_out_BTN.setOnClickListener(v -> logoutUser());

        menuTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selected_tab_index = tab.getPosition();

                switch (selected_tab_index)
                {
                    case (0):
                        about_me_fragment.setVisibility(View.VISIBLE);
                        events_map_fragment.setVisibility(View.GONE);
                        break;
                    case (1):
                        about_me_fragment.setVisibility(View.GONE);
                        events_map_fragment.setVisibility(View.VISIBLE);
                        break;
                }

            //    Toast toast = Toast.makeText(getApplicationContext(), "Tab selected", Toast.LENGTH_LONG);
            //    toast.show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            //    Toast toast = Toast.makeText(getApplicationContext(), "Tab reselected", Toast.LENGTH_LONG);
            //    toast.show();
            }
        });
    }
/*
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
*/
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - onRecentBackPressedTime > 2000) {
            onRecentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();
            return;
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}
