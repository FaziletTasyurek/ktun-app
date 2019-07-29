package com.app.ktun.ktunhome.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.app.ktun.ktunhome.Model.Duyuru;
import com.app.ktun.ktunhome.Model.Slider;
import com.app.ktun.ktunhome.R;
import com.app.ktun.ktunhome.Service.Duyuru_ac;
import com.app.ktun.ktunhome.Service.VolleyRequest;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
   private LayoutInflater layoutInflater;

    private List<Slider> sliderImg;
    private ImageLoader imageLoader;


    public ViewPagerAdapter(List sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;

    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

    String url="http://ktun.edu.tr/apimobile/Duyurulistesi";
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_viewpager, null);


        Slider utils = sliderImg.get(position);

        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        //TextView txt_slider_title = (TextView) view.findViewById(R.id.txt_slider_title);
        Button vp_right = view.findViewById(R.id.vp_right);
        Button vp_left = view.findViewById(R.id.vp_left);
       TextView txt_slider_title=(TextView)view.findViewById(R.id.slider_title) ;






        imageLoader = VolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getFOTOPATH(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher,
                android.R.drawable.ic_dialog_alert));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
Intent ıntent=new Intent(context.getApplicationContext(),Duyuru_ac.class);


                ıntent.putExtra("baslık",String.valueOf(sliderImg.get(position).getID()));
                context.startActivity(ıntent);
            }
        });

        final ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);



        return view;
}

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);


    }

}

