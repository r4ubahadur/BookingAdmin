package com.smi.bookingadmin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SingleItemActivity extends AppCompatActivity {


    private TextView mDelivery, mDispatch;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);


        final String pushKey = Objects.requireNonNull(super.getIntent().getExtras()).getString("push_key");
        final String uidKey = super.getIntent().getExtras().getString("uid_key");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(Objects.requireNonNull(uidKey)).child("oderKey").child(Objects.requireNonNull(pushKey));

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    TextView aDelivery = findViewById(R.id.delivery_option);
                    String mDelivery = Objects.requireNonNull(dataSnapshot.child("delivered").getValue()).toString();


                    if (mDelivery.equals("true")){
                        aDelivery.setText("Delivered");

                    }
                    if (mDelivery.equals("false")){
                        aDelivery.setText("Not Delivered");

                    }




                }catch (Exception e){


                }


                try {

                    TextView aDispatch = findViewById(R.id.dispatch_option);
                    String mDispatch = Objects.requireNonNull(dataSnapshot.child("dispatched").getValue()).toString();

                    if (mDispatch.equals("true")){
                        aDispatch.setText("Dispatched");

                    }
                    if (mDispatch.equals("false")){
                        aDispatch.setText("Not Dispatched");

                    }


                }catch (Exception e){


                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDelivery = findViewById(R.id.delivery_option);
        mDispatch = findViewById(R.id.dispatch_option);

        mDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(uidKey).child("oderKey").child(pushKey);
                mDatabase.child("delivered").setValue(true);

            }
        });

        mDispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(uidKey).child("oderKey").child(pushKey);
                mDatabase.child("dispatched").setValue(true);
            }
        });


        mDatabase = FirebaseDatabase.getInstance().getReference().child("booking").child("uid").child(uidKey).child("oderKey").child(pushKey);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = Objects.requireNonNull(dataSnapshot.child("imageUrl").getValue()).toString();
                String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String size = Objects.requireNonNull(dataSnapshot.child("size").getValue()).toString();


                SimpleDraweeView mImageView = findViewById(R.id.image1);

                Uri uri = Uri.parse(image);
                mImageView.setImageURI(uri);

                TextView item_price = findViewById(R.id.item_price);
                TextView item_name = findViewById(R.id.item_name);
                TextView item_size = findViewById(R.id.item_size);

                item_price.setText(price);
                item_name.setText(name);
                item_size.setText(size);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}
