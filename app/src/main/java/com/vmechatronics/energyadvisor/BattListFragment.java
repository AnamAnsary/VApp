package com.vmechatronics.energyadvisor;


import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vmplapp on 27/7/17.
 */

public class BattListFragment extends ListFragment {
     //implements AdapterView.OnItemClickListener

 /*   private String nameofBatt[] = {"Li-Rack", "Joulie", "Li-Rack Eco", "Joulie+", "Li-V"};
    private String desc[] = {"Life Cycle : 20 Years\nCharging Time: 2~3 Hrs", "Life Cycle: 20 Years\nCharging Time: 2~3 Hrs", "Life Cycle: 10 Years\nCharging Time: 2~3 Hrs", "Life Cycle: 20 Years\nCharging Time: 2~3 Hrs", "Life Cycle : 13.5 Years\nCharging Time: .5~3 Hrs"};
    private Integer imageId[] = {R.drawable.lirack, R.drawable.joulie, R.drawable.lirack, R.drawable.joulie, R.drawable.liv};
*/
    FloatingActionButton fab_bt;
    private FirebaseAnalytics mFirebaseAnalytics;
    // Array of strings storing battery names
    String[] nameofBatt = new String[] {
            "Li-Rack", "Joulie", "Li-Rack Eco", "Joulie+", "Li-V"
    };
    // Array of integers points to images stored in /res/drawable/
    int[] imageId = new int[]{
            R.drawable.lirack, R.drawable.joulie, R.drawable.lirack, R.drawable.joulie, R.drawable.liv
    };

    // Array of strings to store description of battery
    String[] desc = new String[]{
            "Life Cycle : 20 Years\nCharging Time: 2~3 Hrs",
            "Life Cycle: 20 Years\nCharging Time: 2~3 Hrs",
            "Life Cycle: 10 Years\nCharging Time: 2~3 Hrs",
            "Life Cycle: 20 Years\nCharging Time: 2~3 Hrs",
            "Life Cycle : 13.5 Years\nCharging Time: .5~3 Hrs"
    };

    OnHeadlineSelectedListener mCallback;
    private static final String TAG = "BatteryListFragment";

    public interface OnHeadlineSelectedListener {
        public void onArticleSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Each row in the list stores battery name, description and image
        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        for(int i=0;i<nameofBatt.length;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("nametext", nameofBatt[i]);
            hm.put("destext", desc[i]);
            hm.put("image", Integer.toString(imageId[i]) );
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "nametext","destext","image" };

        // Ids of views in listview_layout
        int[] to = { R.id.nameid,R.id.descid,R.id.imgid};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getBaseContext(), aList, R.layout.batterylist_layout, from, to);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (OnHeadlineSelectedListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w(TAG, "inside onStart ");
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int position, long id) {
                //Toast.makeText(getActivity(), data.get(pos).get("Name"), Toast.LENGTH_SHORT).show();
                Log.w(TAG, "onItemClick: Item number is " + position);
                mCallback.onArticleSelected(position);
            }
        });

        fab_bt = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if(getActivity().findViewById(R.id.fragment_container) != null)
            fab_bt.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        mFirebaseAnalytics.setCurrentScreen(getActivity(), "BatteryList", null);
        Log.d("FAnalytics", "setCurrentScreen: " + getClass().getSimpleName());
        super.onResume();
    }
}
