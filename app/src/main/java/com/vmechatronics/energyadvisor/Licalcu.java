package com.vmechatronics.energyadvisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 12/10/16.
 */
public class Licalcu extends AppCompatActivity {
    private String TAG = "Lithiumcalulation";
    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;
    //HashMap<String, String> userDetail;
    //float hours;
    float inputperhr;
    float r_battery;
    String unitsPm;
    //String cost = "";
    float temptotalresultsize;
    float totalresultsize;
    String sbatterysize="";
    int ind;
    String Hours;
    //String rbattery;
    String ip;
    String hr;
    float batterycost = 0;
    String bcost;
    //boolean entrance;

    String state="";
    String email;
    String phone;
    String qid = "";
    String qdate = "";
    String name ;
    String pdf_url="" ;
    ImageButton lbBkNow;
    ImageButton bGoHom;
    ImageButton lbDowQuo;
    TextView tv12;
    TextView ltvRefID;
    TextView tvName;
    private float batterycapacity;
    private GoogleApiClient client;
    private String makebattery_url = "https://vmechatronics.com/app/battery.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());
           // userDetail = session.getUserDetails();
            //name = userDetail.get("name");
            //email = userDetail.get("email");
            //phone = userDetail.get("phone");

        final HashMap<String, String> user = session.getUserDetails();
          final String name = user.get(UserSessionManager.KEY_NAME);
        // get email
          final String email = user.get(UserSessionManager.KEY_EMAIL);

        final float[][] arrbatterycapacity = new float[1][1];
        arrbatterycapacity[0] = new float[]{3.5f, 5.6f, 8.9f, 11.2f, 16.2f, 22.4f, 33.6f, 55f};// addition of li battery sizes
        Log.w(TAG, "onCreate: " + email + phone + name);

        tv12 = (TextView) findViewById(R.id.tv12);
        ltvRefID = (TextView) findViewById(R.id.ltvRefID);
        tvName = (TextView) findViewById(R.id.ltvName);
        lbBkNow = (ImageButton) findViewById(R.id.lBkNow);
        lbDowQuo = (ImageButton) findViewById(R.id.lbDowQuo);
        tvName.setText("Hello " + name + ",");
        final ProgressDialog pd = ProgressDialog.show(Licalcu.this, "", "Calculating...", true);
        pd.dismiss();
        new Thread(new Runnable() {
            public void run() {
        Bundle b = Licalcu.this.getIntent().getExtras();
        arrbatterycapacity[0] = b.getFloatArray("arrbatterycapacity");
        final String[] liarray = b.getStringArray("li");
        //  String unit=b.getString("units");
        unitsPm = liarray[0].toString();
        Hours = liarray[1];
        inputperhr = Float.parseFloat(unitsPm)/ (30 * 24);
        ip = Float.toString(inputperhr);
        Log.w(TAG, "ip" + ip);
        Log.w(TAG, "un" + unitsPm);

        r_battery = inputperhr * Float.parseFloat(Hours);
        //  hr=Float.toString(r_battery);
        Log.w(TAG, "hr" + Hours);


        totalresultsize = Math.abs(arrbatterycapacity[0][0] - r_battery);
        temptotalresultsize = 55f;
        for (int i = 0; i < arrbatterycapacity[0].length; i++) {
            temptotalresultsize = Math.abs(arrbatterycapacity[0][i] - r_battery);
            Log.w(TAG, "arrbatterysize" + arrbatterycapacity[0][i]);
            Log.w(TAG, "temp" + temptotalresultsize);
            if (temptotalresultsize <= totalresultsize)
            {
                ind = i;
                Log.w(TAG, "i" + ind);
                totalresultsize = temptotalresultsize;
            }
        }
        batterycapacity = arrbatterycapacity[0][ind];
        sbatterysize = Float.toString(batterycapacity);
        Log.w(TAG, "batterysize" + sbatterysize);
        // rbattery=Float.toString(r_battery);
        // Log.w(TAG,"rbattery"+rbattery);

       /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    JSONArray array = new JSONArray(response);
                    jsonResponse = array.getJSONObject(0);
                    Log.w(TAG, "Mail PDF: jsonresponse" + response.toString());
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                          pd.dismiss();
                        pdf_url = "https://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";

                        qid = jsonResponse.getString("qid");
                        qdate = jsonResponse.getString("qdate");
                        batterycost = Float.parseFloat(jsonResponse.getString("battery_cost"));
                        ltvRefID.setText("Quote ID: " + qid);
                        bcost=Float.toString(batterycost);
                        Log.w(TAG, "QQ" + qid + qdate+bcost);

                    } else {
                            pd.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Licalcu.this);
                        builder.setMessage("Please Try again later, some Error Occured in generating PDF")
                                .setNegativeButton("OK", null)
                                .setTitle("Error")
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Log.w(TAG, "onLive: " + email + phone + name+sbatterysize);
        Makebattery mpd = new Makebattery(name,email, state, sbatterysize, phone,responseListener);
        RequestQueue queue = Volley.newRequestQueue(Licalcu.this);
        queue.add(mpd);
        Log.w(TAG, "onClick: " + email + " " + phone+sbatterysize);
*/
                StringRequest stringRequest = new StringRequest(Request.Method.POST, makebattery_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray array = new JSONArray(response);
                                    JSONObject jsonResponse = array.getJSONObject(0);
                                    Log.w(TAG, "Mail PDF: jsonresponse" + response.toString());
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        pd.dismiss();
                                        pdf_url = "https://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";

                                        qid = jsonResponse.getString("qid");
                                        qdate = jsonResponse.getString("qdate");
                                        batterycost = Float.parseFloat(jsonResponse.getString("battery_cost"));
                                        ltvRefID.setText("Quote ID: " + qid);
                                        bcost=Float.toString(batterycost);
                                        Log.w(TAG, "QQ" + qid + qdate+bcost);

                                    } else {
                                        pd.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Licalcu.this);
                                        builder.setMessage("Please Try again later, some Error Occured in generating PDF")
                                                .setNegativeButton("OK", null)
                                                .setTitle("Error")
                                                .create()
                                                .show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.w(TAG, "onErrorResponse: Error occurred");
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name",name);
                        params.put("email", email+"");
                        params.put("state", state+"");
                        params.put("battery",sbatterysize+"");
                        params.put("phone",phone);
                        Log.w(TAG, "PDF: Called");
                        return params;
                    }
                };

                MySingleton.getInstance(Licalcu.this).addToRequestQueue(stringRequest);

            }

    }).start();
    String first = "Your Quote for the Solar Power system has been e-mailed to you and your Download is available here. Thank you for taking your First Step towards ";
    String next = "<font color='#72CD27'><b>Going Green!!</b></font>";
    tv12.setText(Html.fromHtml(first + next));


    lbBkNow = (ImageButton) findViewById(R.id.lBkNow);
    lbBkNow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i1 = new Intent(Licalcu.this, Book.class);
            i1.putExtra("qid", qid);
            i1.putExtra("qdate", qdate);
            startActivity(i1);
        }
    });

    lbDowQuo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Uri uri = Uri.parse(pdf_url);
            Intent i2 = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(i2);
        }
    });
    bGoHom = (ImageButton) findViewById(R.id.bGoHom);
    bGoHom.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent igh = new Intent(Licalcu.this, Home.class);
            startActivity(igh);
        }
    });
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
 }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Quote Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.energyadvisor/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Quote Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.energyadvisor/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "LithiumCalculation", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }

}