package com.developerdesk9.ecommerce;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

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

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        final Address address=addressList.get(position);
        holder.tvname.setText(address.getName());
        holder.tvaddress.setText(address.getAddress());



        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("users").child(currentUser.getUid()).child("Address").child(address.getAddress_key()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext.getApplicationContext(),"Address deleted",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(mContext.getApplicationContext(),"Failed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        holder.tv_make_default_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> data=new HashMap<>();
                data.put("name",address.getName());
                data.put("address",address.getAddress());
                mDatabase.child("users").child(currentUser.getUid()).child("DefaultAddress").updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(mContext.getApplicationContext(),"Default Address Changed",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(mContext.getApplicationContext(),"Failed!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvname;
        private TextView tvaddress;
        private ImageView del_btn;
        private TextView tv_make_default_address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvname=itemView.findViewById(R.id.all_address_name_tv);
            tvaddress=itemView.findViewById(R.id.all_addrress_address_tv);
            del_btn=itemView.findViewById(R.id.IV_delete_IV_myaddress);
            tv_make_default_address=itemView.findViewById(R.id.tv_make_it_default_address);

        }
    }
}

