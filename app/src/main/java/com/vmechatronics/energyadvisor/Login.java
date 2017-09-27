package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.android.volley.RequestQueue;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {

    public static final String TAG = "LOGIN";

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;
    String email;
    String pass;
    String name;
    String phone;
    String state;
    String password;
    Boolean verify = false;
    String className = "";

    EditText etUser;
    EditText etPass;
    Button tvSignUp;
    Button bSignIn;
    Button bGPlus;
    TextView tvHelp;
    Button tvForPas;
    Typeface font;

    HashMap<String,String> user;
    private String sigin_url = "https://vmechatronics.com/app/Sigin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();

        className = this.getIntent().getStringExtra("class");
        Log.w(TAG, "onCreate: " + className );

        etPass = (EditText)findViewById(R.id.etPass);
        etUser = (EditText)findViewById(R.id.etUser);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("Islogin", false).commit();


        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        tvForPas = (Button)findViewById(R.id.tvForPas);
        tvForPas.setTypeface(font);
        tvForPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ifp = new Intent(Login.this, ForPass.class);
                startActivity(ifp);
            }
        });

        tvSignUp = (Button)findViewById(R.id.tvSignUp);
        tvSignUp.setTypeface(font);
        //tvSignUp.setText("Sign Up");
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Login.this, SignUp.class);
                startActivity(i1);
                // finish();
                Log.w("LOGIN", "onClick: Signup clicked" );
            }
        });

        tvHelp = (TextView)findViewById(R.id.tvHelp);
        tvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://vmechatronics.com/contactus.php");
                Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i3);
                Log.w(TAG, "onClick: tvHelp");
            }
        });

        bSignIn = (Button)findViewById(R.id.bSignIn);
        bSignIn.setTypeface(font);
        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "onClick: Its working Bro!!", null );
                boolean entrance = true;
                email = etUser.getText().toString().trim();
                pass = etPass.getText().toString().trim();
                Log.w(TAG,"name and paas "+email +pass);
                if(checkInternetConnection()) {
                    if (email.length() > 0 && pass.length() > 0) {

                        final ProgressDialog pd = ProgressDialog.show(Login.this, "", "Connecting...", true);

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
                                                session.updateVerify();
                                                try {
                                                    name = jsonResponse.getString("name");
                                                    phone = jsonResponse.getString("phone");
                                                    verify = jsonResponse.optBoolean("verify");
                                                    state = jsonResponse.getString("state");
                                                    password = jsonResponse.getString("pass");
                                                }
                                                catch (NullPointerException e)
                                                {
                                                    e.printStackTrace();
                                                }
                                                // int BooleanError = jsonResponse.getInt("verify");
                                                // boolean verify = (BooleanError > 0) ? true : false;
                                              *//*  MessageDigest mdEnc;
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
                                                }*//*
                                                session.createUserLoginSession(name, email, phone, state, verify, password);
                                                session = new UserSessionManager(getApplicationContext());
                                                user = session.getUserDetails();
                                                String ee = user.get("email");
                                                String ph = user.get("phone");
                                                String vv = user.get("verify");
                                                String pp = user.get("pwd");
                                                Log.w(TAG, "session created " + ee+ " "+ pp+" " + vv +" "+ph);
                                                String myClass = "com.vmechatronics.energyadvisor." + className;//visionmechatronics
                                                Log.w(TAG, className + " " + myClass);
                                                Class<?> next = Class.forName(myClass);
                                                Intent intent;
                                                new Intent(Login.this, next);
                                                if (className.equals("mainActivity")) {
                                                    intent = new Intent(Login.this, Choice.class);
                                                    final Bundle b1 = Login.this.getIntent().getExtras();
                                                    final int plantSize = b1.getInt("plantSize");
                                                    final String[] mainArray = b1.getStringArray("main");
                                                    Bundle b = new Bundle();
                                                    b.putStringArray("main", mainArray);
                                                    b.putInt("plantSize", plantSize);
                                                    intent.putExtras(b);
                                                    startActivity(intent);
                                                } else if (className.equals("LiBattListActivity")) {
                                                    intent = new Intent(Login.this, OrderBatt.class);
                                                    //final Bundle b1 = Login.this.getIntent().getExtras();
                                                    //final float arraybattery = b1.getInt("arraybattery");
                                                    //final String[] liArray = b1.getStringArray("li");
                                                    Bundle b = new Bundle();
                                                    b.putBoolean("FrmLoginPage", true);
                                                    b.putString("battery_selected","");
                                                    //b.putFloat("arraybattery", arraybattery);
                                                    intent.putExtras(b);
                                                    startActivity(intent);
                                                }
                                                else if(className.equals("BattDetailsFragment")){
                                                    final Bundle b1 = Login.this.getIntent().getExtras();
                                                    final String url_chosen = b1.getString("CurrentUrl");
                                                    final String batt_chosen;
                                                    switch (url_chosen) {
                                                        case "https://vmechatronics.com/lithium-ion-battery-li-v.php":
                                                            batt_chosen = "Li-V";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-lirack.php":
                                                            batt_chosen = "LiRack";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-joulieplus.php":
                                                            batt_chosen = "Joulie+";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-joulie.php":
                                                            batt_chosen = "Joulie";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-lirackeco.php":
                                                            batt_chosen = "LiRack-Eco";
                                                            break;
                                                        default:
                                                            batt_chosen = "";
                                                            break;
                                                    }
                                                    //final String batt_chosen = b1.getString("battery_selected");

                                                    Bundle b = new Bundle();
                                                    b.putString("battery_selected", batt_chosen);
                                                    b.putBoolean("FrmLoginPage",true);
                                                    Log.w(TAG,"batt is "+batt_chosen);
                                                    intent = new Intent(Login.this, OrderBatt.class);
                                                    intent.putExtras(b);
                                                    startActivity(intent);

                                                }
                                                else if(className.equals("ForPass")){
                                                    intent = new Intent(Login.this, Profile.class);
                                                    startActivity(intent);
                                                }
                                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                                prefs.edit().putBoolean("Islogin", true).commit();
                                                Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_LONG).show();
                                                pd.dismiss();
                                                finish();

                                            }else {
                                                pd.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                                builder.setMessage("Username or Password is wrong")
                                                        .setNegativeButton("Retry", null)
                                                        .setTitle("Login Error")
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            pd.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                            builder.setMessage("There is problem while Connecting to Server")
                                                    .setNegativeButton("OK", null)
                                                    .setTitle("Connection Error")
                                                    .create()
                                                    .show();
                                            e.printStackTrace();
                                            //   }

                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                LoginRequest loginRequest = new LoginRequest(email, pass, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Login.this);
                                queue.add(loginRequest);*/
                                // }

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, sigin_url,
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
                                                session.updateVerify();
                                                try {
                                                    name = jsonResponse.getString("name");
                                                    phone = jsonResponse.getString("phone");
                                                    verify = jsonResponse.optBoolean("verify");
                                                    state = jsonResponse.getString("state");
                                                    password = jsonResponse.getString("pass");
                                                }
                                                catch (NullPointerException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                // int BooleanError = jsonResponse.getInt("verify");
                                                // boolean verify = (BooleanError > 0) ? true : false;
                                              /*  MessageDigest mdEnc;
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
                                                }*/
                                                session.createUserLoginSession(name, email, phone, state, verify, password);
                                                session = new UserSessionManager(getApplicationContext());
                                                user = session.getUserDetails();
                                                String ee = user.get("email");
                                                String ph = user.get("phone");
                                                String vv = user.get("verify");
                                                String pp = user.get("pwd");
                                                Log.w(TAG, "session created " + ee+ " "+ pp+" " + vv +" "+ph);
                                                String myClass = "com.vmechatronics.energyadvisor." + className;//visionmechatronics
                                                Log.w(TAG, className + " " + myClass);
                                                Class<?> next = Class.forName(myClass);
                                                Intent intent;
                                                new Intent(Login.this, next);
                                                if (className.equals("mainActivity")) {
                                                    intent = new Intent(Login.this, Choice.class);

                                                    final Bundle b1 = Login.this.getIntent().getExtras();
                                                    final int plantSize = b1.getInt("plantSize");
                                                    final String[] mainArray = b1.getStringArray("main");
                                                    Bundle b = new Bundle();
                                                    b.putStringArray("main", mainArray);
                                                    b.putInt("plantSize", plantSize);
                                                    intent.putExtras(b);
                                                    startActivity(intent);
                                                } else if (className.equals("LiBattListActivity")) {
                                                    intent = new Intent(Login.this, OrderBatt.class);
                                                    //final Bundle b1 = Login.this.getIntent().getExtras();
                                                    //final float arraybattery = b1.getInt("arraybattery");
                                                    //final String[] liArray = b1.getStringArray("li");
                                                    Bundle b = new Bundle();
                                                    b.putBoolean("FrmLoginPage", true);
                                                    b.putString("battery_selected","");
                                                    //b.putFloat("arraybattery", arraybattery);
                                                    intent.putExtras(b);
                                                    startActivity(intent);

                                                }
                                                else if(className.equals("BattDetailsFragment")){
                                                    final Bundle b1 = Login.this.getIntent().getExtras();
                                                    final String url_chosen = b1.getString("CurrentUrl");
                                                    final String batt_chosen;
                                                    switch (url_chosen) {
                                                        case "https://vmechatronics.com/lithium-ion-battery-li-v.php":
                                                            batt_chosen = "Li-V";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-lirack.php":
                                                            batt_chosen = "LiRack";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-joulieplus.php":
                                                            batt_chosen = "Joulie+";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-joulie.php":
                                                            batt_chosen = "Joulie";
                                                            break;
                                                        case "https://vmechatronics.com/lithium-ion-battery-lirackeco.php":
                                                            batt_chosen = "LiRack-Eco";
                                                            break;
                                                        default:
                                                            batt_chosen = "";
                                                            break;
                                                    }
                                                    //final String batt_chosen = b1.getString("battery_selected");

                                                    Bundle b = new Bundle();
                                                    b.putString("battery_selected", batt_chosen);
                                                    b.putBoolean("FrmLoginPage",true);
                                                    Log.w(TAG,"batt is "+batt_chosen);
                                                    intent = new Intent(Login.this, OrderBatt.class);
                                                    intent.putExtras(b);
                                                    startActivity(intent);

                                                }
                                                else if(className.equals("ForPass")){
                                                    intent = new Intent(Login.this, Profile.class);
                                                    startActivity(intent);
                                                }
                                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                                prefs.edit().putBoolean("Islogin", true).commit();
                                                Toast.makeText(getApplicationContext(), "Logged in Successfully", Toast.LENGTH_LONG).show();
                                                pd.dismiss();
                                                finish();

                                            }else {
                                                pd.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                                builder.setMessage("Username or Password is wrong")
                                                        .setNegativeButton("Retry", null)
                                                        .setTitle("Login Error")
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            pd.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                            builder.setMessage("There is problem while Connecting to Server")
                                                    .setNegativeButton("OK", null)
                                                    .setTitle("Connection Error")
                                                    .create()
                                                    .show();
                                            e.printStackTrace();
                                            //   }

                                        } catch (ClassNotFoundException e) {
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
                                return params;
                            }
                        };

                        MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);


                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please Enter Valid Username and Password",
                                Toast.LENGTH_LONG).show();
                    }
                }

                else{
                    Snackbar snackbar = Snackbar
                            .make(view, "No internet connection!", Snackbar.LENGTH_LONG);
                           /* .setAction("RETRY", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });*/
                    // Changing message text color
                    //snackbar.setActionTextColor(Color.RED);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();
                }
            }
        });
        bGPlus=(Button)findViewById(R.id.bGPlus);
        bGPlus.setTypeface(font);
        bGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i4 = new Intent(Login.this, GSignInActivity.class);
                startActivity(i4);
            }
        });

    }

    /*private boolean checkInternetConnection() {

      *//*  ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(Login.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }*//*

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                //Toast.makeText(this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                //Toast.makeText(this, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            // not connected to the internet
            Toast.makeText(Login.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
*/
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
           // Toast.makeText(Login.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void onBackPressed () {
        super.onBackPressed();
        finish();
    }


    public class Session {
        // Shared Preferences
        SharedPreferences pref;
        // Editor for Shared preferences
        SharedPreferences.Editor editor;
        // Context
        Context _context;
        // Shared pref mode
        int PRIVATE_MODE = 0;
        // Sharedpref file name
        private static final String PREF_NAME = "AndroidHivePref";
        // All Shared Preferences Keys
        private static final String IS_LOGIN = "IsLoggedIn";
        // User name (make variable public to access from outside)
        public static final String KEY_NAME = "name";
        // Email address (make variable public to access from outside)
        public static final String KEY_EMAIL = "email";
        // Constructor
        public Session(Context context){
            this._context = context;
            pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
            editor = pref.edit();
        }

        /**
         * Get stored session data
         * */
        public HashMap<String, String> getUserDetails(){
            HashMap<String, String> user = new HashMap<String, String>();
            // user name
            user.put(KEY_NAME, pref.getString(KEY_NAME, null));
            // user email id
            user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
            // return user
            return user;
        }
        /**
         * Quick check for login
         * **/
        // Get Login State
        public boolean isLoggedIn(){
            return pref.getBoolean(IS_LOGIN, false);
        }
    }

    @Override
    protected void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "LoginActivity", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
