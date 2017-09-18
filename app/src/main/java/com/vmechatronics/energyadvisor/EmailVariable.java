package com.vmechatronics.energyadvisor;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/*  Created by root on 3/12/16.*/

public class EmailVariable extends StringRequest {

    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/EVerification_Variable.php";
    private Map<String,String> params;
    public EmailVariable(String email, Response.Listener<String> listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
