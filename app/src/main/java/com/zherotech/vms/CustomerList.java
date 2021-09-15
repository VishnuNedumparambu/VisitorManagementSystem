package com.zherotech.vms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CustomerList extends AppCompatActivity implements ListAdapter.ListListener {
    FirebaseFirestore db;
    RecyclerView recylerview;
    ListAdapter listAdapter;
    ImageView pickerButton;
    String phone = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        recylerview = findViewById(R.id.recylerview);
        pickerButton = findViewById(R.id.pickerButton);
        db = FirebaseFirestore.getInstance();
        Util.InitCustomers();
        getData();
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        // now define the properties of the
        // materialDateBuilder that is title text as SELECT A DATE
        materialDateBuilder.setTitleText("SELECT A DATE");

        // now create the instance of the material date
        // picker
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        // handle select date button which opens the
        // material design date picker
        pickerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now handle the positive button click from the
        // material design date picker

        materialDatePicker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Long>) selection -> {
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(selection)).toString();
            sort(dateString);
            //Toast.makeText(this, dateString, Toast.LENGTH_SHORT).show();
        });



    }
    public void getData()
    {
        Util.ShowProgressDialogue(this,"Loading..");
        db.collection(SharedPref.getInstance(this).getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name").toString();
                                String phone = document.getString("phone").toString();
                                String address = document.getString("address").toString();
                                String purpose = document.getString("purpose").toString();
                                String date = document.getString("date").toString();
                                String time = document.getString("time").toString();
                                CustomerModel customerModel = new CustomerModel(name,phone,address,purpose,time,date);
                                Util.customers.add(customerModel);

                               // Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            Util.RemoveProgressDialogue();
                            if (Util.customers.size() > 0) {
                                Util.InitList();
                                Util.lists = Util.customers;
                                setRecylerview();
                            }
                            else
                            {
                                Util.ShowAlertDialogue1(CustomerList.this, "Alert", "No entries available", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        finish();
                                    }
                                });
                            }
                        } else {
                            //Log.w(TAG, "Error getting documents.", task.getException());
                            Util.RemoveProgressDialogue();
                            Util.ShowAlertDialogue1(CustomerList.this, "Alert", "Please try again later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                        }
                    }
                });
    }

    public void setRecylerview()
    {
        listAdapter= new ListAdapter(this,Util.lists,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recylerview.setLayoutManager(layoutManager);
        recylerview.setAdapter(listAdapter);
    }

    public void sort(String date){
        ArrayList<CustomerModel> list = new ArrayList<>();
        for (int i =0; i<Util.customers.size(); i++)
        {
            if (Util.customers.get(i).date.equals(date))
            {
                CustomerModel model = Util.customers.get(i);
              list.add(model);
            }

        }

        if (list.size()>0)
        {
            listAdapter.setData(list);
        }
        else {
            Toast.makeText(CustomerList.this, "No Data available on"+date, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void clickPhone(String phone) {
        this.phone = phone;
        CallCustomer(phone);
    }

    private void CallCustomer(String phone) {
        String num = "tel:" + phone;

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(num));
        if (ActivityCompat.checkSelfPermission(CustomerList.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CustomerList.this, new String[]{android.Manifest.permission.CALL_PHONE}, 11);
        } else {
            startActivity(callIntent);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {

                case 11:
                    CallCustomer(phone);
                    break;
            }
        }
    }
}