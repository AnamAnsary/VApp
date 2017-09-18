package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 25/7/16.
 */
public class InsertQuote extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://www.vmechatronics.com/app/ins_quote.php";

    private static final String TAG = "Insert Quote";
    private Map<String,String> params;
    public InsertQuote(String email, String phone, int amount, Response.Listener<String>listener)
    {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);

        Log.w(TAG, "InsertQuote: " + email + phone + amount );
        params = new HashMap<>();
        params.put("email", email);

        //if(phone==null)
          //  Toast.makeText(InsertQuote.this, "Cannot send email because your mobile number is not verified.Please verify your number first", Toast.LENGTH_LONG).show();
       // else{
        params.put("phone",phone);
       // }
        params.put("amount", amount+"");


        Log.w(TAG, "Insert Quote is working well  " + email + phone + amount);
    }

    @Override
    public Map<String,String> getParams() {

        return params;


    }
}
