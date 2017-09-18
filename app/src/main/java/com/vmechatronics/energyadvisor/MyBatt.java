package com.vmechatronics.energyadvisor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by root on 25/2/17.
 */
public class MyBatt extends AppCompatActivity {
    Button b1;
    private WebView mWebView = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybatteries);

        b1 =(Button) findViewById(R.id.B1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_pay_uweb);
                mWebView = (WebView) findViewById(R.id.webView1);
                mWebView.getSettings().setJavaScriptEnabled(true);
                mWebView.getSettings().setDomStorageEnabled(true);
                mWebView.setWebViewClient(new MyBrowser());
                mWebView.loadUrl("https://vmechatronics.com/demo/monitor.php");

            }
        });

    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
