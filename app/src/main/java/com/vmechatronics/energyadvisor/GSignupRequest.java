package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
public class GSignupRequest extends StringRequest
{

    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/GPlusSignup.php";
    private static final String TAG = "Signup Request";
    private Map<String,String> params;
    public GSignupRequest(String name, String email, String phone,String state, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();

        params.put("email", email);
        //params.put("pass",pass);
        params.put("phone",phone);
        params.put("name", name);
        params.put("state", state);
        Log.w(TAG, "Signup Request is working well  " + email +phone +name);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
