package com.example.a2amunch;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CompleteorderAdapter extends RecyclerView.Adapter<CompleteorderAdapter.ViewHolder>{
    private int c_order_layout;
    private ArrayList c_order_id;
    DatabaseReference rootref;
    ProgressDialog loadingbar;
    public CompleteorderAdapter(int c_order_recyclerview, ArrayList c_order_id) {
        c_order_layout = c_order_recyclerview;
        this.c_order_id = c_order_id;

    }
    @NonNull
    @Override
    public CompleteorderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(c_order_layout,parent,false);
        CompleteorderAdapter.ViewHolder myviewHolder = new CompleteorderAdapter.ViewHolder(view);

        return myviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteorderAdapter.ViewHolder holder, int position)
    {
        String curr_position = c_order_id.get(position) +"";
        TextView c_user_details = holder.c_user_details;
        TextView c_ordered_item = holder.c_ordered_item;
        TextView c_order_status = holder.c_order_status;
        TextView c_last_price = holder.c_last_price;
        TextView c_user_ad = holder.c_user_ad;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                c_user_details.setText(snapshot.child("pendingorders").child(curr_position).child("details").getValue()+"");
                c_ordered_item.setText(snapshot.child("pendingorders").child(curr_position).child("item").getValue()+"");
                c_order_status.setText(snapshot.child("pendingorders").child(curr_position).child("status").getValue()+"");
                c_last_price.setText("₹ "+snapshot.child("pendingorders").child(curr_position).child("total_price").getValue()+"");
                c_user_ad.setText(snapshot.child("pendingorders").child(curr_position).child("address").getValue()+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return c_order_id == null?0 : c_order_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView c_user_details,c_ordered_item,c_last_price,c_order_status,c_user_ad;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            c_user_details = itemView.findViewById(R.id.c_user_details);
            c_ordered_item = itemView.findViewById(R.id.c_ordered_item);
            c_last_price = itemView.findViewById(R.id.c_last_price);
            c_order_status = itemView.findViewById(R.id.c_order_status);
            c_user_ad = itemView.findViewById(R.id.c_user_ad);

        }
    }
}
