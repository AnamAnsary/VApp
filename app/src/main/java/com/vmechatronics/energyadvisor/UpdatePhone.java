package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdatePhone extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/upd_phone.php";
    private static final String TAG = "Update Phone";
    private Map<String,String> params;
    public UpdatePhone(String email, String phone, Response.Listener<String> listener)
    {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("phone", phone);
        Log.w(TAG, "UpdatePhone: upd_phone.php is executing: " + email + " " + phone);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
