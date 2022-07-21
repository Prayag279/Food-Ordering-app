package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {
    RadioGroup groupradio;
    RadioButton selected_box;
    DatabaseReference rootref;
    ProgressDialog loadingbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        groupradio = findViewById(R.id.groupradio);
        loadingbar = new ProgressDialog(this);
        loadingbar.setTitle("Ordering");
        loadingbar.setMessage("Hold up for a while");
        loadingbar.setCanceledOnTouchOutside(false);
        rootref = FirebaseDatabase.getInstance().getReference();






    }
    public void go_back(View view) {
        onBackPressed();
    }

    public void payment(View view)
    {
        int checked_id = groupradio.getCheckedRadioButtonId();
        selected_box =(RadioButton)findViewById(checked_id);
        String selected_method = selected_box.getText().toString();
        if(checked_id==-1)
        {
            Toast.makeText(getApplicationContext(),"Select Payment Method", Toast.LENGTH_SHORT).show();
        }
        else if(selected_method.equals("Credit Card / Debit Card") || selected_method.equals("UPI Payments"))
        {
            Toast.makeText(this, "This Method is Unavailable", Toast.LENGTH_SHORT).show();
        }
        else
        {
           loadingbar.show();
           rootref.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot)
               { float total_price=0;
                   String order_item = "";
                   String address = snapshot.child("users").child(CurrentRequest.current_number).child("address").getValue()+"";
                   int item_id = Integer.parseInt(snapshot.child("pendingorders").getChildrenCount()+"");
                   for (DataSnapshot snap : snapshot.child("cart").child(CurrentRequest.current_number).getChildren())
                   {
                       if(order_item.equals(""))
                       {
                           order_item = order_item + snap.child("quantity").getValue()+" X "+snap.child("name").getValue()+"";

                       }
                       else
                       {
                           order_item = order_item + "\n" + snap.child("quantity").getValue() + " X " + snap.child("name").getValue();
                       }
                       total_price =  total_price + (Float.parseFloat(snap.child("price").getValue()+"") * Float.parseFloat(snap.child("quantity").getValue()+""));

                       HashMap<String, Object> orderitemmap = new HashMap<>();
                       orderitemmap.put("imgid", snap.child("imgid").getValue() + "");
                       orderitemmap.put("name", snap.child("name").getValue() + "");
                       orderitemmap.put("price", snap.child("price").getValue() + "");
                       orderitemmap.put("quantity", snap.child("quantity").getValue() + "");
                       orderitemmap.put("method", selected_method);
                       rootref.child("order").child(CurrentRequest.current_number).child(snap.child("name").getValue() + "").updateChildren(orderitemmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               rootref.child("cart").child(CurrentRequest.current_number).child(snap.getKey()).child("imgid").removeValue();
                               rootref.child("cart").child(CurrentRequest.current_number).child(snap.getKey()).child("name").removeValue();
                               rootref.child("cart").child(CurrentRequest.current_number).child(snap.getKey()).child("price").removeValue();
                               rootref.child("cart").child(CurrentRequest.current_number).child(snap.getKey()).child("quantity").removeValue();
                           }
                       });
                   }


                    HashMap<String, Object> p_orderitemmap = new HashMap<>();
                    item_id = item_id+1;
                    p_orderitemmap.put("item",order_item);
                    p_orderitemmap.put("total_price",total_price);
                   p_orderitemmap.put("address",address);
                   p_orderitemmap.put("details",CurrentRequest.current_number+"-"+CurrentRequest.current_name);
                    p_orderitemmap.put("status","Pending");
                    rootref.child("pendingorders").child(item_id+"").updateChildren(p_orderitemmap);
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    CurrentRequest.request_goto_order = true;
                     startActivity(i);
                    loadingbar.dismiss();

               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });



        }
    }
}