package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class createAccount extends AppCompatActivity {

    EditText ph_num,name,email,address,pass;
    private ProgressDialog loadingbar;
    DatabaseReference rootref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ph_num = findViewById(R.id.ph_num);
        pass = findViewById(R.id.pass);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        address = findViewById(R.id.address);
        loadingbar = new ProgressDialog(this);
        rootref = FirebaseDatabase.getInstance().getReference();
        ph_num.setText(CurrentRequest.current_number);



    }

    public void signUp(View view) {
        if(ph_num.getText().toString().equals("") || name.getText().toString().equals("") ||
                email.getText().toString().equals("") || address.getText().toString().equals("")
                || pass.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please,fill all the details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.setTitle("Creating Account");
            loadingbar.setMessage("Hold up,We are making space for ya");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();


            // data gose to database using bottom code
            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("phonenumber",ph_num.getText()+"");
                    userdataMap.put("password",pass.getText()+"");
                    userdataMap.put("name",name.getText()+"");
                    userdataMap.put("email",email.getText()+"");
                    userdataMap.put("address",address.getText()+"");


                    rootref.child("users").child(ph_num.getText()+"").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                loadingbar.dismiss();
                                Toast.makeText(createAccount.this, "Account Created", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(createAccount.this,LoginActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                loadingbar.dismiss();
                                Toast.makeText(createAccount.this, "Something went Wronge", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }
    }
}