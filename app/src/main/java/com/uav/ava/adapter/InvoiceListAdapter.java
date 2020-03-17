package com.uav.ava.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uav.ava.BO.InvoiceBO;
import com.uav.ava.R;
import com.uav.ava.activity.InvoiceList;
import com.uav.ava.activity.PrintInvoice;
import com.uav.ava.activity.Success;
import com.uav.ava.permission.Session;
import com.uav.ava.util.Utility;
import com.uav.ava.vo.DataAdapterVO;
import com.uav.ava.volley.volley.VolleyResponseListener;
import com.uav.ava.volley.volley.VolleyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class InvoiceListAdapter  extends RecyclerView.Adapter<InvoiceListAdapter.InvoiceViewHolder> {

    Context mctx;
    List<DataAdapterVO> invoiceList;
    ViewGroup vg;

    boolean imageset=true;

    public InvoiceListAdapter(Context mctx, List<DataAdapterVO> invoiceList,boolean imageset) {
        this.mctx = mctx;
        this.invoiceList = invoiceList;
        this.imageset=imageset;
    }

    @Override
    public InvoiceListAdapter.InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        vg = parent;
        LayoutInflater layoutInflater= LayoutInflater.from(mctx);
        View view=layoutInflater.inflate(R.layout.layout_invoicelist,parent,false);
       /* ProdectViewHolder holder=new ProdectViewHolder(view);
        return holder;*/
        return new InvoiceListAdapter.InvoiceViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final InvoiceListAdapter.InvoiceViewHolder holder,  int position) {

        final DataAdapterVO dataAdapterVO =invoiceList.get(position);

        holder.posSaleId.setText(Html.fromHtml("<pre>       <font color=#cc0029>          "     +   dataAdapterVO.getPosSaleId()+"</font></pre>"));
        holder.invoiceDate.setText(dataAdapterVO.getInvoiceDate());
        holder.passengerName.setText(dataAdapterVO.getPassengerName());
        holder.emailId.setText(dataAdapterVO.getEmail());


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
        Button print,sendEmail;
        EditText emailId;
        public InvoiceViewHolder(View itemView) {
            super(itemView);

            posSaleId = itemView.findViewById(R.id.posSaleId);
            invoiceDate = itemView.findViewById(R.id.invoiceDate);
            passengerName = itemView.findViewById(R.id.passengerName);
            re = itemView.findViewById(R.id.invoiceListLayout);
            emailId=itemView.findViewById(R.id.emailId);
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mctx, PrintInvoice.class);
                    intent.putExtra("posSaleId", posSaleId.getText().toString());
                    mctx.startActivity(intent);
                }
            });
            */
            emailId = itemView.findViewById(R.id.emailId);
            print = itemView.findViewById(R.id.print);
            sendEmail = itemView.findViewById(R.id.sendEmail);
            print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mctx, PrintInvoice.class);
                    intent.putExtra("posSaleId", posSaleId.getText().toString());
                    mctx.startActivity(intent);

                }
            });

            sendEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emailId.getText().equals("")) {
                        Utility.alertDialog(mctx, "Validation!", "Email Id not valid", "Ok");
                        return;

                    }
                    sendEmail();

                }
            });
        }
            private void sendEmail(){
                VolleyUtils.makeJsonObjectRequest(mctx, InvoiceBO.sendEmail(Session.getUserId(mctx),emailId.getText().toString(),posSaleId.getText().toString()), new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {

                    }
                    @Override
                    public void onResponse(Object resp) throws JSONException {
                        JSONObject response = (JSONObject) resp;
                        if(response.get("status").equals("fail")){
                            VolleyUtils.furnishErrorMsg(  "Error !" ,response, mctx);
                            return;
                        }
                        Utility.alertDialog(mctx, "Success!", response.getString("msg"), "Ok");
                        return;
                    }
                });

            }

    }

}
