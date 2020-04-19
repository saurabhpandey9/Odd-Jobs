package com.developerdesk9.ecommerce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    private List<Address> addressList;
    private Context mContext;

    public AddressAdapter (List<Address> addressList,Context mContext){
        this.addressList=addressList;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myaddress_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Address address=addressList.get(position);
        holder.tvname.setText(address.getName());
        holder.tvaddress.setText(address.getAddress());
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvname;
        private TextView tvaddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.all_address_name_tv);
            tvaddress=itemView.findViewById(R.id.all_addrress_address_tv);

        }
    }
}

