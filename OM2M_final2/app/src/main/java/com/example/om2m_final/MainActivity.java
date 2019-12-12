package com.example.om2m_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.io.InputStream;
import java.net.*;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private String gscl_base="http://127.0.0.1:8181/om2m/";
    private String gscl_name="gscl";
    private String control_ip="http://127.0.0.1:9999";
    private int control_port;
    private final String password="admin:admin";
    private String GA_uri="";
    private final boolean init=false;
    //private final boolean init=true;
    //private String app_name="";
    public URL url;



    ArrayList<String> app=new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    //send http request
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void send_http(String method, URL url, String data){
        try {

            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod(method);
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Authorization", "Basic "+ Base64.getEncoder().encodeToString(password.getBytes()));//Base64加密 並登入
            if(method=="POST"||method=="PUT"){
                DataOutputStream out=new DataOutputStream(httpConnection.getOutputStream());
                out.write(data.getBytes(Charset.forName("utf-8")));
                out.flush();
            }
            else if(method=="GET"){
                httpConnection.connect();
            }
            else if(method=="DELETE"){
                httpConnection.connect();
            }
            System.out.println("response code:"+httpConnection.getResponseCode()+"\tmethod:"+method+"\turl:"+url);
            InputStreamReader isr = null;
            BufferedReader br = null;
            String line = null;

            isr = new InputStreamReader(httpConnection.getInputStream());
            br = new BufferedReader(isr);
            br.close();
            isr.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //開啟app實作discovery動作
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void discovery(){
        try {
            url=new URL("http://127.0.0.1:8181/om2m/gscl/applications");
            String data=null;
            send_http("GET",url,data);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    
    
    public void subscribe(String app_name){
        //url=new URL("")
    }
}
