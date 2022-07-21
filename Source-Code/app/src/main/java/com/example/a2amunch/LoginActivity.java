package com.example.a2amunch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class LoginActivity extends AppCompatActivity {
   private EditText ph_num,password;
    private DatabaseReference rootref;
    ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ph_num = findViewById(R.id.ph_num);
        password = findViewById(R.id.password);

        Paper.init(getApplicationContext());
        ph_num.setText(CurrentRequest.current_number);
        loadingbar = new ProgressDialog(this);
        loadingbar.setTitle("Fetching Data");
        loadingbar.setMessage("Checking your Inputs");
        loadingbar.setCanceledOnTouchOutside(false);

        rootref = FirebaseDatabase.getInstance().getReference();


    }

    public void login(View view) {



        if(ph_num.getText().toString().equals("") || password.getText().toString().equals(""))
        {

            Toast.makeText(this, "PLease,fill all the details", Toast.LENGTH_SHORT).show();
        }
        else if(ph_num.getText().toString().equals("636465") && password.getText().toString().equals("psm123"))
        {
            CurrentRequest.current_number ="636465";
            CurrentRequest.is_manager = true;
            Intent i = new Intent(this,HomeActivity.class);
            startActivity(i);
        }
        else if(ph_num.getText().toString().length() > 10 || ph_num.getText().toString().length() < 10 )
        {


            Toast.makeText(this, "Enter legit Number", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingbar.show();
            CurrentRequest.current_number = ph_num.getText().toString();
            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("users").child(ph_num.getText().toString()).exists())
                    {
                        if(snapshot.child("users").child(ph_num.getText().toString()).child("password").getValue().toString().
                                equals(password.getText().toString()))
                        {
                            loadingbar.dismiss();
                            Paper.book().write("phonenumber", CurrentRequest.current_number);
                            Paper.book().write("password",snapshot.child("users").child(ph_num.getText().toString()).child("password").getValue().toString());
                            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                            startActivity(i);

                        }
                        else
                        {
                            loadingbar.dismiss();

                            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        loadingbar.dismiss();

                        Toast.makeText(LoginActivity.this, "Number not exist", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError e) {
                    Toast.makeText(LoginActivity.this, ""+e, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    public void forgetPass(View view) {


        if(ph_num.getText().toString().equals("") )
        {

            Toast.makeText(this, "PLease,Enter Number", Toast.LENGTH_SHORT).show();
        }
        else if(ph_num.getText().toString().length() > 10 || ph_num.getText().toString().length() < 10 )
        {

            Toast.makeText(this, "Enter legit Number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingbar.show();
            CurrentRequest.current_number = ph_num.getText().toString();
            rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("users").child(ph_num.getText().toString()).exists()) {
                        loadingbar.dismiss();
                        CurrentRequest.forget_pass = true;
                        Intent i = new Intent(getApplicationContext(), otpAuth.class);
                        startActivity(i);
                    } else {
                        loadingbar.dismiss();
                        Toast.makeText(LoginActivity.this, "Number not Exist", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loadingbar.dismiss();
                    Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();                }
            });
        }

    }

    public void create_id(View view) {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}









