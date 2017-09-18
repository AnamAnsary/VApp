package com.vmechatronics.energyadvisor;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Calculate extends StringRequest{

    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/price.php";
    private static final String TAG = "Login Request";
    private Map<String,String> params;
    public Calculate(String solarType, String inverter, String panels, String mount, String wires, String nuts,String battery, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("solarType",solarType);
        params.put("inverter",inverter);
        params.put("panels", panels);
        params.put("mount",mount);
        params.put("wires",wires);
        params.put("nuts", nuts);
        params.put("battery",battery);
        Log.w(TAG, "Calculate is working well: st " + solarType+" , it " + inverter+ " , pt  " + panels + " mt " +  mount + " wt" + wires + " nt " +  nuts+ "bt"+battery);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
