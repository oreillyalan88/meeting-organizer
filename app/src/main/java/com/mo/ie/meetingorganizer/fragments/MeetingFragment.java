package com.mo.ie.meetingorganizer.fragments;



import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;


import com.mo.ie.R;
import com.mo.ie.meetingorganizer.activities.Base;
import com.mo.ie.meetingorganizer.activities.Home;
import com.mo.ie.meetingorganizer.adapters.MeetingListAdapter;
import com.mo.ie.meetingorganizer.controllers.MeetingController;
import com.mo.ie.meetingorganizer.models.Meeting;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.mode;


public class MeetingFragment extends Fragment implements AbsListView.MultiChoiceModeListener, AdapterView.OnItemClickListener , View.OnClickListener
{
    protected static MeetingListAdapter listAdapter;
   // protected ListView listView;
    //protected         CoffeeFilter        coffeeFilter;
    public            boolean             favourites = false;
    protected TextView titleBar;
    //ArrayList<Integer> selectedIndex = new ArrayList<>();
    //MeetingListFragment listFragment;
    private FloatingActionButton floatingActionButton;
    public ActionMode mode;
    List<Meeting> meetings;
    MeetingListAdapter adapter;
    //MeetingListType listType;
    ListView listView;
    TextView textViewNoMeeting;
    Button buttonShowPrevious;
    Boolean showAll = false;
    ArrayList<Integer> selectedIndex = new ArrayList<>();

    public MeetingFragment() {
        // Required empty public constructor
    }

    public static MeetingFragment newInstance() {
        MeetingFragment fragment = new MeetingFragment();
        return fragment;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked)
            selectedIndex.add(position);
        else
            selectedIndex.remove(Integer.valueOf(position));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.main_action_menu, menu);
        this.mode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteSelected(mode);
                return true;
            case R.id.action_select_all:
                selectedIndex.clear();
                for (int i = 0; i < listView.getCount(); i++) {
                    listView.setItemChecked(i, true);
                }
                return true;
            case R.id.action_move:
                moveToDate(mode);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        //this.activity = (Base) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedIndex.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;

        v = inflater.inflate(R.layout.fragment_home, container, false);

        listView =              (ListView) v.findViewById(R.id.listView);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(this);
        listView.setOnItemClickListener(this);
       // updateList();

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();

        List<Meeting> valueList = new ArrayList<>(Base.app.dbManager.loadMeetings().values());
//        titleBar = (TextView)getActivity().findViewById(R.id.recentAddedBarTextView);
//        titleBar.setText(R.string.recentlyViewedLbl);
//        ((TextView)getActivity().findViewById(R.id.empty_list_view)).setText(R.string.recentlyViewedEmptyMessage);
        listAdapter = new MeetingListAdapter(getActivity(), this, valueList);
//        coffeeFilter = new CoffeeFilter(Base.app.dbManager.getAll(),"all",listAdapter);
        listAdapter.notifyDataSetChanged();



       // ((TextView)getActivity().findViewById(R.id.empty_list_view)).setText(R.string.recentlyViewedEmptyMessage);
//        //coffeeFilter = new CoffeeFilter(Base.app.dbManager.getAll(),"all",listAdapter);

//        if (favourites) {
//            titleBar.setText(R.string.favouritesCoffeeLbl);
//            ((TextView)getActivity().findViewById(R.id.empty_list_view)).setText(R.string.favouritesEmptyMessage);
//
//            coffeeFilter.setFilter("favourites"); // Set the filter text field from 'all' to 'favourites'
//            coffeeFilter.filter(null); // Filter the data, but don't use any prefix
//            listAdapter.notifyDataSetChanged(); // Update the adapter
//        }
       listView.setAdapter (listAdapter);
       listView.setOnItemClickListener(this);
       listView.setEmptyView(getActivity().findViewById(R.id.textViewNoMeetings));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        // mListener = null;
    }



    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View view)
    {
        if (view.getTag() instanceof Meeting)
        {
            onMeetingDelete ((Meeting) view.getTag());
        }
  }

    public void onMeetingDelete(final Meeting meeting)
    {
        String stringName = meeting.title;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to Delete this \'Meeting\' " + stringName + "?");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Base.app.dbManager.deleteMeeting(meeting); // remove from our list
                listAdapter.meetingList.remove(meeting); // update adapters data
                listAdapter.notifyDataSetChanged(); // refresh adapter
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Home mainActivity = (Home) getActivity();
        mainActivity.showAddFragment(listAdapter.getItem(position));
    }

    private void deleteSelected(final ActionMode mode) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setMessage("Delete Selected Meetings?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (Integer index : selectedIndex) {
                    MeetingController.getInstance().deleteMeeting(listAdapter.getItem(index));
                }
                mode.finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void moveToDate(final ActionMode mode) {
        Meeting meetingTemp = listAdapter.getItem(selectedIndex.get(0));
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                for (int i = 0; i < selectedIndex.size(); i++) {
                    Meeting meeting = listAdapter.getItem(i);
                    meeting.startDateTime.setYear(year);
                    meeting.startDateTime.setMonthOfYear(monthOfYear + 1);
                    meeting.startDateTime.setDayOfMonth(dayOfMonth);

                    meeting.endDateTime.setYear(year);
                    meeting.endDateTime.setMonthOfYear(monthOfYear + 1);
                    meeting.endDateTime.setDayOfMonth(dayOfMonth);

                    MeetingController.getInstance().createMeeting(meeting);
                }
                mode.finish();
            }
        }, meetingTemp.startDateTime.getYear(), meetingTemp.startDateTime.getMonthOfYear() - 1, meetingTemp.startDateTime.getDayOfMonth()).show();
    }

}


