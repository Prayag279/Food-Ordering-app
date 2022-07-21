package com.example.a2amunch;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class HomeFragment extends Fragment {
    View view;
    Button add_sandwich,add_pizza,add_garlicbread,add_fries,add_nooodle,add_pasta,add_tacos,add_frankie;
    private ProgressDialog loadingbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
      view =  inflater.inflate(R.layout.fragment_home, container, false);

        add_garlicbread = view.findViewById(R.id.add_garlicbread);
        add_pizza = view.findViewById(R.id.add_pizza);
        add_sandwich = view.findViewById(R.id.add_sandwich);
        add_fries = view.findViewById(R.id.add_fries);
        add_nooodle = view.findViewById(R.id.add_nooodle);
        add_pasta = view.findViewById(R.id.add_pasta);
        add_tacos = view.findViewById(R.id.add_tacos);
        add_frankie = view.findViewById(R.id.add_frankie);

        add_garlicbread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.gralicbread,"Garlic Bread","90.00");

            }
        });
        add_pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.pza,"Pizza","120.00");
            }
        });

        add_sandwich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.sandwich,"Sandwich","60.00");

            }
        });
        add_fries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.fries,"French Fries","50.00");

            }
        });
        add_nooodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.noodles,"Noodles","70.00");

            }
        });
        add_pasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.pasta,"Pasta","80.00");

            }
        });
        add_tacos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.tacos,"Tacos","100.00");

            }
        });
        add_frankie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocart(R.drawable.frankie,"Frankie","90.00");

            }
        });



      return view;
    }


    private void addtocart(int img_id,String item_name,String item_price)
    {
        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Adding "+item_name);
        loadingbar.setMessage("wait a minute");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("cart").child(CurrentRequest.current_number).child(item_name).exists())
                {
                    String temp_qunatity = snapshot.child("cart").child(CurrentRequest.current_number).child(item_name).child("quantity").getValue()+"";
                    rootref.child("cart").child(CurrentRequest.current_number).child(item_name).child("quantity").setValue((Integer.parseInt(temp_qunatity) + 1)+"").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingbar.dismiss();

                        }
                    });
                }
                else
                {
                    HashMap<String, Object> itemdataMap = new HashMap<>();
                    itemdataMap.put("imgid", img_id);
                    itemdataMap.put("name", item_name);
                    itemdataMap.put("price", item_price);
                    itemdataMap.put("quantity", 1 + "");
                    rootref.child("cart").child(CurrentRequest.current_number).child(item_name).updateChildren(itemdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadingbar.dismiss();
                            Toast.makeText(getContext(), item_name + " added in cart", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}