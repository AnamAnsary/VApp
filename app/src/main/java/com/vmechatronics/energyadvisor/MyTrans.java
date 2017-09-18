package com.vmechatronics.energyadvisor;

import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyTrans extends ListActivity{

    private static final String TAG = "Transaction";
    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;
    String email = "";
    TextView tvTST;

    ArrayList<HashMap<String, String>> transactList = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> user;
    private String getTransactn_url =  "https://vmechatronics.com/app/get_trans.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trans);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        tvTST = (TextView) findViewById(R.id.tvTST);
        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get("email");
        if(!session.isUserLoggedIn()){
            Intent i = new Intent(MyTrans.this, Login.class);
            i.putExtra("class","MyTrans");
            startActivity(i);
            finish();
        }
        getPersonTransact(email);
    }
    public void getPersonTransact(final String email)
    {
        if(checkInternetConnection()) {
           /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.w(TAG, "onResponse: " + response);
                        JSONObject jsonObj = new JSONObject(response);
                        JSONArray jsonArray = jsonObj.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            String tid = c.getString("tid");
                            String amount = c.getString("amount");
                            String qid = c.getString("qid");
                            String tdate = c.getString("t_date");
                            String tstatus = c.getString("tstatus");
                            String t_stats = "";
                            if (tstatus.equals("0")) {
                                t_stats = "Failed";
                            } else {
                                t_stats = "Success";
                            }
                            HashMap<String, String> persons = new HashMap<String, String>();

                            persons.put("tid", tid);
                            persons.put("amount", amount);
                            persons.put("qid", qid);
                            persons.put("tdate", tdate);
                            persons.put("tstatus", t_stats);

                            transactList.add(persons);
                        }
                        Log.w(TAG, "onCreate: " + transactList.size());
                        if (transactList.size() != 0) {
                            ListView listView = getListView();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            });

                            ListAdapter adapter = new SimpleAdapter(MyTrans.this, transactList,
                                    R.layout.transact_entry, new String[]{"tid", "amount", "qid", "tdate", "tstatus"},
                                    new int[]{R.id.tvTID, R.id.tvTAM, R.id.tvTQID, R.id.tvTDT, R.id.tvTST});
                            setListAdapter(adapter);
                        } else {
                            new AlertDialog.Builder(MyTrans.this)
                                    .setTitle("No Transaction")
                                    .setMessage("You have not Performed any Transaction..")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(MyTrans.this, Profile.class));
                                            finish();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            TransRequest tReq = new TransRequest(email, responseListener);
            RequestQueue queue = Volley.newRequestQueue(MyTrans.this);
            queue.add(tReq);
*/

            StringRequest stringRequest = new StringRequest(Request.Method.POST, getTransactn_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Log.w(TAG, "onResponse: " + response);
                                JSONObject jsonObj = new JSONObject(response);
                                JSONArray jsonArray = jsonObj.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject c = jsonArray.getJSONObject(i);
                                    String tid = c.getString("tid");
                                    String amount = c.getString("amount");
                                    String qid = c.getString("qid");
                                    String tdate = c.getString("t_date");
                                    String tstatus = c.getString("tstatus");
                                    String t_stats = "";
                                    if (tstatus.equals("0")) {
                                        t_stats = "Failed";
                                    } else {
                                        t_stats = "Success";
                                    }
                                    HashMap<String, String> persons = new HashMap<String, String>();

                                    persons.put("tid", tid);
                                    persons.put("amount", amount);
                                    persons.put("qid", qid);
                                    persons.put("tdate", tdate);
                                    persons.put("tstatus", t_stats);

                                    transactList.add(persons);
                                }
                                Log.w(TAG, "onCreate: " + transactList.size());
                                if (transactList.size() != 0) {
                                    ListView listView = getListView();
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        }
                                    });

                                    ListAdapter adapter = new SimpleAdapter(MyTrans.this, transactList,
                                            R.layout.transact_entry, new String[]{"tid", "amount", "qid", "tdate", "tstatus"},
                                            new int[]{R.id.tvTID, R.id.tvTAM, R.id.tvTQID, R.id.tvTDT, R.id.tvTST});
                                    setListAdapter(adapter);
                                } else {
                                    new AlertDialog.Builder(MyTrans.this)
                                            .setTitle("No Transaction")
                                            .setMessage("You have not Performed any Transaction..")
                                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(MyTrans.this, Profile.class));
                                                    finish();
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
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

            MySingleton.getInstance(MyTrans.this).addToRequestQueue(stringRequest);
            Log.w(TAG, "onClick: Que added");
            Log.w(TAG, "getPersonTransact: list created and returned");
        }
        else{
            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.Id_trans), "No internet connection!", Snackbar.LENGTH_LONG);
            // Changing action button text color
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(MyTrans.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "MyTransactions", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
