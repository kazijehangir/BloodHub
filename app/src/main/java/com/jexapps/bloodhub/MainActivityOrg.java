package com.jexapps.bloodhub;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.SearchView;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.jexapps.bloodhub.m_Model.User;

public class MainActivityOrg extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        RequestsOrgFragment.OnFragmentInteractionListener,
        ReviewsFragment.OnFragmentInteractionListener,
        AppointmentsOrgFragment.OnFragmentInteractionListener,
        FaqFragment.OnFragmentInteractionListener,
        HomeOrgFragment.OnFragmentInteractionListener,
        OrgPatientListFragment.OnFragmentInteractionListener,
        OrgDonorListFragment.OnFragmentInteractionListener,
        OtherOrganizationsFragment.OnFragmentInteractionListener,
        ReportFragment.OnFragmentInteractionListener,
        SettingsOrgFragment.OnFragmentInteractionListener{
    //TODO: add separate layout for organizations
//    TODO: Understand inflating views. probably is fix to update email and photo
//    inflating views is creating view and view-groups from an xml file/resource
//    not sure if that helps.
    private DrawerLayout mDrawer;
    Button button_request, button_patient;
    FloatingActionButton fab_plus, fab_request, fab_appointment;
    Animation FabOpen, FabClose, FabRClockwise, FabRanticlockwise;
    TextView mNav_name;
    boolean isOpen = false;
    private String mEmail = null;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        get email from login activity
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mEmail = user.getEmail();
//        TODO: comment this toast out when done debugging
//        Toast.makeText(this, "Logged in " + mEmail + " successfully.",
//                Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_org);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab_plus = (FloatingActionButton)findViewById(R.id.fab2);
        fab_request = (FloatingActionButton)findViewById(R.id.fab1);
        fab_appointment = (FloatingActionButton)findViewById(R.id.fab);
        button_request = (Button) findViewById(R.id.add_blood_request);
        button_patient = (Button) findViewById(R.id.add_patient);
        FabOpen = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_open);
        FabClose = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        FabRClockwise=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        FabRanticlockwise = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        fab_plus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isOpen) {
                    fab_request.startAnimation(FabClose);
                    fab_appointment.startAnimation(FabClose);
                    button_request.setVisibility(View.GONE);
                    button_patient.setVisibility(View.GONE);
                    fab_plus.startAnimation(FabRanticlockwise);
                    fab_request.setClickable(false);
                    fab_appointment.setClickable(false);
                    isOpen = false;

                }
                else
                {
                    fab_request.startAnimation(FabOpen);
                    fab_appointment.startAnimation(FabOpen);
                    fab_plus.startAnimation(FabRClockwise);
                    button_request.setVisibility(View.VISIBLE);
                    button_patient.setVisibility(View.VISIBLE);
                    fab_request.setClickable(true);
                    fab_appointment.setClickable(true);
                    isOpen = true;

                }
            }
        });
        fab_request.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivityOrg.this, AddPatientActivity.class);
                intent.putExtra("mEmail", mEmail);
                startActivity(intent);
            }
        });
        fab_appointment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivityOrg.this, AddDonorActivity.class);
                intent.putExtra("mEmail", mEmail);
                startActivity(intent);
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

//        Home should be selected when this activity starts
        navigationView.setCheckedItem(R.id.nav_home);
        // Insert the home fragment by replacing any existing fragment
        try {
            Fragment fragment = HomeOrgFragment.class.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

//      Name and email Address should be displayed on nav-bar
        View header = navigationView.getHeaderView(0);
        TextView mNav_email = (TextView) header.findViewById(R.id.nav_header_email);
        mNav_name = (TextView) header.findViewById(R.id.nav_header_name);
        ImageView mNav_image = (ImageView) header.findViewById(R.id.nav_header_image);

        mNav_email.setText(mEmail);

//        TODO: Get name and image from database and display here
        getNameFromDatabase();
//        mNav_image.setImageDrawable();
//        TODO: Implement swipe views for home page
        LinearLayout lheader = (LinearLayout) header.findViewById(R.id.header);
        lheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityOrg.this, UserProfile.class);
                intent.putExtra("mEmail", mEmail);
                startActivity(intent);
            }
        });
    }

    private void getNameFromDatabase() {
        final Context context = getApplicationContext();
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User usr = dataSnapshot.getValue(User.class);
                        mNav_name.setText(usr.username);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        return;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        Bundle args = new Bundle();
        if (menuItem.getItemId() == R.id.signout) {
            finish();
//            Intent intent = new Intent(this,LoginActivity.class);
//            startActivity(intent);
        }
        switch(menuItem.getItemId()) {
            case R.id.nav_home:
                fragmentClass = HomeOrgFragment.class;
                break;
            case R.id.nav_requests:
                fragmentClass = RequestsOrgFragment.class;
                break;
            case R.id.nav_appointments:
                fragmentClass = AppointmentsOrgFragment.class;
//                TODO: Make appointments dynamic
                args.putString("appointments",null);
                break;
            case R.id.nav_reviews:
                fragmentClass = ReviewsFragment.class;
                break;
            case R.id.nav_organizations:
                fragmentClass = OtherOrganizationsFragment.class;
                break;
            case R.id.nav_faq:
                fragmentClass = FaqFragment.class;
                break;
            case R.id.nav_report:
                fragmentClass = ReportFragment.class;
                break;
            case R.id.nav_Settings:
                fragmentClass = SettingsOrgFragment.class;
                args.putBoolean("request",true);
                break;
            default:
                fragmentClass = HomeOrgFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setArguments(args);
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
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        //SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
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
    public void onAppointmentsOrgFragmentInteraction(Uri uri) {

    }
    @Override
    public void onReviewsFragmentInteraction(Uri uri) {

    }
    @Override
    public void onFaqFragmentInteraction(Uri uri) {

    }
    @Override
    public void onHomeOrgFragmentInteraction(Uri uri) {

    }
    @Override
    public void onOtherOrganizationsFragmentInteraction(Uri uri) {

    }
    @Override
    public void onReportFragmentInteraction(Uri uri) {

    }
    @Override
    public void onRequestsOrgFragmentInteraction(Uri uri) {

    }
    @Override
    public void onOrgPatientListFragmentInteraction(Uri uri) {

    }
    @Override
    public void onOrgDonorListFragmentInteraction(Uri uri) {

    }



    @Override
    public void onSettingsOrgFragmentInteraction(Uri iri) {

    }
}
