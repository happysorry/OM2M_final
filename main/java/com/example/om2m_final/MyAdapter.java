package com.example.om2m_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Member;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String>app_name=new ArrayList<String>();
    private final String password = "admin:admin";
    public String result;
    class ViewHolder extends RecyclerView.ViewHolder{
        // 宣告元件
        private TextView tv1;

        ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.txtitem);
        }
    }

    private void task_finish(String res){
       this.result=res;
       System.out.println("rsult"+res);
    }



    MyAdapter( ArrayList<String> app_name) {

        this.app_name=app_name;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        TextView tv1=(TextView)view.findViewById(R.id.txtitem);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, int position) {
        holder.tv1.setText(app_name.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                int pos=holder.getAdapterPosition();
                String name=app_name.get(pos);
                //get sensor name
                System.out.println(app_name.get(pos));
                goodtask mtask;
                mtask= (goodtask) new goodtask().execute("http://140.116.247.73:8181/om2m/gscl/applications/"+name+"/containers/DATA/contentInstances/latest/content","GET",name);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mtask.cancel(true);
                Context context = v.getContext();
                Intent intent = new Intent(context, sensor.class);
                System.out.println("res"+result);
                //while(result==null);
                intent.putExtra("res",result);
                context.startActivity(intent);

                /*
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(member.getImage());
                Toast toast = new Toast(context);
                toast.setView(imageView);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();

                 */
            }
        });

    }

    @Override
    public int getItemCount() {
        return app_name.size();
    }
/*
    //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv1;
        ViewHolder(View itemView) {
            super(itemView);

        }
    }

 */




    //實作HTTP REQUEST
    public class goodtask extends AsyncTask<String, Integer, String>  {

        @Override
        @RequiresApi(api = Build.VERSION_CODES.O)
        protected String doInBackground(String... strings) {

            try {
                //輸入URL
                String name=strings[2];
                URL url=new URL(strings[0]);
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                if(strings[1].equals("GET")){
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Authorization", "Basic "+Base64.getEncoder().encodeToString(password.getBytes()));//Base64加密 並登入
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

                return  "23";
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



        public void split_app(String s){

            String[]tmp=s.split("</om2m:namedReference>");
            int con=tmp.length-1;
            //System.out.println(con);
            //tv1.setText(con);
            String[] str=s.split("\"");
            for(int i=0;i<con;i++){
                app_name.add(str[11+2*i]);
            }

        }


/*
        public void send_data(String name,String res){
            data da=(data)getApplicationContext();
            da.setNmae(name);
            da.setResponse(res);
        }

*/


    }
}