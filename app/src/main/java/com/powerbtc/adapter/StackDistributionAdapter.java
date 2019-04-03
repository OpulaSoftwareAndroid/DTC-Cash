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

import java.util.List;


@SuppressWarnings("ALL")
public class StackDistributionAdapter extends RecyclerView.Adapter<StackDistributionAdapter.MyViewHolder> {

    private List<com.powerbtc.model.StackDistribution.Info> arrayListStackDepositTransaction;
    private Context mContext;

    public StackDistributionAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public StackDistributionAdapter(Context mContext, List<com.powerbtc.model.StackDistribution.Info> listTicket) {
        this.mContext = mContext;
        this.arrayListStackDepositTransaction = listTicket;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_stack_distribution, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.textViewDate.setText(Html.fromHtml(arrayListStackDepositTransaction.get(position).getDate()));
        holder.textViewTotalDistribution.setText(Html.fromHtml(arrayListStackDepositTransaction.get(position).getTotalDistribution()+" DTCH"));
        holder.textView35Amount.setText(arrayListStackDepositTransaction.get(position).get35Amount()+" DTCH");
        holder.textView65Amount.setText(arrayListStackDepositTransaction.get(position).get65Amount()+" DTCH");

    }

    @Override
    public int getItemCount() {
        return arrayListStackDepositTransaction.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTotalDistribution, textViewDate,textView35Amount,textView65Amount;

        //        ImageView imDP;
        public MyViewHolder(View view) {
            super(view);
            textViewTotalDistribution = (TextView) view.findViewById(R.id.textViewTotalDistribution);
            textViewDate= (TextView) view.findViewById(R.id.textViewDate);
            textView35Amount= (TextView) view.findViewById(R.id.textView35Amount);
            textView65Amount= (TextView) view.findViewById(R.id.textView65Amount);

//            imDP = (ImageView) view.findViewById(R.id.image_Replay);
        }
    }
}