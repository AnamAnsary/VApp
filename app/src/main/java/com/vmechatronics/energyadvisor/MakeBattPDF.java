package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 17/1/17.
 */
public class MakeBattPDF extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/make_pdf_batt.php";
    private static final String TAG = "Make Battery Quote PDF";
    private Map<String, String> params;
    public MakeBattPDF(String selected_batt, String qid, String qdate, String name, String email,String state, ArrayList<Float> finalCap_selected,String BattChar, int backupHr,
                       int PowerCut, String BPowerCut, float total_load,float load,float sizeOfBattbyUser,int price,int occurrencesOf1st,int occurrencesOf2st,int occurrencesOf3rd,
                       int occurrencesOf4th,int occurrencesOf5th,int occurrencesOf6th, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("selected_batt", selected_batt);
        params.put("qid", qid);
        params.put("qdate", qdate);
        params.put("name",name);
        params.put("email", email);
        params.put("state", state);

        JSONArray jsArray = new JSONArray(finalCap_selected);
        params.put("cap_selected", jsArray.toString());

       // for(Float val: finalCap_selected) {
        //params.put("cap_selected", String.valueOf(val));
        //}

        //params.put("cap_selected", String.valueOf(cap_selected));

        params.put("BattChar", BattChar);
        params.put("backupHr", String.valueOf(backupHr));
        params.put("PowerCut", String.valueOf(PowerCut));
        params.put("BPowerCut", BPowerCut);
        params.put("total_load", String.valueOf(total_load));
        params.put("load", String.valueOf(load));
        params.put("sizeOfBattbyUser", String.valueOf(sizeOfBattbyUser));
        params.put("price", String.valueOf(price));
        params.put("occurrencesOf1st", String.valueOf(occurrencesOf1st));
        params.put("occurrencesOf2st", String.valueOf(occurrencesOf2st));
        params.put("occurrencesOf3rd", String.valueOf(occurrencesOf3rd));
        params.put("occurrencesOf4th", String.valueOf(occurrencesOf4th));
        params.put("occurrencesOf5th", String.valueOf(occurrencesOf5th));
        params.put("occurrencesOf6th", String.valueOf(occurrencesOf6th));

        Log.w(TAG, "MakePDF: Called"+finalCap_selected);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}