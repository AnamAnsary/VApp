package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Populate extends StringRequest{


        public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/state_tb.php";
        private static final String TAG = "State Table Request";
        private Map<String,String> params;
        public Populate(String state,Response.Listener<String> listener)
        {
            super(Method.POST, REGISTER_REQUEST_URL, listener, null);
            params = new HashMap<>();
            params.put("state",state);
            Log.w(TAG, "Database populated" + state);
        }

        @Override
        public Map<String, String> getParams() {
            return params;
        }
}
