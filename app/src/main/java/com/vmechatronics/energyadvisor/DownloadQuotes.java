package com.vmechatronics.energyadvisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 11/3/17.
 */
public class DownloadQuotes extends AppCompatActivity {

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;
    HashMap<String, String> user;
    private String name;
    private String email;
    String qid="";
    public String pdf_url;
    Typeface font;
    //public String filename;
    private static final String TAG = "DownloadQuotes";
    private String downloadQuotes_url = "https://www.vmechatronics.com/app/get_Filename.php";
    private String reportIssue_url =  "https://vmechatronics.com/app/reportIssue.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_quote);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvRefID = (TextView) findViewById(R.id.tvRefID);
        Button bGoHom = (Button) findViewById(R.id.bGoHom);
        Button bDowQuo = (Button) findViewById(R.id.bDowQuo);
        Button rep = (Button) findViewById(R.id.report);
        Button bOrNow = (Button) findViewById(R.id.bOrNow);

        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get("email");
        name = user.get("name");

        Intent mIntent = getIntent();
        Bundle b = mIntent.getExtras();
        qid = b.getString("qid");

        bOrNow.setTypeface(font);
        tvName.setText("Hello " +name+ ",");
        tvRefID.setText("Quote ID: " + qid);
        tv1.setText("The Quote generated can be downloaded from here.");

        bGoHom.setTypeface(font);
        bGoHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igh = new Intent(DownloadQuotes.this, Home.class);
                //igh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //igh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                igh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(igh);
                finish();
            }
        });

        bDowQuo.setTypeface(font);
        bDowQuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = ProgressDialog.show(DownloadQuotes.this, "", "Please Wait...", true);
                /*final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            Log.w(TAG, "onResponse: before jsonarray" );
                            JSONArray array = new JSONArray(response);
                            jsonResponse = array.getJSONObject(0);
                            boolean success = jsonResponse.getBoolean("success");
                            Log.i(TAG, "onResponse: success value is "+success);
                            if (success) {
                                pd.dismiss();
                                //filename = jsonResponse.getString("pdf");
                                pdf_url = "https://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                Log.i(TAG, "onResponse: "+pdf_url);
                                Uri uri = Uri.parse(pdf_url);
                                Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(i3);

                                //pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                            } else {
                                pd.dismiss();
                                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DownloadQuotes.this);
                                builder.setMessage("Please try again later, some error occured.")
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
                GetFilename getF = new GetFilename(email, qid, responseListener);
                RequestQueue queue = Volley.newRequestQueue(DownloadQuotes.this);
                queue.add(getF);*/

                StringRequest stringRequest = new StringRequest(Request.Method.POST, downloadQuotes_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    Log.w(TAG, "onResponse: before jsonarray" );
                                    JSONArray array = new JSONArray(response);
                                    JSONObject jsonResponse = array.getJSONObject(0);
                                    boolean success = jsonResponse.getBoolean("success");
                                    //Log.i(TAG, "onResponse: success value is "+success);
                                    if (success) {
                                        pd.dismiss();
                                        //filename = jsonResponse.getString("pdf");
                                        pdf_url = "https://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                        Log.i(TAG, "onResponse: "+pdf_url);
                                        Uri uri = Uri.parse(pdf_url);
                                        Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(i3);

                                        //pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                    } else {
                                        pd.dismiss();
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DownloadQuotes.this);
                                        builder.setMessage("Please try again later, some error occured.")
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
                        params.put("email", email);
                        params.put("qid",qid);
                        return params;
                    }
                };

                MySingleton.getInstance(DownloadQuotes.this).addToRequestQueue(stringRequest);

            }
        });

        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = ProgressDialog.show(DownloadQuotes.this, "", "Sending Mail...", true);
               // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DownloadQuotes.this);
               // float sizeOfBattbyUser = prefs.getFloat("sizeOfBattbyUser", 0);
               /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject jsonResponse = array.getJSONObject(0);
                            Log.w(TAG, "Mail PDF: jsonresponse" + response.toString());
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                pd.dismiss();
                                Toast.makeText(DownloadQuotes.this, "Mail sent!", Toast.LENGTH_LONG).show();
                                // pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                            } else {
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(DownloadQuotes.this);
                                builder.setMessage("Please try again later, couldn't sent mail.")
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
                ReportIssue reportIssue = new ReportIssue(pdf_url,qid,name,email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(DownloadQuotes.this);
                queue.add(reportIssue);
*/

                StringRequest stringRequest = new StringRequest(Request.Method.POST, reportIssue_url,
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
                                        Toast.makeText(DownloadQuotes.this, "Mail sent!", Toast.LENGTH_LONG).show();
                                        // pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                    } else {
                                        pd.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DownloadQuotes.this);
                                        builder.setMessage("Please try again later, couldn't sent mail.")
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
                        params.put("pdf",pdf_url);
                        params.put("qid", qid);
                        params.put("name",name);
                        params.put("email", email);
                        return params;
                    }
                };
                MySingleton.getInstance(DownloadQuotes.this).addToRequestQueue(stringRequest);

            }
        });
    }
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Download_Quotes", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}