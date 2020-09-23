package com.example.pairingserver;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetAllPairsReq extends StringRequest {


    private static final String LOGIN_REQUEST_URL =  "https://aarasna.in/GetAllPairs.php";
    private Map<String, String> params;

    public GetAllPairsReq(String dbname, String dbuser, String dbpass, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("dbname", dbname);
        params.put("dbuser", dbuser);
        params.put("dbpass", dbpass);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }


}

