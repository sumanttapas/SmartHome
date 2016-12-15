package com.sumant.iot.fortyninersense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragHomeScreen extends Fragment {
    ExpandableListView expandableListView;

    String userID;
    String ipAddress;
    Bundle bundle;
    public FragHomeScreen() {
        // Required empty public constructor
        //this.userID = userID;
        //this.ipAddress = ipAddress;
        //Log.d("inCONS","Hi");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_home_page, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        List<String> headings = new ArrayList<String>();
        headings.add("Security System");
        headings.add("Locks");
        headings.add("Garage Doors");
        headings.add("Lights");
        headings.add("Motion Detectors");
        headings.add("Door Sensors");
        headings.add("Thermostat");
        headings.add("Weather Forecast");


        List<String> listSecuritySystem = new ArrayList<String>();
        listSecuritySystem.add("Armed");
        List<String> listLocks = new ArrayList<String>();
        listLocks.add("Closed");
        List<String> listLights = new ArrayList<String>();
        listLights.add("Kitchen: Off");
        List<String> listThermo = new ArrayList<String>();
        listThermo.add(userID);
        List<String> listGarageDoor = new ArrayList<String>();
        listGarageDoor.add(ipAddress);
        List<String> listMotionDetectors = new ArrayList<String>();
        listMotionDetectors.add("");
        List<String> listDoorSensors = new ArrayList<String>();
        listDoorSensors.add("");
        List<String> listWeather = new ArrayList<String>();
        listWeather.add("");

        HashMap<String, List<String>> childList = new HashMap<String, List<String>>();
        childList.put(headings.get(0),listSecuritySystem);
        childList.put(headings.get(1),listLocks);
        childList.put(headings.get(2), listGarageDoor);
        childList.put(headings.get(3), listLights);
        childList.put(headings.get(4), listMotionDetectors);
        childList.put(headings.get(5), listDoorSensors);
        childList.put(headings.get(6), listThermo);
        childList.put(headings.get(7), listWeather);
        MyAdapter myAdapter = new MyAdapter(getActivity(), childList, headings);
        expandableListView.setAdapter(myAdapter);

        return view;
    }

}
