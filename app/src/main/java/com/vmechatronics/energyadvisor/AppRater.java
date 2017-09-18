package com.vmechatronics.energyadvisor;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import static com.google.android.gms.wearable.DataMap.TAG;
import static com.vmechatronics.energyadvisor.R.id.button;

/**
 * Created by vmplapp on 24/6/17.
 */


public class AppRater {

    private final static String APP_TITLE = "Visions Energy Advisor"; // App Name
    private final static String APP_PNAME = "com.vmechatronics.energyadvisor"; // Package Name

    private final static int DAYS_UNTIL_PROMPT = 0;//Min number of days it was 3.
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches it was 3.

    public static void app_launched(Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }

        SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext, editor);
            }
        }

        editor.commit();
    }

    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        //final Dialog dialog = new Dialog(mContext, R.style.CustomDialog);
        dialog.setTitle("Rate " + APP_TITLE);


        LinearLayout.LayoutParams buttonLayoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams1.setMargins(20, 2, 20, 8);
        //b1.setLayoutParams(buttonLayoutParams);

        LinearLayout.LayoutParams buttonLayoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams2.setMargins(80, 2, 80, 8);

        LinearLayout.LayoutParams buttonLayoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams3.setMargins(140, 2, 140, 8);


        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(500);
        tv.setPadding(10, 10, 10, 20);
        tv.setTextSize(18);
        tv.setMaxWidth(600);
        tv.setTextColor(Color.parseColor("#2196f3"));
        tv.setGravity(Gravity.CENTER);
        ll.addView(tv);

        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setBackgroundColor(Color.parseColor("#2196f3"));
        b1.setLayoutParams(buttonLayoutParams1);
        b1.setBackgroundResource(R.drawable.bgcolor);
        b1.setTextColor(Color.parseColor("#ffffff"));
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setBackgroundColor(Color.parseColor("#2196f3"));
        b2.setLayoutParams(buttonLayoutParams2);
        b2.setBackgroundResource(R.drawable.bgcolor);
        b2.setTextColor(Color.parseColor("#ffffff"));
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setBackgroundColor(Color.parseColor("#2196f3"));
        b3.setLayoutParams(buttonLayoutParams3);
        b3.setBackgroundResource(R.drawable.bgcolor);
        b3.setTextColor(Color.parseColor("#ffffff"));
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);
        dialog.show();
    }
}