package com.vmechatronics.energyadvisor;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

public class PayUMoney extends Activity {

    private static final String TAG = "PayUMoney";
    WebView webviewPayment;

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TEST_KEY = "JiAPvBzb";
    private static final String TEST_SALT = "hXSJQh7fIQ";

    private static final String MAIN_KEY = "KhuABzZW";
    private static final String MAIN_SALT = "099hGwzuj3";

    String emailI = "";
    String nameI = "";
    String phoneI = "";
    String amountI = "";
    String productI = "";
    String txid = "";
    String qid = "";
    String qdate = "";
    String tstat = "";
    String ttype = "";
    String addr = "";
    String sid = "";

    HashMap<String,String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_uweb);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session     = new UserSessionManager(getApplicationContext());
        user        = session.getUserDetails();

        emailI      = user.get("email");
        phoneI      = user.get("phone");
        nameI       = user.get("name");

        amountI     = this.getIntent().getStringExtra("amount");
        qid         = this.getIntent().getStringExtra("qid");
        qdate       = this.getIntent().getStringExtra("qdate");
        addr        = this.getIntent().getStringExtra("addr");
        sid         = this.getIntent().getStringExtra("sid");
        productI    = this.getIntent().getStringExtra("prod");

        if(productI.equals("Site Visit")){
            ttype = "0";
        }else {
            ttype = "1";
        }

        webviewPayment = (WebView) findViewById(R.id.webView1);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);
        webviewPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webviewPayment.getSettings().setSupportMultipleWindows(true);
        webviewPayment.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webviewPayment.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");

        StringBuilder url_s = new StringBuilder();
        url_s.append("https://secure.payu.in/_payment");

        Log.e(TAG, "call url " + url_s);

        webviewPayment.postUrl(url_s.toString(),EncodingUtils.getBytes(getPostString(), "utf-8"));

        webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                Log.e("Error", "Exception caught!");
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private final class PayUJavaScriptInterface {
        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success( long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(PayUMoney.this, "Payment is Successful", Toast.LENGTH_LONG).show();
                    tstat = "1";
                    Log.w(TAG, "Insert Trans: emailI " + emailI + ", phoneI " + phoneI+", amountI "+amountI + ", txid "+txid+", qid "+qid+", qdate " +qdate+", tstat "+tstat+", ttype "+ttype+", addr "+addr+", sid "+sid );
                    final ProgressDialog pd = ProgressDialog.show(PayUMoney.this, "", "Your Payment was Successful, redirecting to Vision Solar Calculator, please wait...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonResponse = null;
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        jsonResponse = array.getJSONObject(0);
                                        Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                        boolean success = jsonResponse.getBoolean("success");
                                        Log.w(TAG, "onResponse: jsonresponse" + success);
                                        if (success) {
                                            Intent ii = new Intent(PayUMoney.this, AfterTrans.class);
                                            ii.putExtra("qid",qid);
                                            ii.putExtra("amount",amountI);
                                            ii.putExtra("status", tstat);
                                            ii.putExtra("txid", txid);
                                            startActivity(ii);
                                            pd.dismiss();
                                        } else {
                                            pd.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(PayUMoney.this);
                                            builder.setMessage("Sorry some error occured!")
                                                    .setNegativeButton("Retry", null)
                                                    .setTitle("Invalid Inputs")
                                                    .create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            InsertTrans insT = new InsertTrans(emailI, phoneI, amountI, txid, qid, qdate, tstat, ttype, addr, sid, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(PayUMoney.this);
                            queue.add(insT);
                        }
                    }).start();

                }
            });
        }



        @JavascriptInterface
        public void failure (long id, final String paymentId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(PayUMoney.this, "Payment Failed", Toast.LENGTH_LONG).show();
                    tstat = "0";
                    Log.w(TAG, "Insert Trans: emailI " + emailI + ", phoneI " + phoneI+", amountI "+amountI + ", txid "+txid+", qid "+qid+", qdate " +qdate+", tstat "+tstat+", ttype "+ttype+", addr "+addr+", sid "+sid );
                    final ProgressDialog pd = ProgressDialog.show(PayUMoney.this, "", "Your Payment has Failed, redirecting to Vision's Energy Advisor, please wait...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonResponse = null;
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        jsonResponse = array.getJSONObject(0);
                                        Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                        boolean success = jsonResponse.getBoolean("success");
                                        Log.w(TAG, "onResponse: jsonresponse" + success);
                                        if (success) {
                                            Intent ii = new Intent(PayUMoney.this, AfterTrans.class);
                                            ii.putExtra("qid",qid);
                                            ii.putExtra("amount",amountI);
                                            ii.putExtra("status", tstat);
                                            ii.putExtra("txid", txid);
                                            startActivity(ii);
                                        } else {
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            InsertTrans insT = new InsertTrans(emailI, phoneI, amountI, txid, qid, qdate, tstat, ttype, addr, sid, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(PayUMoney.this);
                            queue.add(insT);
                        }
                    }).start();
                }
            });
        }

    }


    private String getPostString()
    {
        String key = MAIN_KEY;
        String salt = MAIN_SALT;
        DateFormat yf = new SimpleDateFormat("yy");
        DateFormat df = new SimpleDateFormat("dd");
        DateFormat mf = new SimpleDateFormat("MM");
        DateFormat hf = new SimpleDateFormat("hh");
        DateFormat nf = new SimpleDateFormat("mm");
        DateFormat sf = new SimpleDateFormat("ss");

        Date date = new Date();

        String yy = yf.format(date);
        String dd = df.format(date);
        String mm = mf.format(date);
        String hh = hf.format(date);
        String nn = nf.format(date);
        String ss = sf.format(date);
        txid = "A"+hh+yy+nn+"P"+mm+dd+ss+"P";

        String amount = amountI;
        String firstname = nameI;
        String email = emailI;
        String phone = phoneI;
        String productInfo = productI;
        String txnid = txid;

        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append(phone);
        post.append("&");
        post.append("surl=");
        post.append("https://www.payumoney.com/mobileapp/payumoney/success.php");
        post.append("&");
        post.append("furl=");
        post.append("https://www.payumoney.com/mobileapp/payumoney/failure.php");
        post.append("&");

        StringBuilder checkSumStr = new StringBuilder();
		/* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
        MessageDigest digest=null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        post.append("service_provider=");
        post.append("payu_paisa");
        return post.toString();
    }

    private JSONObject getProductInfo()
    {
        try {
            JSONObject productInfo = new JSONObject();
            JSONObject jsonPaymentPart = new JSONObject();
            jsonPaymentPart.put("name", "TapFood");
            jsonPaymentPart.put("description", "Lunchcombo");
            jsonPaymentPart.put("value", "500");
            jsonPaymentPart.put("isRequired", "true");
            jsonPaymentPart.put("settlementEvent", "EmailConfirmation");
            JSONArray jsonPaymentPartsArr = new JSONArray();
            jsonPaymentPartsArr.put(jsonPaymentPart);
            JSONObject jsonPaymentIdent = new JSONObject();
            jsonPaymentIdent.put("field", "CompletionDate");
            jsonPaymentIdent.put("value", "31/10/2012");
            JSONArray jsonPaymentIdentArr = new JSONArray();
            jsonPaymentIdentArr.put(jsonPaymentIdent);
            productInfo.put("paymentParts", jsonPaymentPartsArr);
            productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

            Log.e(TAG, "product Info = " + productInfo.toString());
            return productInfo;


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "PayUMoney", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
