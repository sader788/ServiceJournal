package com.example.servicejournal;

import java.io.IOException;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostRequest{

    private String url;

    public PostRequest (String _url) {
        url = _url;
    }

    public void sendData(String num, String type, String date, String duration) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("nubmer", num)
                .add("type", type)
                .add("date", date)
                .add("duration", duration)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void sendFinish(){
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("status", "finish")
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            // Do something with the response.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
