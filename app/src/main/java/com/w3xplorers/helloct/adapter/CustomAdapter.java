package com.w3xplorers.helloct.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.w3xplorers.helloct.R;
import com.w3xplorers.helloct.ReportClass;
import com.w3xplorers.helloct.activity.ReportDetails;

import java.util.ArrayList;

/**
 * Created by Avijit on 10/9/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {


    private ArrayList<ReportClass> crime_data;
    private Context context;
    private String response;


    public CustomAdapter(Context context,ArrayList<ReportClass> crime_data,String response) {
        this.context = context;
        this.crime_data = crime_data;
        this.response = response;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.report_layout, parent, false);

        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {

        holder.vReportCode.setText(crime_data.get(position).getCrimeReport());

        holder.vReportCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), ReportDetails.class);
                in.putExtra("Response",response);
                in.putExtra("crime_code",crime_data.get(position).getCrimeReport());
                v.getContext().startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crime_data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView vReportCode;

        private CustomViewHolder(View itemView) {
            super(itemView);
            vReportCode = (TextView) itemView.findViewById(R.id.idReport);
        }
    }
}