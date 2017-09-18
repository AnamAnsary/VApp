package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertTrans extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://www.vmechatronics.com/app/ins_trans.php";
    private static final String TAG = "Insert Trans";
    private Map<String,String> params;
    public InsertTrans(String email, String phone, String amount,String tid,String qid,  String qdate,String tstat,String ttype,String addr,String sid,  Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("qid", qid);
        params.put("qdate",qdate);
        params.put("tid", tid);
        params.put("email", email);
        params.put("phone",phone);
        params.put("ttype",ttype);
        params.put("tstat",tstat);
        params.put("amount", amount);
        params.put("sid",sid);
        params.put("addr", addr);
        Log.w(TAG, "Tid"+tid);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
