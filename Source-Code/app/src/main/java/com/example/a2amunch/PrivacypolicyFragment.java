package com.example.a2amunch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;


public class PrivacypolicyFragment extends Fragment {
    WebView pp;
    ProgressDialog loadingbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_privacypolicy, container, false);
        pp = v.findViewById(R.id.pp);
        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Fetching Policies");
        loadingbar.setMessage("Hold up for a while");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        pp.loadUrl("https://www.privacypolicytemplate.net/live.php?token=IIWYMX8qWwVyzzEqzY6T0b8iOHweOQhA");
        pp.getSettings().setJavaScriptEnabled(true);
        pp.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingbar.dismiss();
            }
        });



        return v;
    }
}