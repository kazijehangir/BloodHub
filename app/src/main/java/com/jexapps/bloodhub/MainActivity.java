package com.jexapps.bloodhub;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.database.DatabaseErrorHandler;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RequestsFragment.OnFragmentInteractionListener,
        DonationsFragment.OnFragmentInteractionListener,
        AppointmentsFragment.OnFragmentInteractionListener,
        FaqFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        InviteFriendFragment.OnFragmentInteractionListener,
        OrganizationsFragment.OnFragmentInteractionListener,
        ReportFragment.OnFragmentInteractionListener{
    //TODO: add separate layout for organizations
//    TODO: Understand inflating views. probably is fix to update email and photo
//    inflating views is creating view and view-groups from an xml file/resource
//    not sure if that helps.
    private DrawerLayout mDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        get email from login activity
        String mEmail;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                mEmail= null;
            } else {
                mEmail= extras.getString("mEmail");
            }
        } else {
            mEmail= (String) savedInstanceState.getSerializable("mEmail");
        }
//        TODO: comment this toast out when done debugging
        Toast.makeText(this, "Logged in " + mEmail + " successfully.",
                Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, AddRequestActivity.class);
            }
        });

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        setupDrawerContent(navigationView);

        TextView emailView = (TextView) navigationView.findViewById(R.id.email_View);
        if (emailView != null) {
            emailView.setText(mEmail);
            Toast.makeText(this, "email_View != null",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "email_View == null",Toast.LENGTH_LONG).show();
        }
//        Home should be selected when this activity starts
        navigationView.setCheckedItem(R.id.nav_home);
        // Insert the home fragment by replacing any existing fragment
        try {
            Fragment fragment = HomeFragment.class.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        TODO: Name and email Address should be displayed on nav-bar

//        TODO: Implement swipe views for home page

    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.nav_requests:
                fragmentClass = RequestsFragment.class;
                break;
            case R.id.nav_appointments:
                fragmentClass = AppointmentsFragment.class;
                break;
            case R.id.nav_donations:
                fragmentClass = DonationsFragment.class;
                break;
            case R.id.nav_organizations:
                fragmentClass = OrganizationsFragment.class;
                break;
            case R.id.nav_faq:
                fragmentClass = FaqFragment.class;
                break;
            case R.id.nav_friend:
                fragmentClass = InviteFriendFragment.class;
                break;
            case R.id.nav_report:
                fragmentClass = ReportFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
//        TODO: Implement search using this guide
//        https://developer.android.com/training/search/setup.html
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        TODO: add search button
        if (id == R.id.action_settings) {
//            TODO: add settings activity here
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//        change fragment based on drawer item selected
        selectDrawerItem(item);
//        close drawer after click
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//    TODO: Understand what OnFragmentInteractionListener is supposed to do
    @Override
    public void onAppointmentsFragmentInteraction(Uri uri) {

    }
    @Override
    public void onDonationsFragmentInteraction(Uri uri) {

    }
    @Override
    public void onFaqFragmentInteraction(Uri uri) {

    }
    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }
    @Override
    public void onInviteFriendFragmentInteraction(Uri uri) {

    }
    @Override
    public void onOrganizationsFragmentInteraction(Uri uri) {

    }
    @Override
    public void onReportFragmentInteraction(Uri uri) {

    }
    @Override
    public void onRequestsFragmentInteraction(Uri uri) {

    }

}
