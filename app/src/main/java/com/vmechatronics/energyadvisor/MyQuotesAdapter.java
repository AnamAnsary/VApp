package com.vmechatronics.energyadvisor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by vmplapp on 10/8/17.
 */

public class MyQuotesAdapter extends RecyclerView.Adapter<MyQuotesAdapter.ViewHolder> {


    private List<QuoteListItem> listItems;
    private Context context;
    TextView tvQID;
    TextView tvQDT;
    TextView tvBSV;
    RelativeLayout RLQuote;
    String qid = "";
    String qdate = "";

    public MyQuotesAdapter(List<QuoteListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.myquotes_listitem,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final QuoteListItem listItem = listItems.get(position);

        holder.textViewQDate.setText(listItem.getTvdate());
        holder.textViewQId.setText(listItem.getTvqid());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewQId;
        public TextView textViewQDate;
        private CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewQId = (TextView) itemView.findViewById(R.id.tvQID);
            textViewQDate = (TextView) itemView.findViewById(R.id.tvQDT);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.w(TAG, "onClick: quoteId " +listItem.getTvqid()+ "clicked");
                    RLQuote = (RelativeLayout) view.findViewById(R.id.RLQuote);
                    tvQID = (TextView) view.findViewById(R.id.tvQID);
                    tvQDT = (TextView) view.findViewById(R.id.tvQDT);
                    tvBSV = (TextView) view.findViewById(R.id.tvBSV);
                    qid = tvQID.getText().toString();
                    qdate = tvQDT.getText().toString();
                    Log.w(TAG, "onClick: in adapter qid and qdate is " +qid+ " " +qdate);
                    tvBSV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent ii = new Intent(view.getContext(), Book.class);
                            ii.putExtra("qid", qid);
                            ii.putExtra("qdate", qdate);
                            view.getContext().startActivity(ii);
                        }
                    });

                    RLQuote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Bundle b = new Bundle();
                            b.putString("qid",qid);
                            Intent in = new Intent(view.getContext(), DownloadQuotes.class);
                            in.putExtras(b);
                            view.getContext().startActivity(in);
                        }
                    });
                }
            });*/


        }
    }
}
