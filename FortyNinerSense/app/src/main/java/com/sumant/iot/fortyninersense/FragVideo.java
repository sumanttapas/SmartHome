package com.sumant.iot.fortyninersense;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


public class FragVideo extends Fragment {


    private WebView mWebView;
    String ipAddress ="10.0.0.5";
    String userID = "1001";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camerawebcam,container, false);
        //getView().setContentView(R.layout.fragment_web);
        userID = this.getArguments().getString("userID");
        ipAddress = "10.0.0.5";
        mWebView = (WebView)view.findViewById(R.id.fragment_web);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(String.format("http://%s:8080/",ipAddress));

        return view;
    }




}
