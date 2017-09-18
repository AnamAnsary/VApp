package com.vmechatronics.energyadvisor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.vmechatronics.energyadvisor.R;

public class Profile extends Activity {

    UserSessionManager session;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String TAG = "Profile";
    Button bHome;
    Button bWeb;
    Button bSoc;
    TextView tvCUHC;
    ImageButton tvMTC;
    ImageButton tvMQC;
    ImageButton tvEPC;
    ImageButton tvSOC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        session = new UserSessionManager(getApplicationContext());

        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Profile.this);
        //Boolean deatilsinDB = prefs.getBoolean("deatilsinDB", false);
        if (!session.isUserLoggedIn()) {
            Intent i = new Intent(Profile.this, Login.class);
            i.putExtra("class", "Profile");
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        bHome = (Button) findViewById(R.id.bHome);
        bWeb = (Button) findViewById(R.id.bWeb);
        bSoc = (Button) findViewById(R.id.bSoc);

         tvEPC = (ImageButton) findViewById(R.id.tvEPC);
       // String info =tvEPC.getText().toString();//String text = (TextView)findViewById(R.id.tv).getText().toString();
        tvMTC = (ImageButton) findViewById(R.id.tvMTC);
        tvMQC = (ImageButton) findViewById(R.id.tvMQC);
        tvSOC = (ImageButton) findViewById(R.id.tvSOC);
        tvCUHC = (TextView) findViewById(R.id.tvCUHC);

        tvEPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iep = new Intent(Profile.this, EditProf.class);
                startActivity(iep);

            }
        });

        tvMTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imt = new Intent(Profile.this, MyTrans.class);
                startActivity(imt);
            }
        });

        tvMQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imq = new Intent(Profile.this, MyQuotesActivity.class);
                startActivity(imq);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        tvSOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Profile.this);
                Boolean IsGAcc = prefs.getBoolean("IsGp", false);
                if (IsGAcc == true) {
                    mAuth.signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    // [START_EXCLUDE]
                                    session.logoutUser();
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Profile.this);
                                    prefs.edit().putBoolean("IsGp", false).commit();
                                    Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                                    // [END_EXCLUDE]
                                }
                            });
                }

                  /*  if (mGoogleApiClient.isConnected()) {
                        AccountApi.clearDefaultAccount(mGoogleApiClient);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient.connect();
                    }*/

                   // GSignInActivity Gso = new GSignInActivity();
                  //  Gso.signOut();

                    //super.onStart();
                else{
                Log.w(TAG, "Log out of profile");
                session.logoutUser();
                //Toast.makeText(getApplicationContext(), "You are Logged Out", Toast.LENGTH_LONG).show();
                if (session.isUserLoggedIn()) {
                    Toast.makeText(getApplicationContext(), "You are Logged Out", Toast.LENGTH_LONG).show();
                    //finish();
                }
            }
                prefs.edit().putBoolean("Islogin", false).commit();

                Intent i1 = new Intent(Profile.this, Home.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
                finish();
           }
        });


        bHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(Profile.this, Home.class);
                startActivity(i1);
                finish();
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

        final Dialog dialog = new Dialog(Profile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bSoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Uri uri = Uri.parse("http://facebook.com/visionmechatronics");

                dialog.setCancelable(true);
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


        tvCUHC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://vmechatronics.com/contactus.php");
                Intent i3 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(i3);
            }
        });
    }
    public void onBackPressed () {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(this, "ProfileActivity", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}

