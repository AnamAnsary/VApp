package com.vmechatronics.energyadvisor;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class EditProf extends Activity {

    private static final String TAG = "EditProfile";

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;
    HashMap<String, String> userDetail;
    Typeface font;

    EditText etEPName;
    EditText etEPEmail;
    EditText etEPDOB;
    EditText etEPPhone;
    Button etEPState;
    Button bEPSave;

    RadioGroup rgSex;
    RadioButton rbMal;
    RadioButton rbFem;
    RadioButton rbOth;

    TextView tvChngPass;


    String date = "";
    String email = "";
    String phone = "";
    String name = "";
    String dob = "";
    String sex = "";
    //String verify="";
    String ch = "";
    String state="";

    String phone1 = "";
    String name1 = "";
    String dob1 = "";
    String sex1 = "";
    String otp = "";
    String state1="";
    String pass="";
    String password;

    private String[] arrayStates;
    private View view;

    private String getdetails_url = "https://vmechatronics.com/app/get_details.php";
    private String updatedetails_url ="https://vmechatronics.com/app/upd_detail.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_prof);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());
        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        if(!session.isUserLoggedIn()){
            Intent i = new Intent(EditProf.this, Login.class);
            i.putExtra("class","EditProf");
            startActivity(i);
            finish();
        }

        userDetail = session.getUserDetails();
        name = userDetail.get("name");
        email = userDetail.get("email");
        phone = userDetail.get("phone");
        //verify =userDetail.get("verify");
        state = userDetail.get("state");
        pass = userDetail.get("password");
        Log.w(TAG, "onCreate: " + email + phone);

        etEPDOB = (EditText)findViewById(R.id.etEPDOB);
        etEPEmail = (EditText)findViewById(R.id.etEPEmail);
        etEPName = (EditText)findViewById(R.id.etEPName);
        etEPPhone = (EditText)findViewById(R.id.etEPPhone);
        etEPState = (Button)findViewById(R.id.etEPState);
        view = (View) findViewById(R.id.view);

        rgSex = (RadioGroup)findViewById(R.id.rgSex);
        rbMal = (RadioButton)findViewById(R.id.rbMal);
        rbFem = (RadioButton)findViewById(R.id.rbFem);
        rbOth = (RadioButton)findViewById(R.id.rbOth);

        tvChngPass = (TextView)findViewById(R.id.tvChngPass);

        bEPSave = (Button)findViewById(R.id.bEPSave);
        bEPSave.setTypeface(font);

        arrayStates = new String[]{
                "Andhra Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","" +
                "Odisha (Orissa)","Punjab","Rajasthan","Tamil Nadu","Telangana","Uttar Pradesh","Uttarakhand","West Bengal","Andaman and Nicobar Islands","Chandigarh",
                "Dadra and Nagar Haveli","Daman and Diu","Lakshadweep","Delhi - National Capital Territory","Puducherry (Pondicherry)"
        };

        etEPState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProf.this, R.layout.spinner_row, arrayStates);
                new AlertDialog.Builder(EditProf.this).setTitle("Select State").setAdapter(adapter, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etEPState.setText(arrayStates[i]);
                        state1 = arrayStates[i];
                    }
                }).create().show();
            }
        });


        if(checkInternetConnection()) {
            final ProgressDialog pd = ProgressDialog.show(EditProf.this, "", "Connecting...", true);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProf.this);
            Boolean IsGAcc = prefs.getBoolean("IsGp", false);

            if (IsGAcc == true) {
                tvChngPass.setVisibility(View.INVISIBLE);
            }

  /*          if (IsGAcc == true) {
                etEPName.setText(name);
                etEPEmail.setText(email);
                etEPPhone.setText(phone);
                pd.dismiss();
            }
            else { */

  //start of code
                /*new Thread(new Runnable() {
                    public void run() {
                        final Response.Listener<String> responseListener = new Response.Listener<String>() {
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
                                        pd.dismiss();
                                        name = jsonResponse.getString("name");
                                        phone = jsonResponse.getString("phone");
                                        dob = jsonResponse.getString("dob");
                                        sex = jsonResponse.getString("sex");
                                        email = jsonResponse.getString("email");
                                        state = jsonResponse.getString("state");//---A- set json response

                                        etEPName.setText(name);
                                        etEPEmail.setText(email);
                                        etEPState.setText(state);
                                        if (dob.equals("null")) {
                                            etEPDOB.setText("");
                                        } else {
                                            etEPDOB.setText(dob);
                                        }
                                        etEPPhone.setText(phone);

                                        switch (sex) {
                                            case "1":
                                                rbMal.setChecked(true);
                                                break;
                                            case "2":
                                                rbFem.setChecked(true);
                                                break;
                                            case "3":
                                                rbOth.setChecked(true);
                                                break;
                                        }
                                    } else {
                                        pd.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                                        builder.setMessage("Sorry some error occurred, Please try again later")
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
                        GetUDetails gud = new GetUDetails(email, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(EditProf.this);
                        queue.add(gud);
                        Log.w(TAG, "onClick: Que added");
                    }
                }).start();*/
            // End of code

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getdetails_url,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray array = new JSONArray(response);
                        JSONObject jsonObject = array.getJSONObject(0);
                        Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            pd.dismiss();
                            name = jsonObject.getString("name");
                            phone = jsonObject.getString("phone");
                            dob = jsonObject.getString("dob");
                            sex = jsonObject.getString("sex");
                            email = jsonObject.getString("email");
                            state = jsonObject.getString("state");//---A- set json response

                            etEPName.setText(name);
                            etEPEmail.setText(email);
                            etEPState.setText(state);
                            if (dob.equals("null")) {
                                etEPDOB.setText("");
                            } else {
                                etEPDOB.setText(dob);
                            }
                            etEPPhone.setText(phone);

                            switch (sex) {
                                case "1":
                                    rbMal.setChecked(true);
                                    break;
                                case "2":
                                    rbFem.setChecked(true);
                                    break;
                                case "3":
                                    rbOth.setChecked(true);
                                    break;
                            }
                        } else {
                            pd.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                            builder.setMessage("Sorry some error occurred, Please try again later")
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
                    Log.w(TAG, "onErrorResponse: Error occurred" );
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError{
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    Log.w(TAG, "Email sent to server is " + email);
                    return params;
                }
            };

            MySingleton.getInstance(EditProf.this).addToRequestQueue(stringRequest);
          // }
        }
        else{
            Snackbar snackbar = Snackbar.make(view,"No Internet connection",Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }


        etEPDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        etEPEmail.setEnabled(false);
        etEPName.setClickable(false);

        bEPSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name1 = etEPName.getText().toString();
                dob1 = etEPDOB.getText().toString();
                sex1  = ch;
                email=etEPEmail.getText().toString();//-----A--get the  email text
                phone1 = etEPPhone.getText().toString();
                state1 =etEPState.getText().toString();

                Log.w(TAG, "" + name1 + dob1 +"aas"+ sex1 +  phone1 + email +state1);
                if(!sex.equals(sex1) || !name.equals(name1) || !dob.equals(dob1) || !state.equals(state1)){
                    if(sex1.length() > 0 && dob1.length() > 0 && name1.length() > 0) {
                        if (checkInternetConnection()) {
                            final ProgressDialog pd = ProgressDialog.show(EditProf.this, "", "Connecting...", true);

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProf.this);
                            Boolean IsGAcc = prefs.getBoolean("IsGp", false);
                            prefs.edit().putBoolean("IsEditPro", true).commit();

                            if (IsGAcc == true) {
                                //Start old code
                                        /*final Response.Listener<String> responseListener = new Response.Listener<String>() {
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
                                                        if (!phone.equals(phone1)) {
                                                            Bundle b2 = new Bundle();
                                                            b2.putString("phone", phone1);
                                                            b2.putString("email", email);
                                                            b2.putString("name", name1);
                                                            Log.w(TAG, "new phone1 is " + phone1);

                                                            Intent intent = new Intent(EditProf.this, OTP.class);
                                                            intent.putExtras(b2);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Your Profile has been updated...",
                                                                    Toast.LENGTH_LONG).show();
                                                            Intent ii = new Intent(EditProf.this, Profile.class);
                                                            startActivity(ii);
                                                            finish();
                                                            pd.dismiss();
                                                        }
                                                    } else {
                                                        pd.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                                                        builder.setMessage("Sorry some error occurred, Please try again later")
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
                                        UpdateDetails upd = new UpdateDetails(email, name1, dob1, sex1,state1, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(EditProf.this);
                                        queue.add(upd);*/
                                //End of code


                                StringRequest stringRequest = new StringRequest(Request.Method.POST, updatedetails_url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    JSONObject jsonResponse = array.getJSONObject(0);
                                                    Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        if (!phone.equals(phone1)) {
                                                            Bundle b2 = new Bundle();
                                                            b2.putString("phone", phone1);
                                                            b2.putString("email", email);
                                                            b2.putString("name", name1);
                                                            Log.w(TAG, "new phone1 is " + phone1);

                                                            Intent intent = new Intent(EditProf.this, OTP.class);
                                                            intent.putExtras(b2);
                                                            startActivity(intent);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Your Profile has been updated...",
                                                                    Toast.LENGTH_LONG).show();
                                                            Intent ii = new Intent(EditProf.this, Profile.class);
                                                            startActivity(ii);
                                                            finish();
                                                            pd.dismiss();
                                                        }
                                                    } else {
                                                        pd.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                                                        builder.setMessage("Sorry some error occurred, Please try again later")
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
                                        params.put("name", name1);
                                        params.put("dob", dob1);
                                        params.put("sex", sex1);
                                        params.put("state", state1);
                                        return params;
                                    }
                                };

                                MySingleton.getInstance(EditProf.this).addToRequestQueue(stringRequest);

                            } else {

                                //Start old code
                               /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
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
                                                if (!phone.equals(phone1)) {
                                                    int randomPIN = (int) (Math.random() * 9000) + 1000;//----random number generated for 4 digit.
                                                    otp = "" + randomPIN;
                                                    otp = otp.toString().trim();//----otp is equal to random pin generated.
                                                    Log.w(TAG, "onResponse: " + otp);


                                                    MessageDigest mdEnc;
                                                    try {
                                                        mdEnc = MessageDigest.getInstance("MD5");
                                                        mdEnc.update(pass.getBytes(), 0, pass.length());
                                                        pass = new BigInteger(1, mdEnc.digest()).toString(16);
                                                        while (pass.length() < 32) {
                                                            pass = "0" + pass;
                                                        }
                                                        password = pass;
                                                        Log.w(TAG, "password md5: " + password);
                                                    } catch (NoSuchAlgorithmException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    session.createUserLoginSession(name, email, phone1, state, false, password);
                                                    String nn = userDetail.get("name");
                                                    String ee = userDetail.get("email");
                                                    String pp = userDetail.get("phone");
                                                    Log.w(TAG, nn + ee + pp);
                                                    String url = "http://bhashsms.com/api/sendmsg.php?user=visionmech&pass=vision&sender=LIRACK&phone=" + phone1 + "&text=Your%20OTP%20for%20Vision's%20Energy%20Advisor%20App%20is%20" + otp + "&priority=ndnd&stype=normal";
                                                    StringRequest stringRequest = new StringRequest(url,
                                                            new Response.Listener<String>() {
                                                                @Override
                                                                public void onResponse(String response) {
                                                                    Log.w(TAG, "onResponse: " + response.toString());
                                                                }
                                                            },
                                                            new Response.ErrorListener() {
                                                                @Override
                                                                public void onErrorResponse(VolleyError error) {
                                                                }
                                                            });
                                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                            30000,
                                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                    //Adding the request to request queue
                                                    RequestQueue requestQueue = Volley.newRequestQueue(EditProf.this);
                                                    requestQueue.add(stringRequest);

                                                    Intent ii = new Intent(EditProf.this, OTP.class);
                                                    ii.putExtra("phone", phone1);
                                                    ii.putExtra("name", name1);
                                                    ii.putExtra("email", email);
                                                    ii.putExtra("otp", otp);

                                                    Toast.makeText(EditProf.this, "Enter the OTP send to your Registered Phone Number to verify!!", Toast.LENGTH_LONG).show();

                                                    pd.dismiss();

                                                    startActivity(ii);
                                                } else {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Your Profile has been updated...",
                                                            Toast.LENGTH_LONG).show();
                                                    Intent ii = new Intent(EditProf.this, Profile.class);
                                                    startActivity(ii);
                                                    finish();
                                                    pd.dismiss();
                                                }
                                            } else {
                                                pd.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                                                builder.setMessage("Sorry some error occurred, Please try again later")
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
                                UpdateDetails upd = new UpdateDetails(email, name1, dob1, sex1, state1, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(EditProf.this);
                                queue.add(upd);
                            }*/
                                // End old code

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, updatedetails_url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    JSONObject jsonResponse = array.getJSONObject(0);
                                                    Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                                    boolean success = jsonResponse.getBoolean("success");
                                                    if (success) {
                                                        if (!phone.equals(phone1)) {
                                                            int randomPIN = (int) (Math.random() * 9000) + 1000;//----random number generated for 4 digit.
                                                            otp = "" + randomPIN;
                                                            otp = otp.toString().trim();//----otp is equal to random pin generated.
                                                            Log.w(TAG, "onResponse: " + otp);

                                                            MessageDigest mdEnc;
                                                            try {
                                                                mdEnc = MessageDigest.getInstance("MD5");
                                                                mdEnc.update(pass.getBytes(), 0, pass.length());
                                                                pass = new BigInteger(1, mdEnc.digest()).toString(16);
                                                                while (pass.length() < 32) {
                                                                    pass = "0" + pass;
                                                                }
                                                                password = pass;
                                                                Log.w(TAG, "password md5: " + password);
                                                            } catch (NoSuchAlgorithmException e1) {
                                                                e1.printStackTrace();
                                                            }

                                                            session.createUserLoginSession(name, email, phone1, state, false, password);
                                                            String nn = userDetail.get("name");
                                                            String ee = userDetail.get("email");
                                                            String pp = userDetail.get("phone");
                                                            Log.w(TAG, nn + ee + pp);
                                                            String url = "http://bhashsms.com/api/sendmsg.php?user=visionmech&pass=vision&sender=LIRACK&phone=" + phone1 + "&text=Your%20OTP%20for%20Vision's%20Energy%20Advisor%20App%20is%20" + otp + "&priority=ndnd&stype=normal";
                                                            StringRequest stringRequest = new StringRequest(url,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            Log.w(TAG, "onResponse: " + response.toString());
                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                        }
                                                                    });
                                                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                                    30000,
                                                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                            //Adding the request to request queue
                                                            RequestQueue requestQueue = Volley.newRequestQueue(EditProf.this);
                                                            requestQueue.add(stringRequest);

                                                            Intent ii = new Intent(EditProf.this, OTP.class);
                                                            ii.putExtra("phone", phone1);
                                                            ii.putExtra("name", name1);
                                                            ii.putExtra("email", email);
                                                            ii.putExtra("otp", otp);

                                                            Toast.makeText(EditProf.this, "Enter the OTP send to your Registered Phone Number to verify!!", Toast.LENGTH_LONG).show();
                                                            pd.dismiss();
                                                            startActivity(ii);
                                                        } else {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Your Profile has been updated...",
                                                                    Toast.LENGTH_LONG).show();
                                                            Intent ii = new Intent(EditProf.this, Profile.class);
                                                            startActivity(ii);
                                                            finish();
                                                            pd.dismiss();
                                                        }
                                                    } else {
                                                        pd.dismiss();
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                                                        builder.setMessage("Sorry some error occurred, Please try again later")
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
                                        error.printStackTrace();
                                        Log.w(TAG, "onErrorResponse: Error occurred");
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("email", email);
                                        params.put("name", name1);
                                        params.put("dob", dob1);
                                        params.put("sex", sex1);
                                        params.put("state", state1);
                                        return params;
                                    }
                                };

                                MySingleton.getInstance(EditProf.this).addToRequestQueue(stringRequest);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                        builder.setMessage("No Fields can be Left Empty, Please fill in all details..")
                                .setNegativeButton("OK", null)
                                .setTitle("Invalid Inputs")
                                .create()
                                .show();
                    }

                }
                else if(!phone1.equals(phone)){
                    if(phone1.length()==10){

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProf.this);
                        Boolean IsGAcc = prefs.getBoolean("IsGp", false);
                        prefs.edit().putBoolean("IsEditPro", true).commit();
                        if (IsGAcc == true) {

                            Bundle b2 = new Bundle();
                            b2.putString("phone", phone1);
                            b2.putString("email",email);
                            b2.putString("name",name);
                            Intent intent = new Intent(EditProf.this, OTP.class);
                            intent.putExtras(b2);
                            startActivity(intent);
                        }

                        else {
                            //SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(EditProf.this);

                            int randomPIN = (int) (Math.random() * 9000) + 1000;// random pin generated
                            otp = "" + randomPIN;
                            Log.w(TAG, "onResponse: " + otp);
                            String url =  "http://bhashsms.com/api/sendmsg.php?user=visionmech&pass=vision&sender=LIRACK&phone=" + phone1 + "&text=Your%20OTP%20for%20Vision's%20Energy%20Advisor%20App%20is%20" + otp + "&priority=ndnd&stype=normal";
                            StringRequest stringRequest = new StringRequest(url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.w(TAG, "onResponse: " + response.toString());
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                        }
                                    });
                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                    30000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            //Adding the request to request queue
                            RequestQueue requestQueue = Volley.newRequestQueue(EditProf.this);
                            requestQueue.add(stringRequest);

                           /* MessageDigest mdEnc;
                            try {
                                mdEnc = MessageDigest.getInstance("MD5");
                                mdEnc.update(pass.getBytes(), 0, pass.length());
                                pass = new BigInteger(1, mdEnc.digest()).toString(16);
                                while (pass.length() < 32) {
                                    pass = "0" + pass;
                                }
                                password = pass;
                                Log.w(TAG, "password md5: " + password);
                            } catch (NoSuchAlgorithmException e1) {
                                e1.printStackTrace();
                            }
                            session.createUserLoginSession(name, email, phone1,state, false,password);*/
                            Intent ii = new Intent(EditProf.this, OTP.class);
                            ii.putExtra("phone", phone1);
                            ii.putExtra("otp", otp);
                            Toast.makeText(EditProf.this, "Enter the OTP send to your Registered Phone Number to verify!!", Toast.LENGTH_LONG).show();
                            startActivity(ii);
                        }
                    }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProf.this);
                    builder.setMessage("Phone Number should be 10 Digits")
                            .setNegativeButton("OK", null)
                            .setTitle("Invalid Inputs")
                            .create()
                            .show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "No Changes made..",
                            Toast.LENGTH_LONG).show();
                    Intent ii = new Intent(EditProf.this, Profile.class);
                    startActivity(ii);
                    finish();
                }
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rbMal)
                {
                    ch = "1";
                }else if(checkedId == R.id.rbFem)
                {
                    ch = "2";
                }else if(checkedId == R.id.rbOth){
                    ch = "3";
                }
            }
        });

        tvChngPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ii = new Intent(EditProf.this, ChangePassword.class);
                startActivity(ii);
            }
        });

    }
    protected Dialog onCreateDialog(int id)
    {
        if(id == 999)
        {
            return new DatePickerDialog(this,myDateListener, 1970, 0, 1);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date = "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
            etEPDOB.setText(date);
            Log.w(TAG, "onDateSet: date: " + date);
        }
    };

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(EditProf.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void onBackPressed () {
        super.onBackPressed();
        finish();
    }
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "EditProfile", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
