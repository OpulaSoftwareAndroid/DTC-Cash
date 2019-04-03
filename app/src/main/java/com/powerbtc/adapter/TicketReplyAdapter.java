package com.powerbtc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.powerbtc.R;
import com.powerbtc.model.GetReplyTicketResponse;

import java.util.ArrayList;


@SuppressWarnings("ALL")
public class TicketReplyAdapter extends RecyclerView.Adapter<TicketReplyAdapter.MyViewHolder> {

    private ArrayList<GetReplyTicketResponse.Info> listTicket;
    private Context mContext;

    public TicketReplyAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TicketReplyAdapter(Context mContext, ArrayList<GetReplyTicketResponse.Info> listTicket) {
        this.mContext = mContext;
        this.listTicket = listTicket;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ticket_reply, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvSubject.setText(Html.fromHtml(listTicket.get(position).getPerson()));

        holder.tvDate.setText(Html.fromHtml(listTicket.get(position).getDate()));

        holder.tvMSG.setText(Html.fromHtml(listTicket.get(position).getMessage()));


        holder.tvAttachment.setPaintFlags(holder.tvAttachment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        String fileName = listTicket.get(position).getTicket().substring(listTicket.get(position).getTicket().lastIndexOf("/"));
        if (fileName.contains(".")) {
            holder.tvAttachment.setVisibility(View.VISIBLE);
        } else {
            holder.tvAttachment.setVisibility(View.GONE);
        }

        holder.tvAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(listTicket.get(position).getTicket()));
                mContext.startActivity(browserIntent);
            }
        });

        /*if (fileName.contains(".")) {
            holder.imDP.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(listTicket.get(position).getTicket())
                    .into(holder.imDP);
        } else {
            holder.imDP.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return listTicket.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubject, tvMSG, tvDate, tvAttachment;

        //        ImageView imDP;
        public MyViewHolder(View view) {
            super(view);
            tvSubject = (TextView) view.findViewById(R.id.tv_ItemTicketReply_Subject);
            tvMSG = (TextView) view.findViewById(R.id.tv_ItemTicketReply_Msg);
            tvDate = (TextView) view.findViewById(R.id.tv_ItemTicketReply_Date);
            tvAttachment = (TextView) view.findViewById(R.id.tv_ItemTicketReply_Attachment);
//            imDP = (ImageView) view.findViewById(R.id.image_Replay);
        }
    }
}