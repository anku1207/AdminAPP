package com.uav.ava.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.uav.ava.R;
import com.uav.ava.adapter.ImageTextAdapter;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.vo.DataAdapterVO;

import java.util.ArrayList;

public class SettingApp extends AppCompatActivity {

    ImageView back_activity_button;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_app);

        getSupportActionBar().hide();

        back_activity_button=(ImageView)findViewById(R.id.back_activity_button);
        listView=(ListView)findViewById(R.id.listview);

        back_activity_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final ArrayList<DataAdapterVO> dataList =  getDataList();
        ImageTextAdapter myAdapter=new ImageTextAdapter(this, dataList, R.layout.listimagewithtextview);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // String s=(String)parent.getItemAtPosition(position);
                int i=position;
                DataAdapterVO dataAdapterVO = (DataAdapterVO)dataList.get(position);
                startActivity(new Intent(getApplicationContext(),dataAdapterVO.getActivityname()));
            }
        });
    }
    public  ArrayList<DataAdapterVO> getDataList(){
        ArrayList<DataAdapterVO> datalist = new ArrayList<>();
        DataAdapterVO dataAdapterVO = new DataAdapterVO();
        dataAdapterVO.setText("App Offline Mode Setting");
        dataAdapterVO.setActivityname(OffLineSetting.class);
        dataAdapterVO.setImage(R.drawable.offline);
        datalist.add(dataAdapterVO);


        return  datalist;
    }
}
