package com.zherotech.vms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    TextInputLayout textField;
    TextInputLayout textField2;
    TextInputLayout textField3;
    TextInputLayout textField4;
    TextInputLayout textField5;
    MaterialButton signup;
    String name ="";
    String phone ="";
    String address ="";
    String city ="";
    String orgtype ="";
    RelativeLayout relativeLayout;
    FirebaseFirestore db ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textField = findViewById(R.id.textField);
        textField2 = findViewById(R.id.textField2);
        textField3 = findViewById(R.id.textField3);
        textField4 = findViewById(R.id.textField4);
        textField5 = findViewById(R.id.textField5);
        signup = findViewById(R.id.signup);
        relativeLayout = findViewById(R.id.parentReg);
        db = FirebaseFirestore.getInstance();

        checkData();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = textField.getEditText().getText().toString();
                phone =textField2.getEditText().getText().toString();
                address =textField3.getEditText().getText().toString();
                city =textField4.getEditText().getText().toString();
                orgtype =textField5.getEditText().getText().toString();
                Log.e("f",name);
                if (name.length()==0)
                {
                    Snackbar.make(relativeLayout,"Please Enter Name", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else if (phone.length()==0)
                {
                    Snackbar.make(relativeLayout,"Please Enter Phone", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else if (address.length()==0)
                {
                    Snackbar.make(relativeLayout,"Please Enter Address", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else if (city.length()==0)
                {
                    Snackbar.make(relativeLayout,"Please Enter City", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else if (orgtype.length()==0)
                {
                    Snackbar.make(relativeLayout,"Please Enter Organization Type", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
                else
                {
                    AddData();
                }
            }
        });

    }

    private void AddData()
    {

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("email", SharedPref.getInstance(this).getEmail());
        user.put("cname", name);
        user.put("phone", phone);
        user.put("address", address);
        user.put("city", city);
        user.put("orgtype", orgtype);

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Snackbar.make(relativeLayout,"Success", BaseTransientBottomBar.LENGTH_SHORT).show();
                        gotoNextPage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.e("gh",e.getMessage());
                        Snackbar.make(relativeLayout,"Failed, Please Try again later", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkData()
    {
        Util.ShowProgressDialogue(this,"Please Wait");
        String email = SharedPref.getInstance(this).getEmail();
        db.collection("users").whereEqualTo("email",email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {

                            Log.d("TAG", "Error getting documents.", task.getException());
                        } else {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Util.RemoveProgressDialogue();
                                gotoNextPage();
                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        }
                    }
                });
    }

    private void gotoNextPage()
    {
        Intent intent = new Intent(Register.this,QRDetailPage.class);
        startActivity(intent);
        finish();
    }
}