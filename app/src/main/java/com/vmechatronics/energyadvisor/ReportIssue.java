package com.vmechatronics.energyadvisor;

/*
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

*/
/*
*
 * Created by root on 9/3/17.

*//*


public class ReportIssue extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/reportIssue.php";
    private static final String TAG = "Report Issue";
    private Map<String, String> params;
    public ReportIssue(String pdf_url, String qid, String name, String email, Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("pdf",pdf_url);
        params.put("qid", qid);
        //params.put("qdate", qdate);
        params.put("name",name);
        params.put("email", email);
        //params.put("sizeOfBattbyUser", String.valueOf(sizeOfBattbyUser));

        Log.w(TAG, "ReportIssue called");
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
*/
