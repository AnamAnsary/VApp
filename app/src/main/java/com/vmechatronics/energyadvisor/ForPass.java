package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

public class ForPass extends Activity {

    private static final String TAG = "FOR PASS";
    private FirebaseAnalytics mFirebaseAnalytics;
    Typeface font;
    EditText etFMI;
    Button bFPGP;
    TextView tvFPSU;

    String email = "";
    private String newpwd_url = "https://vmechatronics.com/app/for_pass.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_pass);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        etFMI = (EditText)findViewById(R.id.etFMI);
        bFPGP.setTypeface(font);
        bFPGP = (Button)findViewById(R.id.bFPGP);
        bFPGP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etFMI.getText().toString().trim();
                if(email.length()>0) {
                    if(checkInternetConnection()) {
                        final ProgressDialog pd = ProgressDialog.show(ForPass.this, "", "Connecting...", true);

                              /*  final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JSONObject jsonResponse = null;
                                        try {
                                            JSONArray array = new JSONArray(response);
                                            jsonResponse = array.getJSONObject(0);
                                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                            boolean mail = jsonResponse.getBoolean("mail");
                                            boolean db = jsonResponse.getBoolean("db");

                                            Log.w(TAG, "onResponse: jsonresponse" + mail + db);
                                            if (db) {
                                                if (mail) {
                                                    Toast.makeText(ForPass.this, "Your New Password has been sent to your Registered mail id..", Toast.LENGTH_LONG).show();
                                                    Intent ii = new Intent(ForPass.this, Login.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("class", "ForPass");
                                                    ii.putExtras(b);
                                                    startActivity(ii);
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ForPass.this);
                                                    builder.setMessage("Some Error Occured, please try agian later!!")
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Error")
                                                            .create()
                                                            .show();
                                                }
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ForPass.this);
                                                builder.setMessage("This mail Id does not Exist!")
                                                        .setNegativeButton("OK", null)
                                                        .setTitle("Invalid Mail ID")
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                NewPass np = new NewPass(email, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(ForPass.this);
                                queue.add(np);*/

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, newpwd_url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray array = new JSONArray(response);
                                            JSONObject jsonResponse = array.getJSONObject(0);
                                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());
                                            boolean mail = jsonResponse.getBoolean("mail");
                                            boolean db = jsonResponse.getBoolean("db");

                                            Log.w(TAG, "onResponse: jsonresponse" + mail + db);
                                            if (db) {
                                                if (mail) {
                                                    Toast.makeText(ForPass.this, "Your New Password has been sent to your Registered mail id..", Toast.LENGTH_LONG).show();
                                                    Intent ii = new Intent(ForPass.this, Login.class);
                                                    Bundle b = new Bundle();
                                                    b.putString("class", "ForPass");
                                                    ii.putExtras(b);
                                                    startActivity(ii);
                                                } else {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(ForPass.this);
                                                    builder.setMessage("Some Error Occured, please try agian later!!")
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Error")
                                                            .create()
                                                            .show();
                                                }
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ForPass.this);
                                                builder.setMessage("This mail Id does not Exist!")
                                                        .setNegativeButton("OK", null)
                                                        .setTitle("Invalid Mail ID")
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
                                return params;
                            }
                        };

                        MySingleton.getInstance(ForPass.this).addToRequestQueue(stringRequest);

                    }else{
                        Snackbar snackbar = Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG);
                        // Changing action button text color
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);

                        snackbar.show();
                    }
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForPass.this);
                    builder.setMessage("You have not Entered any mail id!!")
                            .setNegativeButton("OK", null)
                            .setTitle("")
                            .create()
                            .show();

                }
            }
        });

        tvFPSU = (TextView) findViewById(R.id.tvFPSU);
        tvFPSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent isu = new Intent(ForPass.this, SignUp.class);
                startActivity(isu);
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
            //Toast.makeText(ForPass.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Forgot_Password", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }

}
