package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewAccountActivity extends AppCompatActivity {

    private EditText email;
    private EditText pass;
    private Button siginBtn;
    private TextView re_Pass;

    private FirebaseAuth mAuth;
    ProgressDialog pd;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        context = this;

        mAuth = FirebaseAuth.getInstance();
        re_Pass = findViewById(R.id.re_Pass_t);
        email = findViewById(R.id.email_singin);
        pass = findViewById(R.id.pass);
        siginBtn = findViewById(R.id.siginBtn);

        siginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siginUser();
            }
        });
    }

    private void siginUser(){

        String comePas = re_Pass.getText().toString();
        String userEmail = email.getText().toString();
        String userPass = pass.getText().toString();

        if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass)&&!TextUtils.isEmpty(comePas)){
            if(userPass.equals(comePas)){
                pd = new ProgressDialog(context);
                pd.setMessage("SignIn...");
                pd.show();
                mAuth.createUserWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pd.dismiss();
                            Intent setupIntent = new Intent(context, LoginActivity.class);
                            startActivity(setupIntent);
                            finish();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(context, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                    }
                });
            } else {
                Toast.makeText(context, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
