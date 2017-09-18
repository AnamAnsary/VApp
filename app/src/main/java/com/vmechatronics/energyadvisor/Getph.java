package com.vmechatronics.energyadvisor;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

/**
 * Created by root on 25/11/16.
 */


public class Getph extends AppCompatActivity {

    String pho="";
    String  email;
    String name;
    private String[] arrayStates;
    String state="";
    UserSessionManager session;
    HashMap<String,String> user;
    private FirebaseAnalytics mFirebaseAnalytics;
    EditText eText;
    Button s1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        eText =  (EditText)  findViewById(R.id.editTextId);
        ImageButton bu = (ImageButton) findViewById(R.id.ContB);

        email = this.getIntent().getStringExtra("email");
        name = this.getIntent().getStringExtra("name");


        s1 = (Button)findViewById(R.id.State);
        arrayStates = new String[]{
                "Andhra Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala","Madhya Pradesh","Maharashtra","Manipur","Meghalaya","" +
                "Odisha (Orissa)","Punjab","Rajasthan","Tamil Nadu","Telangana","Uttar Pradesh","Uttarakhand","West Bengal","Andaman and Nicobar Islands","Chandigarh",
                "Dadra and Nagar Haveli","Daman and Diu","Lakshadweep","Delhi - National Capital Territory","Puducherry (Pondicherry)"
        };
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Getph.this, R.layout.spinner_row, arrayStates);
                new AlertDialog.Builder(Getph.this).setTitle("Select State").setAdapter(adapter, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        s1.setText(arrayStates[i]);
                        state = arrayStates[i];
                    }
                }).create().show();
            }
        });

        bu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                //Intent i=new Intent();
                //i.putExtra("phone", pho);
                //setResult(RESULT_OK,i);

                pho = eText.getText().toString();
                //if(!pho || !state)
                if(pho.isEmpty() || pho.length() == 0 || pho.equals("") || pho == null || state==null)
                {
                    Toast.makeText(Getph.this, "Please enter all fields", Toast.LENGTH_LONG).show();
                }
                else {
                 /*   Intent intentOfValues = getIntent();
                    email = intentOfValues.getString("email");
                    name = intentOfValues.getString("name");
                    */
                    session = new UserSessionManager(getApplicationContext());
                    user = session.getUserDetails();
                    session.createGLoginSession(name, email, pho, state);

                    Bundle b2 = new Bundle();
                    b2.putString("email",email);
                    b2.putString("name",name);
                    b2.putString("phone",pho);
                    b2.putString("state",state);

                    Intent intent = new Intent(Getph.this, OTP.class);
                    intent.putExtras(b2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    //finish();
                    startActivity(intent);
                }
                }
        });

        /*i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i1.putExtra("phone", pho);
        i1.putExtra("email",email); */
       // gph.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
       // startActivity(gph); finish();
    }
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Get_Phoneno.", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}