package com.wenhuaijun.cartoonautosplit.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.boycy815.pinchimageview.PinchImageView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.wenhuaijun.cartoonautosplit.R;
import com.wenhuaijun.cartoonautosplit.imageLoader.EasyImageLoader;
import com.wenhuaijun.cartoonautosplit.imageLoader.MD5Utils;
import com.wenhuaijun.cartoonautosplit.utils.JUtils;
import com.wenhuaijun.cartoonautosplit.view.PinchImageViewPager;

import java.io.IOException;
import java.io.InputStream;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenActivity extends AppCompatActivity implements View.OnClickListener, PinchImageViewPager.OnPageChangeListener {
    private PinchImageView pinchImageView;
    private PinchImageViewPager viewPager;
    private TextView tip;
    private Button btn1;
    private Bitmap[] bitmaps;
    private MyViewPager adapter;
    private String url;
    private Handler handler;
    private ProgressWheel progressWheel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        url =getIntent().getStringExtra("url");
        pinchImageView = (PinchImageView)findViewById(R.id.imageView);
        progressWheel =(ProgressWheel)findViewById(R.id.progress_wheel);
        tip = (TextView)findViewById(R.id.tip);
        viewPager =(PinchImageViewPager)findViewById(R.id.pager);
        btn1 =(Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        handler = new Handler();
        EasyImageLoader.getInstance(this).getBitmap(url, new EasyImageLoader.BitmapCallback() {
            @Override
            public void onResponse(Bitmap bitmap) {

                pinchImageView.setImageBitmap(bitmap);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pinchImageView.doubleTap(544, 723);

                    }
                }, 200);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressWheel.setVisibility(View.GONE);
                        pinchImageView.setVisibility(View.VISIBLE);

                    }
                }, 600);
            }
        });

      //  ins =this.getClass().getClassLoader().getResourceAsStream("assets/sample.jpg");
        //pinchImageView.setImageBitmap(getRectBitmap(ins));



    }
    public void getRectBitmap() throws IOException {
        InputStream ins = EasyImageLoader.getImageDiskLrucache(this).getmDiskLruCache().get(MD5Utils.hashKeyFromUrl(url)).getInputStream(0);
      //  InputStream ins =this.getClass().getClassLoader().getResourceAsStream("assets/sample.jpg");
        //获得图片的宽、高
        BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
        tmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ins, null, tmpOptions);
        int width = tmpOptions.outWidth;
        int height = tmpOptions.outHeight;
        JUtils.Log("width  = "+width+" height = "+height);
        //设置显示图片的中心区域
        BitmapRegionDecoder bitmapRegionDecoder = null;
        try {
            ins = EasyImageLoader.getImageDiskLrucache(this).getmDiskLruCache().get(MD5Utils.hashKeyFromUrl(url)).getInputStream(0);
           // ins =this.getClass().getClassLoader().getResourceAsStream("assets/sample.jpg");
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(ins, false);
            BitmapFactory.Options options = new BitmapFactory.Options();
            // options.inPreferredConfig = Bitmap.Config.RGB_565;
            bitmaps = new Bitmap[4];
            options.inJustDecodeBounds =false;
            //左上角
            bitmaps[0] = bitmapRegionDecoder.decodeRegion(new Rect(0, 0, width / 2, height / 2), options);
            //右上角
            bitmaps[1] = bitmapRegionDecoder.decodeRegion(new Rect(width / 2, 0, width, height / 2), options);
            //左下角
            bitmaps[2] = bitmapRegionDecoder.decodeRegion(new Rect(0, height / 2, width / 2, height), options);
            //右下角
            bitmaps[3] = bitmapRegionDecoder.decodeRegion(new Rect(width / 2, height / 2, width, height), options);
        } catch (IOException e) {
            e.printStackTrace();        }
      /*  if (bitmap1!=null){
            JUtils.Log("bitmap1 ! = null");
        }else{
            JUtils.Log("bitmap1 = = null");
        }*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
                try {
                    getRectBitmap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pinchImageView.setVisibility(View.GONE);
                btn1.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
                adapter = new MyViewPager();
                viewPager.setAdapter(adapter);
                viewPager.setOnPageChangeListener(FullscreenActivity.this);
                tip.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tip.setText(position+1+"/"+bitmaps.length);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyViewPager extends PagerAdapter{

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PinchImageView pinchImageView = new PinchImageView(FullscreenActivity.this);
            pinchImageView.setImageBitmap(bitmaps[position]);
            container.addView(pinchImageView);
            return pinchImageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((PinchImageView)object);
        }
    }
}
