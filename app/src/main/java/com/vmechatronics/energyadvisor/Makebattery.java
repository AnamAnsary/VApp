package com.vmechatronics.energyadvisor;

/*import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

*//**
 * Created by root on 14/10/16.
 *//*
public class Makebattery extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/battery.php";
    private static final String TAG = "Make Battery PDF";
    private Map<String,String> params;
    public Makebattery (String name,String email,String state,String battery,String phone, Response.Listener<String>listener)
    {
        super(Method.POST,REGISTER_REQUEST_URL,listener,null);
        params = new HashMap<>();
        params.put("name",name);
        params.put("email", email+"");
        params.put("state", state+"");
        params.put("battery",battery+"");
        params.put("phone",phone);
        Log.w(TAG, "PDF: Called");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}*/
