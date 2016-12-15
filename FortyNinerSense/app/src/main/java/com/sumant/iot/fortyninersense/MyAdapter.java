package com.sumant.iot.fortyninersense;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sumant on 10/23/16.
 */
public class MyAdapter extends BaseExpandableListAdapter implements SeekBar.OnTouchListener{
    private HashMap<String, List<String>> child_values;
    List<String> parent_values;
    private Context ctxt;
    String ipAddress = "192.168.1.114";
    int userID = 1001;
    String uid;
    MyAdapter(Context ctx, HashMap<String, List<String>> val, List<String> list)
    {
        this.ctxt = ctx;
        this.child_values = val;
        this.parent_values = list;
        ipAddress = child_values.get("Garage Doors").get(0);
        uid = child_values.get("Thermostat").get(0);
        userID = Integer.parseInt(uid);
        //userID = 1001;
    }

    @Override
    public int getGroupCount() {
        return child_values.size();
    }
    @Override
    public int getChildTypeCount()
    {
        return 8;
    }

    @Override
    public int getChildType (int groupPosition, int childPosition) {
    /*
     * In case of group 1 this function returns 1,
     * in case of group 2 it returns 2, and so on.
     */
        /*if(groupPosition == 0)
            return groupPosition;
        else
            return 1;*/
        return groupPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child_values.get(parent_values.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parent_values.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child_values.get(parent_values.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);

        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.parent_explist, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textViewHeadingItem);
        textView.setTextSize(convertView.getResources().getDimension(R.dimen.textsizeParent));
        textView.setTextColor(Color.parseColor("#05aeba"));
        textView.setText(title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childTitle = (String) this.getChild(groupPosition, childPosition);
        Integer childType = (Integer) getChildType(groupPosition, childPosition);
        //Log.d("Child", childTitle);
        if(convertView == null || convertView.getTag() != childType)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (childType) {
                case 0://security system
                    convertView = layoutInflater.inflate(R.layout.child_security_system, null);
                    convertView.setTag(childType);
                    break;
                case 1://locks
                    convertView = layoutInflater.inflate(R.layout.child_locks, null);
                    convertView.setTag(childType);
                    break;
                case 2://garage doors
                    convertView = layoutInflater.inflate(R.layout.child_garage_doors, null);
                    convertView.setTag(childType);
                    break;
                case 3://lights
                    convertView = layoutInflater.inflate(R.layout.child_lights, null);
                    convertView.setTag(childType);
                    break;
                case 4://motion detector
                    convertView = layoutInflater.inflate(R.layout.child_motion_detector, null);
                    convertView.setTag(childType);
                    break;
                case 5://door
                    convertView = layoutInflater.inflate(R.layout.child_door_sensors, null);
                    convertView.setTag(childType);
                    break;
                case 6://thermo
                    convertView = layoutInflater.inflate(R.layout.child_thermostat, null);
                    convertView.setTag(childType);
                    break;
                case 7://weather
                    convertView = layoutInflater.inflate(R.layout.child_weather_forecast, null);
                    convertView.setTag(childType);
                    break;

            }


            //convertView = layoutInflater.inflate(R.layout.child_explist, null);
        }
        switch (childType) {
            case 0://security system
                TextView textViewStatus = (TextView) convertView.
                        findViewById(R.id.textViewChildSecurityStatus);
                ImageView imageViewStatus = (ImageView) convertView.
                        findViewById(R.id.imageViewChildSecSysLock);
                String rep = GetSSMotionDoorData.getSecuritySystemData
                        ("getSecurityStatus.php", ipAddress, Integer.toString(userID));
                if(rep.equals("armed stay")) {
                    textViewStatus.setText("Armed Stay");
                    imageViewStatus.setImageDrawable(convertView.getResources().
                            getDrawable(R.drawable.ic_armed_orange_b));
                }else if(rep.equals("armed away")){
                    textViewStatus.setText("Armed Away");
                    imageViewStatus.setImageDrawable(convertView.getResources().
                            getDrawable(R.drawable.ic_armed_red_b));
                }else if(rep.equals("disarmed")){
                    textViewStatus.setText("Disarmed");
                    imageViewStatus.setImageDrawable(convertView.getResources().
                            getDrawable(R.drawable.ic_armed_green_b));
                }
                break;
            case 1://locks
                ImageView imageViewGlock = (ImageView) convertView.findViewById(R.id.imageViewChildGarage);
                ImageView imageViewFlock = (ImageView) convertView.findViewById(R.id.imageViewChildLockBack);
                ImageView imageViewBLock = (ImageView) convertView.findViewById(R.id.imageViewChildLockBack);

                HashMap<String, String> ret1 = GetLockData.getLockData("getLock.php",ipAddress,Integer.toString(userID));
                if(ret1 != null)
                {
                    if(ret1.get("lock_status_garage").equals("locked")){
                        imageViewGlock.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_keylocked_s));

                    }else if(ret1.get("lock_status_garage").equals("unlocked")){
                        imageViewGlock.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_keyunlocked_s));

                    }

                    if(ret1.get("lock_status_front").equals("locked")){
                        imageViewFlock.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_keylocked_s));

                    }else if(ret1.get("lock_status_front").equals("unlocked")){
                        imageViewFlock.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_keyunlocked_s));

                    }

                    if(ret1.get("lock_status_back").equals("locked")){
                        imageViewBLock.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_keylocked_s));

                    }else if(ret1.get("lock_status_back").equals("unlocked")){
                        imageViewBLock.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_keyunlocked_s));

                    }
                }



                break;
            case 2://garage doors
                ImageView imageView1Door = (ImageView) convertView.findViewById(R.id.imageViewChildGarage1CarDoor);
                ImageView imageView2Door = (ImageView) convertView.findViewById(R.id.imageViewChildGarage2CarDoor);
                String [] retG = GetSSMotionDoorData.
                        getGarageDoorStatus("getGarageDoor.php", ipAddress, Integer.toString(userID));
                if(retG != null)
                {
                    if(retG[0].equals("open"))
                        imageView1Door.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_garage_door_open));
                    else if(retG[0].equals("close"))
                        imageView1Door.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_garage_door_closed));
                    if(retG[1].equals("open"))
                        imageView2Door.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_garage_door_open));
                    else if(retG[1].equals("close"))
                        imageView2Door.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_garage_door_closed));
                }
                break;
            case 3://lights
                ImageView imageViewKitchen = (ImageView) convertView.findViewById(R.id.imageViewChildKitchen);
                ImageView imageViewLiving = (ImageView) convertView.findViewById(R.id.imageViewChildLivingRoom);
                ImageView imageViewBasement = (ImageView) convertView.findViewById(R.id.imageViewChildBasement);
                SeekBar seekBarKitchen = (SeekBar) convertView.findViewById(R.id.seekBarChildKitchen);
                SeekBar seekBarLiving = (SeekBar) convertView.findViewById(R.id.seekBarChildLivingRoom);

                seekBarKitchen.setOnTouchListener(this);
                seekBarLiving.setOnTouchListener(this);

                JSONArray data = GetSSMotionDoorData.getJSONArray("getLightStatus.php", ipAddress, Integer.
                                                                                    toString(userID));
                int flag = 1;
                for (int i = 0; i < data.length(); i++) {
                    try {
                        JSONObject k = data.getJSONObject(i);
                        switch(k.getInt("room"))
                        {
                            case 1:
                                if(k.getString("light_status").equals("on")) {
                                    imageViewKitchen.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_light_on));
                                    seekBarKitchen.setProgress(k.getInt("dimmer_status"));
                                }
                                else {
                                    flag = 0;
                                    seekBarKitchen.setEnabled(false);
                                    imageViewKitchen.setTag(R.drawable.ic_light_off);
                                    imageViewKitchen.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_light_off));
                                }
                                break;
                            case 2:
                                if(k.getString("light_status").equals("on")) {
                                    imageViewLiving.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_light_on));
                                    seekBarLiving.setProgress(k.getInt("dimmer_status"));
                                }
                                else {
                                    flag = 0;
                                    imageViewLiving.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_light_off));
                                    seekBarLiving.setEnabled(false);
                                }

                                break;
                            case 3:
                                if(k.getString("light_status").equals("on")) {
                                    //imageViewBasement.setTag(R.drawable.ic_light_on);
                                    imageViewBasement.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_light_on));
                                }
                                else {
                                    flag = 0;
                                    //imageViewBasement.setTag(R.drawable.ic_light_off);
                                    imageViewBasement.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_light_off));
                                }// seekBarLivingRoom.setProgress(k.getInt("dimmer_status"));
                                break;
                        }

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }



                break;
            case 4://motion detector
                ImageView imageViewMain = (ImageView) convertView.findViewById(R.id.imageViewChildMainMotionSensor);
                ImageView imageViewUp = (ImageView) convertView.findViewById(R.id.imageViewChildUpMotionSensor);
                String[] retMD = GetSSMotionDoorData.getMotionSensorsStatus
                        ("getMotionSensors.php", ipAddress, Integer.toString(userID));
                Log.d("retMD",retMD.toString());
                if(retMD != null)
                {
                    if(retMD[0].equals("engaged"))
                        imageViewMain.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_engaged));
                    else if(retMD[0].equals("disengaged"))
                        imageViewMain.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_disengaged));

                    if(retMD[1].equals("engaged"))
                        imageViewUp.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_engaged));
                    else if(retMD[1].equals("disengaged"))
                        imageViewUp.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_disengaged));
                }
                break;
            case 5://door
                ImageView imageViewMainDoor = (ImageView) convertView.findViewById(R.id.imageViewChildMainDoorSensor);
                ImageView imageViewUpDoor = (ImageView) convertView.findViewById(R.id.imageViewChildUpDoorSensor);
                String[] retD = GetSSMotionDoorData.getMotionSensorsStatus
                        ("getDoorSensor.php", ipAddress, Integer.toString(userID));

                if(retD != null)
                {
                    Log.d("ret",retD.toString());
                    if(retD[0].equals("engaged"))
                        imageViewMainDoor.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_engaged));
                    else if(retD[0].equals("disengaged"))
                        imageViewMainDoor.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_disengaged));

                    if(retD[1].equals("engaged"))
                        imageViewUpDoor.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_engaged));
                    else if(retD[1].equals("disengaged"))
                        imageViewUpDoor.setImageDrawable(convertView.getResources().getDrawable(R.drawable.ic_disengaged));
                }
                break;
            case 6://thermo
                TextView textViewMainCur = (TextView) convertView.findViewById(R.id.textViewChildThermoCurTempMain);
                TextView textViewUpCur = (TextView) convertView.findViewById(R.id.textViewChildThermoCurTempUp);
                TextView textViewMainSet = (TextView) convertView.findViewById(R.id.textViewChildThermoSetTempMain);
                TextView textViewUpSet = (TextView) convertView.findViewById((R.id.textViewChildThermoSetTempUp));
                TextView textViewMainFan = (TextView) convertView.findViewById(R.id.textViewChildThermoMainFan);
                TextView textViewUpFan = (TextView) convertView.findViewById(R.id.textViewChildThermoUpFan);
                TextView textViewOutside = (TextView) convertView.findViewById(R.id.textViewChildThermoOutsideTemp);

                textViewOutside.setText(GetWeatherData.getTempValue(convertView.getContext()));
                HashMap<String, String> ret = GetThermoData.getThermoData("getTemp.php",ipAddress,userID);

                ImageView imageViewModeMain = (ImageView) convertView.findViewById(R.id.imageViewChildThermoModeMainFloor);
                ImageView imageViewModeUp = (ImageView) convertView.findViewById(R.id.imageViewChildThermoModeUpFloor);

                if(ret != null)
                {
                    textViewMainCur.setText(ret.get("MainCurr")+" \u2109");
                    textViewMainSet.setText(ret.get("MainSet")+" \u2109");
                    textViewMainFan.setText(ret.get("MainFan"));
                    textViewUpCur.setText(ret.get("UpCurr")+" \u2109");
                    textViewUpSet.setText(ret.get("UpSet")+" \u2109");
                    textViewUpFan.setText(ret.get("UpFan"));
                    if(ret.get("MainMode").equals("Cool")){
                        imageViewModeMain.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_snowflake));
                    }else if(ret.get("MainMode").equals("Heat")){
                        imageViewModeMain.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_heating));
                    }else if(ret.get("MainMode").equals("off")){
                        imageViewModeMain.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_off));
                    }
                    if(ret.get("UpMode").equals("Cool")){
                        imageViewModeUp.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_snowflake));
                    }else if(ret.get("UpMode").equals("Heat")){
                        imageViewModeUp.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_heating));
                    }else if(ret.get("UpMode").equals("off")){
                        imageViewModeUp.setImageDrawable(convertView.getResources().
                                getDrawable(R.drawable.ic_off));
                    }
                }

                break;
            case 7://weather
                TextView textViewDateDay1 = (TextView) convertView.findViewById(R.id.textViewDateDay1);
                TextView textViewDateDay2 = (TextView) convertView.findViewById(R.id.textViewDateDay2);
                TextView textViewDateDay3 = (TextView) convertView.findViewById(R.id.textViewDateDay3);
                TextView textViewDateDay4 = (TextView) convertView.findViewById(R.id.textViewDateDay4);
                TextView textViewDateDay5 = (TextView) convertView.findViewById(R.id.textViewDateDay5);

                TextView textViewHiLoDay1 = (TextView) convertView.findViewById(R.id.textViewDay1HighLow);
                TextView textViewHiLoDay2 = (TextView) convertView.findViewById(R.id.textViewDay2HighLow);
                TextView textViewHiLoDay3 = (TextView) convertView.findViewById(R.id.textViewDay3HighLow);
                TextView textViewHiLoDay4 = (TextView) convertView.findViewById(R.id.textViewDay4HighLow);
                TextView textViewHiLoDay5 = (TextView) convertView.findViewById(R.id.textViewDay5HighLow);

                ImageView imageViewWeatherDay1 = (ImageView) convertView.findViewById(R.id.imageViewDay1);
                ImageView imageViewWeatherDay2 = (ImageView) convertView.findViewById(R.id.imageViewDay2);
                ImageView imageViewWeatherDay3 = (ImageView) convertView.findViewById(R.id.imageViewDay3);
                ImageView imageViewWeatherDay4 = (ImageView) convertView.findViewById(R.id.imageViewDay4);
                ImageView imageViewWeatherDay5 = (ImageView) convertView.findViewById(R.id.imageViewDay5);

                ArrayList<ArrayList<String>> list = GetWeatherForecast.getForecastValue
                                                                (convertView.getContext());

                if(list != null) {
                    String[] date = new String[5];
                    String[] hiLo = new String[5];
                    String[] id = new String[5];
                    for (int i = 0; i < list.size(); i++) {
                        date[i] = list.get(i).get(3);
                        hiLo[i] = "min:"+list.get(i).get(0) + "℉ /" + "max:" + list.get(i).get(1)+"℉";
                        id[i] = list.get(i).get(2);
                    }
                    textViewDateDay1.setText(date[0]);
                    textViewDateDay2.setText(date[1]);
                    textViewDateDay3.setText(date[2]);
                    textViewDateDay4.setText(date[3]);
                    textViewDateDay5.setText(date[4]);

                    textViewHiLoDay1.setText(hiLo[0]);
                    textViewHiLoDay2.setText(hiLo[1]);
                    textViewHiLoDay3.setText(hiLo[2]);
                    textViewHiLoDay4.setText(hiLo[3]);
                    textViewHiLoDay5.setText(hiLo[4]);

                    imageViewWeatherDay1.setImageDrawable(convertView.
                            getResources().getDrawable(Integer.parseInt(id[0])));
                    imageViewWeatherDay2.setImageDrawable(convertView.
                            getResources().getDrawable(Integer.parseInt(id[1])));
                    imageViewWeatherDay3.setImageDrawable(convertView.
                            getResources().getDrawable(Integer.parseInt(id[2])));
                    imageViewWeatherDay4.setImageDrawable(convertView.
                            getResources().getDrawable(Integer.parseInt(id[3])));
                    imageViewWeatherDay5.setImageDrawable(convertView.
                            getResources().getDrawable(Integer.parseInt(id[4])));

                }



                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
