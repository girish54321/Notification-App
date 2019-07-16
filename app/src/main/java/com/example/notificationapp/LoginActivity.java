package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passLogin;

    private Button loginBtn;
    private Button signeBtn;

    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.emal_login);
        passLogin = findViewById(R.id.pass_login);
        loginBtn = findViewById(R.id.login_btn);
        signeBtn = findViewById(R.id.sigin_newAc);


        signeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToSign();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String loginEmail = emailLogin.getText().toString();
                String loginPass = passLogin.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    //loginProgress.setVisibility(View.VISIBLE);
                    pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Login...");
                    pd.show();

                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {   // LOGIN CODE FOR FIREBASE
                            if(task.isSuccessful()){
                                pd.dismiss();
                                sendToMain();
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                Log.v("ERROR LOGIN",errorMessage);
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendToSign() {
        Intent i = new Intent(LoginActivity.this,NewAccountActivity.class);
        startActivity(i);
        finish();
    }

    private void sendToMain(){
        Intent i = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
