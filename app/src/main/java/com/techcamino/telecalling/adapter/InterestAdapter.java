package com.techcamino.telecalling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.InterestDetails;
import com.techcamino.telecalling.details.StateCityDetails;

import java.util.ArrayList;
import java.util.List;


public class InterestAdapter extends BaseAdapter {

    private List<InterestDetails> cityLists;
    private Context context;
    private LayoutInflater inflter;
    private boolean state = false;

    public InterestAdapter(Context applicationContext, ArrayList<InterestDetails> cityLists) {
        this.context = applicationContext;
        this.cityLists = cityLists;
        inflter = (LayoutInflater.from(applicationContext));
    }


    @Override
    public int getCount() {
        return cityLists.size();
    }

    @Override
    public Object getItem(int position) {
        return cityLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.spinner_item, null);
        TextView names = (TextView) convertView.findViewById(R.id.spinner_item);
        names.setText(cityLists.get(position).getIntsLevel());
        return convertView;
    }
}
