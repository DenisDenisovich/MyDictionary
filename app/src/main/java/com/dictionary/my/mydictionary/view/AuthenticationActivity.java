package com.dictionary.my.mydictionary.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dictionary.my.mydictionary.R;

/**
 * Created by luxso on 06.05.2018.
 */

public class AuthenticationActivity  extends AppCompatActivity{
    private final static String LOG_TAG = "Log_AuthActivity";
    Button btnSignIn;
    Button btnSignCreateAccount;
    EditText etLogin;
    EditText etPassword;

    SharedPreferences sPref;
    public static final String APP_PREFERENCES = "mysettings";
    private final static String KEY_AUTH = "auth";
    private final static String KEY_AUTH_LOGIN = "login";
    private final static String KEY_AUTH_PASSWORD = "password";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        Log.d(LOG_TAG, "onCreate()");

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        btnSignIn = findViewById(R.id.btnSingIn);
        btnSignCreateAccount = findViewById(R.id.btnCreateAccount);

        btnSignCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });

        btnSignIn.setOnClickListener(view -> {
            String login = etLogin.getText().toString();
            String password = etPassword.getText().toString();
            if(login.isEmpty() || password.isEmpty()){
                Toast.makeText(getApplicationContext(), "You can't use empty login/password",Toast.LENGTH_LONG).show();
            }else{
                Log.d(LOG_TAG, "save");
                sPref = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
                SharedPreferences.Editor ed = sPref.edit();
                ed.putString(KEY_AUTH_LOGIN, etLogin.getText().toString());
                ed.putString(KEY_AUTH_PASSWORD, etLogin.getText().toString());
                ed.putBoolean(KEY_AUTH,true);
                ed.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
