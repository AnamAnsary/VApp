package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;


public class Home extends Activity {
    private static final String TAG = "Home";
    private FirebaseAnalytics mFirebaseAnalytics;
    //private int count =0;

    Button bSolCalc;
    ImageView bLitCal;
    Button bProf;
    Button bWeb;
    Button bSoc;
    Button bHome;
    TextView tvLiBatt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Make sure this is before calling super.onCreate
        setTheme(R.style.MyApp);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

     /*   mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Home", this.getClass().getSimpleName());
        Log.w(TAG, "onCreate: analytics " +mFirebaseAnalytics );*/

        LocalBroadcastManager.getInstance(this).registerReceiver(mHandler, new IntentFilter("com.vmechatronics.energyadvisor_FCM-MESSAGE"));

        //Handle intent from firebase if any notification received
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                TextView dialogTitle = (TextView) dialog.findViewById(R.id.title);
                TextView dialogText = (TextView) dialog.findViewById(R.id.txt);
                Button dialogButton = (Button) dialog.findViewById(R.id.btn);
                if (key.equals("Message")) {
                    //String NMsg = getIntent().getExtras().getString(key));
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setMessage("" + getIntent().getExtras().getString(key))
                            .setNegativeButton("OK", null)
                            .setTitle("Notification for You")
                            .create()
                            .show();*/
/*
                    final Dialog dialog = new Dialog(this);
                    dialog.setContentView(R.layout.custom_dialog);
                    TextView dialogTitle = (TextView) dialog.findViewById(R.id.title);
                    TextView dialogText = (TextView) dialog.findViewById(R.id.txt);
                    Button dialogButton = (Button) dialog.findViewById(R.id.btn);*/
                    dialogText.setText(getIntent().getExtras().getString(key));
                    dialogTitle.setText("Notification for You");
                    dialogButton.setText("OK");
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
                else if (key.equals("Quote")) {
                    //String NMsg = getIntent().getExtras().getString(key));
                   /* AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                    builder.setMessage("" + getIntent().getExtras().getString(key))
                            .setNegativeButton("Close", null)
                            .setTitle("Quote for the day")
                            .create()
                            .show();*/

                    dialogTitle.setText("Quote for the day");
                    dialogText.setText(getIntent().getExtras().getString(key));
                    dialogButton.setText("Close");
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            }
        }

        bSolCalc = (Button) findViewById(R.id.bSolCalc);
        bSolCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Home.this, mainActivity.class);
                startActivity(i1);
            }
        });

        bHome = (Button) findViewById(R.id.bHome);
        bWeb = (Button) findViewById(R.id.bWeb);
        bProf = (Button) findViewById(R.id.bUser);
        bSoc = (Button) findViewById(R.id.bSoc);
        bLitCal = (ImageView) findViewById(R.id.bLitCal);
        tvLiBatt = (TextView) findViewById(R.id.tvLiBatt);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean Islogin = prefs.getBoolean("Islogin", false); // get value of last login status
        if (Islogin) {
            AppRater.app_launched(this);
            Log.w(TAG, "onCreate: AppRater Called !");
        }

       /* bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

        bProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, Profile.class);
                startActivity(i);
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

      /*  bSoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                }
            }
        }); */

      /*  bSoc.setOnClickListener(new View.OnClickListener() {
            //Dialog dialog = new Dialog(Home.this);
                                    @Override
                                    public void onClick(View view) {
                                        Intent i4 = new Intent(Home.this, Popup.class);
                                            if(clicked == true) {

                                                i4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                //i4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                // if (count % 2 == 0) {
                                                startActivity(i4);
                                            }
                                        else {
                                                i4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                clicked = false;
                                            }
                                        // @Override
                                        //public void onClick(View view) {
                                        //  Intent i4 = new Intent(Home.this, Popup.class);
                                        //(i4);
                                        // }
                                     /*else *///{//i4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // }
        //count++;
        //
        final Dialog dialog = new Dialog(Home.this);
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
        //closeButton.OnClickListener(new View.OnClickListener() {


        // @Override
        // public void onClick(View v) {
        // finish();
        //   }
        // });

        tvLiBatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i5 = new Intent(Home.this, LiBattListActivity.class);
                startActivity(i5);
            }
        });
    }

    //Handle Notification Message when app is in foreground
    private BroadcastReceiver mHandler = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("Message");
            String quote = intent.getStringExtra("Quote");
            final Dialog dialog = new Dialog(Home.this);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView dialogTitle = (TextView) dialog.findViewById(R.id.title);
            TextView dialogText = (TextView) dialog.findViewById(R.id.txt);
            Button dialogButton = (Button) dialog.findViewById(R.id.btn);

            if(msg != null) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("" + msg)
                        .setNegativeButton("OK", null)
                        .setTitle("Notification for You")
                        .create()
                        .show();*/
                dialogText.setText(msg);
                dialogTitle.setText("Notification for You");
                dialogButton.setText("OK");
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            if(quote != null) {
                /*AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("" + quote)
                        .setNegativeButton("Close", null)
                        .setTitle("Quote for the day")
                        .create()
                        .show();*/
                dialogTitle.setText("Quote for the day");
                dialogText.setText(quote);
                dialogButton.setText("Close");
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        }
    };

   /* @Override
    protected void onResume() {
        super.onResume();
       *//* mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "Home", "Home" *//**//* class override *//**//*);*//*
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        params.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, params);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mHandler);
    }

    public void onBackPressed() {
        //Toast.makeText(Home.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
        //finish();
    }
    public FirebaseAnalytics getmFirebaseAnalytics() {
        return mFirebaseAnalytics;
    }

    protected void onResume() {

        //FirebaseAnalytics firebaseAnalytics =  ((ApplicationClass) getApplication()).getmFirebaseAnalytics();
        mFirebaseAnalytics.setCurrentScreen(this,"Home", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}