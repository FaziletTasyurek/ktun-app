package com.app.ktun.ktunhome.Service;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.ktun.ktunhome.Model.Duyuru;
import com.app.ktun.ktunhome.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Duyuru_ac extends AppCompatActivity {
TextView icerik;
 TextView baslık;
    RequestQueue rq;
    Context context;
    String request_url = "http://ktun.edu.tr/apimobile/DuyuruDetay/";
    String rerere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyuru);
        icerik=(TextView)findViewById(R.id.textView) ;
       baslık=(TextView)findViewById(R.id.baslık) ;
        Intent i=getIntent();
context=this;
     String value=i.getStringExtra("baslık");
        rq = VolleyRequest.getInstance(getApplicationContext()).getRequestQueue();
        icerik.setText(value);
        request_url="http://ktun.edu.tr/apimobile/DuyuruDetay/"+value;
        Log.d("hacıamca", request_url);
sendRequest();
    }
    private void sendRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                    Duyuru duyuru = new Duyuru();

                    try {


                        Log.d("sonveri", timestampToDate(parseDate(response.getString("YAYINTARIHIBAS"))));
                        timestampToDate(parseDate(response.getString("YAYINTARIHIBAS")));
                        duyuru.setFOTOPATH("http://ktun.edu.tr/" + response.getString("FOTOPATH"));
                        duyuru.setBASLIK(response.getString("BASLIK"));
                        duyuru.setID(Integer.valueOf(response.getString("ID")));
                        duyuru.setANAHTARKELIMELER(response.getString("ANAHTARKELIMELER"));
                        duyuru.setKAYITTARIHI(timestampToDate(parseDate(response.getString("KAYITTARIHI"))));
                        duyuru.setYAYINTARIHIBAS(timestampToDate(parseDate(response.getString("YAYINTARIHIBAS"))));
                        duyuru.setYAYINTARIHIBIT(timestampToDate(parseDate(response.getString("YAYINTARIHIBIT"))));
                        baslık.setText(duyuru.getBASLIK());



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hata", error.getMessage());
            }
        });

        VolleyRequest.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }
    public static String parseDate(String data) {
        String date1 = data.replace("/Date(", "");
        return date1.replace(")/", "");
    }

    public static String timestampToDate(String timestamp) {


        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(Long.parseLong(timestamp));
        return sf.format(date);
    }
}
