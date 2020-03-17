package com.uav.ava.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.ava.activity.FlightNoProtocol;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.vo.DataAdapterVO;

import java.util.List;

import com.uav.ava.R;

/**
 * Created by anku on 10/26/2017.
 */
/*
recyclerview.adapter
recyclerview.viewholder
*/

public class RecyclerViewAdapterMenu extends RecyclerView.Adapter<RecyclerViewAdapterMenu.ProdectViewHolder>{
    Context mctx;
    List<DataAdapterVO> productslist;


    public RecyclerViewAdapterMenu(Context mctx, List<DataAdapterVO> productslist) {
        this.mctx = mctx;
        this.productslist = productslist;
    }

    @Override
    public ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(mctx);
        View view=layoutInflater.inflate(R.layout.two_tailes,null);
       /* ProdectViewHolder holder=new ProdectViewHolder(view);
        return holder;*/
       return new ProdectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProdectViewHolder holder, int position) {

         final DataAdapterVO pro=productslist.get(position);
         holder.textViewtitle.setText(pro.getText());

         holder.imageView.setImageDrawable(mctx.getResources().getDrawable(pro.getImage()));
         holder.mailmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(pro.getActivityname()!=null){
                   mctx.startActivity(new Intent(mctx,pro.getActivityname()));
               }else {
                   Toast.makeText(mctx, "Something is wrong ", Toast.LENGTH_SHORT).show();
               }


            }
        });



    }

    @Override
    public int getItemCount() {
        return productslist.size();
    }

    public class ProdectViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mailmenu;
        ImageView imageView;
        TextView textViewtitle;
        public ProdectViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            textViewtitle=itemView.findViewById(R.id.textViewTitle);
            mailmenu=itemView.findViewById(R.id.mailmenu);

        }
    }
}
