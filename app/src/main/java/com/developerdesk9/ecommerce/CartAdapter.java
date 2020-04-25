package com.developerdesk9.ecommerce;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<Cart> cartList;
    private Context mContext;

    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    public CartAdapter(List<Cart> cartList, Context mContext) {
        this.cartList = cartList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        viewHolder.cartDelbtn.setVisibility(View.VISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        final Cart cart = cartList.get(i);
        viewHolder.product_name.setText(cart.getProduct_name());
        viewHolder.product_price.setText("₹" + cart.getProduct_price());
        viewHolder.seller_name.setText("by " + cart.getCompany_name());
        Picasso.get().load(cart.getProduct_image()).fit().into(viewHolder.imageView10, new Callback() {
            @Override
            public void onSuccess() {
                viewHolder.progressBar3.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });

        viewHolder.cartDelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("cart").child(currentUser.getUid()).child(cart.getCart_key()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Product deleted successfully", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView10;
        private TextView product_name, product_price, seller_name;
        private CardView productcv;
        private TextView cartDelbtn;
        private ProgressBar progressBar3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView10 = itemView.findViewById(R.id.imageView10);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            seller_name = itemView.findViewById(R.id.seller_name);
            productcv = itemView.findViewById(R.id.productcv);
            cartDelbtn = itemView.findViewById(R.id.cartDelbtn);
            progressBar3 = itemView.findViewById(R.id.progressBar3);

        }
    }
}
