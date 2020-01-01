package com.techcamino.telecalling.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.CommentsDetails;

import java.util.ArrayList;
import java.util.List;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MemberView> {

    private List<CommentsDetails> inquiryDetailsList;
    private Context context;
    private LayoutInflater inflter;
    private CustomDialogListener mViewClickListener;

    public CommentsAdapter(Context applicationContext, ArrayList<CommentsDetails> inquiryList) {
        this.context = applicationContext;
        this.inquiryDetailsList = inquiryList;
        inflter = (LayoutInflater.from(applicationContext));
        this.mViewClickListener = (CustomDialogListener) context;
    }

    public interface CustomDialogListener {
        void onItemClicked(CommentsDetails commentsDetails);
    }

    @NonNull
    @Override
    public MemberView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.inquiry_history, viewGroup, false);
        Log.d("In view holder"," Inini");
        return new MemberView(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MemberView unclearedView, final int i) {

        //,,,,;
        unclearedView.remark.setText(inquiryDetailsList.get(i).getComment());
        unclearedView.phoneOption.setText(inquiryDetailsList.get(i).getPhoneOption());
        unclearedView.inquiryUpdate.setText(inquiryDetailsList.get(i).getInquiryUpdate());
        unclearedView.date.setText(inquiryDetailsList.get(i).getFollowupDate());
        //unclearedView.bind(inquiryDetailsList.get(i), mViewClickListener);
        /*unclearedView.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewClickListener.onImageClicked(inquiryDetailsList.get(i));
            }
        });*/
    }


    @Override
    public int getItemCount() {
        Log.d("In item count"," Inini");
        return inquiryDetailsList.size();
    }



    public class MemberView extends RecyclerView.ViewHolder {

        public TextView remark,inquiryUpdate,phoneOption,date;

        public MemberView(View view) {
            super(view);
            remark = (TextView) view.findViewById(R.id.remarks);
            inquiryUpdate = (TextView) view.findViewById(R.id.inquiry_update);
            phoneOption = (TextView) view.findViewById(R.id.phone_option);
            date = (TextView) view.findViewById(R.id.next_date);
        }

        /*public void bind(final CommentsDetails item, final CustomDialogListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClicked(item);
                }
            });
        }*/
    }



}
