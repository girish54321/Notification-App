package com.example.notificationapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notificationapp.modal.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendNoteActivity extends AppCompatActivity {

    private TextView textView, error;
    private EditText editTextTile,editTextBody;
    Button butSend;

    ArrayList<String> phoneNumbers;

    String emailGet,tokenGet;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_note);

         tokenGet =getIntent().getStringExtra("token");
         emailGet =getIntent().getStringExtra("email");
         context = this;

        //
        // .makeText(SendNoteActivity.this,tokenGet,Toast.LENGTH_LONG).show();

      //  final User user = (User) getIntent().getSerializableExtra("user");

        textView = findViewById(R.id.textView4);
        editTextTile = findViewById(R.id.title);
        editTextBody = findViewById(R.id.body);
        butSend = findViewById(R.id.send);

        textView.setText("Email : "+emailGet);

        error = findViewById(R.id.error);

        butSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNote();
               // loop();
            }
        });

        phoneNumbers = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });


    }

    private void collectPhoneNumbers(Map<String,Object> users) {
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            phoneNumbers.add((String) singleUser.get("token"));
        }

        System.out.println(phoneNumbers.toString());
    }

    private void loop(){
        for (int counter = 0; counter < phoneNumbers.size(); counter++) {
            sendbyToken(phoneNumbers.get(counter));
        }
    }

    private void sendbyToken(String tokens) {
        String title = editTextTile.getText().toString().trim();
        String body = editTextBody.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://notificationapp-6b5ad.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(tokens, "Title", "body");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               // try {
                  //  Toast.makeText(SendNoteActivity.this, String.valueOf(response.code()), Toast.LENGTH_LONG).show();
                  // Toast.makeText(SendNoteActivity.this, response.body().string(), Toast.LENGTH_LONG).show();

                //} catch (IOException e) {
                  //  e.printStackTrace();
               // }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SendNoteActivity.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }



    private void sendNote() {

        String title = editTextTile.getText().toString().trim();
        String body = editTextBody.getText().toString().trim();

        if (title.isEmpty()){
            editTextTile.setError("Title Nedded");
            editTextTile.requestFocus();
            return;
        }

        if (body.isEmpty()){
            editTextBody.setError("Nedded");
            editTextBody.requestFocus();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
               // .baseUrl(" https://techblogadmin-40753.firebaseapp.com/api/")
                .baseUrl(" https://notificationapp-6b5ad.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.sendNotification(tokenGet, title, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code()==200){
                    Toast.makeText(context,"Notification Send",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(context,"Some Thing Went Wrong",Toast.LENGTH_LONG).show();
                }
              //  try {
                   // Toast.makeText(SendNoteActivity.this, response.body().string(), Toast.LENGTH_LONG).show();
              //  } catch (IOException e) {
              //      e.printStackTrace();
              //  }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
