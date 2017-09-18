package com.vmechatronics.energyadvisor;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/*
*
 * Created by root on 2/12/16.
*/


public class EMotp extends StringRequest {

    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/EmailVerify.php";
    private static final String TAG = "EMotp";
    private Map<String,String> params;
    public EMotp(String email, String name, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("email", email);
        params.put("name",name);
        //params.put("eotp",eotpR);
        //params.put("otp",otp);
        Log.w(TAG, "email is send  " + email+name);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
