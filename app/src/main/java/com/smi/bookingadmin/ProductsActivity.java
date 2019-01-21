package com.smi.bookingadmin;




import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ImageAdapter mAdapter;


    TextView headlineCategoryName;
    private ProgressBar mProgressCircle;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;

    private List<Upload> mUploads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        headlineCategoryName = (TextView)findViewById(R.id.headlineCategoryName);

        String category_name = super.getIntent().getExtras().getString("category_passing_key");
        headlineCategoryName.setText(category_name);
        final String product = String.valueOf(category_name);


        mUploads = new ArrayList<>();

        mAdapter = new ImageAdapter(ProductsActivity.this, mUploads);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(ProductsActivity.this);

        mStorage = FirebaseStorage.getInstance();

        mStorage.getReference().child(category_name);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(category_name);


        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }

                mAdapter.notifyDataSetChanged();

                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProductsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });







        FloatingActionButton upload_product = (FloatingActionButton) findViewById(R.id.upload_product);
        upload_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent neww = new Intent(ProductsActivity.this, UploadProductsActivity.class);

                neww.putExtra("category_passing_key2", product);
                startActivity(neww);

            }
        });

    }

    @Override
    public void onItemClick(int position) {

        Upload clickItem = mUploads.get(position);
        final String ruma = clickItem.getKey();

        openSingleItem(ruma); // manually method
    }

    private void openSingleItem(String ruma) {


        String category_name = super.getIntent().getExtras().getString("category_passing_key");

        Intent itemIntent = new Intent(ProductsActivity.this, ItemDetailsActivity.class);
        itemIntent.putExtra("item_details_key", ruma);
        itemIntent.putExtra("item_category_key", category_name);

        startActivity(itemIntent);

    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {
        Upload selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ProductsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onBackPressed() {

        Intent rp = new Intent(ProductsActivity.this, MainActivity.class);
        startActivity(rp);

        finish();

    }



}