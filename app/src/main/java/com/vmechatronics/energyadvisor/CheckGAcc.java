package com.vmechatronics.energyadvisor;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
public class CheckGAcc extends StringRequest{
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/checkGplusAcc.php";
    private Map<String,String> params;
    public CheckGAcc(String email, Response.Listener<String> listener)
    {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        /*params.put("name", name);
        params.put("dob",dob);
        params.put("sex",sex);
        params.put("state",state);*/
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
