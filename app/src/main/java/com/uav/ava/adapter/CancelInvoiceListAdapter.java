package com.uav.ava.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.uav.ava.BO.InvoiceBO;
import com.uav.ava.R;
import com.uav.ava.activity.Cancel_Invoice;
import com.uav.ava.activity.PrintInvoice;
import com.uav.ava.permission.Session;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.ConnectionVO;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.vo.PosSaleVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class CancelInvoiceListAdapter extends RecyclerView.Adapter<CancelInvoiceListAdapter.InvoiceViewHolder> {
    Context mctx;
    List<DataAdapterVO> invoiceList;
    ViewGroup vg;

    boolean imageset=true;


    public CancelInvoiceListAdapter() {
    }

    public CancelInvoiceListAdapter(Context mctx, List<DataAdapterVO> invoiceList, boolean imageset) {
        this.mctx = mctx;
        this.invoiceList = invoiceList;
        this.imageset=imageset;
    }

    @Override
    public CancelInvoiceListAdapter.InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        vg = parent;
        LayoutInflater layoutInflater= LayoutInflater.from(mctx);
        View view=layoutInflater.inflate(R.layout.cancel_invoice_design,parent,false);
       /* ProdectViewHolder holder=new ProdectViewHolder(view);
        return holder;*/
        return new CancelInvoiceListAdapter.InvoiceViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final CancelInvoiceListAdapter.InvoiceViewHolder holder, final int position) {

        final DataAdapterVO dataAdapterVO =invoiceList.get(position);

        holder.posSaleId.setText(Html.fromHtml("<pre>       <font color=#cc0029>          "     +   dataAdapterVO.getPosSaleId()+"</font></pre>"));
        holder.invoiceDate.setText(dataAdapterVO.getInvoiceDate());
        holder.passengerName.setText(dataAdapterVO.getPassengerName());


        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDoubleButtonDialog(mctx,dataAdapterVO,position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }



    public class InvoiceViewHolder extends RecyclerView.ViewHolder {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
        LinearLayout re;
        TextView posSaleId,invoiceDate,passengerName;
        Button cancel;
        public InvoiceViewHolder(View itemView) {
            super(itemView);

            posSaleId = itemView.findViewById(R.id.posSaleId);
            invoiceDate = itemView.findViewById(R.id.invoiceDate);
            passengerName = itemView.findViewById(R.id.passengerName);
            re = itemView.findViewById(R.id.invoiceListLayout);
            cancel=itemView.findViewById(R.id.cancel);
        }


    }

    public  void showDoubleButtonDialog(final Context var1,final DataAdapterVO dataAdapterVO,final int position) {


        final Dialog var3 = new Dialog(var1);
        var3.requestWindowFeature(1);
        var3.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        var3.setContentView(R.layout.dialog_two_button_with_edittext);
        var3.setCanceledOnTouchOutside(false);
        TextView msg = (TextView)var3.findViewById(R.id.msg);

        Button cancel = (Button)var3.findViewById(R.id.btn_cancel);
        Button ok = (Button)var3.findViewById(R.id.btn_ok);
        final EditText editText=var3.findViewById(R.id.edittext);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var) {
                var3.dismiss();

                HashMap<String, Object> params = new HashMap<String, Object>();

                ConnectionVO connectionVO =InvoiceBO.cancelRetailInvoice();

                PosSaleVO posSaleVO =new PosSaleVO();
                posSaleVO.setPosSaleID(dataAdapterVO.getPosSaleId());
                posSaleVO.setInvoiceCancelRemark(editText.getText().toString());

                Gson gson = new Gson();
                String json = gson.toJson(posSaleVO);
                params.put("userId",Session.getUserId(mctx));
                params.put("POSSaleVO", json);

                Log.w("request",params.toString());
                connectionVO.setParams(params);

                VolleyUtils.makeJsonObjectRequest(mctx,connectionVO , new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {

                    }
                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;

                        if(response.get("status").equals("fail")){
                            Utility.alertDialog(mctx,"",response.getString("error"),"ok");
                            return;
                        }

                        invoiceList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, invoiceList.size());



                    }
                });


            }
        });
        var3.show();
    }

}
