    package com.mo.ie.meetingorganizer.activities;

    import android.app.Fragment;

    import android.app.FragmentTransaction;
    import android.app.NotificationManager;
    import android.content.Context;
    import android.content.Intent;
    import android.content.res.Configuration;
    import android.os.Bundle;
    import android.os.Handler;
    import android.support.annotation.NonNull;
    import android.support.annotation.UiThread;
    import android.support.design.widget.FloatingActionButton;
    import android.support.design.widget.NavigationView;
    import android.support.v4.widget.DrawerLayout;
    import android.support.design.widget.Snackbar;
    import android.support.design.widget.TabLayout;
    import android.support.v4.app.FragmentManager;
    import android.support.v4.app.FragmentPagerAdapter;
    import android.support.v4.view.GravityCompat;
    import android.support.v4.view.ViewPager;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.animation.Animation;
    import android.view.animation.AnimationUtils;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.TextView;
    import  android.R.anim;
    import android.widget.Toast;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.engine.DiskCacheStrategy;
    import com.mo.ie.R;
    import com.mo.ie.meetingorganizer.adapters.MeetingListAdapter;
    import com.mo.ie.meetingorganizer.controllers.MeetingController;
    import com.mo.ie.meetingorganizer.fragments.AddFragment;
    import com.mo.ie.meetingorganizer.fragments.EditFragment;


    import com.mo.ie.meetingorganizer.fragments.MeetingFragment;
    import com.mo.ie.meetingorganizer.fragments.TodayFragment;
    import com.mo.ie.meetingorganizer.models.Meeting;
    import com.mo.ie.meetingorganizer.other.CircleTransform;

    import org.joda.time.MutableDateTime;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;


    import static android.R.id.toggle;
    import static com.mo.ie.R.id.fab;


    public class Home extends Base  implements AddFragment.Delegate, NavigationView.OnNavigationItemSelectedListener{

        private ListView mDrawerList;
        private ArrayAdapter<String> mAdapter;

        private FloatingActionButton floatingActionButton;
        private AddFragment addFragment;
        MeetingFragment fragment = MeetingFragment.newInstance();
        private NavigationView navigationView;
        private DrawerLayout drawer;
        private View navHeader;
        private ImageView imgNavHeaderBg, imgProfile;
        private TextView txtName, txtWebsite;
         Toolbar toolbar;
        private FloatingActionButton fabDrawer;

        private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
        private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

        // index to identify current nav menu item
        public static int navItemIndex = 0;

        // tags used to attach the fragments
        private static final String TAG_HOME = "home";
        private static final String TAG_PHOTOS = "photos";
        private static final String TAG_MOVIES = "movies";
        private static final String TAG_NOTIFICATIONS = "notifications";
        private static final String TAG_SETTINGS = "settings";
        public static String CURRENT_TAG = TAG_HOME;
        private ActionBarDrawerToggle mDrawerToggle;
        private DrawerLayout mDrawerLayout;
        private String mActivityTitle;
        // toolbar titles respected to selected nav menu item
        private String[] activityTitles;

        // flag to load home fragment when user presses back key
        private boolean shouldLoadHomeFragOnBackPress = true;
        public Handler mHandler;
        ViewPager mViewPager;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            floatingActionButton = (FloatingActionButton) findViewById(fab);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAddFragment(null);
                }
            });

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                   toolbar, R.string.drawer_open, R.string.drawer_close) {


                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    getSupportActionBar().setTitle("Navigation!");
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    getSupportActionBar().setTitle(mActivityTitle);
                    invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };
            drawer.setDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
            navHeader = navigationView.getHeaderView(0);
            txtName = (TextView) navHeader.findViewById(R.id.name);
            txtWebsite = (TextView) navHeader.findViewById(R.id.website);
            imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
            imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);


            mActivityTitle = getTitle().toString();



            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.cancelAll();


            MeetingController.getInstance().setContext(getApplicationContext());
            MeetingController.getInstance().loadMeetings();


            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.homeFrame, fragment);
            ft.commit();


//            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Toast.makeText(Home.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();
//                }
//            });

          //  drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
           // navigationView = (NavigationView) findViewById(R.id.nav_view);
//            fabDrawer = (FloatingActionButton) findViewById(fab);
//
//            // Navigation view header
//            navHeader = navigationView.getHeaderView(0);
//            txtName = (TextView) navHeader.findViewById(R.id.name);
//            txtWebsite = (TextView) navHeader.findViewById(R.id.website);
//            imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
//            imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
//
//            // load toolbar titles from string resources
//            activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


            // load nav menu header data


        }

        @Override
        public void onBackPressed() {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
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
                addFragment = EditFragment.newInstance(meeting.id);

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

        /***
         * Load navigation menu header information
         * like background image, profile image
         * name, website, notifications action view (dot)
         */
//        private void loadNavHeader() {
//            // name, website
//            txtName.setText("Alan O' Reilly");
//            txtWebsite.setText("www.mywebsite.com");
//
//            // loading header background image
//            Glide.with(this).load(urlNavHeaderBg)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgNavHeaderBg);
//
//            // Loading profile image
//            Glide.with(this).load(urlProfileImg)
//                    .crossFade()
//                    .thumbnail(0.5f)
//                    .bitmapTransform(new CircleTransform(this))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfile);
//
//            // showing dot next to notifications label
//            navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
//        }

//
//        /***
//         * Returns respected fragment that user
//         * selected from navigation menu
//         */
//        private void loadHomeFragment() {
//            // selecting appropriate nav menu item
//            selectNavMenu();
//
//            // set toolbar title
//           setToolbarTitle();
//
//            // if user select the current navigation menu again, don't do anything
//            // just close the navigation drawer
//            if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
//                drawer.closeDrawers();
//
//                // show or hide the fab button
//                toggleFab();
//                return;
//            }
//
//            // Sometimes, when fragment has huge data, screen seems hanging
//            // when switching between navigation menus
//            // So using runnable, the fragment is loaded with cross fade effect
//            // This effect can be seen in GMail app
//
//            Runnable mPendingRunnable = new Runnable() {
//                @Override
//                public void run() {
//                    // update the main content by replacing fragments getFragmentManager().beginTransaction();
//
//                    Fragment fragment = getHomeFragment();
//                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                    //fragmentTransaction.setCustomAnimations(myFadeInAnimation.hashCode(), myFadeoutAnimation.hashCode());
//                    fragmentTransaction.replace(R.id.homeFrame, fragment, CURRENT_TAG);
//                    fragmentTransaction.commitAllowingStateLoss();
//                }
//            };
//
//            // If mPendingRunnable is not null, then add to the message queue
//            if (mPendingRunnable != null) {
//                mHandler.post(mPendingRunnable);
//            }
//
//            // show or hide the fab button
//
//
//            //Closing drawer on item click
//            drawer.closeDrawers();
//
//            // refresh toolbar menu
//            invalidateOptionsMenu();
//        }

//        private Fragment getHomeFragment() {
//            switch (navItemIndex) {
//                case 0:
//                    // home
//                    MeetingFragment homeFragment = new MeetingFragment();
//                    return homeFragment;
//
//                case 1:
//                    // notifications fragment
//                    MeetingFragment notificationsFragment = new MeetingFragment();
//                    return notificationsFragment;
//
//                case 2:
//                    // settings fragment
//                    MeetingFragment settingsFragment = new MeetingFragment();
//                    return settingsFragment;
//                default:
//                    return new MeetingFragment();
//            }
//        }
//
//        private void setToolbarTitle() {
//
//          getSupportActionBar().setTitle(activityTitles[navItemIndex]);
//        }
//
//        private void selectNavMenu() {
//            navigationView.getMenu().getItem(navItemIndex).setChecked(true);
//        }
//
//        private void setUpNavigationView() {
//            //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
//            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//
//                // This method will trigger on item Click of navigation menu
//                @Override
//                public boolean onNavigationItemSelected(MenuItem menuItem) {
//
//                    //Check to see which item was being clicked and perform appropriate action
//                    switch (menuItem.getItemId()) {
//                        //Replacing the main content with ContentFragment Which is our Inbox View;
//                        case R.id.nav_home:
//                            navItemIndex = 0;
//                            CURRENT_TAG = TAG_HOME;
//                            break;
//                        case R.id.nav_notifications:
//                            navItemIndex = 3;
//                            CURRENT_TAG = TAG_NOTIFICATIONS;
//                            break;
//                        case R.id.nav_settings:
//                            navItemIndex = 4;
//                            CURRENT_TAG = TAG_SETTINGS;
//                            break;
//                        case R.id.nav_about_us:
//                            // launch new intent instead of loading fragment
//                            startActivity(new Intent(Home.this, AboutMeActivity.class));
//                            drawer.closeDrawers();
//                            return true;
//                        case R.id.nav_privacy_policy:
//                            // launch new intent instead of loading fragment
//                            startActivity(new Intent(Home.this, PrivacyPolicyActivity.class));
//                            drawer.closeDrawers();
//                            return true;
//                        default:
//                            navItemIndex = 0;
//                    }
//
//                    //Checking if the item is in checked state or not, if not make it in checked state
//                    if (menuItem.isChecked()) {
//                        menuItem.setChecked(false);
//                    } else {
//                        menuItem.setChecked(true);
//                    }
//                    menuItem.setChecked(true);
//
//                    loadHomeFragment();
//
//                    return true;
//                }
//            });



//    }




//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // Inflate the menu; this adds items to the action bar if it is present.
//
//            // show menu only when home fragment is selected
//            if (navItemIndex == 0) {
//                getMenuInflater().inflate(R.menu.main, menu);
//            }
//
//            // when fragment is notifications, load the menu created for notifications
//            if (navItemIndex == 3) {
//                getMenuInflater().inflate(R.menu.notifications, menu);
//            }
//            return true;
//        }


        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.

            // http://stackoverflow.com/questions/32944798/switch-between-fragments-with-onnavigationitemselected-in-new-navigation-drawer

            int id = item.getItemId();
            Fragment fragment;
            FragmentTransaction ft = getFragmentManager().beginTransaction();

            if (id == R.id.nav_home) {
                fragment = MeetingFragment.newInstance();

                ft.replace(R.id.homeFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();

            } else if (id == R.id.nav_notifications) {
                fragment = MeetingFragment.newInstance();
                ft.replace(R.id.homeFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();


            } else if (id == R.id.nav_about_us) {
                fragment = MeetingFragment.newInstance();
                ft.replace(R.id.homeFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();

            } else if (id == R.id.nav_privacy_policy) {
                fragment = MeetingFragment.newInstance();
                ft.replace(R.id.homeFrame, fragment);
                ft.addToBackStack(null);
                ft.commit();

            } else if (id == R.id.nav_settings) {

            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }






    }
