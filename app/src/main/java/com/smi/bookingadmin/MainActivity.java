package com.smi.bookingadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("S.M. India");






        RelativeLayout view_order = findViewById(R.id.view_order);
        RelativeLayout add_category = findViewById(R.id.add_category);
        RelativeLayout upload_product_2 = findViewById(R.id.upload_product_2);

        upload_product_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingsIntent = new Intent(MainActivity.this, CategoryListActivity.class);
                startActivity(settingsIntent);

            }
        });


        view_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingsIntent = new Intent(MainActivity.this, OrderHomeActivity.class);
                startActivity(settingsIntent);

            }
        });

        add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingsIntent = new Intent(MainActivity.this, CategoryListActivity.class);
                startActivity(settingsIntent);

            }
        });











    }  //onCreate






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.category){

            Intent settingsIntent = new Intent(MainActivity.this, CategoryListActivity.class);
            startActivity(settingsIntent);

        }

        if(item.getItemId() == R.id.viewOrder){

            Intent settingsIntent = new Intent(MainActivity.this, OrderHomeActivity.class);
            startActivity(settingsIntent);

        }

        return true;
    }


}
