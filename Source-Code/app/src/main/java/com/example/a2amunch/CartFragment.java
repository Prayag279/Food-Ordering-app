package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    Button order;
    ProgressDialog loadingbar;

    TextView total_price;
    DatabaseReference rootref;
    public ArrayList cart_id = new ArrayList();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        total_price = v.findViewById(R.id.cart_total_price);
        order = v.findViewById(R.id.order);
        recyclerView = v.findViewById(R.id.recyclerview);
        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Fetching Details");
        loadingbar.setMessage("wait a minute");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        order.setEnabled(false);


        //aya arraylist ma id nakhva no code lkhvo
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {   float price=0;
                if(snapshot.child("cart").child(CurrentRequest.current_number).exists())
                {
                    for (DataSnapshot snap : snapshot.child("cart").child(CurrentRequest.current_number).getChildren())
                    {
                        cart_id.add(snap.getKey());
                        price = price + (Float.parseFloat(snap.child("price").getValue()+"") * Float.parseFloat(snap.child("quantity").getValue()+""));


                    }
                }
                if(cart_id.isEmpty())
                {
                    Toast.makeText(getContext(), "order something", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    order.setEnabled(true);
                }

                total_price.setText("₹ "+price);

                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(new CartAdapter(R.layout.cart_recyclerview,cart_id));
                loadingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

     //



        order.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                    Intent i = new Intent(view.getContext(), PaymentActivity.class);
                    startActivity(i);

            }
        });




        return v;
    }
}