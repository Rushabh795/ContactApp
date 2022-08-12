package com.rushabh.contactapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rushabh.contactapp.data.Contact;
import com.rushabh.contactapp.data.SharedPrefManager;
import com.squareup.picasso.Picasso;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextInputEditText tvName ,tvNickName;
    TextInputEditText edMobileNumber,edEmail,edAddress;
    ImageView ivProfile;
    MaterialButton btUpdate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String strID ;
    private Uri selectedImage ;
    String strFileName="";
    TextView tvUpload;
    StorageReference mStorageRef;
    String strImage ,strImagName;
    private static int RESULT_UPDATE_IMG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        bindID();
    }

    private void bindID() {
        tvName= findViewById(R.id.tvName);
        tvNickName= findViewById(R.id.tvNickName);
        edMobileNumber = findViewById(R.id.edMobileNumber);
        edEmail = findViewById(R.id.edEmail);
        btUpdate= findViewById(R.id.btUpdate);
        edAddress = findViewById(R.id.edAddress);
        ivProfile= findViewById(R.id.ivProfile);
        tvUpload = findViewById(R.id.tvUpload);
         setData();

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact();
                contact.setId(getIntent().getExtras().getString("ID"));
                contact.setStrFirstName(tvName.getText().toString().trim());
                contact.setStrNickName(tvNickName.getText().toString().trim());
                contact.setStrEmail(edEmail.getText().toString().trim());
                contact.setStrAdd(edAddress.getText().toString().trim());
                contact.setStrLastName(getIntent().getExtras().getString("contact_LastName"));
                contact.setStrMobileNum(edMobileNumber.getText().toString().trim());
                String strImageName =  SharedPrefManager.getString("ImageFileName1",strImagName);
                String strImagePath=  SharedPrefManager.getString("ImagePath1",strImage);
                contact.setStrImageName(strImageName);
                contact.setStrImagePath(strImagePath);
                databaseReference.setValue(contact);
                Toast.makeText(ContactDetailsActivity.this,"Contact Updated",Toast.LENGTH_SHORT);
                Intent intent = new Intent(ContactDetailsActivity.this , MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setData() {
        strID = getIntent().getExtras().getString("ID");
        String strFullName = getIntent().getExtras().getString("contact_FullName");
        String strMobileNum = getIntent().getExtras().getString("contact_Mobile");
        String strEmailID = getIntent().getExtras().getString("contact_Email");
        String strAddress = getIntent().getExtras().getString("contact_Address");
        String strNickName = getIntent().getExtras().getString("contact_NickName");
         strImage = getIntent().getExtras().getString("contact_Image");
         try {
             strImagName = getIntent().getExtras().getString("contact_Image_Name");
         }catch (Exception e)
         {
             strImage ="";
             e.printStackTrace();
         }
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Contact").child(strID);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        tvName.setText(strFullName.toString());
        tvNickName.setText(strNickName.toString());
        edMobileNumber.setText(strMobileNum.toString());
        edEmail.setText(strEmailID.toString());
        edAddress.setText(strAddress.toString());
        if(strImage.trim().equals("")) {
            ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));
        }else
    {
        Picasso.get().load(strImage).rotate(270f).into(ivProfile);
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_delete:
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(ContactDetailsActivity.this);
                materialAlertDialogBuilder.setTitle("Are you sure want to delete ?");
                materialAlertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Query applesQuery = databaseReference.child("Contact").orderByChild("id").equalTo(strID);

                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });

                        databaseReference.removeValue();
                        // displaying a toast message on below line.
                        // opening a main activity on below line.
                        startActivity(new Intent(ContactDetailsActivity.this, MainActivity.class));
                        Snackbar.make(ContactDetailsActivity.this.findViewById(android.R.id.content) , "Done",Snackbar.LENGTH_LONG).show();
                    }
                });
                materialAlertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
//        materialAlertDialogBuilder.setBackground(getResources().get)
                materialAlertDialogBuilder.show();
        }
        return super.onOptionsItemSelected(item);
    }
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery,RESULT_UPDATE_IMG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_UPDATE_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                selectedImage = data.getData();
                if(selectedImage != null) {
                    Picasso.get().load(selectedImage).rotate(270f).into(ivProfile);
                    strFileName = System.currentTimeMillis() + "." +getFileExt(selectedImage);
                    SharedPrefManager.putString("ImageFileName1",strFileName);
                    uploadFile();
                }else
                {
                    SharedPrefManager.putString("ImageFileName1",strImagName);
                    SharedPrefManager.putString("ImagePath1",strImage);
                }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();


                SharedPrefManager.putString("ImageFileName1",strImagName);
                SharedPrefManager.putString("ImagePath1", strImage);


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            SharedPrefManager.putString("ImageFileName1",strImagName);
            SharedPrefManager.putString("ImagePath1",strImage);


        }
    }
    private String getFileExt(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile()
    {
        if (selectedImage != null)
        {
            StorageReference storageReference = mStorageRef.child(strFileName);
            storageReference.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // Download file From Firebase Storage
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadPhotoUrl) {
                            //Now play with downloadPhotoUrl
                            //Store data into Firebase Realtime Database
                            SharedPrefManager.putString("ImagePath1",downloadPhotoUrl.toString());
                        }
                    });
                }
            });
        }
    }

}

