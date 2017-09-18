package com.vmechatronics.energyadvisor;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.vmechatronics.energyadvisor.R;

import java.util.HashMap;

public class AfterTrans extends Activity {

    UserSessionManager session;
    private FirebaseAnalytics mFirebaseAnalytics;

    Button bHome;
    Button bProf;
    Button bWeb;
    Button bSoc;

    String tid = "";
    String status = "";
    String email= "";
    String phone = "'";
    String amount = "";
    String qid = "";
    String qdate = "";
    HashMap<String, String> user;

    TextView tvTID;
    TextView tvTransSF;
    TextView tvTAM;
    TextView tvTrDet;
    TextView tvTQQQ;
    String pp = "Congratulation! Your transaction was successful. We have mailed you the the Payment Receipt. Please keep it safe for further use. Our Expert will soon Visit your location within Estimated Time.";
    String ss = "Sorry! Your Transaction has failed. Please try again from Quote history in MyQuotes..";
    private String TAG = "After Trans";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_trans);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());

        if(!session.isUserLoggedIn())
        {
            finish();
        }

        user = session.getUserDetails();

        email= user.get("email");
        phone = user.get("phone");

        tid         = getIntent().getStringExtra("txid");
        amount      = getIntent().getStringExtra("amount");
        status      = getIntent().getStringExtra("status");
        qid         = getIntent().getStringExtra("qid");

        Log.w(TAG, "tid :" + tid + ", amount: " + amount + ", status: " + status + ", qid: " + qid );

        tvTransSF   = (TextView)findViewById(R.id.tvTranSF);
        tvTAM       = (TextView)findViewById(R.id.tvTAM);
        tvTID       = (TextView)findViewById(R.id.tvTID);
        tvTrDet     = (TextView)findViewById(R.id.tvTrDet);
        tvTQQQ      = (TextView)findViewById(R.id.tvTQQQ);

        bHome       = (Button)findViewById(R.id.bHome);
        bWeb        = (Button)findViewById(R.id.bWeb);
        bProf       = (Button)findViewById(R.id.bUser);
        bSoc        = (Button)findViewById(R.id.bSoc);

        if(status.equals("1")){
            tvTransSF.setText("Transaction Successful");

            tvTrDet.setText(pp);
        }else{
            tvTrDet.setText(ss);
            tvTransSF.setText("Transaction Failed");
        }




        tvTID.setPaintFlags(tvTID.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvTID.setText(tid);
        tvTAM.setText(amount);
        tvTQQQ.setText(qid);

        bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(AfterTrans.this, Home.class);
                startActivity(i1);
            }
        });

        bProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2 = new Intent(AfterTrans.this, Profile.class);
                startActivity(i2);
            }
        });

        bWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://vmechatronics.com/index.php");
                Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i3);
            }
        });

        final Dialog dialog = new Dialog(AfterTrans.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bSoc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                dialog.setCancelable(true);

                                       /* WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                                        Display display = wm.getDefaultDisplay();
                                        int width = display.getWidth();  // deprecated
                                        int height = display.getHeight();  // deprecated*/

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                Log.w(TAG, "Screen Resolution is " + width + " " + height);

                WindowManager.LayoutParams WMLP = dialog.getWindow().getAttributes();

                WMLP.x = (int) (width - (0.40 * width));   //x position
                WMLP.y = (int) (height - (0.95 * height));   //y position

                dialog.getWindow().setAttributes(WMLP);
                                        /*DisplayMetrics dm = getResources().getDisplayMetrics();
                                        int dispwidth = dm.widthPixels;
                                        int dispheight = dm.heightPixels;
                                        Log.w(TAG, "Screen Resolution is " + dispwidth + " " + dispheight);
                                        getWindowManager().getDefaultDisplay().getMetrics(dm);
                                        float twidth = dispwidth * 0.118f;
                                        float theight = dispheight * 0.4f;
                                        int width = (int) twidth;
                                        int height = (int) theight;
                                        Log.w(TAG, "Set Dim are " + width + " " + height);
                                        getWindow().setLayout(width, height);
                                        getWindow().setGravity(Gravity.RIGHT);*/


                dialog.setContentView(R.layout.soc_popup);

                ImageView img1 = (ImageView) dialog.findViewById(R.id.img_Fb);
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://www.facebook.com/Vision-Mechatronics-Pvt-Ltd-957579120993009/"));
                        startActivity(intent);
                        //finish();
                    }
                });

                ImageView img2 = (ImageView) dialog.findViewById(R.id.img_Blog);
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://visionmechatronics.blogspot.in/"));
                        startActivity(intent);
                        // finish();
                    }
                });


                ImageView img3 = (ImageView) dialog.findViewById(R.id.img_twit);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://twitter.com/VisionMechX"));
                        startActivity(intent);
                        //finish();
                    }
                });

                ImageView img5 = (ImageView) dialog.findViewById(R.id.img_insta);
                img5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://www.instagram.com/visionmechatronics/?hl=en"));
                        startActivity(intent);
                        //finish();
                    }
                });

                ImageView img4 = (ImageView) dialog.findViewById(R.id.img_Pin);
                img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("http://in.pinterest.com/visionmechatronics"));
                        startActivity(intent);
                        //finish();
                    }
                });

                ImageView img6 = (ImageView) dialog.findViewById(R.id.img_LI);
                img6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://www.linkedin.com/in/vision-mechatronics-pvt-limited-61653a112"));
                        startActivity(intent);
                        //finish();
                    }
                });

                ImageView img7 = (ImageView) dialog.findViewById(R.id.img_YT);
                img7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse("https://www.youtube.com/channel/UCsFmRkC44Et6dFkTvD-AhDg"));
                        startActivity(intent);
                        //finish();
                    }
                });

                dialog.show();
            }
        });
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "After_Transaction", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
