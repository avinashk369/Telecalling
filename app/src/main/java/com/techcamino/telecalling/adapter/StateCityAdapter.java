package com.techcamino.telecalling.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techcamino.telecalling.R;
import com.techcamino.telecalling.details.StateCityDetails;

import java.util.ArrayList;
import java.util.List;



public class StateCityAdapter extends BaseAdapter {

    private List<StateCityDetails> cityLists;
    private Context context;
    private LayoutInflater inflter;
    private boolean state = false;

    public StateCityAdapter(Context applicationContext, ArrayList<StateCityDetails> cityLists,boolean state) {
        this.context = applicationContext;
        this.cityLists = cityLists;
        this.state = state;
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
        if(state) {
            names.setText(cityLists.get(position).getParent());
        }else{
            names.setText(cityLists.get(position).getState());
        }
        return convertView;
    }
}
