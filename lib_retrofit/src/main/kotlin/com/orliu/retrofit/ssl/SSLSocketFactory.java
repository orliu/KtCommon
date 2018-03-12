package com.orliu.retrofit.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @description desc
 * Created by orliu
 * 17/8/22 下午3:19.
 */
public class SSLSocketFactory {

    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }};

    public static javax.net.ssl.SSLSocketFactory getSSLSocketFactory() {

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            return sc.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }
}
