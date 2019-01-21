package com.smi.bookingadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class OrderHomeActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    ListView listview;
    ArrayList<String> list=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_home);




        listview= (ListView) findViewById(R.id.listview);

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);



        mDatabase = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child("uid_list");

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                list.add(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {




            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                String item = listview.getItemAtPosition(i).toString();



                goToFullOrderPage(item);




            }
        });













    } // onCreate

    private void goToFullOrderPage(String item) {

        Intent intent = new Intent(OrderHomeActivity.this, ViewAllOrderActivity.class);
        intent.putExtra("uid_key", item);

        startActivity(intent);


    }
}
