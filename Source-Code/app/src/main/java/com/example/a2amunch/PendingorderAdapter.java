package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingorderAdapter extends RecyclerView.Adapter<PendingorderAdapter.ViewHolder>{
    private int p_order_layout;
    private ArrayList p_order_id;
    DatabaseReference rootref;
    ProgressDialog loadingbar;
    public PendingorderAdapter(int p_order_recyclerview, ArrayList p_order_id) {
        p_order_layout = p_order_recyclerview;
        this.p_order_id = p_order_id;

    }
    @NonNull
    @Override
    public PendingorderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(p_order_layout,parent,false);
        PendingorderAdapter.ViewHolder myviewHolder = new PendingorderAdapter.ViewHolder(view);

        return myviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PendingorderAdapter.ViewHolder holder, int position) {
        String curr_position = p_order_id.get(position) +"";
        TextView user_details = holder.user_details;
        TextView ordered_item = holder.ordered_item;
        TextView order_status = holder.order_status;
        TextView user_ad = holder.user_ad;
   
        TextView last_price = holder.last_price;
        Button order_complete= holder.order_complete;
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_details.setText(snapshot.child("pendingorders").child(curr_position).child("details").getValue()+"");
                ordered_item.setText(snapshot.child("pendingorders").child(curr_position).child("item").getValue()+"");
                order_status.setText(snapshot.child("pendingorders").child(curr_position).child("status").getValue()+"");
                last_price.setText("₹ "+snapshot.child("pendingorders").child(curr_position).child("total_price").getValue()+"");
                user_ad.setText(snapshot.child("pendingorders").child(curr_position).child("address").getValue()+"");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        order_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingbar = new ProgressDialog(view.getContext());
                loadingbar.setTitle("Processing ");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.setMessage("Wait for a while");
                loadingbar.show();
                rootref.child("pendingorders").child(curr_position).child("status").setValue("Completed").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) 
                    {

                        CurrentRequest.is_manager=true;
                        Intent i = new Intent(view.getContext(),HomeActivity.class);
                        view.getContext().startActivity(i);
                        loadingbar.dismiss();
                    }
                });



            }
        });


    }

    @Override
    public int getItemCount() {
        return p_order_id == null?0 : p_order_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView user_details,ordered_item,last_price,order_status,user_ad;
        Button order_complete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            user_details = itemView.findViewById(R.id.user_details);
            ordered_item = itemView.findViewById(R.id.ordered_item);
            order_status = itemView.findViewById(R.id.order_status);
            last_price = itemView.findViewById(R.id.last_price);
            user_ad = itemView.findViewById(R.id.user_ad);
            order_complete = itemView.findViewById(R.id.order_complete);
        }
    }
}
