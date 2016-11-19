package com.mo.ie.meetingorganizer.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.widget.Button;

import com.mo.ie.R;

/**
 * Created by LickiRake on 04/11/2016.
 */

public class EditFragment extends AddFragment {

    public EditFragment() {
    }


    public static EditFragment newInstance(Long meetingId) {
        AddFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable("meeting_id", meetingId);
        fragment.setArguments(args);
        return (EditFragment) fragment;


    }
}





