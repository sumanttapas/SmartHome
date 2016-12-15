package com.sumant.iot.fortyninersense;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragThermostat extends Fragment implements NumberPicker.OnValueChangeListener,
        View.OnClickListener, Spinner.OnItemSelectedListener,CompoundButton.OnCheckedChangeListener,
        View.OnTouchListener{

    View view;
    NumberPicker numberPickerPickTemp;
    TextView textViewOutSideTemp;
    TextView textViewUpFloorCurrentTemp;
    TextView textViewMainFloorCurrentTemp;
    TextView textViewMode;
    TextView textViewFan;
    TextView textViewUpFloorTargetTemp;
    TextView textViewMainFloorTargetTemp;

    CheckBox checkBoxMain;
    CheckBox checkBoxUp;

    Button buttonSetTemp;
    Spinner spinnerMainMode;
    Spinner spinnerMainFan;
    Spinner spinnerUpMode;
    Spinner spinnerUpFan;

    ImageView imageViewMainMode;
    ImageView imageViewMainFan;
    ImageView imageViewUpMode;
    ImageView imageViewUpFan;
    Boolean touched = false;
    int check = 0;
    final String getPHPfile = "getTemp.php";
    final String URL = "http://%s/%s?userID=%s";
    String ipAddress ="192.168.1.109";
    String userID = "1001";
    final String setPHPfile = "";
    final String current_temp = "insideTemp";
    final String set_temp = "setTemp";
    final String mode = "ac_mode";
    final String fan = "fan";
    public FragThermostat()
    {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_frag_thermostat, container, false);
        userID = this.getArguments().getString("userID");
        ipAddress = this.getArguments().getString("ipAddress");
        //All Initializers
        numberPickerPickTemp = (NumberPicker) view.findViewById(R.id.numberPickerTargetTemp);
        //outside
        textViewOutSideTemp = (TextView) view.findViewById(R.id.textViewOutSideTemp);
        //current
        textViewMainFloorCurrentTemp = (TextView) view.findViewById(R.id.textViewMainFloorCurrent);
        textViewUpFloorCurrentTemp = (TextView) view.findViewById(R.id.textViewUpFloorCurrent);
        //target1
        textViewUpFloorTargetTemp = (TextView) view.findViewById(R.id.textViewUpTargetTemp);
        textViewMainFloorTargetTemp = (TextView) view.findViewById(R.id.textViewMainTargetTemp);
       // textViewMode = (TextView) view.findViewById(R.id.textViewMode);
        //textViewFan = (TextView) view.findViewById(R.id.textViewFan);

        buttonSetTemp = (Button) view.findViewById(R.id.buttonSetTemp);
        numberPickerPickTemp.setMaxValue(90);
        numberPickerPickTemp.setMinValue(50);
        //numberPickerPickTemp.setValue(65);
        check = 0;
        checkBoxMain = (CheckBox) view.findViewById(R.id.checkBoxMainFloor);
        checkBoxUp = (CheckBox) view.findViewById(R.id.checkBoxUpFloor);
        textViewOutSideTemp.setText("65");

        textViewMainFloorCurrentTemp.setText("70");
        textViewMainFloorTargetTemp.setText("70");
        textViewUpFloorCurrentTemp.setText("70");
        textViewUpFloorTargetTemp.setText("70");
        textViewOutSideTemp.setText(GetWeatherData.getTempValue(getActivity()));


        imageViewMainFan = (ImageView) view.findViewById(R.id.imageViewMainFan);
        imageViewMainFan.setTag(R.drawable.ic_fan);
        imageViewUpFan = (ImageView) view.findViewById(R.id.imageViewUpFan);
        imageViewUpFan.setTag(R.drawable.ic_fan);

        imageViewMainMode = (ImageView) view.findViewById(R.id.imageViewMainMode) ;
        imageViewMainMode.setTag(R.drawable.ic_snowflake);
        imageViewUpMode = (ImageView) view.findViewById(R.id.imageViewUpMode) ;
        imageViewUpMode.setTag(R.drawable.ic_snowflake);


        spinnerMainFan = (Spinner) view.findViewById(R.id.spinnerMainFloorFan);
        spinnerMainMode = (Spinner) view.findViewById(R.id.spinnerMainFloorMode);

        spinnerUpFan = (Spinner) view.findViewById(R.id.spinnerUpFloorFan);
        spinnerUpMode = (Spinner) view.findViewById(R.id.spinnerUpFloorMode);

        List<String> fan = new ArrayList<String>();
        fan.add("On");
        fan.add("Auto");
        List<String> mode = new ArrayList<String>();
        mode.add("Cool");
        mode.add("Heat");
        mode.add("off");


        ArrayAdapter<String> fanAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, fan);
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mode);
        fanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //All Listeners

        spinnerMainFan.setAdapter(fanAdapter);
        spinnerMainMode.setAdapter(modeAdapter);
        spinnerUpFan.setAdapter(fanAdapter);
        spinnerUpMode.setAdapter(modeAdapter);

        setAllInitialValues(getCurrentTempStatus(getPHPfile));


        spinnerUpMode.setOnItemSelectedListener(this);
        spinnerUpFan.setOnItemSelectedListener(this);
        spinnerMainMode.setOnItemSelectedListener(this);
        spinnerMainFan.setOnItemSelectedListener(this);


        numberPickerPickTemp.setOnValueChangedListener(this);
        buttonSetTemp.setOnClickListener(this);
        checkBoxMain.setOnCheckedChangeListener(this);
        checkBoxUp.setOnCheckedChangeListener(this);
        return view;
    }


    public String setTemp(String userId, int floor, String insideTemp,
                          String outsideTemp, String setTemp, String mode, String fan){

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("userID",userId));
        nameValuePairs1.add(new BasicNameValuePair("floor",Integer.toString(floor)));
        nameValuePairs1.add(new BasicNameValuePair("insideTemp",insideTemp));
        nameValuePairs1.add(new BasicNameValuePair("outsideTemp",outsideTemp));
        nameValuePairs1.add(new BasicNameValuePair("setTemp",setTemp));
        nameValuePairs1.add(new BasicNameValuePair("fan",fan));
        nameValuePairs1.add(new BasicNameValuePair("ac_mode",mode));



        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(String.format("http://%s/%s",ipAddress,"setTemp.php"));
            //"http://192.168.1.109/setLightStatus.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
            Toast.makeText(getActivity(), "Server Not Responding", Toast.LENGTH_SHORT).show();
            return "";
        }
        //convert response to string
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result=sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        Log.d("res",result);
        return result;

    }

    public HashMap<String, String> getCurrentTempStatus(String phpfile){
        HashMap<String, String> dataFromDB = new HashMap<String, String>();
        JSONArray data = getJSONObject(phpfile);
        if(data != null)
        {
            try {

                for (int i = 0; i < data.length(); i++)
                {
                    JSONObject d = data.getJSONObject(i);
                    //d = d.getJSONObject("data");
                    switch (d.getInt("floor"))
                    {
                        case 1:
                            dataFromDB.put("curTempMain",Integer.toString(d.getInt(current_temp)));
                            dataFromDB.put("setTempMain",Integer.toString(d.getInt(set_temp)));
                            dataFromDB.put("modeMain",d.getString(mode));
                            dataFromDB.put("fanMain",d.getString(fan));
                            break;
                        case 2:
                            dataFromDB.put("curTempUp",Integer.toString(d.getInt(current_temp)));
                            dataFromDB.put("setTempUp",Integer.toString(d.getInt(set_temp)));
                            dataFromDB.put("modeUp",d.getString(mode));
                            dataFromDB.put("fanUp",d.getString(fan));
                            break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("NULL","Exception in JSON");
                return null;
            }
        }
        return dataFromDB;
    }

    public JSONArray getJSONObject(String phpfile) {
        try {
            URL url = new URL(String.format(URL, ipAddress, phpfile, userID));

            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();
            Log.d("json",json.toString());

            JSONArray data = new JSONArray(json.toString());
            Log.d("json",data.toString());
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAllInitialValues(HashMap<String, String> dbVal){
        if(dbVal != null) {
            textViewUpFloorTargetTemp.setText(dbVal.get("setTempUp")+" ℉");
            textViewMainFloorTargetTemp.setText(dbVal.get("setTempMain")+" ℉");
            textViewUpFloorCurrentTemp.setText(dbVal.get("curTempUp")+" ℉");
            textViewMainFloorCurrentTemp.setText(dbVal.get("curTempMain")+" ℉");
            numberPickerPickTemp.setValue(Integer.parseInt(dbVal.get("setTempUp")));
            if(dbVal.get("modeMain").equals("Cool")) {
                spinnerMainMode.setSelection(0);
                imageViewMainMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_snowflake));
            }
            else if(dbVal.get("modeMain").equals("Heat")) {
                spinnerMainMode.setSelection(1);
                imageViewMainMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_heating));
            }
            else if(dbVal.get("modeMain").equals("off")) {
                spinnerMainMode.setSelection(2);
                imageViewMainMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_off));
            }
            if(dbVal.get("modeUp").equals("Cool")) {
                spinnerUpMode.setSelection(0);
                imageViewUpMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_snowflake));
            }
            else if(dbVal.get("modeUp").equals("Heat")) {
                spinnerUpMode.setSelection(1);
                imageViewUpMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_heating));
            }
            else if(dbVal.get("modeUp").equals("off")) {
                spinnerUpMode.setSelection(2);
                imageViewUpMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_off));
            }


            if(dbVal.get("fanMain").equals("On")) {
                spinnerMainFan.setSelection(0);
            }
            else if(dbVal.get("fanMain").equals("Auto")) {
                spinnerMainFan.setSelection(1);
            }

            if(dbVal.get("fanUp").equals("On")) {
                spinnerUpFan.setSelection(0);
            }
            else if(dbVal.get("fanUp").equals("Auto")) {
                spinnerUpFan.setSelection(1);
            }
        }
        else
        {
            Log.d("NULL","null in HashMap");
        }
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if(checkBoxMain.isChecked() && checkBoxUp.isChecked())
        {
            textViewMainFloorTargetTemp.setText(Integer.toString(newVal));
            textViewUpFloorTargetTemp.setText(Integer.toString(newVal));
        }
        else if(checkBoxUp.isChecked())
        {
            textViewUpFloorTargetTemp.setText(Integer.toString(newVal));
        }
        else if(checkBoxMain.isChecked())
        {
            textViewMainFloorTargetTemp.setText(Integer.toString(newVal));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonSetTemp:
                if(checkBoxMain.isChecked()){
                    setTemp(userID, 1, textViewMainFloorCurrentTemp.getText().toString(),
                            textViewOutSideTemp.getText().toString(), textViewMainFloorTargetTemp.getText().toString(),
                            spinnerMainMode.getSelectedItem().toString(),
                            spinnerMainFan.getSelectedItem().toString());
                }
                if(checkBoxUp.isChecked()){
                    setTemp(userID, 2, textViewUpFloorCurrentTemp.getText().toString(),
                            textViewOutSideTemp.getText().toString(), textViewUpFloorTargetTemp.getText().toString(),
                            spinnerUpMode.getSelectedItem().toString(),
                            spinnerUpFan.getSelectedItem().toString());
                }

                Toast.makeText(getActivity(), "Temperature Set!",Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item;
        check++;

        Log.d("InItemSel",Integer.toString(check));
        if(check>4) {
            switch (parent.getId()) {
                case R.id.spinnerMainFloorMode:

                    item = parent.getItemAtPosition(position).toString();
                    if (item.equals("Cool")) {
                        imageViewMainMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_snowflake));

                    } else if (item.equals("Heat")) {
                        imageViewMainMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_heating));

                    } else {
                        imageViewMainMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_off));
                    }

                    //textViewMode.setText(item);
                    setTemp(userID, 1, textViewMainFloorCurrentTemp.getText().toString(),
                            textViewOutSideTemp.getText().toString(), textViewMainFloorTargetTemp.getText().toString(),
                            spinnerMainMode.getSelectedItem().toString(),
                            spinnerMainFan.getSelectedItem().toString());
                    break;
                case R.id.spinnerUpFloorMode:

                    item = parent.getItemAtPosition(position).toString();
                    if (item.equals("Cool")) {
                        imageViewUpMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_snowflake));
                    } else if (item.equals("Heat"))
                        imageViewUpMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_heating));
                    else
                        imageViewUpMode.setImageDrawable(getResources().getDrawable(R.drawable.ic_off));
                    //textViewMode.setText(item);
                    setTemp(userID, 2, textViewUpFloorCurrentTemp.getText().toString(),
                            textViewOutSideTemp.getText().toString(), textViewUpFloorTargetTemp.getText().toString(),
                            spinnerUpMode.getSelectedItem().toString(),
                            spinnerUpFan.getSelectedItem().toString());
                    break;
                case R.id.spinnerMainFloorFan:
                    //item = parent.getItemAtPosition(position).toString();
                    //textViewFan.setText(item);
                    setTemp(userID, 1, textViewMainFloorCurrentTemp.getText().toString(),
                            textViewOutSideTemp.getText().toString(), textViewMainFloorTargetTemp.getText().toString(),
                            spinnerMainMode.getSelectedItem().toString(),
                            spinnerMainFan.getSelectedItem().toString());
                    break;
                case R.id.spinnerUpFloorFan:
                    //item = parent.getItemAtPosition(position).toString();
                    //textViewFan.setText(item);
                    setTemp(userID, 2, textViewUpFloorCurrentTemp.getText().toString(),
                            textViewOutSideTemp.getText().toString(), textViewUpFloorTargetTemp.getText().toString(),
                            spinnerUpMode.getSelectedItem().toString(),
                            spinnerUpFan.getSelectedItem().toString());
                    break;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.checkBoxMainFloor:
                if(isChecked)
                {
                    String a = Integer.toString(numberPickerPickTemp.getValue());
                    textViewMainFloorTargetTemp.setText(a);
                }
                break;
            case R.id.checkBoxUpFloor:
                if(isChecked)
                {
                    String b = Integer.toString(numberPickerPickTemp.getValue());
                    textViewUpFloorTargetTemp.setText(b);
                }
                break;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        touched = true;
        return touched;
    }
}
