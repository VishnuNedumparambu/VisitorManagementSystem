package com.zherotech.vms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VisitorEntry extends AppCompatActivity {
    TextInputLayout text1,text2,text3,text4;
    MaterialButton upload;
    String name ="";
    String address="";
    String phone="";
    String purpose="";
    String email="";
    RelativeLayout parent_visit;
    FirebaseFirestore db ;
    TextView date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_entry);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        text4 = findViewById(R.id.text4);
        upload = findViewById(R.id.upload);
        date = findViewById(R.id.date);
        parent_visit = findViewById(R.id.parent_visit);
         Intent intent = getIntent();
        email = intent.getStringExtra("email");
        Log.e("mail1",email);
        db = FirebaseFirestore.getInstance();
        date.setText(getDate(System.currentTimeMillis(), "dd/MM/yyyy hh:mm"));



        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = text1.getEditText().getText().toString();
                address = text2.getEditText().getText().toString();
                phone = text3.getEditText().getText().toString();
                purpose = text4.getEditText().getText().toString();

                if (name.length()==0)
                {
                    Snackbar.make(parent_visit,"Please Enter Name", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else if (address.length()==0)
                {
                    Snackbar.make(parent_visit,"Please Enter Address", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else if (phone.length()==0)
                {
                    Snackbar.make(parent_visit,"Please Enter Phone", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else
                    {
                    AddData();
                }
            }
        });

    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void AddData()
    {
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("phone", phone);
        user.put("address", address);
        user.put("purpose", purpose);
        user.put("time", System.currentTimeMillis()+"");
        user.put("date",getDate(System.currentTimeMillis(),"dd/MM/yyyy"));


// Add a new document with a generated ID
        db.collection(email)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Snackbar.make(parent_visit,"Success", BaseTransientBottomBar.LENGTH_SHORT).show();
                        upload.setEnabled(false);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                gotoMain();
                            }
                        }, 2000);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.e("gh",e.getMessage());
                        Snackbar.make(parent_visit,"Failed, Please Try again later", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    public void gotoMain()
    {
        Intent intent = new Intent(VisitorEntry.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}