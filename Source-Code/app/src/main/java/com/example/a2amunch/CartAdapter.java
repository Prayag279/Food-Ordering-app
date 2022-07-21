package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>
{
    private int cartitemlayout;
    private ArrayList cart_id;
    DatabaseReference rootref;
    ProgressDialog loadingbar;
    public CartAdapter(int cart_recyclerview, ArrayList cart_id) {
        cartitemlayout = cart_recyclerview;
        this.cart_id = cart_id;

    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(cartitemlayout,parent,false);
        ViewHolder myviewHolder = new ViewHolder(view);

        return myviewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        // aya button click no and all code avse okkk
        String curr_position = cart_id.get(position) +"";
        TextView item_name = holder.item_name;
        TextView item_price = holder.item_price;
        ImageView quantity_max = holder.quantity_max;
        ImageView quantity_min = holder.quantity_min;
        ImageView image = holder.image;

        Button item_remove = holder.item_remove;
        EditText item_quantity = holder.item_quantity;
        item_quantity.setEnabled(false);
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
               /* Picasso.with(holder.image.getContext())
                        .load("https://media.geeksforgeeks.org/wp-content/cdn-uploads/logo-new-2.svg")
                        .resize(400,500)
                        .into(holder.image);*/


               image.setImageResource(Integer.parseInt(snapshot.child("cart").child(CurrentRequest.current_number).child(curr_position).child("imgid").getValue().toString()));

                item_name.setText(snapshot.child("cart").child(CurrentRequest.current_number).child(curr_position).child("name").getValue().toString());
                item_price.setText("₹ "+snapshot.child("cart").child(CurrentRequest.current_number).child(curr_position).child("price").getValue());
                item_quantity.setText(snapshot.child("cart").child(CurrentRequest.current_number).child(curr_position).child("quantity").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        item_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingbar = new ProgressDialog(view.getContext());
                loadingbar.setTitle("Deleting "+item_name.getText());
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.setMessage("Wait for while");
                loadingbar.show();

                rootref.child("cart").child(CurrentRequest.current_number).child(curr_position).child("imgid").removeValue();
                rootref.child("cart").child(CurrentRequest.current_number).child(curr_position).child("name").removeValue();
                rootref.child("cart").child(CurrentRequest.current_number).child(curr_position).child("price").removeValue();
                rootref.child("cart").child(CurrentRequest.current_number).child(curr_position).child("quantity").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            CurrentRequest.request_goto_cart = true;
                            Intent i = new Intent(view.getContext(),HomeActivity.class);
                            view.getContext().startActivity(i);
                            loadingbar.dismiss();

                        }
                    }
                });
            }
        });

        quantity_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item_quantity.getText().toString().equals("1"))
                {
                    Toast.makeText(view.getContext(), "1 is minimum limit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    rootref.child("cart").child(CurrentRequest.current_number).child(curr_position).child("quantity").setValue(Integer.parseInt(item_quantity.getText().toString()) - 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            CurrentRequest.request_goto_cart = true;
                            Intent i = new Intent(view.getContext(),HomeActivity.class);
                            view.getContext().startActivity(i);
                        }
                    });
                }
            }
        });

        quantity_max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item_quantity.getText().toString().equals("10"))
                {
                    Toast.makeText(view.getContext(), "10 is maximum limit", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    rootref.child("cart").child(CurrentRequest.current_number).child(curr_position).child("quantity").setValue(Integer.parseInt(item_quantity.getText().toString()) + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            CurrentRequest.request_goto_cart = true;
                            Intent i = new Intent(view.getContext(),HomeActivity.class);
                            view.getContext().startActivity(i);

                        }
                    });
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return cart_id == null?0 : cart_id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_name,item_price;
        Button item_remove;
        EditText item_quantity;
        ImageView image,quantity_max,quantity_min;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            quantity_max = itemView.findViewById(R.id.quantity_max);
            quantity_min = itemView.findViewById(R.id.quantity_min);

            item_remove = itemView.findViewById(R.id.item_remove);
            item_quantity = itemView.findViewById(R.id.item_quantity);

            image = itemView.findViewById(R.id.image);



        }
    }
}
