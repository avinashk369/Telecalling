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
import com.techcamino.telecalling.details.FollowupDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TopFollowupAdapter extends RecyclerView.Adapter<TopFollowupAdapter.MemberView> {

    private List<FollowupDetails> followupDetailsList;
    private Context context;
    private LayoutInflater inflter;

    public TopFollowupAdapter(Context applicationContext, ArrayList<FollowupDetails> followupDetails) {
        this.context = applicationContext;
        this.followupDetailsList = followupDetails;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @NonNull
    @Override
    public MemberView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.top_followup_items, viewGroup, false);
        Log.d("In view holder"," Inini");
        return new MemberView(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MemberView unclearedView, int i) {

        Log.d("In bind view holder"," Inini");
        unclearedView.name.setText(followupDetailsList.get(i).getComment());

    }


    @Override
    public int getItemCount() {
        Log.d("In item count"," Inini");
        return followupDetailsList.size();
    }



    public class MemberView extends RecyclerView.ViewHolder {

        public TextView name;

        public MemberView(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.s_type);
            //view.setBackgroundColor(context.getResources().getColor(R.color.blue));
        }
    }



}
