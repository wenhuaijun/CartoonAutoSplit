package com.wenhuaijun.cartoonautosplit.main;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by wenhuaijun on 2016/4/24 0024.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageItemViewHolder>{
    private NetImage[] data;
    private boolean isScrolling =false;
    private int layoutManagerType =Constants.StagedGridLayoutStyle;
    public ImageRecyclerAdapter(NetImage[] data,int style) {
        this.data = data;
        this.layoutManagerType =style;
    }

    @Override
    public ImageItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ImageItemViewHolder holder, int position) {
        holder.setLayoutParams(data[position],layoutManagerType);
        holder.setData(data[position],layoutManagerType);
    }

    @Override
    public int getItemCount() {
        if(data==null){
            return 0;
        }
        return data.length;
    }

    public NetImage[] getData() {
        return data;
    }
    public void clear(){
        data =null;
        notifyDataSetChanged();
    }

    public int getLayoutManagerType() {
        return layoutManagerType;
    }

    public void setLayoutManagerType(int layoutManagerType) {
        this.layoutManagerType = layoutManagerType;
    }

    public void setData(NetImage[] data) {
        this.data = data;
    }

    public boolean isScrolling() {
        return isScrolling;
    }


    public void setIsScrolling(boolean isScrolling) {
        this.isScrolling = isScrolling;
    }
}
