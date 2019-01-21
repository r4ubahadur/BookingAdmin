package com.smi.bookingadmin;



import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class UploadProductsActivity extends AppCompatActivity {



    private static final int GALLERY_PICK = 1;

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mEditTextFileName;
    private ImageView mImageView;

    private EditText mEditTextFileDesc;
    private EditText mEditTextFilePrice;
    private EditText mEditTextFileColor;
    private EditText mEditTextFileSize;

    private ProgressBar mProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mProductDatabase;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_products);



        String category_name2 = super.getIntent().getExtras().getString("category_passing_key2");



        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        //    mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mImageView = findViewById(R.id.image_view);

        mEditTextFileDesc = findViewById(R.id.edit_text_file_desc);
        mEditTextFilePrice = findViewById(R.id.edit_text_file_price);
        mEditTextFileColor = findViewById(R.id.edit_text_file_color);
        mEditTextFileSize = findViewById(R.id.edit_text_file_size);

        mProgressBar = findViewById(R.id.progress_bar);


        assert category_name2 != null;
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads").child(category_name2);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads").child(category_name2);




        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


        Toast.makeText(UploadProductsActivity.this, "Item Upload for" + category_name2 + "Category", Toast.LENGTH_LONG).show();


        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadProductsActivity.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, GALLERY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();


            CropImage.activity(mImageUri)
                    .start(this);


        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {


                mImageUri = result.getUri();

                Picasso.with(this).load(mImageUri).into(mImageView);






            }


        }


    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." +"jpg" /* getFileExtension(mImageUri) */ );


            final StorageReference thumb_filepath = mStorageRef.child("thumbs").child(System.currentTimeMillis()
                    + "." +"jpg" /* getFileExtension(mImageUri) */ );




            // image size change

            File thumb_filePath = new File(mImageUri.getPath());

            Bitmap thumb_bitmap = null;

            try {


                thumb_bitmap = new Compressor(this)
                        .setMaxHeight(140)
                        .setMaxWidth(120)
                        .setQuality(30)
                        .compressToBitmap(thumb_filePath);


            } catch (Exception e) {

                e.printStackTrace();
            }

            ByteArrayOutputStream bios = new ByteArrayOutputStream();
            Objects.requireNonNull(thumb_bitmap).compress(Bitmap.CompressFormat.JPEG, 22, bios);
            final byte[] thumb_byte = bios.toByteArray();

            // image size change






            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                            if (task.isSuccessful()){

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setProgress(0);
                                    }
                                }, 500);











                                // Toast.makeText(UploadProductsActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                                final Upload upload = new Upload(
                                        mEditTextFileName.getText().toString().trim(),
                                        Objects.requireNonNull(task.getResult().getDownloadUrl()).toString() ,
                                        task.getResult().getDownloadUrl().toString()        ,
                                        mEditTextFileDesc.getText().toString().trim(),
                                        mEditTextFileSize.getText().toString().trim(),
                                        mEditTextFileColor.getText().toString().trim(),
                                        mEditTextFilePrice.getText().toString());

                                final String uploadId = mDatabaseRef.push().getKey();





                                mDatabaseRef.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        mProductDatabase = FirebaseDatabase.getInstance().getReference("product");

                                        mProductDatabase.child(uploadId).setValue(upload);


                                        UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);

                                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                                String thumbImageUrl = Objects.requireNonNull(task.getResult().getDownloadUrl()).toString();

                                                if (task.isSuccessful()){

                                                    DatabaseReference thumbUrlDatabase = FirebaseDatabase.getInstance().getReference("product");

                                                    mDatabaseRef.child(uploadId)
                                                            .child("thumbImageUrl")
                                                            .setValue(thumbImageUrl);

                                                    thumbUrlDatabase.child(uploadId)
                                                            .child("thumbImageUrl")
                                                            .setValue(thumbImageUrl)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            AlertDialog.Builder alert = new AlertDialog.Builder(UploadProductsActivity.this);

                                                            alert.setMessage("Upload successfully...");
                                                            //  alert.setTitle("Create New Category");
                                                            alert.setCancelable(false);

                                                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int whichButton) {

                                                                    Intent ru = new Intent(UploadProductsActivity.this, MainActivity.class);
                                                                    startActivity(ru);

                                                                    finish();

                                                                }
                                                            });

                                                            alert.show();






                                                        }
                                                    });




                                                }



                                            }
                                        });


                                    }
                                });




                            }







                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadProductsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (10000.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

}