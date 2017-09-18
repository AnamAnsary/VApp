package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import java.util.HashMap;


public class Pay extends Activity {

    private static final String TAG = "Pay";
    ImageButton bPay;
    CheckBox cbTC;
    TextView tvBillAm;
    TextView tvDAdd;
    TextView tvTC;

    String distance = "123";
    String cAdd = "";
    String dAdd = "";
    String qid = "";
    String qdate = "";
    String name = "";
    String email = "";
    String phone = "";
    String sid = "";
    HashMap<String,String> userDetail;
    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        distance = this.getIntent().getStringExtra("distance");
        dAdd = this.getIntent().getStringExtra("dAdd");
        cAdd = this.getIntent().getStringExtra("cAdd");
        qid  = this.getIntent().getStringExtra("qid");
        qdate = this.getIntent().getStringExtra("qdate");
        sid = this.getIntent().getStringExtra("sid");

        session = new UserSessionManager(getApplicationContext());

        if(!session.isUserLoggedIn()){
            Intent i = new Intent(Pay.this, Login.class);
            i.putExtra("class","Pay");
            startActivity(i);
            finish();
        }

        userDetail = session.getUserDetails();
        name = userDetail.get("name");
        email = userDetail.get("email");
        phone = userDetail.get("phone");
        Log.w(TAG, "onCreate: " + name );


        final float amount = (float) (Float.parseFloat(distance)*2.0 +200);

        tvBillAm = (TextView)findViewById(R.id.tvBillAm);
        tvDAdd = (TextView)findViewById(R.id.tvDAdd);
        bPay = (ImageButton) findViewById(R.id.bPay);
        cbTC = (CheckBox) findViewById(R.id.cbTC);
        tvTC = (TextView)findViewById(R.id.tvTC);


        tvBillAm.setText(amount+"*");
        tvDAdd.setText(dAdd);
        bPay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cbTC.isChecked()) {
                    Intent ii = new Intent(Pay.this, PayUMoney.class);
                    ii.putExtra("amount",amount+"");
                    ii.putExtra("qid",qid);
                    ii.putExtra("qdate",qdate);
                    ii.putExtra("addr",cAdd);
                    ii.putExtra("sid",sid);
                    ii.putExtra("prod","Site Visit");
                    startActivity(ii);
                }else {

                    Toast.makeText(getApplicationContext(),
                            "Please accept Terms and Conditions",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "Pay", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }

}
