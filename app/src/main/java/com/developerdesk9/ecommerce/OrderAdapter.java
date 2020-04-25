package com.developerdesk9.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<Order> orderList;
    private Context mContext;

    public OrderAdapter(List<Order> orderList, Context mContext) {
        this.orderList = orderList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Order order = orderList.get(i);
        Picasso.get().load(order.getProduct_image()).fit().into(viewHolder.iv_product_image);
        viewHolder.product_name.setText(order.getProduct_name());
        viewHolder.product_price.setText("₹" + order.getProduct_price());
        viewHolder.seller_name.setText("by " + order.getCompany_name());
        viewHolder.order_id.setText(order.getOrder_id());
        viewHolder.order_date.setText(order.getOrder_date());
        viewHolder.order_status.setText(order.getOrder_status());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_product_image;
        private TextView product_name, product_price, seller_name,order_id,order_date,order_status;

        private CardView productcv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_product_image = itemView.findViewById(R.id.order_product_image);
            productcv = itemView.findViewById(R.id.productcv);

            product_name = itemView.findViewById(R.id.order_product_name);
            product_price = itemView.findViewById(R.id.order_product_price);
            seller_name = itemView.findViewById(R.id.order_retailer_name);
            order_id = itemView.findViewById(R.id.order_orderid);
            order_date= itemView.findViewById(R.id.order_date);
            order_status= itemView.findViewById(R.id.order_trasaction_status);


        }
    }
}
