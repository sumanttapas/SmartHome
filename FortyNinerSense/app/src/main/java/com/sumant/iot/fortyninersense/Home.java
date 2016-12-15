package com.sumant.iot.fortyninersense;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Home extends AppCompatActivity {

    static String userString;
    static String pwdString;

    EditText username;
    EditText password;
    EditText address;
    Button login;
    Button register;
    Button reset;
    Button forgotPassword;
    Button exit;
    String ipAddress = "10.0.0.7";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        login = (Button) findViewById(R.id.buttonLogin);
        reset = (Button) findViewById(R.id.buttonReset);
        register = (Button) findViewById(R.id.buttonRegister);
        forgotPassword = (Button) findViewById(R.id.buttonForgotPassword);
        exit = (Button) findViewById(R.id.buttonExit);
        address = (EditText) findViewById(R.id.editTextIPAddress);
        password.setHint(R.string.password_hint);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        userString = username.getText().toString();
        pwdString = password.getText().toString();

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    Log.d("test","Network available");
                    Intent regActivity = new Intent(getBaseContext(),RegisterActivity.class);
                    startActivity(regActivity);
                }

                else{
                    Toast.makeText(getBaseContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                username.setText("");
                password.setText("");
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userString = username.getText().toString();
                pwdString = password.getText().toString();
                if(!address.getText().toString().equals(""))
                {
                    ipAddress = address.getText().toString();
                }
                if(userString.equals("") ||pwdString.equals("")){
                    Toast.makeText(getBaseContext(),"Enter Username and Password!", Toast.LENGTH_SHORT).show();
                }
                else if(isNetworkAvailable()){
                    Log.d("test","Network available");

                    //String passwd = "true\n";//getConnection(userString, pwdString);
                    String passwd = getConnection(userString, pwdString);

                    if(!((passwd.equals("null\n")) || (passwd.equals("")))){
                        if(!passwd.equals("false") || !(passwd.equals(""))){
                        //if(true){

                            Log.d("pass","Password verified");
                            Toast.makeText(getBaseContext(),"Login Successfull!", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", userString);
                            bundle.putString("userID", passwd);
                            bundle.putString("ipAddress", ipAddress);
                            Log.d("pass","Bundle put string");
                            Intent userHomeNavActivity = new Intent(getBaseContext(),UserHomeNav.class);
                            userHomeNavActivity.putExtras(bundle);
                            Log.d("pass","Here");
                            startActivity(userHomeNavActivity);
                        }
                        else
                            Toast.makeText(getBaseContext(),"Invalid Username/Password!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getBaseContext(),"Login Failed!", Toast.LENGTH_SHORT).show();
                }

                else{
                    Toast.makeText(getBaseContext(), R.string.noInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent forgotIntent = new Intent(getBaseContext(),ForgotPasswordActivity.class);
                startActivity(forgotIntent);
            }
        });


        if(userString.equals(""))
        {
            Toast.makeText(getBaseContext(),"Enter User name",Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        username = (EditText) findViewById(R.id.editTextUsername);
        password = (EditText) findViewById(R.id.editTextPassword);
        username.setText("");
        password.setText("");
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public String getConnection(String usr, String pwd){

        InputStream inputStream = null;
        String result = "";
        ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
        nameValuePairs1.add(new BasicNameValuePair("username",usr));
        nameValuePairs1.add(new BasicNameValuePair("password",pwd));

        //http postappSpinners
        try{
            HttpClient httpclient = new DefaultHttpClient();

            // have to change the ip here to correct ip
            HttpPost httppost = new HttpPost(String.format("http://%s/%s",ipAddress,"login.php"));//"http://192.168.1.114/login.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        }
        catch(Exception e){
            Log.e("log_tag", "Error in http connection "+e.toString());
            Toast.makeText(getBaseContext(), "Server Not Responding", Toast.LENGTH_SHORT).show();
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
            if(!result.equals("false")){
                try {
                    JSONArray data = new JSONArray(result);
                    JSONObject k = data.getJSONObject(0);
                    Log.d("json",k.toString());
                    String userID = Integer.toString(k.getInt("userID"));
                    return userID;
                }catch (Exception e)
                {
                    return "false";
                }

            }

        }
        catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
        }
        return result;

    }
}
