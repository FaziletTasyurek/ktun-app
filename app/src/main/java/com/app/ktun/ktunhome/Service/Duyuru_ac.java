package com.app.ktun.ktunhome.Service;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.ktun.ktunhome.Model.Duyuru;
import com.app.ktun.ktunhome.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Duyuru_ac extends AppCompatActivity {
TextView icerik;
 TextView baslık;
 TextView Tarih;
    RequestQueue rq;
    Context context;
    TextView metin;
    String request_url = "http://ktun.edu.tr/apimobile/DuyuruDetay/";
  ImageView foto;
    WebView webView;
    private static String html = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyuru);
        icerik=(TextView)findViewById(R.id.textView) ;
       baslık=(TextView)findViewById(R.id.baslık) ;
       foto=(ImageView)findViewById(R.id.imageView2) ;
       foto.setImageResource(R.drawable.logo);
       Tarih=(TextView)findViewById(R.id.Duyuru_tarih) ;
       metin=(TextView)findViewById(R.id.metin) ;
        webView = findViewById(R.id.webview);
        Intent i=getIntent();
context=this;
     String value=i.getStringExtra("baslık");
        rq = VolleyRequest.getInstance(getApplicationContext()).getRequestQueue();

        request_url="http://ktun.edu.tr/apimobile/DuyuruDetay/"+value;

sendRequest();
    }
    private void sendRequest() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, request_url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                    Duyuru duyuru = new Duyuru();

                    try {
                        Log.d("deneme",request_url);

                        Log.d("sonveri", timestampToDate(parseDate(response.getString("YAYINTARIHIBAS"))));
                        timestampToDate(parseDate(response.getString("YAYINTARIHIBAS")));
                        duyuru.setFOTOPATH("http://ktun.edu.tr/" + response.getString("FOTOPATH"));
                        duyuru.setBASLIK(response.getString("BASLIK"));
                        duyuru.setKISAICERIK(response.getString("KISAICERIK"));
                        duyuru.setID(Integer.valueOf(response.getString("ID")));
                        duyuru.setANAHTARKELIMELER(response.getString("ANAHTARKELIMELER"));
                        duyuru.setKAYITTARIHI(timestampToDate(parseDate(response.getString("KAYITTARIHI"))));
                        duyuru.setYAYINTARIHIBAS(timestampToDate(parseDate(response.getString("YAYINTARIHIBAS"))));
                        duyuru.setYAYINTARIHIBIT(timestampToDate(parseDate(response.getString("YAYINTARIHIBIT"))));
                        baslık.setText(duyuru.getBASLIK());
                        Tarih.setText(timestampToDate(parseDate(response.getString("YAYINTARIHIBAS"))));

                        String icerik = String.valueOf(response.getJSONArray("ICERIK"));

                        String[] byteValues = icerik.substring(12 ,icerik.length() - 1).split(",");

                        byte[] bytes = new byte[byteValues.length];

                        for (int i =0, len = bytes.length; i < len; i++) {
                            if (isTurkishLetter(Integer.valueOf(byteValues[i]))
                            ) {
                                parseTurkishByte(Integer.valueOf(byteValues[i]), Integer.valueOf(byteValues[i + 1]));
                            } else {
                                int a = Integer.parseInt(byteValues[i]);

                                html += Character.toString((char) a);

                            }

                        }  webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
                        html="";


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


        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date(Long.parseLong(timestamp));
        return sf.format(date);
    }
    public static void parseTurkishByte(int letter, int letterInc) {
        if (letter == 196 && letterInc == 176) {
            html += "İ";
        }
        if (letter == 196 && letterInc == 177) {
            html += "ı";
        }
        if (letter == 197 && letterInc == 158) {
            html += "Ş";
        }
        if (letter == 197 && letterInc == 159) {
            html += "ş";
        }
        if (letter == 195 && letterInc == 156) {
            html += "Ü";
        }
        if (letter == 195 && letterInc == 188) {
            html += "ü";
        }
        if (letter == 195 && letterInc == 150) {
            html += "Ö";
        }
        if (letter == 195 && letterInc == 182) {
            html += "ö";
        }
        if (letter == 196 && letterInc == 159) {
            html += "ğ";
        }
        if (letter == 196 && letterInc == 158) {
            html += "Ğ";
        }
        if (letter == 195 && letterInc == 135) {
            html += "Ç";
        }
        if (letter == 196 && letterInc == 167) {
            html += "ç";
        }

    }


    public static boolean isTurkishLetter(int letter) {
        return letter == 195
                || letter == 196
                || letter == 197
                || letter == 159
                || letter == 177
                || letter == 176
                || letter == 167
                || letter == 135

               ;

    }

}
