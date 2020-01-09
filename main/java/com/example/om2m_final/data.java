package com.example.om2m_final;
import android.app.Application;
public class data extends Application{
    private String name;
    private String response;

    public void setNmae(String nmae){
        this.name=nmae;
    }
    public void setResponse(String response){
        this.response=response;
    }

    public String getName(){
        return name;
    }
    public String getResponse(){
        return response;
    }

}
