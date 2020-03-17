package com.uav.ava.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uav.ava.R;
import com.uav.ava.vo.DataAdapterVO;

import java.util.List;

public class Offer_Recyclerview extends RecyclerView.Adapter<Offer_Recyclerview.ProdectViewHolder>{

    Context mctx;
    List<DataAdapterVO> productslist;


    public Offer_Recyclerview(Context mctx, List<DataAdapterVO> productslist) {
        this.mctx = mctx;
        this.productslist = productslist;
    }

    @Override
    public Offer_Recyclerview.ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(mctx);
        View view=layoutInflater.inflate(R.layout.offer_recyclerview,parent,false);
       /* ProdectViewHolder holder=new ProdectViewHolder(view);
        return holder;*/
        return new Offer_Recyclerview.ProdectViewHolder(view);
    }


    @Override
    public void onBindViewHolder(Offer_Recyclerview.ProdectViewHolder holder, int position) {

        final DataAdapterVO pro=productslist.get(position);
        holder.textViewTitle.setText(pro.getText());
        holder.imageView.setImageDrawable(mctx.getResources().getDrawable(pro.getImage()));
        /*holder.re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pro.getActivityname()!=null){
                    mctx.startActivity(new Intent(mctx,pro.getActivityname()));
                }else {
                    Toast.makeText(mctx, "Something is wrong ", Toast.LENGTH_SHORT).show();
                }


            }
        });*/



    }

    @Override
    public int getItemCount() {
        return productslist.size();
    }
    public class ProdectViewHolder extends RecyclerView.ViewHolder {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        LinearLayout re;
        ImageView imageView;
        TextView textViewTitle;
        public ProdectViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            textViewTitle=itemView.findViewById(R.id.textViewTitle);
            re=itemView.findViewById(R.id.re);

        }
    }
}
