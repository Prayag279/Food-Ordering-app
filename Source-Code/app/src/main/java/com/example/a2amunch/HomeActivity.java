package com.example.a2amunch;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {

    }
    DatabaseReference rootref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);
        MaterialToolbar toolbar = findViewById(R.id.topappbar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header_view = navigationView.getHeaderView(0);
        TextView cur_name = header_view.findViewById(R.id.cur_name);
        TextView cur_phn = header_view.findViewById(R.id.cur_phn);
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_home).setVisible(true);
        menu.findItem(R.id.nav_cart).setVisible(true);
        menu.findItem(R.id.nav_order).setVisible(true);
        menu.findItem(R.id.nav_myprofile).setVisible(true);
        menu.findItem(R.id.nav_order_pending).setVisible(false);
        menu.findItem(R.id.nav_completed_order).setVisible(false);

        rootref = FirebaseDatabase.getInstance().getReference();
        cur_phn.setText(CurrentRequest.current_number);


        rootref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.child("users").child(CurrentRequest.current_number).exists())
                    {
                        cur_name.setText(snapshot.child("users").child(CurrentRequest.current_number).child("name").getValue() + "");
                        CurrentRequest.current_name=cur_name.getText()+"";
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        if(CurrentRequest.request_goto_cart)
        {
            CurrentRequest.request_goto_cart = false;
            navigationView.setCheckedItem(R.id.nav_cart);
            change_fragments(new CartFragment());
        }
        else if(CurrentRequest.is_manager)
        {
            CurrentRequest.is_manager = false;
            cur_name.setText("Manager");
            CurrentRequest.current_name=cur_name.getText()+"";
            menu.findItem(R.id.nav_home).setVisible(false);
            menu.findItem(R.id.nav_cart).setVisible(false);
            menu.findItem(R.id.nav_order).setVisible(false);
            menu.findItem(R.id.nav_myprofile).setVisible(false);
            menu.findItem(R.id.nav_order_pending).setVisible(true);
            menu.findItem(R.id.nav_completed_order).setVisible(true);
            navigationView.setCheckedItem(R.id.nav_order_pending);
            change_fragments(new PendingordersFragment());

        }
        else if(CurrentRequest.request_goto_profile)
        {
            CurrentRequest.request_goto_profile = false;
            navigationView.setCheckedItem(R.id.nav_myprofile);
            change_fragments(new ProfileFragment());
        }
        else if(CurrentRequest.request_goto_order)
        {
            CurrentRequest.request_goto_order = false;
            navigationView.setCheckedItem(R.id.nav_order);
            change_fragments(new OrderFragment());
        }
        else
        {
            navigationView.setCheckedItem(R.id.nav_home);
            change_fragments(new HomeFragment());
        }


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);//a amnm nai chale navigation_drawer ma group bnava pdse jethi singl b
                drawerLayout.closeDrawer(GravityCompat.START);
                switch (id)
                {
                    case R.id.nav_home: change_fragments(new HomeFragment());
                                        break;
                    case R.id.nav_cart: change_fragments(new CartFragment());
                                        break;
                    case  R.id.nav_myprofile: change_fragments(new ProfileFragment());
                                        break;
                    case  R.id.nav_order: change_fragments(new OrderFragment());
                                        break;
                    case  R.id.nav_order_pending:
                                                change_fragments(new PendingordersFragment());
                                                break;
                    case  R.id.nav_completed_order:
                                        change_fragments(new CompleteorderFragment());
                                        break;
                    case  R.id.nav_privacypolicy: change_fragments(new PrivacypolicyFragment());
                                         break;
                    case  R.id.nav_aboutus: change_fragments(new AboutusFragment());
                                            break;
                    case  R.id.nav_logout:
                                            CurrentRequest.current_number = "";
                                            CurrentRequest.current_name = "";
                                            Paper.book().delete("phonenumber");
                                            Paper.book().delete("password");
                                            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                                            startActivity(i);

                                            finish();
                                            break;
                }
                return true;
            }
        });
    }
    private  void change_fragments(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment).commit();

    }
}