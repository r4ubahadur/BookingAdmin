package com.smi.bookingadmin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetailsActivity extends AppCompatActivity {

    final Context context = this;
    int imagePosition;
    String stringImageUri;

    private DatabaseReference mProductDatabase;
    private DatabaseReference mOrderCustomerName;
    private DatabaseReference mOrderDetails;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_item_details);

        mAuth = FirebaseAuth.getInstance();

        final String itemCategoryKey = super.getIntent().getExtras().getString("item_category_key");
        final String itemDetailsKey = super.getIntent().getExtras().getString("item_details_key");

        assert itemCategoryKey != null;
        assert itemDetailsKey != null;
        mProductDatabase = FirebaseDatabase.getInstance().getReference().child("uploads").child(itemCategoryKey).child(itemDetailsKey);

        mProductDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String image = dataSnapshot.child("imageUrl").getValue().toString();
                String price = dataSnapshot.child("price").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String size = dataSnapshot.child("size").getValue().toString();


                SimpleDraweeView mImageView = (SimpleDraweeView)findViewById(R.id.image1);

                Uri uri = Uri.parse(image);
                mImageView.setImageURI(uri);

                TextView item_price = findViewById(R.id.item_price);
                TextView item_name = findViewById(R.id.item_name);
                TextView item_size = findViewById(R.id.item_size);

                item_price.setText("₹ " + price + " /-");
                item_name.setText(name);
                item_size.setText(size);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        SimpleDraweeView nImageView = (SimpleDraweeView)findViewById(R.id.image1);

        nImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
