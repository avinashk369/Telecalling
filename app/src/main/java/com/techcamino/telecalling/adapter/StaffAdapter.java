package com.techcamino.telecalling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.InterestDetails;
import com.techcamino.telecalling.details.StaffDetails;

import java.util.ArrayList;
import java.util.List;


public class StaffAdapter extends BaseAdapter {

    private List<StaffDetails> staffList;
    private Context context;
    private LayoutInflater inflter;
    private boolean state = false;

    public StaffAdapter(Context applicationContext, ArrayList<StaffDetails> staffList) {
        this.context = applicationContext;
        this.staffList = staffList;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return staffList.size();
    }

    @Override
    public Object getItem(int position) {
        return staffList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.spinner_item, null);
        TextView names = (TextView) convertView.findViewById(R.id.spinner_item);
        names.setText(staffList.get(position).getFirstName()+" "+staffList.get(position).getLastName());
        return convertView;
    }
}
