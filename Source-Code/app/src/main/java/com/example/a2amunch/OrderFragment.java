package com.example.a2amunch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class OrderFragment extends Fragment {
    RecyclerView recyclerview;
    TextView order_total_price;
    public ArrayList order_item_id = new ArrayList();
    DatabaseReference rootref;
    ProgressDialog loadingbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_order, container, false);

        recyclerview = v.findViewById(R.id.recyclerview);
        order_total_price = v.findViewById(R.id.order_total_price);
        rootref = FirebaseDatabase.getInstance().getReference();
        loadingbar = new ProgressDialog(getContext());
        //aya arraylist ma id nakhva no code lkhvo
        loadingbar.setTitle("Fetching Details");
        loadingbar.setMessage("wait a minute");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        rootref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            { float price=0;
                if(snapshot.child("order").child(CurrentRequest.current_number).exists())
                {
                    for(DataSnapshot snap : snapshot.child("order").child(CurrentRequest.current_number).getChildren() )
                    {
                        order_item_id.add(snap.getKey());
                        price = price + (Float.parseFloat(snap.child("price").getValue()+"") * Float.parseFloat(snap.child("quantity").getValue()+""));

                    }
                }
                else
                {
                    Toast.makeText(getContext(), "add item \nyour cart is empty", Toast.LENGTH_SHORT).show();

                }
                order_total_price.setText("₹ "+price);
                recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.setAdapter(new OrderAdapter(R.layout.order_recuclerview,order_item_id));
                loadingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return v ;
    }
}