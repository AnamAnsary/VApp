package com.vmechatronics.energyadvisor;

import android.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class mainActivity extends AppCompatActivity {

    private static final String TAG = "mainActivity";


    UserSessionManager session;
    HashMap<String, String> user;
    private FirebaseAnalytics mFirebaseAnalytics;
    Typeface font;

    ImageButton bCalc;
    Button s;
    Button s1;
    Button s2;
    Button s3;
    Button s4;

    Bundle b;

    EditText etTotal;
    EditText etUnits;
    EditText etAvailArea;
    EditText etSancLoad;

    String sp;
    String lt1pmin;
    String lt1pmax;
    String lt3pmin;
    String lt3pmax;
    String ht415min;
    String ht415max;
    String ht11min;
    String ht11max;
    String tarrif;
    String pdg;
    String gl;
    String ongird;

    String stateSelected = "";
    String state;
    String userTyp = "";
    String solarTyp = "";
    String connTyp = "";
    String insTyp = "";
    String units = "";
    String avail = "";
    String cost = "";
    String sanc = "";

    float finArea;
    float finPP;
    float plantSize;
    float finSancVal;
    float p1, p2;
    float rawPP;
    float yearlygen;

    private String[] arrayStates;
    private String[] arrayUserType;
    private ArrayList<String> arrayConnection;
    private String[] arraySolar;
    private String[] arrayIns;
    private String key = "main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");

        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        state = user.get("state");

        s = (Button) findViewById(R.id.sUserType);
        s1 = (Button) findViewById(R.id.sState);
        s1.setText(state);
        s2 = (Button) findViewById(R.id.sConnType);
        s3 = (Button) findViewById(R.id.sInsType);
        s4 = (Button) findViewById(R.id.sSolarType);

        etTotal = (EditText) findViewById(R.id.etTotal);
        etAvailArea = (EditText) findViewById(R.id.etAvailArea);
        etSancLoad = (EditText) findViewById(R.id.etSancLoad);
        etUnits = (EditText) findViewById(R.id.etUnits);

        arrayStates = new String[]{
                "Andhra Pradesh", "Assam", "Bihar", "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand", "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "" +
                "Odisha (Orissa)", "Punjab", "Rajasthan", "Tamil Nadu", "Telangana", "Uttar Pradesh", "Uttarakhand", "West Bengal", "Andaman and Nicobar Islands", "Chandigarh",
                "Dadra and Nagar Haveli", "Daman and Diu", "Lakshadweep", "Delhi - National Capital Territory", "Puducherry (Pondicherry)"
        };

        arrayUserType = new String[]{
                "Domestic", "Commercial"
        };

        arrayConnection = new ArrayList<String>();
        arrayConnection.add("LT 1P");
        arrayConnection.add("LT 3P");
        arrayConnection.add("HT 415V");
        arrayConnection.add("HT 11kV");

        arraySolar = new String[]{
                "ON Grid", "OFF Grid", "Hybrid"
        };

        arrayIns = new String[]{
                "Ground Type", "Roof Type"
        };

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity.this, R.layout.spinner_row, arrayUserType);
                new AlertDialog.Builder(mainActivity.this).setTitle("Select User Type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        s.setText(arrayUserType[which]);
                        userTyp = arrayUserType[which];
                        s2.setText("Connection Type");
                        connTyp = "";
                        if (which == 1) {
                            if (arrayConnection.size() == 2) {
                                arrayConnection.add(2, "HT 415V");
                                arrayConnection.add(3, "HT 11kV");
                            }
                        } else {
                            if (arrayConnection.size() == 4) {
                                arrayConnection.remove(2);
                                arrayConnection.remove(2);
                            }
                        }
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity.this, R.layout.spinner_row, arrayStates);
                new AlertDialog.Builder(mainActivity.this).setTitle("Select State").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s1.setText(arrayStates[i]);
                        state = arrayStates[i];
                    }
                }).create().show();
            }
        });
        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity.this, R.layout.spinner_row, arrayConnection);
                new AlertDialog.Builder(mainActivity.this).setTitle("Select Connection Type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s2.setText(arrayConnection.get(i));
                        connTyp = arrayConnection.get(i).toString();
                    }
                }).create().show();
            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity.this, R.layout.spinner_row, arrayIns);
                new AlertDialog.Builder(mainActivity.this).setTitle("Select Installation Type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s3.setText(arrayIns[i]);
                        insTyp = arrayIns[i];
                    }
                }).create().show();
            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(mainActivity.this, R.layout.spinner_row, arraySolar);
                new AlertDialog.Builder(mainActivity.this).setTitle("Select Solar Type").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s4.setText(arraySolar[i]);
                        switch (arraySolar[i]) {
                            case "ON Grid":
                                solarTyp = "ongrid";
                                break;
                            case "OFF Grid":
                                solarTyp = "offgrid";
                                break;
                            default:
                                solarTyp = "hybrid";
                                break;
                        }
                    }
                }).create().show();
            }
        });

        bCalc = (ImageButton) findViewById(R.id.bCalc);
        bCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                units = etUnits.getText().toString();
                sanc = etSancLoad.getText().toString();
                cost = etTotal.getText().toString();
                avail = etAvailArea.getText().toString();


                if (units.length() != 0 && sanc.length() != 0 && cost.length() != 0 && solarTyp.length() != 0 && insTyp.length() != 0 && userTyp.length() != 0 && connTyp.length() != 0) {

                    if (checkInternetConnection()) {
                        final ProgressDialog pd = ProgressDialog.show(mainActivity.this, "", "Connecting...", true);
                        new Thread(new Runnable() {
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
                                                sp = jsonResponse.getString("sp");
                                                lt1pmin = jsonResponse.getString("lt1pmin");
                                                lt1pmax = jsonResponse.getString("lt1pmax");
                                                lt3pmin = jsonResponse.getString("lt3pmin");
                                                lt3pmax = jsonResponse.getString("lt3pmax");
                                                ht415min = jsonResponse.getString("ht415min");
                                                ht415max = jsonResponse.getString("ht415max");
                                                ht11min = jsonResponse.getString("ht11min");
                                                ht11max = jsonResponse.getString("ht11max");
                                                tarrif = jsonResponse.getString("tarrif");
                                                pdg = jsonResponse.getString("pdg");
                                                gl = jsonResponse.getString("gl");

                                                finArea = Float.parseFloat(avail) / 100;
                                                finSancVal = Float.parseFloat(sanc) * Float.parseFloat(sp);
                                                if (finArea > finSancVal) {
                                                    rawPP = finSancVal;
                                                } else {
                                                    rawPP = finArea;
                                                }
                                                Log.w(TAG, "Raw Plant Size " + rawPP);
                                                //finPP = Math.round(rawPP);
                                                finPP = Math.round(rawPP - 0.5f) + 0.5f;
                                                Log.w(TAG, "Rounded Plant Size  " + finPP);
                                                switch (connTyp) {
                                                    case "LT 1P":
                                                        p1 = Float.parseFloat(lt1pmin);
                                                        p2 = Float.parseFloat(lt1pmax);

                                                        break;
                                                    case "LT 3P":
                                                        p1 = Float.parseFloat(lt3pmin);
                                                        p2 = Float.parseFloat(lt3pmax);
                                                        break;
                                                    case "HT 415V":
                                                        p1 = Float.parseFloat(ht415min);
                                                        p2 = Float.parseFloat(ht415max);
                                                        break;
                                                    case "HT 11kV":
                                                        p1 = Float.parseFloat(ht11min);
                                                        p2 = Float.parseFloat(ht11max);
                                                        break;
                                                }
                                                if (finPP >= p1 && finPP <= p2) {
                                                    plantSize = finPP;
                                                } else {
                                                    plantSize = -1;
                                                }
                                                Log.w(TAG, "Plant Size " + plantSize);
                                                yearlygen = plantSize * Float.parseFloat(pdg) * 365;
                                                Log.w(TAG, "onResponse1: " + sp + " " + lt1pmax + " " + lt1pmin + " " + lt3pmax + " " + lt3pmin + " " + ht11max + " " + ht11min + " " + ht415max + " " + ht415min + " " + tarrif + " " + pdg + " " + gl);

                                                if (plantSize == -1) {
                                                    pd.dismiss();

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity.this);
                                                    builder.setMessage("The state limit for " + connTyp + " is Minimum: " + p1 + " Maximum: " + p2 + " while your plantsize is: " + finPP)
                                                            .setNegativeButton("OK", null)
                                                            .setTitle("Wrong Combinations")
                                                            .create()
                                                            .show();

                                                } else {


                                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mainActivity.this);
                                                    boolean Islogin = prefs.getBoolean("Islogin", false); // get value of last login status
                                                    if (Islogin) {   // condition true means user is already login
                                                        b = new Bundle();
                                                        b.putFloat("plantSize", plantSize);
                                                        b.putStringArray("main", new String[]{userTyp, state, units, cost, connTyp, avail, sanc, insTyp, solarTyp, yearlygen + "", tarrif,});
                                                        //b.putString("ON Grid" ,ongird);
                                                        b.putString("units", units);
                                                        Intent i = new Intent(mainActivity.this, Choice.class);
                                                        i.putExtras(b);
                                                        startActivityForResult(i, 1);
                                                        pd.dismiss();
                                                    }

                                                    /*if (session.isUserLoggedIn()) {
                                                        if(session.isUserVerified()) {
                                                            Intent i1 = new Intent(mainActivity.this, Choice.class);
                                                            i1.putExtras(b);
                                                            startActivity(i1);
                                                            pd.dismiss();
                                                        }else {
                                                            pd.dismiss();
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity.this);
                                                            builder.setMessage("Please Verify Your Number Before Proceeding further")
                                                                    .setNegativeButton("OK", null)
                                                                    .setTitle("Verification")
                                                                    .create()
                                                                    .show();
                                                        }
                                                    } */
                                                    else {
                                                        Intent i1 = new Intent(mainActivity.this, Login.class);
                                                        b = new Bundle();
                                                        b.putString("class", "mainActivity");
                                                        i1.putExtras(b);
                                                        startActivity(i1);
                                                        pd.dismiss();
                                                    }
                                                }

                                            } else {
                                                pd.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity.this);
                                                builder.setMessage("Some error occurred, try again later!")
                                                        .setNegativeButton("OK", null)
                                                        .setTitle("Connection Error")
                                                        .create()
                                                        .show();
                                            }
                                        } catch (JSONException ee) {
                                            ee.printStackTrace();
                                        }
                                    }
                                };
                                Populate popl = new Populate(state, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(mainActivity.this);
                                queue.add(popl);
                            }
                        }).start();
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity.this);
                    builder.setMessage("Please Fill or Select all the Fields")
                            .setNegativeButton("Retry", null)
                            .setTitle("Fields Missing")
                            .create()
                            .show();
                }
            }
        });

    }

    public void onStateDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.state_db);

        Button dialogButton = (Button) dialog.findViewById(R.id.db1btn1);
        dialogButton.setTypeface(font);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onSolarDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.solart_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db3btn1);
        dialogButton.setTypeface(font);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onConnDB(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.conn_db);
        Button dialogButton = (Button) dialog.findViewById(R.id.db2btn1);
        // if button is clicked, close the custom dialog
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
            //Toast.makeText(mainActivity.this, "Please check your Internet Connection!!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "SolarCalculator", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}