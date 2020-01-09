package com.example.om2m_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.app.Application;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.net.*;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {

    private String gscl_base = "http://127.0.0.1:8181/om2m/";
    private String gscl_name = "gscl";
    private String control_ip = "http://127.0.0.1:9999";
    private int control_port;
    private final String password = "admin:admin";
    private String GA_uri = "";
    private final boolean init = false;
    //private final boolean init=true;
    //private String app_name="";
    public URL url;
    public ArrayList<String>app_name=new ArrayList<>();
    public ArrayList<String>fake_data=new ArrayList<>();
    final data da=(data)getApplication();


    ArrayList<String> app = new ArrayList();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data da=(data)getApplicationContext();

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        Button but1=(Button)findViewById(R.id.button3);
        tv1.setText("dfas");
        but1.setOnClickListener(but1_listener);
        ///////
        // 新的thread////////////////
        for(int i=0;i<10;i++)
            fake_data.add("asd");
        /*
        //discovery();
       RecyclerView recycle=(RecyclerView)findViewById(R.id.recyclerView);
       recycle.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
       MyAdapter adapter=new MyAdapter(app_name);
       recycle.setAdapter(adapter);
        */
        //set_recyclerView();

    }


    public Button.OnClickListener but1_listener=new Button.OnClickListener(){
        //Button but1=(Button)findViewById(R.id.button3);
        @Override
        public void onClick(View v) {

            new goodtask().execute("http://140.116.247.73:8181/om2m/gscl/applications","GET");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    };


    //實作HTTP REQUEST
    public class goodtask extends AsyncTask<String, Integer, String> {
        TextView tv1=(TextView)findViewById(R.id.tv1);




        @SuppressLint("WrongThread")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {
            try {
                //輸入URL
                URL url=new URL(strings[0]);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                if(strings[1].equals("GET")){
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", "Basic "+Base64.getEncoder().encodeToString(password.getBytes()));//Base64加密 並登入
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "UTF-8"));
                    String response = new String();
                    for (String line; (line = reader.readLine()) != null; response += line);

                    tv1.setText(response);
                    split_app(response);
                    //System.out.println(app_name.get(0));
                }





            } catch (MalformedURLException e) {
                tv1.setText("139");
                e.printStackTrace();
            } catch (IOException e) {
                tv1.setText("142");
                e.printStackTrace();
            }


            return null;
        }


        @Override
        public void onPostExecute(String result )
        {
            super.onPostExecute(result);
            set_recyclerView();

            // 背景工作處理完"後"需作的事


        }

        public void split_app(String s){

            String[]tmp=s.split("</om2m:namedReference>");
            int con=tmp.length-1;
            //System.out.println(con);
            //tv1.setText(con);
            String[] str=s.split("\"");
            for(int i=0;i<con;i++){
                app_name.add(str[11+2*i]);
            }
            tv1.setText(str[11]);

        }
        public void set_recyclerView(){
            RecyclerView recycle=(RecyclerView)findViewById(R.id.recyclerView);
            recycle.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
            ///////////////設置格線//////////////////////
            /////////////暫時不會做 跳過////////////////
            //recycle.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            ////////////////////////////////////////////
            MyAdapter adapter=new MyAdapter(app_name);
            recycle.setAdapter(adapter);

        }

        public void recyclerViewListClicked(View v, int position){

        }

    }

}
