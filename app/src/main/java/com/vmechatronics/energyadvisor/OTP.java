package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class OTP extends Activity {
    private static final String TAG = "OTP";
    Button bOTP;
    EditText etOTP;

    UserSessionManager session;
    HashMap<String, String> user;
    private FirebaseAnalytics mFirebaseAnalytics;
    Typeface font;

    String gPlusId="";
    String gname="";

    String otp = "";
    String eotpR = "";// --A--for mail otp
    String otpR = "";
    String email;
    String pho;
    String name;
    String state;
    String Gotp="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.eotp);
        setContentView(R.layout.activity_otp);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        bOTP = (Button) findViewById(R.id.bOTP);
        bOTP.setTypeface(font);
        etOTP = (EditText) findViewById(R.id.etOTP);

        otpR = this.getIntent().getStringExtra("otp");
        Log.w(TAG, "OTPr is " + otpR);

        eotpR = this.getIntent().getStringExtra("eotp");
        email = this.getIntent().getStringExtra("email");
        name = this.getIntent().getStringExtra("name");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OTP.this);
        Boolean IsGAcc = prefs.getBoolean("IsGp", false);
        if (IsGAcc == true) {

            Intent in = OTP.this.getIntent();
            Bundle b = in.getExtras();
            pho = b.getString("phone");
            gname = b.getString("name");
            gPlusId = b.getString("email");
            state = b.getString("state");
            // email = this.getIntent().getStringExtra("email");
           // name = this.getIntent().getStringExtra("name");
            int randomPIN = (int) (Math.random() * 9000) + 1000;
            otp = "" + randomPIN;
            otp = otp.toString().trim();
            Log.w(TAG, "OTP is " + otp);
            String url = "http://bhashsms.com/api/sendmsg.php?user=visionmech&pass=vision&sender=LIRACK&phone=" + pho + "&text=Your%20OTP%20for%20Vision's%20Energy%20Advisor%20App%20is%20" + otp + "&priority=ndnd&stype=normal";
            // "http://vmechatronics.com/eotp";
            StringRequest stringRequest = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Calling the method drawPath to draw the path
                            Log.w(TAG, "onResponse: " + response.toString() );

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue requestQueue = Volley.newRequestQueue(OTP.this);
            requestQueue.add(stringRequest);

            Toast.makeText(OTP.this, "Enter the OTP sent to your Registered Phone Number to verify!!", Toast.LENGTH_LONG).show();

            bOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gotp = etOTP.getText().toString();
                    if (otp.equals(Gotp)) {//---A-check phone otp
                        if (checkInternetConnection()) {

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OTP.this);
                            Boolean frmEditPro = prefs.getBoolean("IsEditPro", false);

                            if (frmEditPro == true) {

                                final Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject jsonResponse1 = null;
                                        try {
                                            JSONArray array = new JSONArray(response);
                                            jsonResponse1 = array.getJSONObject(0);
                                            Log.w(TAG, "onResponse: jsonresponse1" + response.toString());
                                            boolean success = jsonResponse1.getBoolean("success");

                                            if (success) {
                                                //session.updateVerify();
                                                Toast.makeText(OTP.this, "Your Mobile Number has been Verified!!", Toast.LENGTH_LONG).show();
                                                Intent ii = new Intent(OTP.this, Profile.class);
                                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(ii);
                                                finish();
                                            }
                                            /*else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                                builder.setMessage("Some Error Occurred, please try again later.")
                                                        .setNegativeButton("OK", null)
                                                        .setTitle("Error")
                                                        .create()
                                                        .show();
                                            }*/

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                UpdatePhone upd = new UpdatePhone(email, pho, responseListener1);
                                RequestQueue queue = Volley.newRequestQueue(OTP.this);
                                queue.add(upd);
                                Log.w(TAG, "onClick: " + email + " " + pho);
                            }

                            else {
                                final Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject jsonResponse1 = null;
                                        try {
                                            JSONArray array = new JSONArray(response);
                                            jsonResponse1 = array.getJSONObject(0);
                                            Log.w(TAG, "onResponse of Gsignin: jsonresponse1" + response.toString());
                                            String success = jsonResponse1.getString("success");
                                            Log.w(TAG, "Value of success" + success);

                                            switch (success) {
                                                case "true":
                                                    session = new UserSessionManager(getApplicationContext());
                                                    user = session.getUserDetails();
                                                    session.createGLoginSession(name, email, pho, state);
                                                    session.updateVerify();
                                                    Toast.makeText(OTP.this, "Your Mobile Number has been Verified!!", Toast.LENGTH_LONG).show();
                                                    Intent i1 = new Intent(OTP.this, Profile.class);
                                                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(i1);
                                                    finish();
                                                    break;
                                            /*else if (success.equals("e_exists")) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                                builder.setMessage("This email id already exists")
                                                        .setNegativeButton("OK", null)
                                                        .setTitle("Signup Error")
                                                        .create()
                                                        .show();
                                            }*/
                                                case "p_exists": {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                                    builder.setMessage("This Phone Number is already Registered")
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Signup Error")
                                                            .create()
                                                            .show();
                                                    break;
                                                }
                                                default: {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                                    builder.setMessage("Some Error Occurred, please try again later.")
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Error")
                                                            .create()
                                                            .show();
                                                    break;
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                GSignupRequest gsignupRequest = new GSignupRequest(name, email, pho, state, responseListener1);
                                RequestQueue queue = Volley.newRequestQueue(OTP.this);
                                queue.add(gsignupRequest);
                                Log.w(TAG, "onClick: " + email + " " + pho);
                            }
                        }
                    }
                    else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                            builder.setMessage("OTP is Wrong, Please re-enter !")
                                    .setNegativeButton("OK", null)
                                    .setTitle("Error")
                                    .create()
                                    .show();
                        }
                    }
                });

            //pho = this.getIntent().getStringExtra("phone");
        } else {

            session = new UserSessionManager(getApplicationContext());
            user = session.getUserDetails();
            email = user.get("email");
            //name = this.getIntent().getStringExtra("name");
            pho = this.getIntent().getStringExtra("phone");
            //  ebOTP = (Button) findViewById(R.id.ebOTP);
            Log.w(TAG, "onCreate: " + email);

            //eOtp = (EditText) findViewById(R.id.etOTP);//--A-mail otp text

            // otp = etOTP.getText().toString();

            bOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    otp = etOTP.getText().toString();
                    Log.w(TAG, "OTP is " + otp);
                    if (otp.equals(otpR)) {//---A-check phone otp
                        if (checkInternetConnection()) {

                            final Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonResponse1 = null;
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        jsonResponse1 = array.getJSONObject(0);
                                        Log.w(TAG, "onResponse: jsonresponse1" + response.toString());
                                        boolean success = jsonResponse1.getBoolean("success");

                                        if (success) {
                                            session.updateVerify();
                                            Toast.makeText(OTP.this, "Your Mobile Number has been Verified!!", Toast.LENGTH_LONG).show();

                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OTP.this);
                                            Boolean frmEditPro = prefs.getBoolean("IsEditPro", false);

                                            if (frmEditPro == true) {
                                                Intent ii = new Intent(OTP.this, Profile.class);
                                                ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(ii);
                                                finish();
                                            }
                                            else {
                                            final Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    JSONObject jsonResponse2 = null;
                                                    try {
                                                        JSONArray array = new JSONArray(response);
                                                        jsonResponse2 = array.getJSONObject(0);
                                                        Log.w(TAG, "onResponse: jsonresponse2" + response.toString());
                                                        boolean success = jsonResponse2.getBoolean("success");

                                                        if (success) {

                                                            AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                                            builder.setMessage("A verification link has been emailed to you. Please verify your E-mail id!")
                                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            //Either of the following two lines should work.
                                                                            Intent ii = new Intent(OTP.this, Profile.class);
                                                                            ii.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                            startActivity(ii);
                                                                            dialog.cancel();
                                                                            finish();
                                                                            //dialog.dismiss();
                                                                        }
                                                                    });
                                                            builder.setTitle("Verify E-mail")
                                                                    .create()
                                                                    .show();


                                                         /*   final ProgressDialog ringProgressDialog = ProgressDialog.show(OTP.this, "", "Waiting to verify your Email...", true);
                                                            ringProgressDialog.setCancelable(false);

                                                            new Thread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    try {
                                                                        Log.w(TAG, "Before response3");

                                                                            final Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {
                                                                                    JSONObject jsonResponse3 = null;

                                                                                    try {
                                                                                        JSONArray array = new JSONArray(response);
                                                                                        jsonResponse3 = array.getJSONObject(0);

                                                                                        success3 = jsonResponse3.getBoolean("success");
                                                                                        Log.w(TAG, "Before success3");
                                                                                        if (success3) {
                                                                                            //pd.dismiss();
                                                                                            session.updateVerify();
                                                                                            Toast.makeText(OTP.this, "Your E-mail has been verified!", Toast.LENGTH_LONG).show();
                                                                                            Intent ii = new Intent(OTP.this, Profile.class);
                                                                                            ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                            Log.w(TAG, "In success3");
                                                                                            ringProgressDialog.dismiss();
                                                                                            startActivity(ii);

                                                                                        } /*else {
                                                                                            AlertDialog.Builder builder1 = null;
                                                   EM                                         builder1 = new AlertDialog.Builder(OTP.this);
                                                                                            builder1.setMessage("not verified")
                                                                                            .setNegativeButton("OK", null)
                                                                                            .setTitle("Email Verification")
                                                                                            .create()
                                                                                            .show();
                                                                                }
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            };
                                                                            EmailVariable EV = new EmailVariable(email, responseListener3);
                                                                            RequestQueue queue2 = Volley.newRequestQueue(OTP.this);
                                                                            queue2.add(EV);
                                                                            Thread.sleep(10000);
                                                                    } catch (Exception e) {

                                                                    }
                                                                }
                                                            }).start();  */
                                                        }
                                                        //else
                                                        //{

                                                        //}
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };
                                            EMotp emveri = new EMotp(email, name, responseListener2);
                                            RequestQueue queue = Volley.newRequestQueue(OTP.this);
                                            queue.add(emveri);

                                            //AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                            //builder.setMessage("A verification link has been emailed to you. Please verify your E-mail id!")
                                            //        .setNegativeButton("OK", null)
                                            //       .setTitle("Verify E-mail")
                                            //       .create()
                                            //      .show();

                                            // nextPage(everify);
                                            //setContentView(R.layout.eotp);// here add the eotp activity
                                                /*
                                                //bOTP.setOnClickListener(new View.OnClickListener() {
                                                // @Override
                                                // public void onClick(View view) {
                                             /*   if (eotp.equals(eotpR)) {
                                                    Toast.makeText(OTP.this, "YOUR mail id verified.!", Toast.LENGTH_LONG).show();
                                                    Intent ii = new Intent(OTP.this, Profile.class);//
                                                    startActivity(ii);
                                              } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                                    builder.setMessage("OTP is Wrong, Please re-enter !")
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Error")
                                                            .create()
                                                            .show();
                                                }
                                          //  }
                                       // });*/

                                        }
                                    }else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                                            builder.setMessage("Some Error Occurred, please try again later.")
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

                            UpdatePhone upd = new UpdatePhone(email, pho, responseListener1);
                            RequestQueue queue = Volley.newRequestQueue(OTP.this);
                            queue.add(upd);
                            Log.w(TAG, "onClick: " + email + " " + pho);
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG);
                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);
                            snackbar.show();
                        }


                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(OTP.this);
                        builder.setMessage("OTP is Wrong, Please re-enter !")
                                .setNegativeButton("OK", null)
                                .setTitle("Error")
                                .create()
                                .show();
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed () {
        super.onBackPressed();
        finish();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(OTP.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OTP Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.diysolar/http/host/path")
        );
        //AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OTP Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.diysolar/http/host/path")
        );
       // AppIndex.AppIndexApi.end(client, viewAction);
        //client.disconnect();
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "OTPActivity", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}