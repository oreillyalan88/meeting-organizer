package com.mo.ie.meetingorganizer.adapters;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mo.ie.R;
import com.mo.ie.meetingorganizer.models.Meeting;

import java.util.List;


public class MeetingListAdapter extends ArrayAdapter<Meeting>  {
    private Context context;
    private View.OnClickListener deleteListener;
    public List<Meeting> meetingList;

    public MeetingListAdapter(Context context, View.OnClickListener deleteListener,
                             List<Meeting> meetingList ) {
        super(context, R.layout.meetingrow, meetingList);

        this.context = context;
        this.deleteListener = deleteListener;
        this.meetingList = meetingList;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MeetingItem item = new MeetingItem(context, parent,deleteListener,meetingList.get(position));
        return item.view;
    }

    @Override
    public int getCount() {
        return meetingList.size();
    }
    public List<Meeting> getCoffeeList() {
        return this.meetingList;
    }
    @Override
    public Meeting getItem(int position) {
        return meetingList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getPosition(Meeting c) {
        return meetingList.indexOf(c);
    }
}

