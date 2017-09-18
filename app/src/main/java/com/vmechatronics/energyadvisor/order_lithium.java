package com.vmechatronics.energyadvisor;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 10/1/17.
 */
public class order_lithium  extends AppCompatActivity {

    private static final String TAG = "ORDER_LITHIUM";
    private FirebaseAnalytics mFirebaseAnalytics;

    float sizeOfBatt,sizeOfBatt1;
    int noOfBatt;
    int i;
    int k=0;
    int sum1=0,sum2=0;

    private float[] cap;
    private double min;
    private int priceArray[];
    private int price;


    UserSessionManager session;
    HashMap<String, String> user;
    String email = "";
    String phone = "";
    String state= "";
    String qid = "";
    String qdate = "";
    String name = "";
    String pdf_url = "";
    String filename = "";
    private String repIssue_url = "https://vmechatronics.com/app/reportIssue.php";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_quote);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        final TextView tvRefID = (TextView) findViewById(R.id.tvRefID);

        Intent mIntent = getIntent();
        Bundle b = mIntent.getExtras();
        final String selected_batt = b.getString("selected_batt");
        final String BattChar = b.getString("char_src");
        final int backupHr = b.getInt("backupHr");

        final int PowerCut = b.getInt("PowerCut");
        final float sizeOfBattbyUser = b.getFloat("Size");
        final String BPowerCut = b.getString("BPowerCut");
        final float total_load = b.getFloat("total_load");
        final float load = b.getFloat("load");

       // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(order_lithium.this);
       // prefs.edit().putFloat("sizeOfBattbyUser", +sizeOfBattbyUser).commit();

        //sizeOfBatt = mIntent.getFloatExtra("Size", 0);
        sizeOfBatt1 = sizeOfBattbyUser;
        sizeOfBatt = sizeOfBattbyUser;
        //sizeOfBatt =Float.parseFloat(new DecimalFormat("##.##").format(sizeOfBatt));
        Log.w(TAG, "Battery of " + sizeOfBatt);


        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get("email");
        name = user.get("name");
        phone = user.get("phone");
        state = user.get("state");


        this.cap = new float[]{3.5f, 5.6f, 11.2f, 16.8f, 22.4f, 33.6f};
        this.priceArray = new int[]{325000, 410000, 745000, 1025000, 1280000, 1820000};
        final ArrayList<Float> arrayResult1 = new ArrayList<Float>();
        ArrayList<Float> arrayResult2 = new ArrayList<Float>();
        ArrayList<Float> cap_selected = null;

        int count = 0;

        while (sizeOfBatt >= 1.5) {
            if (sizeOfBatt < 3.5) {
                arrayResult1.add((float) 3.5);
                break;
            }

            ArrayList<Float> capacity = new ArrayList<Float>();
            ArrayList<Double> finalArray = new ArrayList<>();
            if (sizeOfBatt < cap[5]) {
                for (i = 0; i < 6; i++) {
                    if (sizeOfBatt >= cap[i]) {
                        capacity.add(cap[i]);
                        k = i;
                    }
                }
                capacity.add(cap[k + 1]);
            } else {
                for (i = 0; i < 6; i++)
                    capacity.add(cap[i]);

            }

            double c, f;

            float temp[] = new float[6];
            int sizeOfcapacity = capacity.size();
            Log.w(TAG, "size value " + sizeOfcapacity);
            for (int i = 0; i < sizeOfcapacity; i++) {
                temp[i] = sizeOfBatt / capacity.get(i);
                c = Math.ceil(temp[i]);
                f = Math.floor(temp[i]);
                finalArray.add(Math.abs(c - temp[i]));
                finalArray.add(Math.abs(f - temp[i]));
            }
            int size = finalArray.size();
            min = finalArray.get(0);

            for (i = 0; i < size; i++) {
                if (min > finalArray.get(i)) {
                    min = finalArray.get(i);
                    k = i;
                    k = k / 2;
                }
            }
            Log.w(TAG, "Min value " + min);
            Log.w(TAG, "Nearest " + capacity.get(k));

            arrayResult1.add(capacity.get(k));
            sizeOfBatt = sizeOfBatt - arrayResult1.get(count);
            count++;
        }

        for (i = 0; i < arrayResult1.size(); i++)
        {
            Log.w(TAG, "arrayResult 1: " + arrayResult1.get(i));
        }

     /*   int i = 5;
        while(i>=0)
        {
                if (sizeOfBatt1 >= cap[i]) {
                    sizeOfBatt1 = sizeOfBatt1 - cap[i];
                    arrayResult2.add(cap[i]); //this adds an element to the list.
                    Log.w(TAG, "remaining size Of Battery is " + sizeOfBatt1);

                    if (i != 0) {
                        float d1 = Math.abs(sizeOfBatt1 - cap[i]);
                        float d2 = Math.abs(sizeOfBatt1 - cap[i - 1]);
                        if (d1 < d2) {
                            //donot decrement i
                            arrayResult2.add(cap[i]);
                            Log.w(TAG, "d1 is less than d2 add in result array" + arrayResult2.add(cap[i]));
                        }
                    }

                }

            /*else{
                float temp[] = new float[6];
                for(int k=0; k<6;k++)
                {
                    temp[k]=Math.abs(sizeOfBatt-cap[i]);
                }
                for(int k=0; k<6;k++)
                {
                    if (temp[k]==sizeOfBatt)
                        arrayResult.add(cap[k]);
                }

            }*/
 /*           else
            i--;
        }

        if (i == 0 && (sizeOfBatt > 1.5))
            arrayResult2.add(cap[i]);

*/
        int i = 5;
        float mini = 99;
        if (sizeOfBatt1 >= cap[i]) {
            arrayResult2.add(cap[i]);
            sizeOfBatt1 = sizeOfBatt1 - cap[i];
        }

        float diff[] = new float[6];
        for (i = 0; i < 6; i++) {
            diff[i] = Math.abs(sizeOfBatt1 - cap[i]);
            if (diff[i] < mini) {
                mini = Math.min(mini, diff[i]);
                k = i;
            }
                /*if(diff[i] > mini)
                {
                    mini = diff[i];
                    i=k;
                }*/
        }

        arrayResult2.add(cap[k]);

        if (sizeOfBatt1 - cap[k] > 1.5) {
            arrayResult2.add(cap[0]);
        }

        for (i = 0; i < arrayResult2.size(); i++)
        {
            Log.w(TAG, "arrayResult 2: " +arrayResult2.get(i));
        }

        for(i=0;i<arrayResult1.size();i++)
        {
            if(arrayResult1.get(i) == 3.5f)
                sum1 = sum1 + 325000;
            else if(arrayResult1.get(i) == 5.6f)
                sum1 = sum1 + 410000;
            else if(arrayResult1.get(i) == 11.2f)
                sum1 = sum1 + 745000;
            else if(arrayResult1.get(i) == 16.8f)
                sum1 = sum1 + 1025000;
            else if(arrayResult1.get(i) == 22.4f)
                sum1 = sum1 + 1280000;
            else if(arrayResult1.get(i) == 33.6f)
                sum1 = sum1 + 1820000;

            Log.w(TAG, "sum 1 is " + sum1);

        }


        for(i=0;i<arrayResult2.size();i++)
        {
            float r = arrayResult2.get(i);
            if(r == 3.5f)
                sum2 = sum2 + 325000;
            else if(r == 5.6f)
                sum2 = sum2 + 410000;
            else if(r == 11.2f)
                sum2 = sum2 + 745000;
            else if(r == 16.8f)
                sum2 = sum2 + 1025000;
            else if(r == 22.4f)
                sum2 = sum2 + 1280000;
            else if(r == 33.6f)
                sum2 = sum2 + 1820000;
            Log.w(TAG, "sum 2 is " + sum2);
        }


        tvName.setText("Hello " +name+ ",");
        tv1.setText("Based on your inputs, we have calculated that you require ");
        //tv1.setText("According to your inputs,You will require following size of battery:");

        if(sum1 == sum2) {
            price = sum1;
            //size = arrayResult1.size();
            if (arrayResult1.size() > 0) {
                tv1.append(String.valueOf(arrayResult1.get(0)));
                for (i = 1; i < arrayResult1.size(); ++i) {
                    tv1.append("kWh, ");
                    tv1.append(String.valueOf(arrayResult1.get(i)));
                }
            }
            /*for(i=0;i<arrayResult1.size();i++)
                cap_selected.set(i, arrayResult1.get(i));
            Log.w(TAG, "capacity selected " + cap_selected);*/

            cap_selected = new ArrayList<Float>(arrayResult1);
           // Collections.copy( cap_selected,arrayResult1);
            int size =arrayResult1.size();
            for(i=0;i<size;i++) {
                Log.w(TAG, "capacity selected array " + cap_selected.get(i));
            }
        }

            else if(sum1 < sum2) {
            price = sum1;
           // size = arrayResult1.size();
            if (arrayResult1.size() > 0) {
                tv1.append(String.valueOf(arrayResult1.get(0)));
                for (i = 1; i < arrayResult1.size(); ++i) {
                    tv1.append("kWh, ");
                    tv1.append(String.valueOf(arrayResult1.get(i)));
                }
            }
            cap_selected = new ArrayList<Float>(arrayResult1);
            //Collections.copy( cap_selected,arrayResult1);
            int size =arrayResult1.size();
            for(i=0;i<size;i++) {
                Log.w(TAG, "capacity selected array " + cap_selected.get(i));
            }
        }
        else if(sum2 < sum1) {
            price = sum2;

            //size = arrayResult2.size();
            if (arrayResult2.size() > 0) {
                tv1.append(String.valueOf(arrayResult2.get(0)));
                for (i = 1; i < arrayResult2.size(); ++i) {
                    tv1.append("kWh, ");
                    tv1.append(String.valueOf(arrayResult2.get(i)));
                }
            }

            cap_selected = new ArrayList<Float>(arrayResult2);
            //Collections.copy(cap_selected, arrayResult2);
            int size =arrayResult2.size();
            for(i=0;i<size;i++) {
                Log.w(TAG, "capacity selected array " + cap_selected.get(i));
            }
        }

            tv1.append("kWh battery. A quote for the same has been sent to your mail and your Download is available here.");

            Log.w(TAG, "Price is " +price);
 /*  for(i=0;i<cap_selected.size();i++)
        {
            if(cap_selected.get(i)==3.5)

        }
*/
        final int occurrencesOf1st = Collections.frequency(cap_selected, 3.5f);
        final int occurrencesOf2st = Collections.frequency(cap_selected, 5.6f);
        final int occurrencesOf3rd = Collections.frequency(cap_selected, 11.2f);
        final int occurrencesOf4th = Collections.frequency(cap_selected, 16.8f);
        final int occurrencesOf5th = Collections.frequency(cap_selected, 22.4f);
        final int occurrencesOf6th = Collections.frequency(cap_selected, 33.6f);
        Log.w(TAG, "occurrencesOf1st is " +occurrencesOf1st);
        Log.w(TAG, "occurrencesOf2st is " +occurrencesOf2st);
        Log.w(TAG, "occurrencesOf3rd is " +occurrencesOf3rd);
        Log.w(TAG, "occurrencesOf4th is " +occurrencesOf4th);
        Log.w(TAG, "occurrencesOf5th is " +occurrencesOf5th);
        Log.w(TAG, "occurrencesOf6th is " +occurrencesOf6th);


        final ProgressDialog pd = ProgressDialog.show(order_lithium.this, "", "Please Wait...", true);
        final ArrayList<Float> finalCap_selected = cap_selected;
        new Thread(new Runnable() {
            public void run() {
                final Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        //JSONObject =  new JSONObject(response);
                        try {
                            JSONArray array = new JSONArray(response);
                            jsonResponse = array.getJSONObject(0);
                   /*       JSONObject JSONResult = new JSONObject();
                            for (int i = 0; i < cap_selected.size(); i++) {

                                try {
                                    JSONResult.put("" +String.valueOf(i + 1),cap_selected.get(i));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }*/

                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());//tostring
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                qid = jsonResponse.getString("qid");
                                qdate = jsonResponse.getString("qdate");
                                tvRefID.setText("Quote ID: " + qid);
                                Log.w(TAG, "QQ " + qid + qdate);
                                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray array = new JSONArray(response);
                                            JSONObject jsonResponse = array.getJSONObject(0);
                                            Log.w(TAG, "Mail PDF: jsonresponse" + response.toString());
                                            boolean success = jsonResponse.getBoolean("success");
                                            if (success) {
                                                pd.dismiss();
                                                filename = jsonResponse.getString("pdf");
                                                pdf_url = "https://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                                Log.i(TAG, "onResponse: filename:"+filename+" pdf_url:" +pdf_url);

                                               /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        JSONObject jsonResponse = null;
                                                        try {
                                                            JSONArray array = new JSONArray(response);
                                                            jsonResponse = array.getJSONObject(0);
                                                            boolean success = jsonResponse.getBoolean("success");
                                                            if (success) {
                                                                pd.dismiss();
                                                                Log.i(TAG, "onResponse: filename inserted successfully.");
                                                                //pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                                            } else {
                                                                pd.dismiss();
                                                               *//* AlertDialog.Builder builder = new AlertDialog.Builder(order_lithium.this);
                                                                builder.setMessage("Please try again later, some error occured.")
                                                                        .setNegativeButton("OK", null)
                                                                        .setTitle("Error")
                                                                        .create()
                                                                        .show();*//*
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                };
                                                InsertFilename ins = new InsertFilename(email, qid, filename, responseListener);
                                                RequestQueue queue = Volley.newRequestQueue(order_lithium.this);
                                                queue.add(ins);*/

                                            } else {
                                                pd.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(order_lithium.this);
                                                builder.setMessage("Please try again later, couldn't send mail!")
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
                                MakeBattPDF mpdf = new MakeBattPDF(selected_batt, qid, qdate, name, email, state, finalCap_selected, BattChar, backupHr, PowerCut,
                                        BPowerCut, total_load, load, sizeOfBattbyUser, price,occurrencesOf1st,occurrencesOf2st,occurrencesOf3rd,occurrencesOf4th,occurrencesOf5th,occurrencesOf6th, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(order_lithium.this);
                                queue.add(mpdf);

                            } else {
                                Log.i(TAG, "insert quote response not working");
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(order_lithium.this);
                                builder.setMessage("Please try again later, some error occurred.")
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
                // lastcost = Math.round(finalCost);
                InsertQuote insQ = new InsertQuote(email, phone, price, responseListener);
                RequestQueue queue = Volley.newRequestQueue(order_lithium.this);
                queue.add(insQ);
            }
            }).start();


       Button bGoHom = (Button) findViewById(R.id.bGoHom);
        bGoHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igh = new Intent(order_lithium.this, Home.class);
                //igh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //igh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                igh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(igh);
                finish();
            }
        });

        Button bDowQuo = (Button) findViewById(R.id.bDowQuo);
        bDowQuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(pdf_url);
                Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i3);
            }
        });

        Button rep = (Button) findViewById(R.id.report);
        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = ProgressDialog.show(order_lithium.this, "", "Sending Mail...", true);
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
                                Toast.makeText(order_lithium.this, "Mail sent!", Toast.LENGTH_LONG).show();
                               // pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                            } else {
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(order_lithium.this);
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
                RequestQueue queue = Volley.newRequestQueue(order_lithium.this);
                queue.add(reportIssue);
*/

                StringRequest stringRequest = new StringRequest(Request.Method.POST, repIssue_url,
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
                                        Toast.makeText(order_lithium.this, "Mail sent!", Toast.LENGTH_LONG).show();
                                        // pdf_url = "http://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                    } else {
                                        pd.dismiss();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(order_lithium.this);
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

                MySingleton.getInstance(order_lithium.this).addToRequestQueue(stringRequest);

            }
        });
        /*if(sizeOfBatt % 3.5 == 0)
        {
            noOfBatt = (int) (sizeOfBatt / 3.5);
            textView.setText("You will require " + sizeOfBatt+ "number of batteries");

        }
       */
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "BatterySuggestedCalculation", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
