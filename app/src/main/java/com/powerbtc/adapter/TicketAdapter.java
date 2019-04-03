package com.powerbtc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.powerbtc.R;
import com.powerbtc.model.TicketHistoryResponse;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private ArrayList<TicketHistoryResponse.Info> listTicket;
    private Context mContext;
    private OnClickTicket listener;

    public interface OnClickTicket
    {
        void setOnClickTicket(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvDepartment, tvTicketStatus, tvStatus, tvDate;
        public MyViewHolder(View view) {
            super(view);
            tvSubject = (TextView) view.findViewById(R.id.tv_ItemTicket_Subject);
            tvDepartment = (TextView) view.findViewById(R.id.tv_ItemTicket_Department);
            tvTicketStatus = (TextView) view.findViewById(R.id.tv_ItemTicket_TicketStatus);
            tvStatus = (TextView) view.findViewById(R.id.tv_ItemTicket_Status);
            tvDate = (TextView) view.findViewById(R.id.tv_ItemTicket_Date);
        }
    }

    public TicketAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TicketAdapter(Context mContext, ArrayList<TicketHistoryResponse.Info> listTicket, OnClickTicket listener) {
        this.mContext = mContext;
        this.listTicket = listTicket;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ticket, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String mystring=new String("#" + listTicket.get(position).getTicketNumber() + " - " + listTicket.get(position).getSubject());
        SpannableString content = new SpannableString(mystring);
        content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);

        holder.tvSubject.setText(content);
        holder.tvDepartment.setText(listTicket.get(position).getDepartment());

        holder.tvTicketStatus.setText("" + listTicket.get(position).getTicketStatus());

        if (listTicket.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tvStatus.setText("Pending");
        } else {
            holder.tvStatus.setText("Answered");
        }

        holder.tvDate.setText(listTicket.get(position).getLastUpdate());

        holder.tvSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                {
                    listener.setOnClickTicket(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTicket.size();
//        return 10;
    }
}