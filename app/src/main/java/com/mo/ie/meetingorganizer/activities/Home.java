    package com.mo.ie.meetingorganizer.activities;

    import android.app.Fragment;
    import android.app.FragmentTransaction;
    import android.app.NotificationManager;
    import android.content.Context;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.FloatingActionButton;
    import android.support.design.widget.NavigationView;
    import android.support.design.widget.Snackbar;
    import android.support.design.widget.TabLayout;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;
    import android.support.v4.view.ViewPager;
    import android.support.v7.widget.Toolbar;
    import android.view.MenuItem;
    import android.view.View;

    import com.mikepenz.materialdrawer.Drawer;
    import com.mikepenz.materialdrawer.DrawerBuilder;
    import com.mikepenz.materialdrawer.model.DividerDrawerItem;
    import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
    import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
    import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
    import com.mo.ie.R;
    import com.mo.ie.meetingorganizer.controllers.MeetingController;
    import com.mo.ie.meetingorganizer.fragments.AddFragment;
    import com.mo.ie.meetingorganizer.fragments.EditFragment;
    import com.mo.ie.meetingorganizer.fragments.MeetingFragment;
    import com.mo.ie.meetingorganizer.models.Meeting;

    import org.joda.time.MutableDateTime;

    import java.util.Calendar;


    public class Home extends Base  implements AddFragment.Delegate {

            ViewPager mViewPager;
    FragmentTransaction ft = getFragmentManager().beginTransaction();
    private FloatingActionButton floatingActionButton;
    private AddFragment addFragment;
        MeetingFragment fragment = MeetingFragment.newInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0);

            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();


            MeetingController.getInstance().setContext(getApplicationContext());
           MeetingController.getInstance().loadMeetings();


        ft.replace(R.id.homeFrame, fragment);
        ft.commit();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        showAddFragment(null);
            }
            });
            }

    @Override
    public void onAttachFragment(android.app.Fragment fragment) {
            if (fragment instanceof AddFragment) {
            this.addFragment = (AddFragment) fragment;
            this.addFragment.delegate = this;
            }
            }

    public void showAddFragment(Meeting meeting) {


            if (meeting != null) {
            addFragment = EditFragment.newInstance(  meeting.id);

            } else {
                addFragment = new AddFragment();
            }

        addFragment.delegate = this;

        addFragment.show(getFragmentManager(), "dialog");
        //ft.replace(R.id.homeFrame, addFragment);
        //ft.commit();
            floatingActionButton.hide();
            }

    public void hideAddFragment() {
            floatingActionButton.show();
            addFragment.dismiss();
        fragment.onResume();
    }

    @Override
    public void closeFragment() {
        hideAddFragment();
            }






    }

