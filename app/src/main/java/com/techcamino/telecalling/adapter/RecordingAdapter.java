package com.techcamino.telecalling.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.RecordDetails;

import java.util.ArrayList;
import java.util.List;


public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.MemberView> {

    private List<RecordDetails> inquiryDetailsList;
    private Context context;
    private LayoutInflater inflter;

    public RecordingAdapter(Context applicationContext, ArrayList<RecordDetails> inquiryList) {
        this.context = applicationContext;
        this.inquiryDetailsList = inquiryList;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @NonNull
    @Override
    public MemberView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        Log.d("In view holder"," Inini");
        return new MemberView(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MemberView unclearedView, final int i) {

        //,,,,;
        unclearedView.name.setText(inquiryDetailsList.get(i).getMobileNumber());
    }


    @Override
    public int getItemCount() {
        Log.d("In item count"," Inini");
        return inquiryDetailsList.size();
    }



    public class MemberView extends RecyclerView.ViewHolder {

        public TextView name;

        public MemberView(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvItem);
        }
    }



}
