package com.vmechatronics.energyadvisor;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends Activity {

    private static final String TAG = "Change password";
    private FirebaseAnalytics mFirebaseAnalytics;
    UserSessionManager session;
    Typeface font;


    Button bUpdPass;
    EditText etOldP;
    EditText etNewP;
    EditText etNewCP;

    HashMap<String, String> user;

    String email = "";
    String oldPass = "";
    String newPass = "";
    String newCPass = "";
    String updatepwd_url = "https://vmechatronics.com/app/upd_pass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());
        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        user = session.getUserDetails();

        email = user.get("email");

        if(!session.isUserLoggedIn()){
            Intent i = new Intent(ChangePassword.this, Login.class);
            i.putExtra("class","EditProf");
            startActivity(i);
            finish();
        }

        etOldP = (EditText)findViewById(R.id.etOldP);
        etNewP = (EditText)findViewById(R.id.etNewP);
        etNewCP = (EditText)findViewById(R.id.etNewCP);


        bUpdPass = (Button)findViewById(R.id.bUpdPass);
        bUpdPass.setTypeface(font);
        bUpdPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldPass = etOldP.getText().toString().trim();
                newPass = etNewP.getText().toString().trim();
                newCPass = etNewCP.getText().toString().trim();

                if(oldPass.length()>0 && newPass.length()>0 && newCPass.length()>0){
                    if(newPass.equals(newCPass)){
                        if(checkInternetConnection()) {
                            Log.w(TAG, "OLD: " + oldPass + "NEW " + newPass );
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
                                            Toast.makeText(getApplicationContext(),
                                                    "Password changed Successfully",
                                                    Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(ChangePassword.this, Profile.class));
                                        }else{
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                                            builder.setMessage("Please Enter Correct OLD Password")
                                                    .setNegativeButton("OK", null)
                                                    .setTitle("Wrong Password")
                                                    .create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            UpdatePass upp = new UpdatePass(email, oldPass, newPass, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(ChangePassword.this);
                            queue.add(upp);*/

                            StringRequest stringRequest = new StringRequest(Request.Method.POST, updatepwd_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONArray array = new JSONArray(response);
                                                JSONObject jsonResponse = array.getJSONObject(0);
                                                Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                                boolean success = jsonResponse.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(),
                                                            "Password changed Successfully",
                                                            Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(ChangePassword.this, Profile.class));
                                                }else{
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                                                    builder.setMessage("Please Enter Correct OLD Password")
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Wrong Password")
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
                                    params.put("pass",oldPass);
                                    params.put("pass_new", newPass);
                                    return params;
                                }
                            };

                            MySingleton.getInstance(ChangePassword.this).addToRequestQueue(stringRequest);

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                        builder.setMessage("Passwords do not Match!")
                                .setNegativeButton("OK", null)
                                .setTitle("Invalid Inputs")
                                .create()
                                .show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassword.this);
                    builder.setMessage("Please Enter all the Fields!")
                            .setNegativeButton("OK", null)
                            .setTitle("Invalid Inputs")
                            .create()
                            .show();
                }
            }
        });
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(ChangePassword.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Change_Password", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
