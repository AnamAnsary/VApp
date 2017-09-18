package com.vmechatronics.energyadvisor;


import android.util.Log;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 *
 *
 */
//---As--6/9/16 for the email & text otp send
public class action extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/Eotp.php";
    private static final String TAG = "esotp";
    private Map<String,String> params;
    public action (String email,String eotpR,String phone,String otp, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("email", email);
        params.put("phone",phone);
        params.put("eotp",eotpR);
        params.put("otp",otp);
        Log.w(TAG, "eotp is send  " + email+eotpR+otp+phone);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
