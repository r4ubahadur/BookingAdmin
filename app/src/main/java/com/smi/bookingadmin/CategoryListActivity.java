package com.smi.bookingadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    TextView txtview;


    private DatabaseReference mDatabase;

    ListView categoryListView;
    ArrayList<String> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);


        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("S.M. India");




        categoryListView= (ListView) findViewById(R.id.listview);

        final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        categoryListView.setAdapter(adapter);




























        mDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child("product").child("category_name");

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


                list.remove(dataSnapshot.getValue(String.class));
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


               txtview = findViewById(R.id.txtview);

                String item = categoryListView.getItemAtPosition(i).toString();

                txtview.setText(item);




                abc();


            }
        });



        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           final int index, long arg3) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CategoryListActivity.this);

                alert.setTitle("Are u sure delete this item ?");
                alert.setCancelable(false);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child("product").child("category_name");
                        mDatabase.child(categoryListView.getItemAtPosition(index).toString()).removeValue();

                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });










        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CategoryListActivity.this);

                final EditText edittext = new EditText(CategoryListActivity.this);
                // alert.setMessage("Enter Your Message");
                alert.setTitle("Create New Category");
                alert.setCancelable(false);
                alert.setIcon(R.drawable.smi_logo);

                edittext.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                alert.setView(edittext);

                alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String new_category = edittext.getText().toString();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("admin").child("product").child("category_name");

                        //  String key =  mDatabase.push().getKey();

                        mDatabase.child(new_category).setValue(new_category);





                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        // what ever you want to do with No option.
                    }
                });

                alert.show();








            }
        });



        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(CategoryListActivity.this, "Add New Category", Toast.LENGTH_LONG).show();
                return true;
            }
        });



































    }

    private void abc() {

        String category = txtview.getText().toString();



        Intent intent = new Intent(CategoryListActivity.this, ProductsActivity.class);
        intent.putExtra("category_passing_key", category);

        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.category_menu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.home){

            Intent settingsIntent = new Intent(CategoryListActivity.this, MainActivity.class);
            startActivity(settingsIntent);

        }

        if(item.getItemId() == R.id.order){

            Intent settingsIntent = new Intent(CategoryListActivity.this, MainActivity.class);
            startActivity(settingsIntent);

        }

        return true;
    }










}
