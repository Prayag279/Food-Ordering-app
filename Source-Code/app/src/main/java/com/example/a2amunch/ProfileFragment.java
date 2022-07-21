package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment {

    EditText profile_name,profile_num,profile_address;
    TextView change_password,profile_username;
    Button make_changes,save_changes;
    DatabaseReference rootref;
    ProgressDialog loadingbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_name = v.findViewById(R.id.profile_name);
        profile_num = v.findViewById(R.id.profile_num);
        profile_username = v.findViewById(R.id.profile_username);
        profile_address = v.findViewById(R.id.profile_address);
        change_password = v.findViewById(R.id.change_password);
        make_changes = v.findViewById(R.id.make_changes);
        save_changes = v.findViewById(R.id.save_changes);
        rootref = FirebaseDatabase.getInstance().getReference();
        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Fetching Details");
        loadingbar.setMessage("wait a minute");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),otpAuth.class);
                CurrentRequest.chnage_pass_frm_profile = true;
                startActivity(i);

            }
        });

        rootref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                profile_username.setText(snapshot.child("users").child(CurrentRequest.current_number).child("name").getValue()+"");
                profile_num.setText(snapshot.child("users").child(CurrentRequest.current_number).child("phonenumber").getValue()+"");
                profile_address.setText(snapshot.child("users").child(CurrentRequest.current_number).child("address").getValue()+"");
                profile_name.setText(snapshot.child("users").child(CurrentRequest.current_number).child("name").getValue()+"");
                loadingbar.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        make_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_name.setEnabled(true);
                profile_address.setEnabled(true);
                make_changes.setVisibility(View.GONE);
                save_changes.setVisibility(View.VISIBLE);

            }
        });

        save_changes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rootref.child("users").child(CurrentRequest.current_number).child("name").setValue(profile_name.getText()+"");
                rootref.child("users").child(CurrentRequest.current_number).child("phonenumber").setValue(profile_num.getText()+"");
                rootref.child("users").child(CurrentRequest.current_number).child("address").setValue(profile_address.getText()+"").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        profile_name.setEnabled(false);
                        profile_num.setEnabled(false);
                        profile_address.setEnabled(false);
                        make_changes.setVisibility(View.VISIBLE);
                        save_changes.setVisibility(View.GONE);
                        CurrentRequest.request_goto_profile = true;
                        Intent i = new Intent(getContext(),HomeActivity.class);
                        startActivity(i);

                    }
                });


            }
        });

     //   loadingbar.dismiss();
        return v;
    }
}