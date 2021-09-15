package com.zherotech.vms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListAdapterViewHolder>{
    Context context;
    ArrayList<CustomerModel> list;
    ListListener listener;

    public ListAdapter(Context context, ArrayList<CustomerModel> list, ListListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ListAdapterViewHolder listAdapterViewHolder = new ListAdapterViewHolder(view);
        return listAdapterViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapterViewHolder holder, int position) {
        CustomerModel customerModel = list.get(position);
        long stamp = Long.parseLong(customerModel.getTime());
        holder.name_txt.setText(customerModel.getName());
        holder.address_txt.setText(customerModel.getPhone());
        holder.purpose_txt.setText(customerModel.getPurpose());
        holder.button_phone.setText(customerModel.getAddress());
        holder.date_txt.setText(getDate(stamp,"dd/MM/yyyy hh:mm"));
        holder.button_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickPhone(customerModel.getAddress());
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void setData(ArrayList<CustomerModel>list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    class ListAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView name_txt,date_txt,address_txt,purpose_txt;
        MaterialButton button_phone;
        public ListAdapterViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.name_txt = itemView.findViewById(R.id.name_txt);
            this.date_txt = itemView.findViewById(R.id.date_txt);
            this.address_txt = itemView.findViewById(R.id.address_txt);
            this.purpose_txt = itemView.findViewById(R.id.purpose_txt);
            this.button_phone = itemView.findViewById(R.id.button_phone);
        }
    }

    public interface ListListener
    {
        void clickPhone(String phone);
    }
}
