package com.powerbtc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.powerbtc.R;
import com.powerbtc.model.TransactionResponse;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder> {

    ArrayList<TransactionResponse.Info> listData;
    Context mContext;
    String TAG="TransactionHistoryAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvType, tvFee, tvAmount, tvDes, tvDate, tvStatus, tvTxid;

        public MyViewHolder(View view) {
            super(view);

            tvType = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Type);
            tvFee = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Fee);
            tvAmount = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Amount);
            tvDes = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Description);
            tvDate = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Date);
            tvStatus = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Status);
            tvTxid = (TextView) view.findViewById(R.id.tv_ItemLendingHistory_Txid);
        }
    }

    public TransactionHistoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TransactionHistoryAdapter(Context mContext, ArrayList<TransactionResponse.Info> listData) {
        this.mContext = mContext;
        this.listData = listData;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transaction_history, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String des = null;

        Log.d(TAG,"jigar the transaction history type we have is "+listData.get(position).getTransactiontype());

        if(listData.get(position).getTransactiontype().equalsIgnoreCase("ROIWithdraw")) {

            des = "Receive From: Stack Distribution";

        }
        else
            {

                if (listData.get(position).getTransactionType().equalsIgnoreCase("Receive")) {
                    holder.tvFee.setText("Tx Fee : " + listData.get(position).getTransactionFee());
                    holder.tvFee.setVisibility(View.GONE);
                    des = "Receive To:" + listData.get(position).getToAddress();
                } else if (listData.get(position).getTransactionType().equalsIgnoreCase("Send")) {
                    holder.tvFee.setText("Tx Fee : " + listData.get(position).getTransactionFee());
                    des = "Sent To:" + listData.get(position).getToAddress();
                }
            }
        holder.tvAmount.setText("" + listData.get(position).getOtherAmount());

        if (listData.get(position).getTransactionType() != null) {
            holder.tvType.setText("" + listData.get(position).getTransactionType());
            if (listData.get(position).getTransactionType().equalsIgnoreCase("Send")) {
                holder.tvAmount.setTextColor(Color.parseColor("#fe3e3e"));
//                holder.tvFee.setVisibility(View.VISIBLE);
            } else {
                holder.tvAmount.setTextColor(Color.parseColor("#4d804d"));
//                holder.tvFee.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.tvType.setText("");
        }

        holder.tvDes.setText("" + des);

        SpannableString content = new SpannableString("Txid:" + listData.get(position).getTransactionHash());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        holder.tvTxid.setText(content);

        holder.tvTxid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String hash = "http://70.36.107.182/tx/" + listData.get(position).getTransactionHash();
                openWebPage(hash);
            }
        });

        holder.tvDate.setText("" + listData.get(position).getTransactionDate());

        int i = Integer.parseInt(listData.get(position).getStatus());

        if (i==0){
            holder.tvStatus.setText("Status : Pending");
        } else {
            holder.tvStatus.setText("Status : Success");
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void openWebPage(String hash) {
        Uri webpage = Uri.parse(hash);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivity(intent);
        }
    }
}