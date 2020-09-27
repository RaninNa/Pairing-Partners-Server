package com.example.pairingserver;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GetSpecificPairsReq extends StringRequest {


    private static final String LOGIN_REQUEST_URL = "https://aarasna.in/GetSpecificPairs.php";
    private Map<String, String> params;

    public GetSpecificPairsReq(String faculty, String course, String workType, String dbname, String dbuser, String dbpass, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("dbname", dbname);
        params.put("dbuser", dbuser);
        params.put("dbpass", dbpass);
        params.put("faculty", faculty);
        params.put("course", course);
        params.put("workType", workType);
    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }


}

