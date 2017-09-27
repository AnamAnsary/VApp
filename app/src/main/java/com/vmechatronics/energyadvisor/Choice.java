package com.vmechatronics.energyadvisor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.android.gms.appindexing.Action;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Choice extends AppCompatActivity {

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;
    Typeface font;

    HashMap<String, String> user;
    String email = "";
    String name = "";
    String phone = "";

    private String[] arrSolPan;
    private String[] arrWirCab;
    private String[] arrMNB;
    private String[] arrRoofTyp;
    private String[] arrShedTyp;
    private String[] arrMountType;
    private String[] arrMeter;
    private String[] arrPowerCut;
    private float[] arrbatterycapacity;

    ArrayList<String> arrInvTyp;

    private String insType = "";
    private String solType = "";
    private String solPan = "";
    private String invTyp = "";
    private String wirCab = "";
    private String MNB = "";
    private String roofTyp = "null";
    private String shedTyp = "null";
    private String mountStr = "null";
    private String meter = "";
    private String PowerCut="" ;

    //private float batterycapacity;
    private String units="";

    TextView tvRoofI;
    TextView tvShedI;
    TextView tvPCI;

    float totalresultsize;

    EditText etUnits;
    Button s;
    Button s2;
    Button s3;
    Button s4;
    Button s5;
    Button s6;
    Button s7;
    Button s8;
    Button bGetQuo;
    private String TAG = "Choice";
    private String getInverter_url = "https://www.vmechatronics.com/app/readdbase.php";
    private String emailVariable_url = "https://vmechatronics.com/app/EVerification_Variable.php";
    private String emailVerify_url = "https://vmechatronics.com/app/EmailVerify.php";

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get("email");
        name = user.get("name");
        phone = user.get("phone");

        s = (Button) findViewById(R.id.sSolarPanel);
        s2 = (Button) findViewById(R.id.sInvType);
        s3 = (Button) findViewById(R.id.sWirCab);
        s4 = (Button) findViewById(R.id.sMNB);
        s5 = (Button) findViewById(R.id.sRoofType);
        s6 = (Button) findViewById(R.id.sShedTyp);
        s7 = (Button) findViewById(R.id.sMeterType);
        s8 = (Button) findViewById(R.id.sPowerCut);

        bGetQuo = (Button) findViewById(R.id.bGetQuo);

        tvRoofI = (TextView) findViewById(R.id.tvRoofI);
        tvShedI = (TextView) findViewById(R.id.tvShedI);
        tvPCI = (TextView) findViewById(R.id.tvPCI);
        etUnits = (EditText) findViewById(R.id.etUnits);
        arrInvTyp = new ArrayList<String>();


        Bundle b1 = this.getIntent().getExtras();
        final String[] mainArray = b1.getStringArray("main");
        final float plantSize = b1.getFloat("plantSize");
        //final String ongird=b.getString("ON Grid");
        units = mainArray[2].toString();
        solType = mainArray[8].toString();
        insType = mainArray[7].toString();
       /* final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonResponse = null;
                try {
                    Log.w(TAG, "onResponse: " + response);
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jsonArray = jsonObj.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String inv_name = c.getString("inv_name");
                        arrInvTyp.add(inv_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetInverter gi = new GetInverter(solType, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Choice.this);
        queue.add(gi);*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getInverter_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.w(TAG, "onResponse: " + response);
                            JSONObject jsonObj = new JSONObject(response);
                            JSONArray jsonArray = jsonObj.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                String inv_name = c.getString("inv_name");
                                arrInvTyp.add(inv_name);
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
                params.put("solType", solType);
                return params;
            }
        };

        MySingleton.getInstance(Choice.this).addToRequestQueue(stringRequest);

        if (insType.equals("Roof Type")) {
            s5.setText("Roof Type");
            s5.setVisibility(View.VISIBLE);
            tvRoofI.setVisibility(View.VISIBLE);
        } else {
            s5.setVisibility(View.GONE);
            tvRoofI.setVisibility(View.GONE);
        }
        if (solType.equals("hybrid")) {
            s8.setText("Hours of Power Cut");
            s8.setVisibility(View.VISIBLE);
            tvPCI.setVisibility(View.VISIBLE);
        } else {
            s8.setVisibility(View.GONE);
            tvPCI.setVisibility(View.GONE);
        }//comment
        switch (solType) {
            case "ongrid":
                s8.setText("Hours of Power cut");
                s8.setVisibility(View.GONE);
                PowerCut = "null";
                totalresultsize = 0;

           /* if(arrInv.size()>0) {
                arrInv.clear();
            }*/
                //arrInvTyp.add(0,"Kaco");
                //arrInvTyp.add(1,"Delta");
                // arrInvTyp.add(2,"Voltronics");
                //Log.w(TAG, "INV TYPES:   Its working ON" );
                break;
            case "offgrid":
                s8.setText("Hours of Power cut ");
                s8.setVisibility(View.VISIBLE);


                if (arrInvTyp.size() > 0) {
                    arrInvTyp.clear();
                }
                //  arrInvTyp.add("Voltronics");----
                Log.w(TAG, "INV TYPES:   Its working off");
                break;
            default:
                arrInvTyp.add("Studer");
                arrInvTyp.add("Voltronics");
                Log.w(TAG, "onCreate:  I dont want this");
                break;
        }

        this.arrSolPan = new String[]{
                "Vikram Solar", "PV Powertech", "JJ Solar", "EMVEE", "Trina Solar", "Deshmukh Solar", "Rolta", "Navitas Solar"
        };
        this.arrWirCab = new String[]{
                "Lapp", "Polycab", "AE Solar"
        };
        this.arrMNB = new String[]{
                "Galvanised", "Stainless Steel"
        };
        this.arrShedTyp = new String[]{
                "Tin Shed", "Asbestos", "Others"
        };
        this.arrMountType = new String[]{
                "Panda Bear", "Cement Base", "S5"
        };
        this.arrMeter = new String[]{
                "Net Metering", "Gross Metering"
        };
        this.arrRoofTyp = new String[]{
                "Flat", "Shed", "Gabble or Saddle Roof", "Saw Tooth"
        };
        this.arrPowerCut = new String[]{
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
        };

        this.arrbatterycapacity=new float[]{3.5f,5.6f,8.9f,11.2f,16.2f,22.4f,33.6f,55f};


    // checking solar type is on gird or not
        //int powercut=Integer.parseInt("PowerCut");
      //  int zero=Integer.parseInt("0");
       // int twentyfour=Integer.parseInt("24");
       // powercut=PowerCut.compareTo("24");
       // if (PowerCut .equals ("0")) {
        //
      //  }
        //else if (powercut<= twentyfour && powercut > zero)//right or not?
            //(PowerCut <= "24" && PowerCut >= "0")

       //  { System.out.println(solType .equals("hybrid")  || solType .equals("offgrid") );}

        // rsolType=solType;

        //PowerCut!="0" && powercut<=tweentyfour){

        //else if {System.out.println(solType=="offgird");}
       //battery calculation
     //  units = b.getString(units);

     // int Ongird=Integer.parseInt(ongird);
      //  int Soltype=Integer.parseInt("solType");


   /*   if(solType.equals("offgrid"))// || solType.equals("hybrid"))
      {
        //  inputperhr = Float.parseFloat(units) / (30 * 24);

        //  r_battery = inputperhr * powercut;
     // }
        //else if ( solType.equals("hybrid")){

         // inputperhr = Float.parseFloat(units) / (30 * 24);
        //  powercut = Float.parseFloat(PowerCut);

       // r_battery = inputperhr * powercut;

            //usize=Integer.parseInt(units);


          //  try{
       //-------     powercut =Float.parseFloat(PowerCut);
            //}catch(NumberFormatException ex){ // handle your exception

//            }

        //---    inputperhr =Float.parseFloat(units) /(30*24);

         //  powercut=Integer.valueOf(PowerCut);

           // powercut=Integer.parseInt(PowerCut);
            //powercut = Integer.parseInt(PowerCut);
       //----    r_battery = inputperhr *powercut;

            //for (int i = 0; i<arrbatterysize; i++){
            //result = arrbatterysize -r_battery;

            // if (
            //result=r_battery;
            // arrbatterysize.equals(result)){
            //}
            // else
             //{
//ouble[] d=new ArrayList<Double>() {{for (String tempLongString : tempLongStrings) add(new Double(tempLongString));}}.toArray(new Double[tempLongStrings.length]);
            //double battery_Csize = Double.parseDouble(String.valueOf(arrbatterycapacity));

            temptotalresultsize = 55f;
            for (int i = 0; i < arrbatterycapacity.length; i++) {
                temptotalresultsize = Math.abs(r_battery - arrbatterycapacity[i]);
                if (temptotalresultsize <= totalresultsize) {
                    totalresultsize= temptotalresultsize;

                   i=ind;
                   // batterycapacity=arrbatterycapacity[i];
                }
            }
            batterycapacity=arrbatterycapacity[ind];

        }else if (solType.equals("hybrid")){
          inputperhr = Float.parseFloat(units) / (30 * 24);
         // powercut = Integer.parseInt(PowerCut);
          r_battery = inputperhr * powercut;
          temptotalresultsize = 55f;
          for (int i = 0; i < arrbatterycapacity.length; i++) {
              temptotalresultsize = Math.abs(r_battery - arrbatterycapacity[i]);
              if (temptotalresultsize <= totalresultsize) {
                  totalresultsize= temptotalresultsize;
                  i=ind;
                  // batterycapacity=arrbatterycapacity[i];
              }
          }
          batterycapacity=arrbatterycapacity[ind];

      }
                else {totalresultsize=0;}*/


        s.setText("Solar Panels");
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Choice.this, R.layout.spinner_row, arrSolPan);// ArrayAdapter<String> adapter = new ArrayAdapter<String>(Choice.this, R.layout.spinner_row, arrSolPan);//String0011
                new AlertDialog.Builder(Choice.this).setTitle("Select Solar Panel").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s.setText(arrSolPan[which]);
                        solPan = s.getText().toString();
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        s2.setText("Inverter types");
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrInvTyp);
                new AlertDialog.Builder(Choice.this).setTitle("Select Inverter").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s2.setText(arrInvTyp.get(which));
                        invTyp = s2.getText().toString().toLowerCase();
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });


        s3.setText("Wires/Cables");
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrWirCab);
                new AlertDialog.Builder(Choice.this).setTitle("Select Wire/Cables").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s3.setText(arrWirCab[which]);
                        wirCab = s3.getText().toString();
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });


        s4.setText("Mounting nut Bolts");
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrMNB);
                new AlertDialog.Builder(Choice.this).setTitle("Select Mounting Nut Bolts").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s4.setText(arrMNB[which]);
                        MNB = s4.getText().toString();

                        dialog.dismiss();
                    }
                }).create().show();
            }
        });


        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrRoofTyp);
                new AlertDialog.Builder(Choice.this).setTitle("Select Roof Type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s5.setText(arrRoofTyp[which]);
                        roofTyp = s5.getText().toString();
                        if (which == 1) {
                            s6.setText("Shed Type");
                            s6.setVisibility(View.VISIBLE);
                            tvShedI.setVisibility(View.VISIBLE);
                        } else if (which == 0) {
                            s6.setText("Mounting Structure");
                            s6.setVisibility(view.VISIBLE);
                            tvShedI.setVisibility(view.VISIBLE);

                        } else {
                            s6.setVisibility(View.GONE);
                            tvShedI.setVisibility(View.GONE);
                            shedTyp = "null";
                            mountStr = "null";
                        }
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (roofTyp.equals("Shed")) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrShedTyp);
                    new AlertDialog.Builder(Choice.this).setTitle("Select Shed type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            s6.setText(arrShedTyp[i]);
                            shedTyp = s6.getText().toString();
                            mountStr = "null";
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                } else if (roofTyp.equals("Flat")) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrMountType);
                    new AlertDialog.Builder(Choice.this).setTitle("Select Mounting Structure").setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            s6.setText(arrMountType[i]);
                            mountStr = s6.getText().toString();
                            shedTyp = "null";
                            dialogInterface.dismiss();
                        }
                    }).create().show();
                }
            }
        });

        s7.setText("Meter Type");
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrMeter);
                new AlertDialog.Builder(Choice.this).setTitle("Select Metering Type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s7.setText(arrMeter[which]);
                        meter = s7.getText().toString();
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        s8.setText("Power Cut");
        s8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Choice.this, R.layout.spinner_row, arrPowerCut);
                new AlertDialog.Builder(Choice.this).setTitle("Hours of Power Cut").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s8.setText(arrPowerCut[which]);
                        Log.w(TAG, "Checking Hours ");
                        PowerCut = s8.getText().toString();
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        bGetQuo.setTypeface(font);
        bGetQuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.w(TAG, "onClick: " + solPan + invTyp + wirCab + MNB + roofTyp + shedTyp + PowerCut);
                Log.w(TAG, "phone in choice class" +phone);

                if (solPan.length() != 0 && invTyp.length() != 0 && wirCab.length() != 0 && MNB.length() != 0 && roofTyp.length() != 0 && shedTyp.length() != 0 && meter.length() != 0 && PowerCut.length() != 0) {
                    if (checkInternetConnection()) {

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Choice.this);
                        Boolean IsGAcc = prefs.getBoolean("IsGp", false);
                        if (IsGAcc == false) {

                      /*      final Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject jsonResponse3 = null;
                                    try {
                                        JSONArray array = new JSONArray(response);
                                        jsonResponse3 = array.getJSONObject(0);
                                        boolean success = jsonResponse3.getBoolean("success");
                                        Log.w(TAG, "Before success");
                                        if (success) {
                                            //Toast.makeText(Choice.this, "Your E-mail has been verified!", Toast.LENGTH_LONG).show();
                                            Intent i1 = new Intent(Choice.this, Quote.class);
                                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            Bundle b = new Bundle();
                                            b.putStringArray("main", mainArray);
                                            b.putStringArray("choice", new String[]{solPan, invTyp, wirCab, MNB, roofTyp, shedTyp, mountStr, meter,PowerCut});
                                            b.putFloat("plantSize", plantSize);
                                            // b.putFloat("batterycapacity", batterycapacity);
                                            b.putFloatArray("arrbatterycapacity",arrbatterycapacity);
                                            b.putFloat("totalresultsize",totalresultsize);
                                            i1.putExtras(b);
                                            startActivity(i1);

                                        } else {
                                            android.support.v7.app.AlertDialog.Builder builder1 = null;
                                            builder1 = new android.support.v7.app.AlertDialog.Builder(Choice.this);
                                            builder1.setMessage("Your E-mail has not been verified. Please verify your E-mail first. If you have not got verification E-mail, please click on resend button")
                                                    .setTitle("Email Verification")
                                                    .setPositiveButton("OK", null)
                                                    .setNegativeButton("Resend", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            //So sth here when "resend" clicked.
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

                                                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Choice.this);
                                                                            builder.setMessage("A verification link has been emailed to you. Please verify your E-mail id!")
                                                                                    .setNegativeButton("OK", null)
                                                                                    .setTitle("Verify E-mail")
                                                                                    .create()
                                                                                    .show();
                                                                        } else {
                                                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Choice.this);
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
                                                            EMotp emveri = new EMotp(email, name, responseListener2);
                                                            RequestQueue queue = Volley.newRequestQueue(Choice.this);
                                                            queue.add(emveri);
                                                        }
                                                    });

                                            builder1.create()
                                                    .show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            EmailVariable EV = new EmailVariable(email, responseListener3);
                            RequestQueue queue2 = Volley.newRequestQueue(Choice.this);
                            queue2.add(EV);
*/
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, emailVariable_url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            try {
                                                JSONArray array = new JSONArray(response);
                                                JSONObject jsonResponse3 = array.getJSONObject(0);
                                                boolean success = jsonResponse3.getBoolean("success");
                                                Log.w(TAG, "Before success");
                                                if (success) {
                                                    //Toast.makeText(Choice.this, "Your E-mail has been verified!", Toast.LENGTH_LONG).show();
                                                    Intent i1 = new Intent(Choice.this, Quote.class);
                                                    i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    Bundle b = new Bundle();
                                                    b.putStringArray("main", mainArray);
                                                    b.putStringArray("choice", new String[]{solPan, invTyp, wirCab, MNB, roofTyp, shedTyp, mountStr, meter,PowerCut});
                                                    b.putFloat("plantSize", plantSize);
                                                    // b.putFloat("batterycapacity", batterycapacity);
                                                    b.putFloatArray("arrbatterycapacity",arrbatterycapacity);
                                                    b.putFloat("totalresultsize",totalresultsize);
                                                    i1.putExtras(b);
                                                    startActivity(i1);

                                                } else {
                                                    android.support.v7.app.AlertDialog.Builder builder1 = null;
                                                    builder1 = new android.support.v7.app.AlertDialog.Builder(Choice.this);
                                                    builder1.setMessage("Your E-mail has not been verified. Please verify your E-mail first. If you have not got verification E-mail, please click on resend button")
                                                            .setTitle("Email Verification")
                                                            .setPositiveButton("OK", null)
                                                            .setNegativeButton("Resend", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, emailVerify_url,
                                                                            new Response.Listener<String>() {
                                                                                @Override
                                                                                public void onResponse(String response) {

                                                                                    try {
                                                                                        JSONArray array = new JSONArray(response);
                                                                                        JSONObject jsonResponse2 = array.getJSONObject(0);
                                                                                        Log.w(TAG, "onResponse: jsonresponse2" + response.toString());
                                                                                        boolean success = jsonResponse2.getBoolean("success");

                                                                                        if (success) {

                                                                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Choice.this);
                                                                                            builder.setMessage("A verification link has been emailed to you. Please verify your E-mail id!")
                                                                                                    .setNegativeButton("OK", null)
                                                                                                    .setTitle("Verify E-mail")
                                                                                                    .create()
                                                                                                    .show();
                                                                                        } else {
                                                                                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Choice.this);
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
                                                                            params.put("name",name);
                                                                            return params;
                                                                        }
                                                                    };

                                                                    MySingleton.getInstance(Choice.this).addToRequestQueue(stringRequest);

                                                                }
                                                            });

                                                    builder1.create()
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
                            MySingleton.getInstance(Choice.this).addToRequestQueue(stringRequest);

                        }


                        else {

                         /*   if(phone==null)
                            {
                                Intent intent = new Intent(Choice.this, mobileVerification.class);
                                Bundle b = new Bundle();
                                b.putString("name",name);
                                b.putString("email",email);
                                //b.putStringArray("main", mainArray);
                                //b.putStringArray("choice", new String[]{solPan, invTyp, wirCab, MNB, roofTyp, shedTyp, mountStr, meter, PowerCut});
                                //b.putFloat("plantSize", plantSize);
                                // b.putFloat("batterycapacity", batterycapacity);
                                //b.putFloatArray("arrbatterycapacity", arrbatterycapacity);
                                //b.putFloat("totalresultsize", totalresultsize);
                                intent.putExtras(b);
                               // startActivityForResult(intent, 10);
                                startActivity(intent);
                            }
                            */

                            Intent i1 = new Intent(Choice.this, Quote.class);
                            Bundle b = new Bundle();
                            b.putStringArray("main", mainArray);
                            b.putStringArray("choice", new String[]{solPan, invTyp, wirCab, MNB, roofTyp, shedTyp, mountStr, meter, PowerCut});
                            b.putFloat("plantSize", plantSize);
                            // b.putFloat("batterycapacity", batterycapacity);
                            b.putFloatArray("arrbatterycapacity", arrbatterycapacity);
                            b.putFloat("totalresultsize", totalresultsize);
                            i1.putExtras(b);
                            startActivity(i1);
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

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Choice.this);
                    builder.setMessage("Please Select All Values!")
                            .setNegativeButton("Retry", null)
                            .setTitle("Invalid Inputs")
                            .create()
                            .show();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        }

    public void onSolPanDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.solarp_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db4btn1);
        dialogButton.setTypeface(font);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onMntPB(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.panelclaw.com/index.php/roof-mount.html"));
        startActivity(intent);
    }

    public void onMnts5(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.s-5.com/solar/index_2483.cfm#30"));
        startActivity(intent);
    }

    public void onMeterDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.meter_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db5btn1);
        dialogButton.setTypeface(font);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onRRDB(View v) {
        final Dialog dialog = new Dialog(this);
        if (shedTyp.equals("Shed Type")) {
            dialog.setContentView(R.layout.rs_db);
            Button dialogButton = (Button) dialog.findViewById(R.id.db6btn1);
            dialogButton.setTypeface(font);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            dialog.setContentView(R.layout.rf_db);
            Button dialogButton = (Button) dialog.findViewById(R.id.db7btn1);
            dialogButton.setTypeface(font);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void onMNBDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.mnb_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db8btn1);
        dialogButton.setTypeface(font);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onInvDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.inv_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db9btn1);
        dialogButton.setTypeface(font);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onWirDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.wire_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db10btn1);
        dialogButton.setTypeface(font);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onRoofDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.roof_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db11btn1);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void onPCut(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pcut);
        Button dialogButton = (Button) dialog.findViewById(R.id.dbPCbtn1);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            return true;
        } else {
            // not connected to the internet
            //Toast.makeText(Choice.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
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
                "Choice Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.energyadvisor/http/host/path")
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
                "Choice Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.vmechatronics.energyadvisor/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);
        //client.disconnect();
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Choice_Solar_Calcy", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
