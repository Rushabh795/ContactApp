package com.rushabh.contactapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.rushabh.contactapp.data.ConnectionDetector;
import com.rushabh.contactapp.data.Contact;
import com.rushabh.contactapp.data.SharedPrefManager;
import com.squareup.picasso.Picasso;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextInputEditText tvName ,tvNickName;
    TextInputEditText edMobileNumber,edEmail,edAddress,edFirstName;
    ImageView ivProfile;
    MaterialButton btUpdate;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String strID ;
    private Uri selectedImage ;
    String strFileName="";
    TextView tvUpload;
    boolean isAllFieldsChecked = false;
    StorageReference mStorageRef;
    String strImage ,strImagName;
    LinearLayout lvCall,lvEmail,lvAddress;
    private static int RESULT_UPDATE_IMG = 1;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private ProgressDialog progress;

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
        edFirstName= findViewById(R.id.edFirstName);
        btUpdate= findViewById(R.id.btUpdate);
        edAddress = findViewById(R.id.edAddress);
        ivProfile= findViewById(R.id.ivProfile);
        tvUpload = findViewById(R.id.tvUpload);
        lvCall= findViewById(R.id.lvCall);
        lvEmail= findViewById(R.id.lvEmail);
        lvAddress= findViewById(R.id.lvAddress);
        cd = new ConnectionDetector(ContactDetailsActivity.this);
        progress=new ProgressDialog(this);

        isInternetPresent = cd.isConnectingToInternet();
         setData();
        lvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCall = edMobileNumber.getText().toString();
                Snackbar snackbar = Snackbar
                        .make(ContactDetailsActivity.this.findViewById(android.R.id.content), "Calling on "+ strCall, Snackbar.LENGTH_LONG);
                snackbar.show();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strCall, null));
                startActivity(intent);
            }
        });

        lvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edEmail.getText().toString();
                if(!strEmail.isEmpty() && !strEmail.trim().equals(""))
                {
                    Uri uri = Uri.parse("mailto:" + strEmail)
                            .buildUpon()
                            .appendQueryParameter("subject", "Enter subject")
                            .appendQueryParameter("body", "Enter body")
                            .build();
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
                    startActivity(Intent.createChooser(emailIntent, "Welcome"));
                }else
                {
                    Snackbar snackbar = Snackbar
                            .make(ContactDetailsActivity.this.findViewById(android.R.id.content), "No Email-ID for this contact", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        lvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strAdd = edAddress.getText().toString();


                if(!strAdd.isEmpty() && !strAdd.trim().equals(""))
                {
                    String map = "http://maps.google.co.in/maps?q=" + strAdd;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(map));
                    startActivity(intent);
                }else
                {
                    Snackbar snackbar = Snackbar
                            .make(ContactDetailsActivity.this.findViewById(android.R.id.content), "No Address for this contact", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });



        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isInternetPresent) {
                    isAllFieldsChecked = CheckAllFields();
                    if (isAllFieldsChecked) {
                        Contact contact = new Contact();
                        contact.setId(getIntent().getExtras().getString("ID"));
                        contact.setStrFirstName(edFirstName.getText().toString().trim());
                        contact.setStrLastName(tvName.getText().toString().trim());
                        contact.setStrNickName(tvNickName.getText().toString().trim());
                        contact.setStrEmail(edEmail.getText().toString().trim());
                        contact.setStrAdd(edAddress.getText().toString().trim());
                        contact.setStrMobileNum(edMobileNumber.getText().toString().trim());
                        String strImageName = SharedPrefManager.getString("ImageFileName1", strImagName);
                        String strImagePath = SharedPrefManager.getString("ImagePath1", strImage);
                        contact.setStrImageName(strImageName);
                        contact.setStrImagePath(strImagePath);
                        databaseReference.setValue(contact);
                        Snackbar snackbar = Snackbar.make(ContactDetailsActivity.this.findViewById(android.R.id.content), "Contact Updated", Snackbar.LENGTH_SHORT);
                        snackbar.show();

                        Intent intent = new Intent(ContactDetailsActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar
                                .make(ContactDetailsActivity.this.findViewById(android.R.id.content), "Please connect to Internet first", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
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
        String strFirstName = getIntent().getExtras().getString("contact_Firstname");
        String strLastName = getIntent().getExtras().getString("contact_LastName");
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
        tvName.setText(strLastName.toString());
        tvNickName.setText(strNickName.toString());
        edMobileNumber.setText(strMobileNum.toString());
        edFirstName.setText(strFirstName);
        edEmail.setText(strEmailID.toString());
        edAddress.setText(strAddress.toString());
        if(strImage.trim().equals("")) {
            ivProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));
        }else
    {
        Glide.with(ContactDetailsActivity.this)
                .load(strImage)
                .override(300, 200)
                .into(ivProfile);

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
                    Glide.with(ContactDetailsActivity.this)
                            .load(selectedImage)
                            .override(300, 200)
                            .into(ivProfile);
                    strFileName = System.currentTimeMillis() + "." +getFileExt(selectedImage);
                    SharedPrefManager.putString("ImageFileName1",strFileName);
                    progress.setMessage("Image Uploading");
                    progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progress.setIndeterminate(true);
                    progress.setProgress(0);
                    progress.setCancelable(false);
                    progress.show();
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

                            progress.dismiss();
                            SharedPrefManager.putString("ImagePath1",downloadPhotoUrl.toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progress.dismiss();
                            Toast.makeText(ContactDetailsActivity.this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
    private boolean CheckAllFields() {
        if (edFirstName.length() == 0) {
            edFirstName.setError("Enter name");
            return false;
        }

        if (edMobileNumber.length() == 0) {
            edMobileNumber.setError("Enter Number");
            return false;
        }

        if (edEmail.length() == 0) {
            edEmail.setError("Enter E-mailID");
            return false;

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(edEmail.getText().toString()).matches()){
            edEmail.setError(" Email-ID is not proper");
            return false;
        }


        return true;
    }

}

