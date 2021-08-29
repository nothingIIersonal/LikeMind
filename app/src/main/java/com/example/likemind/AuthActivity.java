    package com.example.likemind;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import helper.SQLiteHandler;
import helper.SessionManager;

public class AuthActivity extends AppCompatActivity {

    private EditText login_PT, password_PT, login_reg_PT, password_reg_PT, password_reg_repeat_PT;
    private TextView info_TV;
    private View reg_fragment;
    private SQLiteHandler db;
    private SessionManager session;

//    private boolean doubleBackToExitPressedOnce = false;
    private long onRecentBackPressedTime;
    private static final String TAG = RegistrationFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_activity);

        reg_fragment = findViewById(R.id.reg_fragment);
        reg_fragment.setVisibility(View.GONE);

        Button go_to_main_btn = findViewById(R.id.go_to_main_btn);
        Button create_account_btn = findViewById(R.id.create_account_BTN);
        Button reg_BTN = findViewById(R.id.reg_BTN);
        Button back_to_login_BTN = findViewById(R.id.back_to_login_BTN);

        TextView loginTV, passwordTV, create_acc_TV;
        loginTV = findViewById(R.id.login_TV);
        passwordTV = findViewById(R.id.password_TV);
        create_acc_TV = findViewById(R.id.create_account_TV);
        ImageView logo_view = findViewById(R.id.logo_view);
        login_reg_PT = findViewById(R.id.login_reg_PT);
        password_reg_PT = findViewById(R.id.password_reg_PT);
        password_reg_repeat_PT = findViewById(R.id.password_repeat_reg_PT);

        login_PT = findViewById(R.id.login_PT);
        password_PT = findViewById(R.id.password_PT);
        info_TV = findViewById(R.id.info_TV);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(intent);
            super.finish();
        }

        back_to_login_BTN.setOnClickListener(v -> {
            logo_view.setVisibility(View.VISIBLE);
            loginTV.setVisibility(View.VISIBLE);
            passwordTV.setVisibility(View.VISIBLE);
            login_PT.setVisibility(View.VISIBLE);
            password_PT.setVisibility(View.VISIBLE);
            go_to_main_btn.setVisibility(View.VISIBLE);
            create_account_btn.setVisibility(View.VISIBLE);
            info_TV.setVisibility(View.VISIBLE);
            create_acc_TV.setVisibility(View.VISIBLE);

            reg_fragment.setVisibility(View.GONE);
        });

        // Register Button Click event
        reg_BTN.setOnClickListener(view -> {
            String login = login_reg_PT.getText().toString().trim();
            String password = password_reg_PT.getText().toString().trim();
            String password_repeat = password_reg_repeat_PT.getText().toString().trim();

            if (password.equals(password_repeat)) {
                if (!login.isEmpty() && !password.isEmpty()) {
                    registerUser(login, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Passwords do not match!", Toast.LENGTH_LONG)
                        .show();
            }
        });

        // Login button Click Event
        go_to_main_btn.setOnClickListener(view -> {
            String login = login_PT.getText().toString().trim();
            String password = password_PT.getText().toString().trim();

            // Check for empty data in the form
            if (!login.isEmpty() && !password.isEmpty()) {
                // login user
                checkLogin(login, password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }
        });

/*        go_to_main_btn.setOnClickListener(v -> {
            if (password_PT.getText().toString().equals(login_PT.getText().toString() + "123")) {
                info_TV.setTextColor(getColor(R.color.green));
                info_TV.setText(getString(R.string.auth_success));
                Intent go_to_main_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(go_to_main_intent);
                super.finish();
            }
            else {
                info_TV.setTextColor(getColor(R.color.red));
                info_TV.setText(getString(R.string.auth_error));
            }
        });
*/
        create_account_btn.setOnClickListener(v -> {
            logo_view.setVisibility(View.GONE);
            loginTV.setVisibility(View.GONE);
            passwordTV.setVisibility(View.GONE);
            login_PT.setVisibility(View.GONE);
            password_PT.setVisibility(View.GONE);
            go_to_main_btn.setVisibility(View.GONE);
            create_account_btn.setVisibility(View.GONE);
            info_TV.setVisibility(View.GONE);
            create_acc_TV.setVisibility(View.GONE);

            reg_fragment.setVisibility(View.VISIBLE);
        });

        /*reg_BTN.setOnClickListener(v -> {
            logo_view.setVisibility(View.VISIBLE);
            loginTV.setVisibility(View.VISIBLE);
            passwordTV.setVisibility(View.VISIBLE);
            login_PT.setVisibility(View.VISIBLE);
            password_PT.setVisibility(View.VISIBLE);
            go_to_main_btn.setVisibility(View.VISIBLE);
            create_account_btn.setVisibility(View.VISIBLE);
            info_TV.setVisibility(View.VISIBLE);
            create_acc_TV.setVisibility(View.VISIBLE);

            reg_fragment.setVisibility(View.GONE);
        });*/
    }

/*    @Override
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
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String login, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_LOGIN, response -> {
            Log.d(TAG, "Login Response: " + response.toString());

            try {
                JSONObject jObj = new JSONObject(response);
                boolean error = jObj.getBoolean("error");

                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    session.setLogin(true);

                    info_TV.setTextColor(getColor(R.color.green));
                    info_TV.setText(getString(R.string.auth_success));

                    // Now store the user in SQLite
                    String uid = jObj.getString("uid");

                    JSONObject user = jObj.getJSONObject("user");
                    String login1 = user.getString("login");
                    String created_at = user.getString("created_at");

                    // Inserting row in users table
                    db.addUser(login1, uid, created_at);

                    // Launch main activity
                    Intent intent = new Intent(AuthActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = jObj.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                    info_TV.setTextColor(getColor(R.color.red));
                    info_TV.setText(getString(R.string.auth_error));
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }, error -> {
            Log.e(TAG, "Login Error: " + error.getMessage());
            Toast.makeText(getApplicationContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Function to store user in MySQL database will post params(tag, login,
     * password) to register url
     * */
    private void registerUser(final String login,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String login = user.getString("login");
                        String created_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(login, uid, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        login_reg_PT = findViewById(R.id.login_reg_PT);
                        password_reg_PT = findViewById(R.id.password_reg_PT);
                        password_reg_repeat_PT = findViewById(R.id.password_repeat_reg_PT);

                        login_reg_PT.setText("");
                        password_reg_PT.setText("");
                        password_reg_repeat_PT.setText("");
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("login", login);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}