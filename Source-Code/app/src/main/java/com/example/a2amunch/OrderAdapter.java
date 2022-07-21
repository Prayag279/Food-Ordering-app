package com.example.a2amunch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private int orderitemlayout;
    private ArrayList order_item_id;
    DatabaseReference rootref;


    public OrderAdapter(int order_recyclerview, ArrayList order_item_id) {
        orderitemlayout = order_recyclerview;
        this.order_item_id = order_item_id;

    }
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(orderitemlayout,parent,false);
        OrderAdapter.ViewHolder myviewHolder = new OrderAdapter.ViewHolder(view);

        return myviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        // aya button click no and all code avse okkk
        String curr_position = order_item_id.get(position) +"";
        TextView order_item_name = holder.order_item_name;
        TextView order_item_price = holder.order_item_price;
        TextView order_item_quantity = holder.order_item_quantity;
        ImageView order_image = holder.order_image;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                order_image.setImageResource(Integer.parseInt(snapshot.child("order").child(CurrentRequest.current_number).child(curr_position).child("imgid").getValue().toString()));

                order_item_name.setText(snapshot.child("order").child(CurrentRequest.current_number).child(curr_position).child("name").getValue().toString());
                order_item_price.setText("₹ "+snapshot.child("order").child(CurrentRequest.current_number).child(curr_position).child("price").getValue());
                order_item_quantity.setText(snapshot.child("order").child(CurrentRequest.current_number).child(curr_position).child("quantity").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {

            return order_item_id == null?0 : order_item_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView order_item_name,order_item_price,order_item_quantity;
        ImageView order_image;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            order_item_name = itemView.findViewById(R.id.order_item_name);
            order_item_price = itemView.findViewById(R.id.order_item_price);
            order_item_quantity = itemView.findViewById(R.id.order_item_quantity);
            order_image = itemView.findViewById(R.id.order_image);

        }
    }
}
