package com.mo.ie.meetingorganizer.activities;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.mo.ie.meetingorganizer.main.MeetingOrganizerApp;

public class Base extends AppCompatActivity {

    public static MeetingOrganizerApp app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MeetingOrganizerApp) getApplication();

    }

    protected void goToActivity(Activity current,
                                Class<? extends Activity> activityClass,
                                Bundle bundle) {
        Intent newActivity = new Intent(current, activityClass);

        if (bundle != null) newActivity.putExtras(bundle);

        current.startActivity(newActivity);
    }
//    public void openInfoDialog(Activity current) {
//        Dialog dialog = new Dialog(current);
//        dialog.setTitle("About CoffeeMate");
//        dialog.setContentView(R.layout.info);
//
//        TextView currentVersion = (TextView) dialog
//                .findViewById(R.id.versionTextView);
//        currentVersion.setText("4.0.0");
//
//        dialog.setCancelable(true);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//    }
//@Override
//public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//    getMenuInflater().inflate(R.menu.home, menu);
//    return true;
//}
//
//    public void menuInfo(MenuItem m)
//    {
//        openInfoDialog(this);
//    }
//
//    public void menuHelp(MenuItem m)
//    {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//
//        HelpFragment fragment = HelpFragment.newInstance();
//        ft.replace(R.id.homeFrame, fragment);
//        ft.addToBackStack(null);
//        ft.commit();
//    }
//
    public void menuHome(MenuItem m)
    {
        goToActivity(this, Home.class, null);
    }
}
