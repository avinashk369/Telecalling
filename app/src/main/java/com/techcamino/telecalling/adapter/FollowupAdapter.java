package com.techcamino.telecalling.adapter;

import android.content.Context;
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


public class FollowupAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<InquiryDetails> recordResult;
    private Context context;
    private CustomDialogListener mViewClickListener;
    private boolean isLoadingAdded = false;

    public FollowupAdapter(Context context) {
        this.context = context;
        recordResult = new ArrayList<>();
        this.mViewClickListener = (CustomDialogListener) context;
    }

    public interface CustomDialogListener {
        void onImageClicked(InquiryDetails inquiryDetails);
        void onItemClicked(InquiryDetails inquiry);
    }

    public List<InquiryDetails> getRecordingsList() {
        return recordResult;
    }

    public void setRecordings(List<InquiryDetails> recordResult) {
        this.recordResult = recordResult;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.followup_items, parent, false);
        viewHolder = new RecordingVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        InquiryDetails result = recordResult.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final RecordingVH unclearedView = (RecordingVH) holder;

                unclearedView.name.setText(result.getName());
                unclearedView.mobile.setText(result.getMobileNo());
                unclearedView.compname.setText(result.getCompName());
                if(result.getInterestDetails() != null) {
                    unclearedView.interestLevel.setText(result.getInterestDetails().getIntsLevel());
                }
                unclearedView.lastRemark.setText(result.getComment());
                unclearedView.city.setText(result.getCity());
                unclearedView.dataname.setText(result.getDataName());
                unclearedView.datasource.setText(result.getDataSource());
                unclearedView.staffAssigned.setText(result.getStaffName());
                unclearedView.followupDate.setText(result.getCreateDate());
                int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
                unclearedView.separator.setBackgroundColor(randomAndroidColor);
                unclearedView.bind(result, mViewClickListener);

                break;

            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return recordResult == null ? 0 : recordResult.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == recordResult.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(InquiryDetails r) {
        recordResult.add(r);
        notifyItemInserted(recordResult.size() - 1);
    }

    public void addAll(ArrayList<InquiryDetails> inquiryDetailsArrayList) {
        for (InquiryDetails result : inquiryDetailsArrayList) {
            add(result);
        }
    }

    public void remove(InquiryDetails r) {
        int position = recordResult.indexOf(r);
        if (position > -1) {
            recordResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new InquiryDetails());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = recordResult.size() - 1;
        InquiryDetails result = getItem(position);

        if (result != null) {
            recordResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public InquiryDetails getItem(int position) {
        return recordResult.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class RecordingVH extends RecyclerView.ViewHolder {
        public TextView compname,name,mobile,interestLevel,lastRemark,followupDate,city,dataname,datasource,staffAssigned;
        public View separator;

        public RecordingVH(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            compname = (TextView) view.findViewById(R.id.comp_name);
            mobile = (TextView) view.findViewById(R.id.mobile_number);
            interestLevel = (TextView) view.findViewById(R.id.interest_level);
            lastRemark = (TextView) view.findViewById(R.id.last_remark);
            followupDate = (TextView) view.findViewById(R.id.followup_date);
            city = (TextView) view.findViewById(R.id.city_name);
            dataname = (TextView) view.findViewById(R.id.data_status);
            datasource = (TextView) view.findViewById(R.id.data_source);
            staffAssigned = (TextView) view.findViewById(R.id.staff_assigned);
            separator = (View) view.findViewById(R.id.separator);
            //view.setBackgroundColor(context.getResources().getColor(R.color.blue));

        }

        public void bind(final InquiryDetails item, final CustomDialogListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClicked(item);
                }
            });
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
