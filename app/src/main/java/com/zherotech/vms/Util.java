package com.zherotech.vms;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;

public class Util {
    private static ProgressDialog progress;
    public static ArrayList<CustomerModel> customers = null;
    public static  ArrayList<CustomerModel> lists = null;



    public static void ShowProgressDialogue(Context context, String message) {
        try {
            progress = new ProgressDialog(context);
            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCancelable(false);
            progress.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void RemoveProgressDialogue() {
        try {
            if (progress != null) {
                progress.hide();
                progress.dismiss();
                progress = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void InitCustomers()
    {
        if (customers == null)
        {
            customers = new ArrayList<CustomerModel>();
        }
        else {
            customers.clear();
        }
    }
    public static void InitList()
    {
        if (lists == null)
        {
            lists = new ArrayList<CustomerModel>();
        }
        else {
            lists.clear();
        }
    }

    public static void ShowAlertDialogue1(Context context, String title, String message, DialogInterface.OnClickListener onClickListener1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.phoneimg);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", onClickListener1);
        builder.show();
    }


}
