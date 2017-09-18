package com.vmechatronics.energyadvisor;

/*
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by root on 25/7/16.
 *//*

public class NewPass extends StringRequest {

    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/for_pass.php";
    private static final String TAG = "New Pass";
    private Map<String,String> params;
    public NewPass(String email, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("email", email);
        Log.w(TAG, "New Pass is working well  " + email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
*/
