package com.sumant.iot.fortyninersense;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class UserHomeNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentTransaction fragmentTransaction;
    ExpandableListView expandableListView;
    Bundle bundle;
    public final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bundle = this.getIntent().getExtras();
        String userID = bundle.getString("userID");
        String ipAddress = bundle.getString("ipAddress");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d("frag","Start");
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragHomeScreen = new FragHomeScreen();
        fragHomeScreen.setArguments(bundle);
        fragmentTransaction.add(R.id.mainLayout, fragHomeScreen);
        fragmentTransaction.commit();
        Log.d("frag","End");
        getSupportActionBar().setTitle("49er Sense");
        doTheAutoRefresh();
    }
    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // this is where you put your refresh code
                Fragment frg = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                int stackCount = fragmentManager.getBackStackEntryCount();
                if( fragmentManager.getFragments() != null ) {
                    frg = fragmentManager.getFragments().
                            get(stackCount > 0 ? stackCount - 1 : stackCount);
                     {
                        if (!((frg instanceof FragVideo) || (frg instanceof FragEnergy) ||
                                frg instanceof FragThermostat)) {
                            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.detach(frg);
                            ft.attach(frg);
                            ft.commit();
                        }
                    }
                }
                doTheAutoRefresh();
            }
        }, 5000);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            /*for(Fragment fragment:getSupportFragmentManager().getFragments()){
                    if(fragment!=null)
                        getSupportFragmentManager().beginTransaction().remove(fragment).commit();

            }*/
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment fragHomeScreen = new FragHomeScreen();
            fragHomeScreen.setArguments(bundle);
            fragmentTransaction.replace(R.id.mainLayout, fragHomeScreen);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_home_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_securitySystem:
                //expandableListView = (ExpandableListView)
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragSecuritySystem = new FragSecuritySystem();
                fragSecuritySystem.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragSecuritySystem);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Security System");
                break;
            case R.id.nav_locks:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragLocks = new FragLocks();
                fragLocks.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragLocks);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Door Locks");
                break;
            case R.id.nav_garage:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragGarage = new FragGarage();
                fragGarage.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragGarage);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Door Locks");
                break;
            case R.id.nav_lights:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragLights = new FragLights();
                fragLights.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragLights);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Lights");
                break;
            case R.id.nav_thermostat:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragThermostat = new FragThermostat();
                fragThermostat.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragThermostat);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Thermostat");
                break;
            case R.id.nav_door:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragDoorSensors = new FragDoorSensors();
                fragDoorSensors.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragDoorSensors);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Door Sensors");
                break;
            case R.id.nav_motion:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragMotionSensors = new FragMotionSensors();
                fragMotionSensors.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragMotionSensors);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Motion Sensors");
                break;
            case R.id.nav_energy:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragEnergy = new FragEnergy();
                fragEnergy.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragEnergy);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Energy Consumption");
                break;
            case R.id.nav_video:
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment fragVideo = new FragVideo();
                fragVideo.setArguments(bundle);
                fragmentTransaction.replace(R.id.mainLayout, fragVideo);
                fragmentTransaction.commit();
                getSupportActionBar().setTitle("Video View");
                break;
            case R.id.nav_logout:
                handler.removeCallbacksAndMessages(null);
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
