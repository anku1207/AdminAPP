package com.uav.ava.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.uav.ava.override.UAVTextView;
import com.uav.ava.vo.DataAdapterVO;

import java.util.ArrayList;

import com.uav.ava.R;

public class ImageTextAdapter extends BaseAdapter {
    private Context context;
    ArrayList<DataAdapterVO> dataList;
    private int design;
    private LayoutInflater layoutInflater;
    private int length;

    public ImageTextAdapter(Context context, ArrayList<DataAdapterVO> dataList, int design){
        this.context=context;
        this.dataList = dataList;
        this.design = design;
        layoutInflater=((Activity)context).getLayoutInflater();
        this.length = dataList.size();
    }
    @Override
    public int getCount() {
        return this.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).
                inflate(this.design, parent, false);
        ImageView imageView=(ImageView)convertView.findViewById(R.id.listimage);
        TextView textView=(TextView) convertView.findViewById(R.id.listtext);
        DataAdapterVO  dataAdapterVO = (DataAdapterVO)dataList.get(position);
        imageView.setImageResource(  dataAdapterVO.getImage());
        textView.setText( dataAdapterVO.getText());
        return convertView;
    }
}
