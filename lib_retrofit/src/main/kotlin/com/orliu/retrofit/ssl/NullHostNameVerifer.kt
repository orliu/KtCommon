package com.orliuœ.retrofit.ssl

import android.util.Log
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * Created by liujianping on 09/03/2018.
 */
class NullHostNameVerifer : HostnameVerifier{

    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        Log.i("RestUtilImpl", "Approving certificate for " + hostname);
        return true;
    }
}