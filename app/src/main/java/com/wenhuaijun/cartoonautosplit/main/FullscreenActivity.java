package com.wenhuaijun.cartoonautosplit.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
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
public class FullscreenActivity extends AppCompatActivity implements View.OnClickListener, PinchImageViewPager.OnPageChangeListener, View.OnTouchListener {
    private PinchImageView pinchImageView;
    private PinchImageViewPager viewPager;
    private View verticalView;
    private View horizontalView;
    private TextView tip;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Bitmap[] bitmaps;
    private MyViewPager adapter;
    private String url;
    private Handler handler;
    private ProgressWheel progressWheel;
    private float x;
    private float y;
    private RelativeLayout.LayoutParams layoutParamsVertical;
    private RelativeLayout.LayoutParams layoutParamsHorizontal;
    private  int screenWidth;
    private int screenHeight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        url =getIntent().getStringExtra("url");
        pinchImageView = (PinchImageView)findViewById(R.id.imageView);
        progressWheel =(ProgressWheel)findViewById(R.id.progress_wheel);
        verticalView =findViewById(R.id.vertical_view);
        horizontalView =findViewById(R.id.horizontal_view);
        layoutParamsVertical =(RelativeLayout.LayoutParams)verticalView.getLayoutParams();
        layoutParamsHorizontal =(RelativeLayout.LayoutParams)horizontalView.getLayoutParams();

        tip = (TextView)findViewById(R.id.tip);
        viewPager =(PinchImageViewPager)findViewById(R.id.pager);
        btn1 =(Button)findViewById(R.id.btn1);
        btn2 =(Button)findViewById(R.id.btn2);
        btn3 =(Button)findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
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
        screenWidth =JUtils.getScreenWidth();
        screenHeight =JUtils.getScreenHeight();
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
        JUtils.Log("width  = " + width + " height = " + height);
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
    }

    public void getRectBitmap(int x,int y) throws IOException {
        /* int x2 =((int)x-170)/2;
                    int y2 =((int)y-445)/2;
                    JUtils.Log("x2: "+x2+" y2: "+y2);*/

        InputStream ins = EasyImageLoader.getImageDiskLrucache(this).getmDiskLruCache().get(MD5Utils.hashKeyFromUrl(url)).getInputStream(0);
        //  InputStream ins =this.getClass().getClassLoader().getResourceAsStream("assets/sample.jpg");
        //获得图片的宽、高
        BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
        tmpOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ins, null, tmpOptions);
        int width = tmpOptions.outWidth;
        int height = tmpOptions.outHeight;
        int x_distance =screenWidth/2-width;
        int y_distance =screenHeight/2-JUtils.dip2px(48)-height;
        int currentX = (x-x_distance)/2;
        int currentY = (y-y_distance)/2;
        JUtils.Log("width  = "+width+" height = "+height);
        //等比缩放

      //  height =height*(screenWidth/width);
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
            bitmaps[0] = bitmapRegionDecoder.decodeRegion(new Rect(0, 0, currentX, currentY), options);
            //右上角
            bitmaps[1] = bitmapRegionDecoder.decodeRegion(new Rect(currentX, 0, width, currentY), options);
            //左下角
            bitmaps[2] = bitmapRegionDecoder.decodeRegion(new Rect(0, currentY, currentX, height), options);
            //右下角
            bitmaps[3] = bitmapRegionDecoder.decodeRegion(new Rect(currentX, currentY, width, height), options);
        } catch (IOException e) {
            e.printStackTrace();        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn1:
                try {
                    getRectBitmap();
                    pinchImageView.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                    btn2.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    adapter = new MyViewPager();
                    viewPager.setAdapter(adapter);
                    viewPager.setOnPageChangeListener(FullscreenActivity.this);
                    tip.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn2:
                pinchImageView.doubleTap(544, 723);
                pinchImageView.setVisibility(View.GONE);
                pinchImageView.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.GONE);
                btn2.setVisibility(View.GONE);
                btn3.setVisibility(View.VISIBLE);
                layoutParamsVertical.leftMargin =JUtils.getScreenWidth()/2;
                x=layoutParamsVertical.leftMargin;
                layoutParamsHorizontal.topMargin =(JUtils.getScreenHeight())/2-JUtils.dip2px(48);
               // layoutParamsHorizontal.topMargin =(JUtils.getScreenHeight()-JUtils.dip2px(96))/2;
                y=layoutParamsHorizontal.topMargin;
                verticalView.setLayoutParams(layoutParamsVertical);
                horizontalView.setLayoutParams(layoutParamsHorizontal);
                verticalView.setVisibility(View.VISIBLE);
                horizontalView.setVisibility(View.VISIBLE);
                verticalView.setOnTouchListener(this);
                horizontalView.setOnTouchListener(this);
                break;
            case R.id.btn3:
                btn3.setVisibility(View.GONE);
                try {

                    getRectBitmap((int)x,(int)y);
                    verticalView.setVisibility(View.GONE);
                    horizontalView.setVisibility(View.GONE);
                    pinchImageView.setVisibility(View.GONE);
                    btn1.setVisibility(View.GONE);
                    btn2.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    adapter = new MyViewPager();
                    viewPager.setAdapter(adapter);
                    viewPager.setOnPageChangeListener(FullscreenActivity.this);
                    tip.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(v==verticalView){
                    //verticalView.scrollTo((int) event.getX(), (int) verticalView.getY());
                    layoutParamsVertical.leftMargin+=event.getX();
                    verticalView.setLayoutParams(layoutParamsVertical);
                }else if(v==horizontalView){
                    //horizontalView.scrollTo((int)horizontalView.getX(),(int)event.getY());
                    layoutParamsHorizontal.topMargin+=event.getY();
                    horizontalView.setLayoutParams(layoutParamsHorizontal);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(v==verticalView){
                    x =layoutParamsVertical.leftMargin;
                }else if(v==horizontalView){
                    y =layoutParamsHorizontal.topMargin;
                }
                JUtils.Log("ACTION_UP-------- x="+x+" y=" +y);
                break;
        }
        return true;
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
