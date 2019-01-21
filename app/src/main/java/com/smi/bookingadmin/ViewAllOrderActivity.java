package com.smi.bookingadmin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ViewAllOrderActivity extends AppCompatActivity {


    private DatabaseReference mDatabase;
    ListView listview;
    ArrayList<String> list=new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_view_full_order);



        final String uidKey = Objects.requireNonNull(super.getIntent().getExtras()).getString("uid_key");


        listview=  findViewById(R.id.pushKeyList);

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listview.setAdapter(adapter);


      //  Toast.makeText(ViewAllOrderActivity.this, uidKey, Toast.LENGTH_LONG).show();


        mDatabase = FirebaseDatabase.getInstance().getReference().child("booking").child("uid")
                .child(Objects.requireNonNull(uidKey)).child("itemDetailsKey");

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


                goToSingleOrderPage(item, uidKey);




            }
        });






    }

    private void goToSingleOrderPage(String item, String uid_key) {


        Intent intent = new Intent(ViewAllOrderActivity.this, SingleItemActivity.class);
        intent.putExtra("push_key", item);
        intent.putExtra("uid_key", uid_key);


        startActivity(intent);


    }





}

