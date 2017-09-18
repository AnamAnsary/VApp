package com.vmechatronics.energyadvisor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by root on 24/12/16.
 */
public class  OrderBatt extends AppCompatActivity {

    private static final String TAG = "OrderBatt";
    Button BCharge;
    Button BMinHrs;
    EditText ELoad;
    EditText ETLoad;
    Button BPowerC;
    Button bOrd;
    TextView disp_name;
    Button s1;
    Button BPC;

    private String[] arrPowerCut;
    private String[] arrCharge;
    private String[] arrayBattery;
    private String[] arrPC;

    UserSessionManager session;
    HashMap<String, String> user;
    private FirebaseAnalytics mFirebaseAnalytics;
    String email = "";
    String name = "";
    Typeface font;
    //String phone = "";

    String BattChar;
    String PC;
    String ld;
    String tld;
    String BH;
    String selected_batt;
    int backupHr =0;
    int PowerCut =0;
    float load = 0;
    float total_load = 0;
    String BPowerCut;
    float size =0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        BCharge = (Button) findViewById(R.id.BCharge);
        BMinHrs = (Button) findViewById(R.id.BMinHrs);
        ELoad = (EditText) findViewById(R.id.ELoad);
        ETLoad = (EditText) findViewById(R.id.ETLoad);
        BPowerC = (Button) findViewById(R.id.BPowerC);
        BPC = (Button) findViewById(R.id.BPC);
        bOrd = (Button) findViewById(R.id.bOrd);
        disp_name = (TextView) findViewById(R.id.batt_name);
        s1 = (Button) findViewById(R.id.selB);

        font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");
        //private method of your class
        session = new UserSessionManager(getApplicationContext());
        user = session.getUserDetails();
        email = user.get("email");
        name = user.get("name");

        this.arrCharge = new String[]{
                "Grid", "Solar", "Wind"
        };
        this.arrPowerCut = new String[]{
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
        };
        this.arrPC = new String[]{
                "Continuous", "Intermittent"
        };
        arrayBattery = new String[]{
                "LiRack", "Joulie", "Joulie+", "LiRack-Eco", "Li-V"
        };

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(OrderBatt.this);
        Boolean frmWebView = prefs.getBoolean("frmWeb", false);
        if (frmWebView) {
            findViewById(R.id.dis_id).setVisibility(View.VISIBLE);
            Intent in = getIntent();
            Bundle b = in.getExtras();
            Boolean FrmLoginPage = b.getBoolean("FrmLoginPage");
            //String nameOfBatt = b.getString("nameOfBatt");

            if(!FrmLoginPage) {
                String url = b.getString("CurrentUrl");
                Log.w(TAG, "" + url);
                //s1.setText(nameOfBatt);
                switch (url) {
                    case "https://vmechatronics.com/lithium-ion-battery-li-v.php":
                        s1.setText("Li-V");
                        selected_batt = arrayBattery[4];
                        break;
                    case "https://vmechatronics.com/lithium-ion-battery-lirack.php":
                        s1.setText("LiRack");
                        selected_batt = arrayBattery[0];
                        break;
                    case "https://vmechatronics.com/lithium-ion-battery-joulieplus.php":
                        s1.setText("Joulie+");
                        selected_batt = arrayBattery[2];
                        break;
                    case "https://vmechatronics.com/lithium-ion-battery-joulie.php":
                        s1.setText("Joulie");
                        selected_batt = arrayBattery[1];
                        break;
                    case "https://vmechatronics.com/lithium-ion-battery-lirackeco.php":
                        s1.setText("LiRack-Eco");
                        selected_batt = arrayBattery[3];
                        break;
                    default:
                        s1.setText("");
                        break;
                }
            }
            else{
                String batt_chosen = b.getString("battery_selected");
                Log.w(TAG, "" + batt_chosen);

                switch (batt_chosen){
                    case "Lirack":
                        s1.setText("LiRack");
                        selected_batt = arrayBattery[0];
                        break;
                    case "Joulie" :
                        s1.setText("Joulie");
                        selected_batt = arrayBattery[1];
                        break;
                    case "Joulie+":
                        s1.setText("Joulie+");
                        selected_batt = arrayBattery[2];
                        break;
                    case "LiRack-Eco" :
                        s1.setText("LiRack-Eco");
                        selected_batt = arrayBattery[3];
                        break;
                    case "Li-V" :
                        s1.setText("Li-V");
                        selected_batt = arrayBattery[4];
                        break;
                    default:
                        s1.setText("");
                        break;
                }
            }

            s1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderBatt.this, R.layout.spinner_row, arrayBattery);
                    new AlertDialog.Builder(OrderBatt.this).setTitle("Select Battery").setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selected_batt = arrayBattery[i];
                            s1.setText(selected_batt);
                        }
                    }).create().show();
                }
            });
        }

        else {
            s1.setText("");

            s1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(OrderBatt.this, R.layout.spinner_row, arrayBattery);
                    new AlertDialog.Builder(OrderBatt.this).setTitle("Select Battery").setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            selected_batt = arrayBattery[i];
                            s1.setText(selected_batt);
                        }
                    }).create().show();
                }
            });
        }

        /*final List<String> categories = new ArrayList<String>();
        categories.add("LiRack");
        categories.add("Joulie");
        categories.add("Joulie+");
        categories.add("Li-Lite");
        categories.add("Li-V");

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinB.setAdapter(adapter);*/

        BCharge.setText("Battery Charging Source");
        BCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(OrderBatt.this, R.layout.spinner_row, arrCharge);
                new AlertDialog.Builder(OrderBatt.this).setTitle("Charging your battery").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BCharge.setText(arrCharge[which]);
                        BattChar = BCharge.getText().toString();

                        dialog.dismiss();
                    }
                }).create().show();
            }
        });





        BMinHrs.setText("Minimum backup hours");
        BMinHrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(OrderBatt.this, R.layout.spinner_row, arrPowerCut);
                new AlertDialog.Builder(OrderBatt.this).setTitle("Hours of Backup").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BMinHrs.setText(arrPowerCut[which]);
                        BH = BMinHrs.getText().toString();
                        backupHr = Integer.parseInt(BH);

                        Log.w(TAG, "Hour " +backupHr );
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });


        BPowerC.setText("Powercut in hours");
        BPowerC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(OrderBatt.this, R.layout.spinner_row, arrPowerCut);
                new AlertDialog.Builder(OrderBatt.this).setTitle("Hours of Power Cut").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BPowerC.setText(arrPowerCut[which]);
                        PC = BPowerC.getText().toString();
                        PowerCut = Integer.parseInt(PC);

                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        BPC.setText("Behaviour of Powercut");
        BPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(OrderBatt.this, R.layout.spinner_row, arrPC);
                new AlertDialog.Builder(OrderBatt.this).setTitle("Behaviour of PowerCut").setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BPC.setText(arrPC[which]);
                        BPowerCut = BPC.getText().toString();

                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        bOrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ld = ELoad.getText().toString();
                if (ELoad.getText().toString().length() > 0)
                    load =  Float.parseFloat(ld);

                tld = ETLoad.getText().toString();
                if (ETLoad.getText().toString().length() > 0)
                    total_load =  Float.parseFloat(tld);


                // if(BattChar == "Battery Charging Source" && backupHr != 0 && PowerCut != 0 && load != 0 && total_load!=0 && BPowerCut != null ) {
                if (selected_batt!= null && BattChar!=null && backupHr != 0 && PowerCut != 0 && load != 0 && total_load!=0 && BPC != null)
                {
                    if (load <= total_load) {

                        final Response.Listener<String> responseListener3 = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonResponse3 = null;

                                try {
                                    JSONArray array = new JSONArray(response);
                                    jsonResponse3 = array.getJSONObject(0);

                                    boolean success = jsonResponse3.getBoolean("success");
                                    Log.w(TAG, "Before success");
                                    if (success) {

                                        if (BPowerCut == arrPC[0]) {
                                            size = backupHr * load;
                                            Toast.makeText(OrderBatt.this, "Battery Size is" + size, Toast.LENGTH_LONG).show();
                                            Intent intent =new Intent(OrderBatt.this,order_lithium.class);
                                            Bundle b = new Bundle();
                                            b.putString("selected_batt",selected_batt);
                                            b.putString("char_src",BattChar);
                                            b.putInt("backupHr",backupHr);
                                            b.putInt("PowerCut",PowerCut);
                                            b.putFloat("Size",size);
                                            b.putString("BPowerCut",BPowerCut);
                                            b.putFloat("total_load",total_load);
                                            b.putFloat("load",load);
                                            intent.putExtras(b);
                                            startActivity(intent);

                                        } else if (BPowerCut == arrPC[1]) {
                                            size = load * PowerCut;
                                            Toast.makeText(OrderBatt.this, "Battery Size is" + size, Toast.LENGTH_LONG).show();
                                            Intent intent =new Intent(OrderBatt.this,order_lithium.class);
                                            Bundle b = new Bundle();
                                            b.putString("selected_batt",selected_batt);
                                            b.putString("char_src",BattChar);
                                            b.putInt("backupHr",backupHr);
                                            b.putInt("PowerCut",PowerCut);
                                            b.putFloat("Size",size);
                                            b.putString("BPowerCut",BPowerCut);
                                            b.putFloat("total_load",total_load);
                                            b.putFloat("load",load);
                                            intent.putExtras(b);
                                            startActivity(intent);

                                        }

                                    } else {
                                        android.support.v7.app.AlertDialog.Builder builder1 = null;
                                        builder1 = new android.support.v7.app.AlertDialog.Builder(OrderBatt.this);
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

                                                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderBatt.this);
                                                                        builder.setMessage("A verification link has been emailed to you. Please verify your E-mail id!")
                                                                                .setNegativeButton("OK", null)
                                                                                .setTitle("Verify E-mail")
                                                                                .create()
                                                                                .show();
                                                                    } else {
                                                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrderBatt.this);
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
                                                        RequestQueue queue = Volley.newRequestQueue(OrderBatt.this);
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
                        RequestQueue queue2 = Volley.newRequestQueue(OrderBatt.this);
                        queue2.add(EV);


                    }

                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderBatt.this);
                        builder.setMessage("Load on battery cannot be higher than total load")
                                .setNegativeButton("Retry", null)
                                .setTitle("Wrong inputs")
                                .create()
                                .show();
                    }

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderBatt.this);
                    builder.setMessage("Please Fill or Select all the Fields")
                            .setNegativeButton("Retry", null)
                            .setTitle("Fields Missing")
                            .create()
                            .show();
                }
            }
        });
    }


    public void onChargeDB(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.charge_db);
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

    public void onMinHrsDB(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.minhrs_db);

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

    public void onPCDB(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.pc_db);

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

    public void onLdDB(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.load_db);

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

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        String item = parent.getItemAtPosition(i).toString();
        Log.w(TAG," "+item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // TODO Auto-generated method stub
    }*/

    public void onBpc(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.powercut_behaviour);
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

    public void onTotalLdDB(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.total_ld_db);
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
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Order_Battery", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
