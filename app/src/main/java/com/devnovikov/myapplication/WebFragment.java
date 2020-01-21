package com.devnovikov.myapplication;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebFragment extends Fragment implements OnBackPressedListener{
    private final static String API_URL = "http://www.icndb.com/api/";
    private WebView webView;

    public WebFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_web, container, false);
        webView = (WebView) view.findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(inflater.getContext());
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(API_URL);
        return view;
    }


    @Override
    public void onBackPressed() {
        if (this.webView.canGoBack()) this.webView.goBack();
    }
}
