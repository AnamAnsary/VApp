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

public class GetUDetails extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/get_details.php";
    private static final String TAG = "GetUDetails";
    private Map<String,String> params;
    public GetUDetails(String email, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        Log.w(TAG, "Returning user details is working well  " + email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
*/
