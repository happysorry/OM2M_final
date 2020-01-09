package com.example.om2m_final;


public class http_response_data {
    private String response;
    private int responsecode;
    public String getResponse() {
        return response;
    }
    public void setResponse(String response) {
        if(response!=null)
            this.response = response.replace("\n", "");
        else
            this.response = response;
    }
    public int getResponsecode() {
        return responsecode;
    }
    public void setResponsecode(int responsecode) {
        this.responsecode = responsecode;
    }
}
