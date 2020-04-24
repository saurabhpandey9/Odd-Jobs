package com.developerdesk9.ecommerce;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList){
        this.transactionList=transactionList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_itemlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Transaction transaction=transactionList.get(position);
        holder.tv_orderid.setText("TI"+transaction.getOrder_id());
        holder.tv_transaction_status.setText(transaction.getOrder_status());
        holder.tv_date.setText(transaction.getTr_date());
        holder.tv_amount.setText(transaction.getTr_amount());
    }

    @Override
    public int getItemCount() {

        Log.d("count",""+transactionList.size());

        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_orderid;
        private TextView tv_date;
        private TextView tv_transaction_status;
        private TextView tv_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_orderid=itemView.findViewById(R.id.transaction_orderid);
            tv_transaction_status=itemView.findViewById(R.id.transaction_status);
            tv_date=itemView.findViewById(R.id.transaction_date);
            tv_amount=itemView.findViewById(R.id.transaction_amount);

        }
    }
}
