package com.example.a2amunch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ForgetPassword extends AppCompatActivity {

    EditText new_pass,retype_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        new_pass = findViewById(R.id.new_pass);
        retype_pass = findViewById(R.id.retype_pass);


    }

    public void changePassword(View view) {
        if(new_pass.getText().toString().equals("") || retype_pass.getText().toString().equals(""))
        {
            Toast.makeText(this, "Please,fill all the details ", Toast.LENGTH_SHORT).show();
        }
        else if (!(new_pass.getText().toString().equals(retype_pass.getText().toString())))
        {
            Toast.makeText(this, "Both Password Dosent Match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
            rootref.child("users").child(CurrentRequest.current_number).child("password").setValue(new_pass.getText().toString())
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {

                            Intent i = new Intent(ForgetPassword.this,LoginActivity.class);
                            startActivity(i);
                            Toast.makeText(this, "Enter New Password", Toast.LENGTH_SHORT).show();
                        }

                    });
        }

    }
}