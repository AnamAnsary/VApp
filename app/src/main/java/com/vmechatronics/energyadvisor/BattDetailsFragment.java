package com.vmechatronics.energyadvisor;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by vmplapp on 26/7/17.
 */

public class BattDetailsFragment extends Fragment {
    private static final String TAG = "ArticleFragment";
    //public static String ARG_POSITION;
    private int pos;
    private String urltoLoad;
    private ProgressDialog progressBar;
    private FirebaseAnalytics mFirebaseAnalytics;

    AlertDialog alertDialog;
    View rootView;
    WebView view;
    ImageButton btn;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //return inflater.inflate(R.layout.article_view,container, false);
        //View rootView = super.onCreateView(inflater,container,savedInstanceState);
        rootView = inflater.inflate(R.layout.article_view, container, false);
        /*FloatingActionButton fab_bt = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab_bt.setVisibility(View.INVISIBLE);
*/
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        btn = (ImageButton) rootView.findViewById(R.id.order_btn);

        pos =  getArguments().getInt("ARG_POSITION");
        Log.w(TAG, "onCreateView: position is "+ pos );
        updateArticleView(pos);

        String url = GetUrl(pos);

      /*  view = (WebView) rootView.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);
        view.setWebViewClient(new SwAWebClient());
        view.loadUrl(url);

        alertDialog = new AlertDialog.Builder(getActivity()).create();
        progressBar = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        progressBar.setMessage("Loading...");
        progressBar.setCancelable(false);
        progressBar.show();
*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                prefs.edit().putBoolean("frmWeb", true).commit();

                String currentUrl = view.getUrl();
                boolean Islogin = prefs.getBoolean("Islogin", false); // get value of last login status
                if (!Islogin) {
                    Intent i1 = new Intent(getActivity(), Login.class);
                    Bundle b = new Bundle();
                    b.putString("class", "BattDetailsFragment");
                    b.putString("CurrentUrl", currentUrl);
                    //b.putString("battery_selected", "Li-V");
                    i1.putExtras(b);
                    Toast.makeText(getActivity(), "Please sign in first", Toast.LENGTH_LONG).show();
                    startActivity(i1);
                } else {
                    //String currentUrl = view.getUrl();
                    Log.w(TAG, "Current URL is " +currentUrl);
                    Intent i = new Intent(getActivity(), OrderBatt.class);
                    Bundle b = new Bundle();
                    //String nameOfBatt="Li-V";
                    b.putString("CurrentUrl", currentUrl);
                    //b.putString("nameOfBatt",nameOfBatt);
                    i.putExtras(b);
                    startActivity(i);
                    //getActivity().finish();
                }
            }
        });
        return rootView ;
    }

    private String GetUrl(int pos) {
        switch (pos){
            case 0 : urltoLoad = "https://vmechatronics.com/lithium-ion-battery-lirack.php";
            break;
            case 1 : urltoLoad = "https://vmechatronics.com/lithium-ion-battery-joulie.php";
                break;
            case 2 : urltoLoad = "https://vmechatronics.com/lithium-ion-battery-lirackeco.php";
                break;
            case 3 : urltoLoad = "https://vmechatronics.com/lithium-ion-battery-joulieplus.php";
                break;
            case 4 : urltoLoad = "https://vmechatronics.com/lithium-ion-battery-li-v.php";
                break;
        }
        return urltoLoad;
    }

    public void updateArticleView(int position) {
        //Display the webview according to the position of listview of batteries and the batterylist selected.
        Log.w(TAG, "updateArticleView: Time to update a webview");

        alertDialog = new AlertDialog.Builder(getActivity()).create();
        progressBar = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        progressBar.setMessage("Loading...");
        progressBar.setCancelable(false);
        progressBar.show();

        String url = GetUrl(position);

        view = (WebView) rootView.findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.getSettings().setDomStorageEnabled(true);
        view.setWebViewClient(new SwAWebClient());
        view.loadUrl(url);

    }

    private class SwAWebClient extends WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                          return false;
                     }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return false;
        }

        public void onPageFinished(WebView view, String url) {
            Log.i(TAG, "Finished loading URL: " +url);
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, "Error: " + description);
            Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            alertDialog.setTitle("Error");
            alertDialog.setMessage(description);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            alertDialog.show();
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "BatteryWebView", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}

