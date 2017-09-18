package com.vmechatronics.energyadvisor;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 5/8/16.
 */
public class MakePDF extends StringRequest {
    public static final String REGISTER_REQUEST_URL = "https://vmechatronics.com/app/mail_pdf.php";
    private static final String TAG = "Make Quote PDF";
    private Map<String,String> params;
    public MakePDF(String qid, String qdate, String name, String email, String state, String solar_type, String solar_panel, String roof_type, String plantSize,
                   String breakeven, String inverter_name, String shed_type, String wire_type,
                   float display_total, String units_gen, String unit_cost, String yearly_gen,
                   String mount_type, String tarrif,String battery_size,Response.Listener<String> listener)
    {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("qid", qid);
        params.put("qdate", qdate);
        params.put("name",name);
        params.put("email", email);
        params.put("state", state);
        params.put("roof_type", roof_type);
        params.put("solar_type", solar_type);
        params.put("solar_panel", solar_panel);
        params.put("plantSize", plantSize);
        params.put("breakeven", breakeven);
        params.put("inverter_name", inverter_name);
        params.put("shed_type", shed_type);
        params.put("wire_type", wire_type);
        params.put("display_total", display_total+"");
        params.put("units_gen", units_gen);
        params.put("unit_cost", unit_cost);
        params.put("yearly_gen",yearly_gen);
        params.put("mount_type", mount_type);
        params.put("tarrif",tarrif);
        params.put("battery_size",battery_size);
        Log.w(TAG, "MakePDF: Called"+solar_type+solar_panel);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
