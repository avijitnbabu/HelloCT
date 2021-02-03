package com.w3xplorers.helloct.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.w3xplorers.helloct.R;
import com.w3xplorers.helloct.ReportClass;
import com.w3xplorers.helloct.activity.ReportDetails;

import java.util.ArrayList;

/**
 * Created by Avijit on 10/10/2017.
 */

public class CustomReportAdapter extends RecyclerView.Adapter<CustomReportAdapter.CustomViewHolder> {


    private ArrayList<ReportClass> crime_data;
    private Context context;
    private String response;


    public CustomReportAdapter(Context context,ArrayList<ReportClass> crime_data,String response) {
        this.context = context;
        this.crime_data = crime_data;
        this.response = response;
    }

    @Override
    public CustomReportAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.conversation_layout, parent, false);

        return new CustomReportAdapter.CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomReportAdapter.CustomViewHolder holder, int position) {

        holder.vCommenter.setText(crime_data.get(position).getReplied_by());
        holder.vDate.setText(crime_data.get(position).getReplied_date());
        holder.vComments.setText(crime_data.get(position).getCrime_comments());
//
//        holder.vReportCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(v.getContext(), ReportDetails.class);
//                in.putExtra("Response",response);
//                v.getContext().startActivity(in);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return crime_data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCommenter;
        protected TextView vDate;
        protected TextView vComments;

        private CustomViewHolder(View itemView) {
            super(itemView);
            vCommenter = (TextView) itemView.findViewById(R.id.idCommenter);
            vDate = (TextView) itemView.findViewById(R.id.idDate);
            vComments = (TextView) itemView.findViewById(R.id.IdComments);
        }
    }
}