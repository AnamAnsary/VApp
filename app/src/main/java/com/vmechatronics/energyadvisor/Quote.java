package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Quote extends Activity {


    String usertype;
    String state;
    String unitsPM;
    String costPU;
    String connType;
    String availAr;
    String sancLoad;
    String installType;
    String solarType;
    String solPan;
    String invType;
    String wirCab;
    String mnb;
    String roofType;
    String shedType;
    String mountStr;
    String meter;
    String sbatterysize;
    String Powercut;
    String rbattery;
    int lastcost;
    String email = "";
    String phone = "";
    String qid = "";
    String qdate = "";
    String name = "";
    String pdf_url = "";

    UserSessionManager session;
    HashMap<String, String> userDetail;
    private FirebaseAnalytics mFirebaseAnalytics;
    float battery_size;
    float plantSize;

    int arrLngth;
    int q1 = 0, q2 = 0;
    int c1 = 0, c2 = -1;
    int rem;
    float powercut;
    float inputperhr;
    int ind;
    float totalresultsize;

    float temptotalresultsize;
    float totalbatterycost=0;
    float totalSize;
    float totalCost = 0;
    float totalpanelCost = 0;
    float totalmountCost = 0;
    float totalwireCost = 0;
    float totalnutCost = 0;
    float finalCost = 0;
    float yearlygen = 0;
    float diff = 0;
    float tarrif = 0;
    int breakEven;
    float unitsYear = 0;
    float r_battery;
    private float batterycapacity;
    float panelCost = 0;
    float mountCost = 0;
    float wireCost = 0;
    float nutCost = 0;
    float batterycost = 0;
    Bundle b;

    ArrayList<Float> inv_size;
    ArrayList<Float> inv_cost;

    ImageButton bBkNow;
    ImageButton bGoHom;
    ImageButton bDowQuo;
    TextView tv1;
    TextView tvRefID;
    TextView tvName;
    private String TAG = "Quote";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());
        /*double
        Intent  receiveIntent = this.getIntent();
        "" = receiveIntent.("", )
        "" = receiveIntent.("", defaultValue)*/

        userDetail = session.getUserDetails();
        email = userDetail.get("email");
        phone = userDetail.get("phone");
        name = userDetail.get("name");
        Log.w(TAG, "onCreate: " + email + phone);

        tv1 = (TextView) findViewById(R.id.tv1);
        tvRefID = (TextView) findViewById(R.id.tvRefID);
        tvName = (TextView) findViewById(R.id.tvName);

        bDowQuo = (ImageButton) findViewById(R.id.bDowQuo);
        tvName.setText("Hello " + name + ",");

        final ProgressDialog pd = ProgressDialog.show(Quote.this, "", "Calculating...", true);
        new Thread(new Runnable() {
            public void run() {

                Bundle b = Quote.this.getIntent().getExtras();
                final String[] mainArray = b.getStringArray("main");
                final String[] choiceArr = b.getStringArray("choice");
                final float[]arrbatterycapacity=b.getFloatArray("arrbatterycapacity");
               // float totalresultsize = b.getFloat("totalresultsize");
                //  final String rsolType=b.getString("rsolType");

                sbatterysize = Float.toString(batterycapacity);//
                // sbatterysize=String.valueOf(batterysize);
              //  batterysize = ("batterycapacity");

                plantSize = b.getFloat("plantSize");
                usertype = mainArray[0];
                state = mainArray[1];
                unitsPM = mainArray[2];
                costPU = mainArray[3];
                connType = mainArray[4];
                availAr = mainArray[5];
                sancLoad = mainArray[6];
                installType = mainArray[7];
                solarType = mainArray[8];
                yearlygen = Float.parseFloat(mainArray[9]);
                tarrif = Float.parseFloat(mainArray[10]);
                solPan = choiceArr[0];
                invType = choiceArr[1];
                wirCab = choiceArr[2];
                mnb = choiceArr[3];
                roofType = choiceArr[4];
                shedType = choiceArr[5];
                mountStr = choiceArr[6];
                meter = choiceArr[7];
                Powercut=choiceArr[8];

//------------------battery calculation start from here------------

             if (solarType.equals("ongrid"))
              {powercut=0;r_battery=0;batterycapacity=0;batterycost=0;}
               else{ powercut=Float.parseFloat(Powercut);
                inputperhr =Float.parseFloat(unitsPM) /(30*24);
                r_battery = inputperhr *powercut;}
                // batterysize=choiceArr[8];

                if(solarType.equals("offgrid"))// || solType.equals("hybrid"))
                {

                    inputperhr = Float.parseFloat(unitsPM) / (30 * 24);

                    r_battery = inputperhr * powercut;
                    totalresultsize = Math.abs(arrbatterycapacity[0] - r_battery);


                    temptotalresultsize = 55f;
                    for (int i = 0; i <arrbatterycapacity.length; i++) {

                        temptotalresultsize = Math.abs(r_battery - arrbatterycapacity[i]);

                       // Log.w(TAG, "arrbateerysize" + arrbatterycapacity[i]);
                      //  Log.w(TAG, "temp" + temptotalresultsize);
                        if(  temptotalresultsize <= totalresultsize) {
                            ind=i;
                            totalresultsize = temptotalresultsize;

                                Log.w(TAG, "i" + arrbatterycapacity[i]);
                               // Log.w(TAG, "i" + ind);
                                //totalresultsize = temptotalresultsize;
                            }
                       /* else {
                            temptotalresultsize <= totalresultsize;
                            ind = i;
                           otalresultsize = temptotalresultsize;
                            batterycapacity=arrbatterycapacity[ind];
                            ind = i;
                            batterycapacity = arrbatterycapacity[ind];
                        }*/

                    } batterycapacity = arrbatterycapacity[ind];
                    sbatterysize = Float.toString(batterycapacity);

                }else if (solarType.equals("hybrid")) {
                    inputperhr = Float.parseFloat(unitsPM) / (30 * 24);

                    r_battery = inputperhr * powercut;

                    totalresultsize = Math.abs(arrbatterycapacity[0] - r_battery);

                    temptotalresultsize = 55f;
                    for (int i = 0; i < arrbatterycapacity.length; i++) {

                        temptotalresultsize = Math.abs(arrbatterycapacity[i]-r_battery );

                        Log.w(TAG, "arrbateerysize" + arrbatterycapacity[i]);
                        Log.w(TAG, "temp" + temptotalresultsize);
                            ind=i;
                           // Log.w(TAG, "i+");
                        if (temptotalresultsize <= totalresultsize
                                ) {
                            //Log.w(TAG, "i" + ind);
                            totalresultsize = temptotalresultsize;
                        }
                            /*ind = i;
                             batterycapacity = arrbatterycapacity[ind];
                            ind = i;
                            batterycapacity=arrbatterycapacity[i];
                            batterycapacity = arrbatterycapacity[ind];
                            }
                            batterycapacity=arrbatterycapacity[ind];
                            ind = i;*/
                    }
                    batterycapacity = arrbatterycapacity[ind];
                    sbatterysize = Float.toString(batterycapacity);
                    rbattery=Float.toString(r_battery);

                }else {}

                Log.w(TAG, "Plant Size " + plantSize);
                Log.w(TAG,"rbattery"+r_battery);
                Log.w(TAG,"totalvalue"+totalresultsize);
                Log.w(TAG,"temptotalsize"+temptotalresultsize);
                Log.w(TAG, "batterysize" +batterycapacity);
                Log.w(TAG,"battery"+sbatterysize);
                Log.w(TAG, "user: " + usertype + ", state: " + state + ", unitsPM: " + unitsPM + ", costPU: " + costPU + ", concTyp: " + connType + ", Availarea: " + availAr + ", sancLoad: " + sancLoad + ", installType: " + installType);
                Log.w(TAG, "solar: " + solarType + ", solPan: " + solPan + ", invTyp: " + invType + ", wirCab: " + wirCab + ", mnb: " + mnb + ", rooftype: " + roofType + ", shedtype: " + shedType + ", mountStr: " + mountStr + "meter: " + meter);
                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        JSONObject jsonResponse = null;

                        try {
                            JSONArray array = new JSONArray(response);
                            jsonResponse = array.getJSONObject(0);
                           /* Log.w(TAG, "onResponse: after try");
                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());*/
                            boolean success = jsonResponse.getBoolean("success");
                            Log.w(TAG, "onResponse: jsonresponse" + success);
                            if (success) {

                                panelCost = Float.parseFloat(jsonResponse.getString("panel_cost"));
                                mountCost = Float.parseFloat(jsonResponse.getString("mount_cost"));
                                wireCost = Float.parseFloat(jsonResponse.getString("wire_cost"));
                                nutCost = Float.parseFloat(jsonResponse.getString("nut_cost"));

                                JSONArray arrJSONSize = jsonResponse.getJSONArray("inv_size");
                                JSONArray arrJSONCost = jsonResponse.getJSONArray("inv_cost");

                                batterycost = Float.parseFloat(jsonResponse.getString("battery_cost"));

                                arrLngth = arrJSONCost.length();

                                inv_size = new ArrayList<Float>();
                                inv_cost = new ArrayList<Float>();
                                Log.w(TAG, "Array length " + arrLngth);
                                for (int i = 0; i < arrJSONCost.length(); i++) {
                                    inv_size.add(Float.parseFloat(arrJSONSize.getString(i)));
                                    inv_cost.add(Float.parseFloat(arrJSONCost.getString(i)));
                                }
                                c1 = arrLngth - 1;
                                for (int i = 0; i < arrLngth; i++) {
                                    if (inv_size.get(i) >= plantSize) {
                                        c1 = i;
                                        break;
                                    }
                                }
                                if (plantSize <= inv_size.get(c1)) {
                                    q1 = 1;
                                    rem = 0;
                                } else {
                                    q1 = (int) (plantSize / inv_size.get(c1));
                                    rem = (int) (plantSize % inv_size.get(c1));
                                }
                                if (rem > 0) {
                                    for (int i = 0; i < arrLngth; i++) {
                                        if (rem <= inv_size.get(i)) {
                                            c2 = i;
                                            q2 = 1;
                                        }
                                    }
                                }
                                if (q2 > 0) {
                                    totalSize = inv_size.get(c1) * q1 + inv_size.get(c2) * q2;
                                    totalCost = inv_cost.get(c1) * q1 + inv_cost.get(c2) * q2;
                                    Log.w(TAG, "Your Selection " + invType);
                                    Log.w(TAG, "Total Size: " + inv_size.get(c1) + "*" + q1 + " + " + inv_size.get(c2) + "*" + q2 + " = " + totalSize);
                                    Log.w(TAG, "Total Cost: " + inv_cost.get(c1) + "*" + q1 + " + " + inv_cost.get(c2) + "*" + q2 + " = " + totalCost);
                                } else {
                                    totalSize = inv_size.get(c1) * q1;
                                    totalCost = inv_cost.get(c1) * q1;
                                    Log.w(TAG, "Your Selection " + invType);
                                    Log.w(TAG, "Total Size: " + inv_size.get(c1) + "*" + q1 + " = " + totalSize);
                                    Log.w(TAG, "Total Cost: " + inv_cost.get(c1) + "*" + q1 + " = " + totalCost);
                                }
                                totalbatterycost=batterycost;
                                totalmountCost = plantSize * mountCost;


                                totalnutCost = plantSize * nutCost;

                                totalpanelCost = plantSize * panelCost;

                                totalwireCost = plantSize * wireCost;

                                finalCost = totalpanelCost + totalwireCost + totalmountCost + totalnutCost + totalCost+totalbatterycost ;
                                finalCost = (float) (finalCost * (1.25));

                                Log.w(TAG, "Final Cost: " + finalCost);

                                float cashBack = finalCost;
                                float tempCost = finalCost;
                                float tempNew;
                                float totBenefit = 0;
                                unitsYear = Float.parseFloat(unitsPM) * 12;
                                float unitCost = Float.parseFloat(costPU);
                                float savings = 0;
                                if (yearlygen > unitsYear) {
                                    diff = yearlygen - unitsYear;
                                } else {
                                    diff = 0;
                                }
                                float yearlyDep;
                                for (int i = 0; totBenefit <= cashBack; i++) {
                                    Log.w(TAG, "Yearly units " + unitsYear);
                                    Log.w(TAG, "Per Unit Cost" + unitCost);
                                    Log.w(TAG, "savings on generation " + diff * tarrif + " tarrif " + tarrif + " diff " + diff);
                                    savings = unitsYear * unitCost + diff * tarrif;

                                    Log.w(TAG, "Savings for year  " + (i + 1) + " is : " + savings);

                                    tempNew = (float) (0.8 * tempCost);
                                    yearlyDep = (float) (0.3 * tempNew);
                                    Log.w(TAG, "Income tax benifit for year " + (i + 1) + " is " + yearlyDep);

                                    totBenefit = totBenefit + savings + yearlyDep;

                                    if (totBenefit >= cashBack) {
                                        Log.w(TAG, "Break Even Years " + (i + 1));
                                        breakEven = i + 1;
                                        break;
                                    }

                                    Log.w(TAG, "Total benifit " + totBenefit);
                                    unitsYear = (float) (unitsYear - (unitsYear * 0.01));
                                    unitCost = (float) (unitCost + (unitCost * 0.1));
                                    tempCost = tempCost - tempNew;

                                }
                                Log.w(TAG, "Break Even Years: " + breakEven);

                                final Response.Listener<String> responseListener = new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response){

                                        JSONObject jsonResponse = null;
                                        //JSONObject =  new JSONObject(response);

                                        try {

                                            JSONArray array= new JSONArray(response);
                                            jsonResponse = array.getJSONObject(0);

                                            Log.w(TAG, "onResponse: jsonresponse" + response.toString());//tostring
                                            boolean success = jsonResponse.getBoolean("success");

                                            if (success) {
                                                qid = jsonResponse.getString("qid");
                                                qdate = jsonResponse.getString("qdate");
                                                tvRefID.setText("Quote ID: " + qid);
                                                Log.w(TAG,"QQ"+qid+qdate);

                                                final Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {

                                                        JSONObject jsonResponse = null;
                                                        try {

                                                            JSONArray array = new JSONArray(response);
                                                            jsonResponse = array.getJSONObject(0);
                                                            Log.w(TAG, "Mail PDF: jsonresponse" + response.toString());
                                                            boolean success = jsonResponse.getBoolean("success");
                                                            if (success) {
                                                                pd.dismiss();
                                                                pdf_url = "https://vmechatronics.com/quotes/" + jsonResponse.getString("pdf") + ".pdf";
                                                            } else {
                                                                pd.dismiss();
                                                                AlertDialog.Builder builder = new AlertDialog.Builder(Quote.this);
                                                                builder.setMessage("Please Try again later, some Error Occured in generating PDF")
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
                                                MakePDF mpdf = new MakePDF(qid, qdate, name, email, state, solarType, solPan, roofType, plantSize + "", breakEven + "", invType, shedType, wirCab, finalCost, unitsYear + "", costPU, yearlygen + "", mountStr, tarrif + "", sbatterysize+"", responseListener);
                                                RequestQueue queue = Volley.newRequestQueue(Quote.this);
                                                queue.add(mpdf);
                                            } else {
                                                pd.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Quote.this);
                                                builder.setMessage("Oops! Something went wrong, try again later.")
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
                                lastcost = Math.round(finalCost);
                                InsertQuote insQ = new InsertQuote(email, phone,lastcost,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Quote.this);
                                queue.add(insQ);

                            } else {
                                pd.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Quote.this);
                                builder.setMessage("Please Try again later, some Error Occured")
                                        .setNegativeButton("OK", null)
                                        .setTitle("Error")
                                        .create()
                                        .show();
                            }
                        } catch (JSONException ee) {
                            pd.dismiss();
                            ee.printStackTrace();
                        }
                    }
                };

                Calculate calc = new Calculate(solarType, invType, solPan, mountStr, wirCab, mnb, sbatterysize, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Quote.this);
                queue.add(calc);

            }
        }).start();
        String first = "Your Quote for the Solar Power system has been e-mailed to you and your Download is available here. Thank you for taking your First Step towards ";
        String next = "<font color='#72CD27'><b>Going Green!!</b></font>";
        tv1.setText(Html.fromHtml(first + next));


        bBkNow = (ImageButton) findViewById(R.id.bBkNow);
        bBkNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Quote.this, Book.class);
                i1.putExtra("qid", qid);
                i1.putExtra("qdate", qdate);
                startActivity(i1);
            }
        });

        bDowQuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(pdf_url);
                Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i3);
            }
        });
        bGoHom = (ImageButton) findViewById(R.id.bGoHom);
        bGoHom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent igh = new Intent(Quote.this, Home.class);
                igh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                igh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(igh);
                finish();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Quote Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.diysolar/http/host/path")
        );
       // AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Quote Page", // TODO: Define a title for the content shown.
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
        mFirebaseAnalytics.setCurrentScreen(this, "Quote_Solar", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
