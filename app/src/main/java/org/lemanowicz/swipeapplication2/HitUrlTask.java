package org.lemanowicz.swipeapplication2;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Dave on 10/7/2015.
 */

class HitUrlTask extends AsyncTask<URL,Void,String> {

    private WeakReference<TextView> textViewRef;
    private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };
    // Install the all-trusting trust manager


    public HitUrlTask(TextView view){
        textViewRef = new WeakReference<TextView>(view);


    }

    @Override
    // Actual download method, run in the task thread
    protected String doInBackground(URL... params) {
        // params comes from the execute() call: params[0] is the url.
        String ret = "Empty";
        URL url = (URL)params[0];
        try {
            HttpURLConnection urlConnection;
            Log.e("Huh?", String.format("Protocol: %s", url.getProtocol()));
            if (url.getProtocol().equals("https")) {
                Log.e("Huh?","HANDLING SECURE!");
                final SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                urlConnection = (HttpsURLConnection) url.openConnection();
            } else {
                Log.e("Huh?","NOT SECURE!");
                urlConnection = (HttpURLConnection) url.openConnection();

            }

            String userpassword = "gatewayadmin" + ":" + "kjhcuKKo";
            String encodedAuthorization = Base64.encodeToString(userpassword.getBytes(), Base64.DEFAULT);
            urlConnection.setRequestProperty("Authorization", "Basic "+ encodedAuthorization);

            int responseCode = urlConnection.getResponseCode();
            Log.e("Huh?",String.format("Response Code: %s", responseCode));
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;

            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Log.e("Huh?", String.format("response: %s", response.toString()));
            ret = response.toString();

        } catch (UnknownHostException ex){
            Log.e("Huh?",String.format("IOException: %s", ex.toString()));
            ret = ex.getMessage();
        } catch (IOException ex){
            Log.e("Huh?",String.format("IOException: %s", ex.toString()));
            ret = ex.getMessage();
        } catch (NoSuchAlgorithmException ex) {
            Log.e("Huh?",String.format("NoSuchAlgorithmException: %s", ex.toString()));
            ex.printStackTrace();
            ret = ex.getMessage();
        } catch (KeyManagementException ex) {
            Log.e("Huh?",String.format("KeyManagementException: %s", ex.toString()));
            ex.printStackTrace();
            ret = ex.getMessage();
        }

        return ret;
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(String value) {
        if (isCancelled()) {
        }
        String[] values = {"EmailSent","ErrorEmail","BrightMoveEndpoint","DB","Hostname"};

        String ret = "";

        try {
            JSONObject json = new JSONObject(value);

            if (json.has("BrightMoveEndpoint")){
                ret += "Brightmove Endpoint: " + json.getString("BrightMoveEndpoint") + "\n\n";
            }

            if (json.has("DB")){
                ret += "DB Spec: " + json.getString("DB") + "\n\n";
            }

            if (json.has("PGbouncerDetected") && json.getString("PGbouncerDetected").equals("true")){
                ret += "PGBouncer appears to be installed\n";
                ret += "PGBouncer DB Spec: " + json.getString("PGBDBSpec") + "\n\n";
            }

            if (json.has("Hostname")) {
                ret += "Hostname: " + json.getString("Hostname") + "\n\n";
            }

            if (json.has("ErrorEmail")) {
                ret += "Error Email: " + json.getString("ErrorEmail") + "\n\n";
            }

            if (json.has("EmailSent")){
                if (json.getString("EmailSent").equals("NOT MOCK")){
                    ret += "Email system is not mocked\n\n";
                }else{
                    ret += "Email system is mocked\nNo emails will be going out\n\n";
                }
            }


            //for (String str : values) {
            //    if (json.has(str)) {
            //        ret += str + ": " + json.getString(str) + "\n\n";
            //    }
            //}
        } catch(JSONException ex){
            ret = String.format("Unable to parse '%s'",value);
        }

        TextView textView = textViewRef.get();
        textView.setText(ret);

    }
}
