package com.uav.ava.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uav.ava.R;
import com.uav.ava.activity.MainActivity;
import com.uav.ava.activity.Product_Scan;
import com.uav.ava.constant.ApplicationConstant;
import com.uav.ava.vo.DataAdapterVO;

import java.util.List;

public class Product_Scan_Adapter extends RecyclerView.Adapter<Product_Scan_Adapter.ProdectViewHolder>{

    Context mctx;
    List<DataAdapterVO> productslist;
    ViewGroup vg;

    boolean imageset=true;


    public Product_Scan_Adapter(Context mctx, List<DataAdapterVO> productslist,boolean imageset) {
        this.mctx = mctx;
        this.productslist = productslist;
        this.imageset=imageset;
    }

    @Override
    public Product_Scan_Adapter.ProdectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        vg = parent;
        LayoutInflater layoutInflater= LayoutInflater.from(mctx);
        View view=layoutInflater.inflate(R.layout.productscanlayount,parent,false);
       /* ProdectViewHolder holder=new ProdectViewHolder(view);
        return holder;*/
        return new Product_Scan_Adapter.ProdectViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final Product_Scan_Adapter.ProdectViewHolder holder,  int position) {

        final DataAdapterVO pro=productslist.get(position);

        String s="SKU :- "+ pro.getProductcode() + "<pre>       <font color=#cc0029>          "     +    pro.getProducttype()+"</font></pre>";


        holder.productcode.setText(Html.fromHtml(s));

        holder.deletebtn.setTag(position);

        holder.name.setText(pro.getProductname());
        holder.price.setText(""+(int)Double.parseDouble(pro.getMop()));
        holder.qty.setText(pro.getQuantity());
        holder.total.setText(""+(int)Double.parseDouble(pro.getTotal()));

        if(imageset) {
            if (pro.getProducttype().equals("Primary")) {
                holder.deletebtn.setColorFilter(ContextCompat.getColor(mctx, R.color.colorPrimary));
                holder.deletebtn.setImageDrawable(mctx.getResources().getDrawable(pro.getImage()));
                holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int x = (Integer) v.getTag();
                        if(productslist.get(x).getAssociativeCombo()!=null){
                            productslist.remove(x+1);
                            notifyItemRemoved(x+1);
                            notifyItemRangeChanged(x, productslist.size());
                        }
                        productslist.remove(x);
                        notifyItemRemoved(x);
                        notifyItemRangeChanged(x, productslist.size());


                    }
                });
            }
        }

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
        ImageView deletebtn;
        TextView productcode,name,price,qty,total;
        public ProdectViewHolder(View itemView) {
            super(itemView);
            deletebtn=itemView.findViewById(R.id.deletebtn);
            productcode=itemView.findViewById(R.id.productcode);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            qty=itemView.findViewById(R.id.qty);
            total=itemView.findViewById(R.id.total);
            re=itemView.findViewById(R.id.productlayout);
        }
    }




}
