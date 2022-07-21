package com.example.a2amunch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PendingordersFragment extends Fragment {
    RecyclerView recyclerview_pending;
    ProgressDialog loadingbar;
    DatabaseReference rootref;
    public ArrayList p_order_id = new ArrayList();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pendingorders, container, false);
        recyclerview_pending = v.findViewById(R.id.recyclerview_pending);
        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Fetching Details");
        loadingbar.setMessage("wait a minute");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot snap : snapshot.child("pendingorders").getChildren())
                {
                    if(snap.child("status").getValue().toString().equals("Pending"))
                    {
                        p_order_id.add(snap.getKey());
                    }
                }
                if(p_order_id.isEmpty())
                {
                    Toast.makeText(getContext(), "No Order Found", Toast.LENGTH_SHORT).show();
                }
                recyclerview_pending.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerview_pending.setItemAnimator(new DefaultItemAnimator());
                recyclerview_pending.setAdapter(new PendingorderAdapter(R.layout.p_orders_recyclerview,p_order_id));
                loadingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}