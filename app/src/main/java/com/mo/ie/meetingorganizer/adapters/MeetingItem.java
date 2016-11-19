package com.mo.ie.meetingorganizer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.mo.ie.R;
import com.mo.ie.meetingorganizer.models.Meeting;
import org.joda.time.MutableDateTime;

public class MeetingItem {
    View view;

    CardView cardView;
    TextView textViewTitle, textViewLocation, textViewStartTime, textViewTimeHour, textViewTimeHalf;
    MutableDateTime time;
    public MeetingItem(Context context, ViewGroup parent, View.OnClickListener deleteListener, Meeting meeting){


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.meetingrow, parent, false);
        view.setId((int) meeting.id);

        updateControls(meeting);
        ImageView imgDelete = (ImageView) view.findViewById(R.id.meeting_Item_delete);
        imgDelete.setTag(meeting);
        imgDelete.setOnClickListener(deleteListener);


    }

    private void updateControls(Meeting meeting) {


        cardView = (CardView) view.findViewById(R.id.cardViewMeeting);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewLocation = (TextView) view.findViewById(R.id.textViewLocation);
        textViewStartTime = (TextView) view.findViewById(R.id.textViewStartTime);
        textViewTimeHour = (TextView) view.findViewById(R.id.textViewTimeHour);
        textViewTimeHalf = (TextView) view.findViewById(R.id.textViewTimeHalf);

        textViewTitle.setText(meeting.title);

        if (meeting.location.length() > 0) {
            textViewLocation.setText(meeting.location);
            textViewLocation.setVisibility(View.VISIBLE);
        } else
            textViewLocation.setVisibility(View.GONE);

        String timeString = meeting.startDateTime.toString("h:mm a") + " - " + meeting.endDateTime.toString("h:mm a");
        textViewStartTime.setText(timeString);

        if (meeting.startDateTime.isAfterNow()) {
            textViewTimeHour.setText(meeting.startDateTime.toString("d"));
            textViewTimeHalf.setText(meeting.startDateTime.toString("MMM"));
        } else {
            textViewTimeHour.setText(meeting.startDateTime.toString("h"));
            textViewTimeHalf.setText(meeting.startDateTime.toString("a"));
        }
        }

    }

