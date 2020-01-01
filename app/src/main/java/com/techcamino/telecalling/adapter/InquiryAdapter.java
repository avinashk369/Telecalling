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


public class InquiryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<InquiryDetails> recordResult;
    private Context context;
    private CustomDialogListener mViewClickListener;
    private boolean isLoadingAdded = false;

    public InquiryAdapter(Context context) {
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
        View v1 = inflater.inflate(R.layout.inquiry_items, parent, false);
        viewHolder = new RecordingVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        InquiryDetails result = recordResult.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final RecordingVH unclearedView = (RecordingVH) holder;

                //,,,,;
                unclearedView.name.setText(result.getName());
                unclearedView.companyName.setText(result.getCompName());
                unclearedView.mobile.setText(result.getMobileNo());
                if(result.getInterestDetails() != null) {
                    unclearedView.interestLevel.setText(result.getInterestDetails().getIntsLevel());
                }

        /*unclearedView.state.setText(inquiryDetailsList.get(i).getState());
        unclearedView.city.setText(inquiryDetailsList.get(i).getCity());*/
                unclearedView.address.setText(result.getAddress());
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
        public TextView name,companyName,mobile,interestLevel,state,city,address;
        public View separator;
        public RelativeLayout follow;

        public RecordingVH(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            companyName = (TextView) view.findViewById(R.id.comp_name);
            mobile = (TextView) view.findViewById(R.id.mobile_number);
            interestLevel = (TextView) view.findViewById(R.id.interest_level);
            /*state = (TextView) view.findViewById(R.id.state);
            city = (TextView) view.findViewById(R.id.city);*/
            address = (TextView) view.findViewById(R.id.address);
            separator = (View) view.findViewById(R.id.separator);

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
