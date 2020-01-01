package com.techcamino.telecalling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.RecordDetails;
import com.techcamino.telecalling.util.Constants;
import com.techcamino.telecalling.util.Security;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private List<RecordDetails> recordResult;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        recordResult = new ArrayList<>();
    }

    public List<RecordDetails> getRecordingsList() {
        return recordResult;
    }

    public void setRecordings(List<RecordDetails> recordResult) {
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
        View v1 = inflater.inflate(R.layout.item_list, parent, false);
        viewHolder = new RecordingVH(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RecordDetails result = recordResult.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final RecordingVH recordingVH = (RecordingVH) holder;

                recordingVH.number.setText(result.getMobileNumber());
                recordingVH.calltime.setText(result.getCreatedOn());
                recordingVH.duration.setText(Security.formateMilliSeccond(Long.valueOf(result.getCallDuration())));
                if(result.getFlags().substring(0,1).equalsIgnoreCase(Constants.MISSED)) {
                    recordingVH.phoneState.setImageResource(R.drawable.ic_call_made_black_24dp);
                    recordingVH.phoneState.setColorFilter(context.getResources().getColor(R.color.red));
                }
                if(result.getFlags().substring(0,1).equalsIgnoreCase(Constants.INCOMING)) {
                    recordingVH.phoneState.setImageResource(R.drawable.ic_call_received_black_24dp);
                    recordingVH.phoneState.setColorFilter(context.getResources().getColor(R.color.green));
                }
                if(result.getFlags().substring(0,1).equalsIgnoreCase(Constants.OUTGOING)) {
                    recordingVH.phoneState.setImageResource(R.drawable.ic_call_made_black_24dp);
                    recordingVH.phoneState.setColorFilter(context.getResources().getColor(R.color.yellow_dark));
                }

                recordingVH.blank.setImageResource(R.drawable.ic_call_made_black_24dp);
                recordingVH.blank.setColorFilter(context.getResources().getColor(R.color.white));


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

    public void add(RecordDetails r) {
        recordResult.add(r);
        notifyItemInserted(recordResult.size() - 1);
    }

    public void addAll(ArrayList<RecordDetails> recordingsResult) {
        for (RecordDetails result : recordingsResult) {
            add(result);
        }
    }

    public void remove(RecordDetails r) {
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
        add(new RecordDetails());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = recordResult.size() - 1;
        RecordDetails result = getItem(position);

        if (result != null) {
            recordResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public RecordDetails getItem(int position) {
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
        private ImageView phoneState,blank;
        private TextView number,duration,calltime;
        private ProgressBar mProgress;

        public RecordingVH(View itemView) {
            super(itemView);

            number = (TextView) itemView.findViewById(R.id.mobile_number);
            phoneState = (ImageView) itemView.findViewById(R.id.phone_state);
            blank = (ImageView) itemView.findViewById(R.id.blank);
            duration = (TextView) itemView.findViewById(R.id.length);
            calltime = (TextView) itemView.findViewById(R.id.call_time);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
