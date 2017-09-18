package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class SignUp extends Activity implements AdapterView.OnItemSelectedListener {

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = "Signup";
    EditText etName;
    EditText etMail;
    EditText etPass;
    EditText etConfPass;
    EditText etContNo;
    Button s1;
    Button bCont;

    Typeface font;
    RequestQueue requestQueue;


    private String[] arrayStates;
    String otp = "";
    String eotp="";
    String name="";
    String email="";
    String pass="";
    String phone="";
    String confPass="";
    String  eotp_url="";
    String eotpR="";
    Boolean verify = false;
    String state="";
    String password = null;
    private String signUp_url = "https://vmechatronics.com/app/Signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());
        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        etName = (EditText)findViewById(R.id.etName);
        etMail = (EditText)findViewById(R.id.etMail);
        etPass = (EditText)findViewById(R.id.etPass);
        etConfPass = (EditText)findViewById(R.id.etConfPass);
        etContNo = (EditText)findViewById(R.id.etContNo);

        bCont = (Button) findViewById(R.id.bContinue);
        bCont.setTypeface(font);

        s1 = (Button)findViewById(R.id.sState);
        arrayStates = new String[]{
                "Andhra Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","" +
                "Odisha (Orissa)","Punjab","Rajasthan","Tamil Nadu","Telangana","Uttar Pradesh","Uttarakhand","West Bengal","Andaman and Nicobar Islands","Chandigarh",
                "Dadra and Nagar Haveli","Daman and Diu","Lakshadweep","Delhi - National Capital Territory","Puducherry (Pondicherry)"
        };
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this, R.layout.spinner_row, arrayStates);
                new AlertDialog.Builder(SignUp.this).setTitle("Select State").setAdapter(adapter, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s1.setText(arrayStates[i]);
                        state = arrayStates[i];
                    }
                }).create().show();
            }
        });

       /* Spinner spinner = (Spinner) findViewById(R.id.loc);
        spinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Andhra Pradesh");
        categories.add("Assam");
        categories.add("Bihar");
        categories.add("Chhattisgarh"); categories.add("Goa"); categories.add("Gujarat"); categories.add("Haryana"); categories.add("Himachal Pradesh"); categories.add("Jharkhand"); categories.add("Karnataka");
        categories.add("Kerala"); categories.add("Madhya Pradesh"); categories.add("Maharashtra"); categories.add("Manipur"); categories.add("Meghalaya"); categories.add("Odisha (Orissa)"); categories.add("Punjab");
        categories.add("Rajasthan");categories.add("Tamil Nadu");categories.add("Telangana");categories.add("Uttar Pradesh");categories.add("Uttarakhand");categories.add("West Bengal");categories.add("ndaman and Nicobar Islands");categories.add("Chandigarh");
        categories.add("Dadra and Nagar Haveli");categories.add("Daman and Diu");categories.add("Lakshadweep");categories.add("Delhi - National Capital Territory"); categories.add("Puducherry (Pondicherry");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);*/


        bCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString().trim();
                email = etMail.getText().toString().trim();
                pass = etPass.getText().toString().trim();
                phone = etContNo.getText().toString().trim();
                confPass = etConfPass.getText().toString();


                //eotpR =.getText().getStringExtra("eotp");
                // otp = etOTP.getText().toString("");
                if(name.length()!=0 && email.length()!=0 && pass.length()!=0) {
                    if(phone.length()==10) {
                        if (confPass.equals(pass)) {
                            if(checkInternetConnection()) {
                                final ProgressDialog pd = ProgressDialog.show(SignUp.this, "", "Please Wait...", true);

                                     /*   final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                JSONObject jsonResponse = null;
                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    jsonResponse = array.getJSONObject(0);
                                                    Log.w(TAG, "onResponse: after try");
                                                    Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                                    String success = jsonResponse.getString("success");
                                                    // String eotp=jsonResponse.getString("eotp");
                                                    Log.w(TAG, "onResponse: jsonresponse" + success);
                                                    switch (success) {
                                                        case "true":
                                                            int randomPIN = (int) (Math.random() * 9000) + 1000;
                                                            otp = "" + randomPIN;
                                                            otp = otp.toString().trim();

                                                            // int randomEpin = (int) (Math.random() * 900000) + 100000;
                                                            // eotp = "" + randomEpin;
                                                            //------generating random number of email------------9/9/16
                                                            //  email = etName.getText().toString();//-----A--get the  email text
                                                            //  int randomEpin = (int) (Math.random() * 900000) + 100000;
                                                            //  eotp = "" + randomEpin;

                                                            // Uri uri = Uri.parse("http://vmechatronics.com/eotp");
                                                            //  eotp_url = "http://vmechatronics.com/eotp";

                                                            String url = "http://bhashsms.com/api/sendmsg.php?user=visionmech&pass=vision&sender=LIRACK&phone=" + phone + "&text=Your%20OTP%20for%20Visions%20Energy%20Advisor%20App%20is%20" + otp + "&priority=ndnd&stype=normal";
                                                            // "http://vmechatronics.com/eotp";
                                                            StringRequest stringRequest = new StringRequest(url,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            //Calling the method drawPath to draw the path
                                                                            Log.w(TAG, "onResponse: " + response.toString());

                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                        }
                                                                    });
                                                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                                            SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                                            prefs1.edit().putBoolean("Islogin", true).commit();
                                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                                            prefs.edit().putBoolean("IsGp", false).commit();
                                                            SharedPreferences proprefs = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                                            proprefs.edit().putBoolean("IsEditPro", false).commit();
                                                            //Adding the request to request queue
                                                            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                                                            requestQueue.add(stringRequest);

                                                            Intent i1 = new Intent(SignUp.this, OTP.class);
                                                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            i1.putExtra("email", email);
                                                            i1.putExtra("phone", phone);
                                                            i1.putExtra("otp", otp);
                                                            i1.putExtra("name", name);
                                                            i1.putExtra("state", state);
                                                            //i1.putExtra("eotp",eotp);
                                                            //i1.putExtra("email",email);
                                                            pd.dismiss();


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
                                                            session.createUserLoginSession(name, email, phone, state, verify, password);
                                                            startActivity(i1);
                                                            finish();

                                                            break;
                                                        case "e_exists": {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("This email id already exists")
                                                                    .setNegativeButton("OK", null)
                                                                    .setTitle("Signup Error")
                                                                    .create()
                                                                    .show();
                                                            break;
                                                        }
                                                        case "p_exists": {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("This Phone Number is already Registered")
                                                                    .setNegativeButton("OK", null)
                                                                    .setTitle("Signup Error")
                                                                    .create()
                                                                    .show();
                                                            break;
                                                        }
                                                        default: {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("Please try again later. Some error Occurred!")
                                                                    .setNegativeButton("Retry", null)
                                                                    .setTitle("Signup Error")
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
                                        SignupRequest signupRequest = new SignupRequest(name, email, pass, phone, state, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(SignUp.this);
                                        queue.add(signupRequest);*/

                                StringRequest stringRequest = new StringRequest(Request.Method.POST, signUp_url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                try {
                                                    JSONArray array = new JSONArray(response);
                                                    JSONObject jsonResponse = array.getJSONObject(0);
                                                    Log.w(TAG, "onResponse: after try");
                                                    Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                                    String success = jsonResponse.getString("success");
                                                    // String eotp=jsonResponse.getString("eotp");
                                                    Log.w(TAG, "onResponse: jsonresponse" + success);
                                                    switch (success) {
                                                        case "true":
                                                            int randomPIN = (int) (Math.random() * 9000) + 1000;
                                                            otp = "" + randomPIN;
                                                            otp = otp.toString().trim();
                                                            // int randomEpin = (int) (Math.random() * 900000) + 100000;
                                                            // eotp = "" + randomEpin;
                                                            //------generating random number of email------------9/9/16
                                                            //  email = etName.getText().toString();//-----A--get the  email text
                                                            //  int randomEpin = (int) (Math.random() * 900000) + 100000;
                                                            //  eotp = "" + randomEpin;
                                                            // Uri uri = Uri.parse("http://vmechatronics.com/eotp");
                                                            //  eotp_url = "http://vmechatronics.com/eotp";

                                                            String url = "http://bhashsms.com/api/sendmsg.php?user=visionmech&pass=vision&sender=LIRACK&phone=" + phone + "&text=Your%20OTP%20for%20Visions%20Energy%20Advisor%20App%20is%20" + otp + "&priority=ndnd&stype=normal";
                                                            // "http://vmechatronics.com/eotp";
                                                            StringRequest stringRequest = new StringRequest(url,
                                                                    new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            //Calling the method drawPath to draw the path
                                                                            Log.w(TAG, "onResponse: " + response.toString());

                                                                        }
                                                                    },
                                                                    new Response.ErrorListener() {
                                                                        @Override
                                                                        public void onErrorResponse(VolleyError error) {
                                                                        }
                                                                    });
                                                            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                                                            SharedPreferences prefs1 = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                                            prefs1.edit().putBoolean("Islogin", true).commit();
                                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                                            prefs.edit().putBoolean("IsGp", false).commit();
                                                            SharedPreferences proprefs = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                                            proprefs.edit().putBoolean("IsEditPro", false).commit();
                                                            //Adding the request to request queue
                                                            RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
                                                            requestQueue.add(stringRequest);

                                                            Intent i1 = new Intent(SignUp.this, OTP.class);
                                                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            i1.putExtra("email", email);
                                                            i1.putExtra("phone", phone);
                                                            i1.putExtra("otp", otp);
                                                            i1.putExtra("name", name);
                                                            i1.putExtra("state", state);
                                                            //i1.putExtra("eotp",eotp);
                                                            //i1.putExtra("email",email);
                                                            pd.dismiss();

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
                                                            session.createUserLoginSession(name, email, phone, state, verify, password);
                                                            startActivity(i1);
                                                            finish();

                                                            break;
                                                        case "e_exists": {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("This email id already exists")
                                                                    .setNegativeButton("OK", null)
                                                                    .setTitle("Signup Error")
                                                                    .create()
                                                                    .show();
                                                            break;
                                                        }
                                                        case "p_exists": {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("This Phone Number is already Registered")
                                                                    .setNegativeButton("OK", null)
                                                                    .setTitle("Signup Error")
                                                                    .create()
                                                                    .show();
                                                            break;
                                                        }
                                                        default: {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                                            builder.setMessage("Please try again later. Some error Occurred!")
                                                                    .setNegativeButton("Retry", null)
                                                                    .setTitle("Signup Error")
                                                                    .create()
                                                                    .show();
                                                            break;
                                                        }
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
                                        params.put("pass",pass);
                                        params.put("phone",phone);
                                        params.put("name", name);
                                        params.put("state",state);
                                        Log.w(TAG, "Signup Request is working well  " + email + pass+phone+name+pass +state);
                                        return params;
                                    }
                                };

                                MySingleton.getInstance(SignUp.this).addToRequestQueue(stringRequest);
                            }
                            else {
                                Snackbar snackbar = Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG);
                                // Changing action button text color
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                            builder.setMessage("Passwords do not Match")
                                    .setNegativeButton("Retry", null)
                                    .setTitle("Signup Error")
                                    .create()
                                    .show();
                        }
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        builder.setMessage("This is not a Valid Phone Number")
                                .setNegativeButton("OK", null)
                                .setTitle("Signup Error")
                                .create()
                                .show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    builder.setMessage("Please Fill in all Details")
                            .setNegativeButton("OK", null)
                            .setTitle("Signup Error")
                            .create()
                            .show();
                }
            }
        });

    }
    /* public class eotp extends StringRequest {

         public static final String REGISTER_REQUEST_URL = "http://vmechatronics.com/eotp";
         private static final String TAG = "eotp";
         private Map<String,String> params;
         public eotp (String email,String eotpR,String phone,String otp, Response.Listener<String> listener)
         {
             super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
             params = new HashMap<>();

             params.put("email", email);
            params.put("phone",phone);
             params.put("eotp",eotpR);
               params.put("otp",otp);
             Log.w(TAG, "eotp is send  " + email+eotpR+otp+phone);
         }

         @Override
         public Map<String, String> getParams() {
             return params;
         }
     }*/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(SignUp.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Sign_Up", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
