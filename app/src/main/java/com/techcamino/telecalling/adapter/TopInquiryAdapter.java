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
import com.techcamino.telecalling.details.InquiryDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TopInquiryAdapter extends RecyclerView.Adapter<TopInquiryAdapter.MemberView> {

    private List<InquiryDetails> inquiryDetailsList;
    private Context context;
    private LayoutInflater inflter;
    private CustomDialogListener mViewClickListener;

    public TopInquiryAdapter(Context applicationContext, ArrayList<InquiryDetails> inquiryList) {
        this.context = applicationContext;
        this.inquiryDetailsList = inquiryList;
        inflter = (LayoutInflater.from(applicationContext));
        this.mViewClickListener = (CustomDialogListener) context;
    }

    public interface CustomDialogListener {
        void onImageClicked(InquiryDetails user);
    }

    @NonNull
    @Override
    public MemberView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_inquiry_items, viewGroup, false);
        Log.d("In view holder"," Inini");
        return new MemberView(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MemberView unclearedView, final int i) {

        //,,,,;
        unclearedView.name.setText(inquiryDetailsList.get(i).getName());
        unclearedView.companyName.setText(inquiryDetailsList.get(i).getCompName());
        unclearedView.mobile.setText(inquiryDetailsList.get(i).getMobileNo());
        unclearedView.interestLevel.setText(inquiryDetailsList.get(i).getInterestDetails().getIntsLevel());
        /*unclearedView.state.setText(inquiryDetailsList.get(i).getState());
        unclearedView.city.setText(inquiryDetailsList.get(i).getCity());*/
        unclearedView.address.setText(inquiryDetailsList.get(i).getAddress());

        unclearedView.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewClickListener.onImageClicked(inquiryDetailsList.get(i));
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.d("In item count"," Inini");
        return inquiryDetailsList.size();
    }



    public class MemberView extends RecyclerView.ViewHolder {

        public TextView name,companyName,mobile,interestLevel,state,city,address;
        public RelativeLayout follow;

        public MemberView(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            companyName = (TextView) view.findViewById(R.id.comp_name);
            mobile = (TextView) view.findViewById(R.id.mobile_number);
            interestLevel = (TextView) view.findViewById(R.id.interest_level);
            /*state = (TextView) view.findViewById(R.id.state);
            city = (TextView) view.findViewById(R.id.city);*/
            address = (TextView) view.findViewById(R.id.address);
            follow = (RelativeLayout) view.findViewById(R.id.follow);
            //view.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }
    }



}
