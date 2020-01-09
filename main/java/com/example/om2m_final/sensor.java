package com.example.om2m_final;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;

public class sensor extends AppCompatActivity {
    String res;
    String type;
    String data;
    String on;
    String appId;
    public String result;
    String catagory;
    String unit;
    private final String password = "admin:admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        Intent intent=getIntent();
        res=intent.getStringExtra("res");
        TextView tv1=(TextView)findViewById(R.id.textView);
        split_text();
        layout_onclick();

        //tv1.setText(res);
    }

    public void split_text(){
        // 一項目切
        String tmp[]=res.split("\"");
        for(int i=0;i<tmp.length;i++){
            switch (tmp[i]){
                case "type":
                    TextView tv10=(TextView)findViewById(R.id.textView10);
                    tv10.setText(tmp[i+2]);
                    type=tmp[i+2];
                    break;
                case "appId":
                    TextView tv9=(TextView)findViewById(R.id.textView9);
                    tv9.setText(tmp[i+2]);
                    appId=tmp[i+2];
                    break;
                case "value":
                    TextView tv11=(TextView)findViewById(R.id.textView11);
                    data=tmp[i+2];
                    tv11.setText(tmp[i+2]);
                    break;
                case "switch":
                    TextView tv12=(TextView)findViewById(R.id.textView12);
                    on=tmp[i+2];
                    tv12.setText(tmp[i+2]);
                    break;
                case"category":
                    catagory=tmp[i+2];
                    break;
                case"unit":
                    unit=tmp[i+2];
                    break;
            }
        }
    }


    public void layout_onclick(){
        LinearLayout ll=(LinearLayout)findViewById(R.id.linearLayout2);
        if(type.equals("sensor")){
            Toast.makeText(sensor.this, "Sensor不能更改數值",Toast.LENGTH_SHORT).show();
            return;
        }


        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(sensor.this);
                v= inflater.inflate(R.layout.alert_dialog, null);
                final View finalV = v;
                new AlertDialog.Builder(sensor.this)
                        .setTitle("data")
                        .setView(v)

                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) (finalV.findViewById(R.id.editText1));
                                data=editText.getText().toString();
                                goodtask mtask;
                                mtask= (goodtask) new goodtask().execute("http://140.116.247.73:8181/om2m/gscl/applications/"+appId+"/containers/DATA/contentInstances","POST");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();

                                }
                                mtask.cancel(true);
                                TextView tv11=(TextView)findViewById(R.id.textView11);
                                tv11.setText(data);
                            }
                        })
                        .setNeutralButton("取消",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                // TODO Auto-generated method stub
                                Toast.makeText(sensor.this, "取消",Toast.LENGTH_SHORT).show();
                            }
                        })

                        .show();
            }
        });
    }


    public class goodtask extends AsyncTask<String, Integer, String> {

        @Override
        @RequiresApi(api = Build.VERSION_CODES.O)
        protected String doInBackground(String... strings) {

            try {
                //輸入URL

                URL url=new URL(strings[0]);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                if(strings[1].equals("GET")){
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", "Basic "+ Base64.getEncoder().encodeToString(password.getBytes()));//Base64加密 並登入
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            conn.getInputStream(), "UTF-8"));

                    for (String line; (line = reader.readLine()) != null; result += line);
                    //System.out.println(response);
                    reader.close();
                    conn.disconnect();

                    ;
                    //send_data(name,response);
                    //split_app(response);
                    //System.out.println(app_name.get(0));
                }
                else if(strings[1].equals("POST")){

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "Basic "+ Base64.getEncoder().encodeToString(password.getBytes()));//Base64加密 並登入
                    System.out.println("post"+data);
                    //conn.connect();
                    String str="<obj>" +
                            "<str name=\"type\" val=\""+type+"\"/>" +
                            "<str name=\"appId\" val=\""+appId+"\"/>" +
                            "<str name=\"category\" val=\""+catagory+" \"/>" +
                            "<int name=\"value\" val=\""+data+"\"/>" +
                            "<int name=\"unit\" val=\""+unit+"\"/>" +
                            "<str name=\"switch\" val=\""+on+"\"/>" +
                            "</obj>";


                    conn.setDoOutput(true);
                    conn.setRequestProperty( "charset", "utf-8");
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(String.valueOf(str));
                    wr.flush();
                    wr.close();
                    int responseCode = conn.getResponseCode();
                    System.out.println("res"+responseCode);

//                    conn.getOutputStream().write(str.getBytes());
//                    conn.getOutputStream().flush();
//                    conn.getOutputStream().close();close



                    conn.disconnect();
                }


                //goodtask.cancel(true);
            } catch (MalformedURLException e) {
                System.out.println("157");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("160");
                e.printStackTrace();
            }

//            System.out.println("response"+response);
            System.out.println("over");

            System.out.println(isCancelled());
            return null;
        }


        @Override
        public void onPostExecute(String response)
        {

        }


//
//        public void split_app(String s){
//
//            String[]tmp=s.split("</om2m:namedReference>");
//            int con=tmp.length-1;
//            //System.out.println(con);
//            //tv1.setText(con);
//            String[] str=s.split("\"");
//            for(int i=0;i<con;i++){
//                app_name.add(str[11+2*i]);
//            }
//
//        }



    }
}
