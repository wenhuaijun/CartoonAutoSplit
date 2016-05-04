package com.wenhuaijun.cartoonautosplit.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.wenhuaijun.cartoonautosplit.R;
import com.wenhuaijun.cartoonautosplit.imageLoader.NetRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/5/4 0004.
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageRecyclerAdapter recyclerAdapter;
    private String searchWord="四格漫画";
    private ProgressWheel progressWheel;
    private int layoutSysle =Constants.StagedGridLayoutStyle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView =(RecyclerView)findViewById(R.id.recyclerView);
        progressWheel =(ProgressWheel)findViewById(R.id.progress_wheel);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        // addScrollListener();
        searchPicture(searchWord);
    }
    /**
     * 搜索图片请求
     * @param searchWord 搜索关键字
     */
    private void searchPicture(String searchWord) {
        try {
            searchWord = URLEncoder.encode(searchWord, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            searchWord ="girl";
        }
        //GET网络请求获取数据
        NetRequest.getRequest(Constants.searchPictureUrl + searchWord, NetImageResult.class, new NetRequest.BeanCallback<NetImageResult>() {
            @Override
            public void onSuccess(NetImageResult response) {
                progressWheel.setVisibility(View.GONE);
                recyclerAdapter = new ImageRecyclerAdapter(response.getItems(), layoutSysle);
                recyclerView.setAdapter(recyclerAdapter);

            }

            @Override
            public void onError(Exception exception, String errorInfo) {
                progressWheel.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                recyclerAdapter = new ImageRecyclerAdapter(null, layoutSysle);
            }
        });
    }
}
