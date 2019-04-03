package com.powerbtc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.powerbtc.R;
import com.powerbtc.model.StackDeposit.Info;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ALL")
public class StackDepositAdapter extends RecyclerView.Adapter<StackDepositAdapter.MyViewHolder> {

    private List<Info> arrayListStackDepositTransaction;
    private Context mContext;

    public StackDepositAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public StackDepositAdapter(Context mContext, List<Info> listTicket) {
        this.mContext = mContext;
        this.arrayListStackDepositTransaction = listTicket;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stack_deposit, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.textViewDate.setText(Html.fromHtml(arrayListStackDepositTransaction.get(position).getDate()));
        holder.textViewAmount.setText(Html.fromHtml(arrayListStackDepositTransaction.get(position).getAmount()));

    }

    @Override
    public int getItemCount() {
        return arrayListStackDepositTransaction.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAmount, textViewDate;

        //        ImageView imDP;
        public MyViewHolder(View view) {
            super(view);
            textViewAmount = (TextView) view.findViewById(R.id.textViewAmount);
            textViewDate= (TextView) view.findViewById(R.id.textViewDate);
//            imDP = (ImageView) view.findViewById(R.id.image_Replay);
        }
    }
}