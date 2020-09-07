package com.example.pairingserver;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class UpdatePairsReq extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://pairingapp.000webhostapp.com/UpdatePairs.php";
    private Map<String, String> params;
    public UpdatePairsReq(JSONArray jsonArray, String dbname, String dbuser, String dbpass, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("dbname", dbname);
        params.put("dbuser", dbuser);
        params.put("dbpass", dbpass);
        params.put("array", jsonArray.toString());
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
