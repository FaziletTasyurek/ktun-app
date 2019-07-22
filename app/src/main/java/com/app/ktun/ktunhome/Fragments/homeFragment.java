package com.app.ktun.ktunhome.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.ktun.ktunhome.Adapter.ViewPagerAdapter;
import com.app.ktun.ktunhome.R;
import com.app.ktun.ktunhome.Service.VolleyRequest;
import com.app.ktun.ktunhome.Model.Slider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class homeFragment extends Fragment {
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    View view;
    private graduateFragment.OnFragmentInteractionListener mListener;
    RequestQueue rq;
    List<Slider> sliderImg;
    ViewPagerAdapter viewPagerAdapter;
    TextView slider_title;
    TextView slider_date;

    String request_url = "http://ktun.edu.tr/apimobile/Duyurulistesi";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout lyt_slider_right, lyt_slider_left;
        view = inflater.inflate(R.layout.fragment_home, container, false);

        lyt_slider_right = view.findViewById(R.id.lyt_slider_right);
        lyt_slider_left = view.findViewById(R.id.lyt_slider_left);
        sliderDotspanel = view.findViewById(R.id.SliderDots);
        viewPager = view.findViewById(R.id.viewPager);
        slider_title = view.findViewById(R.id.slider_title);
        slider_date = view.findViewById(R.id.slider_date);

        rq = VolleyRequest.getInstance(getActivity()).getRequestQueue();

        sliderImg = new ArrayList<>();

        sendRequest();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Log.d("tarihveri", sliderImg.get(position).getKAYITTARIHI());
                slider_title.setText(sliderImg.get(position).getBASLIK());
                slider_date.setText(sliderImg.get(position).getYAYINTARIHIBIT());

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_udot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        lyt_slider_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    slider_date.setText(sliderImg.get(tab).getYAYINTARIHIBIT());
                    Log.d("tarihveri", sliderImg.get(tab).getKAYITTARIHI());
                    slider_title.setText(sliderImg.get(tab).getBASLIK());
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });

        lyt_slider_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                slider_date.setText(sliderImg.get(tab).getYAYINTARIHIBIT());

                slider_title.setText(sliderImg.get(tab).getBASLIK());
                viewPager.setCurrentItem(tab);
            }
        });

        return view;
    }

    public void sendRequest() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, request_url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    Slider sliderUtils = new Slider();

                    try {


                        JSONObject jsonObject = response.getJSONObject(i);

                        Log.d("sonveri", timestampToDate(parseDate(jsonObject.getString("YAYINTARIHIBAS"))));
                        timestampToDate(parseDate(jsonObject.getString("YAYINTARIHIBAS")));
                        sliderUtils.setFOTOPATH("http://ktun.edu.tr/" + jsonObject.getString("FOTOPATH"));
                        sliderUtils.setBASLIK(jsonObject.getString("BASLIK"));
                        sliderUtils.setID(Integer.valueOf(jsonObject.getString("ID")));
                        sliderUtils.setANAHTARKELIMELER(jsonObject.getString("ANAHTARKELIMELER"));
                        sliderUtils.setKAYITTARIHI(timestampToDate(parseDate(jsonObject.getString("KAYITTARIHI"))));
                        sliderUtils.setYAYINTARIHIBAS(timestampToDate(parseDate(jsonObject.getString("YAYINTARIHIBAS"))));
                        sliderUtils.setYAYINTARIHIBIT(timestampToDate(parseDate(jsonObject.getString("YAYINTARIHIBIT"))));

                        Log.d("tarih", jsonObject.getString("YAYINTARIHIBIT"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sliderImg.add(sliderUtils);


                }


                viewPagerAdapter = new ViewPagerAdapter(sliderImg,getActivity());

                viewPager.setAdapter(viewPagerAdapter);

                dotscount = viewPagerAdapter.getCount();
                dots = new ImageView[dotscount];

                for (int i = 0; i < dotscount; i++) {

                    dots[i] = new ImageView(getActivity());
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_udot));

                    LinearLayout.LayoutParams params =
                            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);

                    params.setMargins(8, 0, 8, 0);

                    sliderDotspanel.addView(dots[i], params);

                }

                dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dot));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("hata", error.getMessage());
            }
        });

        VolleyRequest.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);

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

    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof graduateFragment.OnFragmentInteractionListener) {
            mListener = (graduateFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

}
