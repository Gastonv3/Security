package com.v3.security.Util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Skylake on 29/3/2018.
 */

public class VolleySingleton {
    private static VolleySingleton instanciaVolley;
    private RequestQueue request;
    private static Context contexto;

    private VolleySingleton(Context context ) {
    contexto = context;
    request = getRequest();
    }

    public static synchronized VolleySingleton getInstanciaVolley(Context context) {
        if ( instanciaVolley == null){
            instanciaVolley = new VolleySingleton(context);
        }
        return instanciaVolley;
    }

    public RequestQueue getRequest() {
        if(request ==null){
            request = Volley.newRequestQueue(contexto.getApplicationContext());
        }
        return request;
    }
    public <T> void addToRequestQueue(Request<T> request) {
        getRequest().add(request);
    }

}
