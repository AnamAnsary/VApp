package com.vmechatronics.energyadvisor;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Book extends Activity {

    private static final String TAG = "Distance";
    private FirebaseAnalytics mFirebaseAnalytics;
    ImageButton bAddr;
    Button bVLState;

    EditText etBuildNo;
    EditText etLandmark;
    EditText etCity;
    EditText etPincode;

    String sid = "";
    String dname = "";
    String qid = "";
    String qdate = "";
    String buildNo = "";
    String landMark = "";
    String city = "";
    String pinCode = "";
    String state = "";
    String srcPin = "";
    String dAddr = "";
    int val = 0;

    ArrayList<String> arrayState;
    private String srcState_url =  "https://vmechatronics.com/app/src_state.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        qid = this.getIntent().getStringExtra("qid");
        qdate = this.getIntent().getStringExtra("qdate");

        bAddr = (ImageButton)findViewById(R.id.bAddr);
        bVLState = (Button)findViewById(R.id.bVLState);
        etBuildNo = (EditText)findViewById(R.id.etBuildNo);
        etCity = (EditText)findViewById(R.id.etCity);
        etPincode = (EditText)findViewById(R.id.etPincode);
        etLandmark = (EditText) findViewById(R.id.etLandmark);

        arrayState = new ArrayList<String>();
        arrayState.add("Maharashtra");
        arrayState.add("NewDelhi");
        arrayState.add("MadhyaPradesh");
        arrayState.add("Odisha");
        arrayState.add("TamilNadu");

        bVLState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Book.this, R.layout.spinner_row, arrayState);
                new AlertDialog.Builder(Book.this).setTitle("Select State").setAdapter(adapter, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bVLState.setText(arrayState.get(i));
                        state = arrayState.get(i).toString();

                    }
                }).create().show();
            }
        });

        bAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildNo = etBuildNo.getText().toString();
                landMark = etLandmark.getText().toString();
                city = etCity.getText().toString();
                pinCode = etPincode.getText().toString().trim();
                Log.w(TAG, "onClick: " + pinCode);

                if(buildNo.length()>0 && landMark.length()>0 && city.length()>0 && state.length()>0 && pinCode.length() >0){
                    if(checkInternetConnection()) {
                             /*   final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject jsonResponse = null;
                                        try {
                                            JSONArray array = new JSONArray(response);
                                            jsonResponse = array.getJSONObject(0);
                                            Log.w(TAG, "onResponse: after try");
                                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                            boolean success = jsonResponse.getBoolean("success");
                                            Log.w(TAG, "onResponse: jsonresponse" + success);
                                            if (success) {
                                                sid = jsonResponse.getString("sid");
                                                srcPin = jsonResponse.getString("srcPin");
                                                dAddr = jsonResponse.getString("addr");
                                                dname = jsonResponse.getString("dname");
                                                Log.w(TAG, "onResponse: " + srcPin);
                                            } else {
                                                Log.w(TAG, "onResponse: failing in getSource pin");
                                                srcPin = "400601";
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                SourceState ss = new SourceState(state, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Book.this);
                                queue.add(ss);*/

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, srcState_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONArray array = new JSONArray(response);
                                            JSONObject jsonResponse = array.getJSONObject(0);
                                            Log.w(TAG, "onResponse: after try");
                                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                            boolean success = jsonResponse.getBoolean("success");
                                            Log.w(TAG, "onResponse: jsonresponse" + success);
                                            if (success) {
                                                sid = jsonResponse.getString("sid");
                                                srcPin = jsonResponse.getString("srcPin");
                                                dAddr = jsonResponse.getString("addr");
                                                dname = jsonResponse.getString("dname");
                                                Log.w(TAG, "onResponse: " + srcPin);
                                            } else {
                                                Log.w(TAG, "onResponse: failing in getSource pin");
                                                srcPin = "400601";
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
                                params.put("state", state);
                                return params;
                            }
                        };

                        MySingleton.getInstance(Book.this).addToRequestQueue(stringRequest);

                        Log.w(TAG, "Source PIN" + srcPin);
                                if(srcPin.length()>0) {
                                    String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + state + "+" + srcPin + "&destinations=" + state + "+" + pinCode + "&key=AIzaSyDs716QJj6ArKkeF62oOvFetZTshoZQM2E";
                                    getDist(url);
                                }else {
                                    Toast.makeText(Book.this, "Please check Your PIN once again!!", Toast.LENGTH_LONG).show();
                                }
                    }
                    else{
                        Snackbar snackbar = Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);

                        snackbar.show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Book.this);
                    builder.setMessage("Please Fill in All Details")
                            .setNegativeButton("OK", null)
                            .setTitle("Invalid Inputs")
                            .create()
                            .show();
                }
            }
        });
    }


    public void getDist(String url){

        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Calling the method drawPath to draw the path
                        Log.w(TAG, "onResponse: " + response.toString() );
                        try {
                            JSONObject json1 = new JSONObject(response);
                            JSONArray rows = json1.getJSONArray("rows");
                            JSONObject json2 = rows.getJSONObject(0);
                            JSONArray ele = json2.getJSONArray("elements");
                            JSONObject json3 = ele.getJSONObject(0);
                            JSONObject dist = json3.getJSONObject("distance");
                            val = dist.getInt("value");
                            Log.w(TAG, "Distance " + (val/1000.0) );
                            Intent ii= new Intent(Book.this, Pay.class);
                            ii.putExtra("sid",sid);
                            ii.putExtra("qid",qid);
                            ii.putExtra("qdate",qdate);
                            ii.putExtra("distance",val/1000+"");
                            ii.putExtra("cAdd",buildNo+" "+landMark+" "+city+" "+state+" " +pinCode);
                            ii.putExtra("dAdd",dname +"\n" + dAddr);
                            startActivity(ii);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(Book.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Book", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
