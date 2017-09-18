package com.vmechatronics.energyadvisor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by vmplapp on 26/7/17.
 */

public class LiBattListActivity extends FragmentActivity implements BattListFragment.OnHeadlineSelectedListener{

    private static final String TAG = "LiBattListActivity";
    private FirebaseAnalytics mFirebaseAnalytics;
    FloatingActionButton fab_bt;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libatlist);

        if(findViewById(R.id.fragment_container) != null)
        {
            Log.w(TAG, "fragment_container not null" );
            fab_bt = (FloatingActionButton) findViewById(R.id.fab);
            fab_bt.setVisibility(View.VISIBLE);

            fab_bt.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    //Toast.makeText(v.getContext(), "Get Quotation for ordering battery", Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar
                            .make(v, "Get Quotation for ordering battery", Snackbar.LENGTH_LONG)
                            .setAction("CLOSE", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            });
                    // Changing message text color
                    snackbar.setActionTextColor(Color.RED);
                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return true;
                }
            });
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            BattListFragment firstFrag = new BattListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,firstFrag).commit();
        }
        //R.id.fragment_container,firstFrag).commit();
        else {
            Log.w(TAG, "onCreate: We are in a tablet !! Change the layout !");
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            BattListFragment battlistfragment = new BattListFragment();
            transaction.add(R.id.listcontainer,battlistfragment);

            BattDetailsFragment battdetailsfragment = new BattDetailsFragment();
            Bundle args = new Bundle();
            args.putInt("ARG_POSITION", 0);
            battdetailsfragment.setArguments(args);
            transaction.add(R.id.detailscontainer,battdetailsfragment);

            transaction.commit();
        }
    }

    @Override
    public void onArticleSelected(int position) {
        //battdetailsfragment battdetailsfrag = (battdetailsfragment) getSupportFragmentManager().findFragmentByTag("BatteryWebViewFragment");
        BattDetailsFragment battdetailsfrag = (BattDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.detailscontainer);

        if(battdetailsfrag != null){
            Log.w(TAG, "onArticleSelected: battdetailsfragment value "+battdetailsfrag );
            //If battdetailsfragment is available, we are in two-pane layout. Call a method in the battdetailsfragment to update its content.
            battdetailsfrag.updateArticleView(position);
        }
        else {
            //We're in the one-pane layout and must swap fragments. Create fragment and give it an argument for the selected article.
            BattDetailsFragment newFrag = new BattDetailsFragment();
            Bundle args = new Bundle();
            args.putInt("ARG_POSITION", position);
            newFrag.setArguments(args);

            fab_bt.setVisibility(View.INVISIBLE);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //Replace whatever is in the fragment_container view with this fragment.
            transaction.replace(R.id.fragment_container,newFrag);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

   /* @Override
    public void onResume(){
        super.onResume();
        //OnResume Fragment
        if(findViewById(R.id.fragment_container) != null)
        fab_bt.setVisibility(View.VISIBLE);
    }
*/

    public void order(View view) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean Islogin = prefs.getBoolean("Islogin", false); // get value of last login status
        if (!Islogin) {
            Intent i1 = new Intent(this, Login.class);
            i1.putExtra("class", "LiBattListActivity");
            i1.putExtras(i1);
           // prefs.edit().putBoolean("FrmLoginPage", true).commit();
            Toast.makeText(this, "Please sign in first", Toast.LENGTH_LONG).show();
            startActivity(i1);
        }
        else {
            Intent intent = new Intent(this, OrderBatt.class);
            prefs.edit().putBoolean("frmWeb", false).commit();
            startActivity(intent);
        }
    }

}
