package com.rushabh.contactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rushabh.contactapp.data.Contact;
import com.rushabh.contactapp.data.SharedPrefManager;
import com.squareup.picasso.Picasso;
public class AddContactActivity extends AppCompatActivity {
    private TextInputEditText edFirstName , edLastname ,edNickName, edMobileNumber , edEmail ,edAddress ;
    private MaterialButton btSave;
    private TextView tvAddImage;
    private ImageView imgProfilePic;
    private  FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private ProgressBar savePB;
    public long lngContactID=0;
    Contact contact;
    boolean isAllFieldsChecked = false;
    String strID= "";
    private static long tempId = 0;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    private Uri selectedImage ;
    String strFileName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        bindID();
    }

    private void bindID() {
        edFirstName = findViewById(R.id.edFirstName);
        edLastname = findViewById(R.id.edLastName);
        edNickName= findViewById(R.id.edNickName);
        edMobileNumber = findViewById(R.id.edMobileNumber);
        edEmail = findViewById(R.id.edEmail);
        edAddress = findViewById(R.id.edAddress);
        btSave= findViewById(R.id.btSave);
        tvAddImage= findViewById(R.id.tvAddImage);
        imgProfilePic = findViewById(R.id.imgProfilePic);
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        // find highest key
        // set tempId = highestKey+1
        contact = new Contact();
        SharedPrefManager.init(AddContactActivity.this);
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference().child("Contact");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // on below line we are setting data in our firebase database.
                if(snapshot.exists())
                    lngContactID=(snapshot.getChildrenCount());
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // displaying a failure message on below line.
                Toast.makeText(AddContactActivity.this, "Fail to add Course..", Toast.LENGTH_SHORT).show();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAllFieldsChecked = CheckAllFields();
                if (isAllFieldsChecked) {
                    String strFirstName = edFirstName.getText().toString();
                    String strLastName = edLastname.getText().toString();
                    String strMobileNumber = edMobileNumber.getText().toString();
                    String strEmail = edEmail.getText().toString();
                    String strAddress = edAddress.getText().toString();
                    String strNickName = edNickName.getText().toString();
//                    databaseReference.orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                            // on below line we are hiding our progress bar.
//                             strID =  snapshot.getKey().toString();
//                            Log.d("TestAdd", "Added Child" + strID);
//                            Toast.makeText(getApplicationContext(), "Added" + strID, Toast.LENGTH_SHORT).show();
//                        }
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @androidx.annotation.Nullable String s) {
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//                   int itCount =  SharedPrefManager.getInt("ID",0) + 1;
//                   contact.setId(String.valueOf(itCount));


                    tempId++;
                    Toast.makeText(AddContactActivity.this, "Please wait contact is saving", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            contact.setId(String.valueOf(tempId));
                            contact.setStrFirstName(strFirstName);
                            contact.setStrLastName(strLastName);
                            contact.setStrNickName(strNickName);
                            contact.setStrMobileNum(strMobileNumber);
                            contact.setStrEmail(strEmail);
                            contact.setStrAdd(strAddress);
                            String strImageName =  SharedPrefManager.getString("ImageFileName","");
                            String strImagePath=  SharedPrefManager.getString("ImagePath","");
                            contact.setStrImageName(strImageName);
                            contact.setStrImagePath(strImagePath);
                            databaseReference.child(String.valueOf(tempId)).setValue(contact);
                            startActivity(new Intent(AddContactActivity.this, MainActivity.class));
                            // displaying a toast message.
                            Toast.makeText(AddContactActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                            SharedPrefManager.putString("ImageFileName","");
                            SharedPrefManager.putString("ImagePath","");
                            //This method will be executed once the timer is over
                            // Start your app main activity
                            //Redirect to Splash Activity to our main Activity
                        }
                    }, 2000);

                    // starting a main activity.
                }
            }
        });

    }
    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery,RESULT_LOAD_IMG);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data
                 selectedImage = data.getData();
                 if(selectedImage != null) {
                     Picasso.get().load(selectedImage).rotate(270f).into(imgProfilePic);
                     strFileName = System.currentTimeMillis() + "." +getFileExt(selectedImage);
                     SharedPrefManager.putString("ImageFileName",strFileName);
                     uploadFile();
                 }else
                 {
                     SharedPrefManager.putString("ImageFileName","");
                     SharedPrefManager.putString("ImagePath","");
                 }
            } else {
                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_SHORT).show();
                SharedPrefManager.putString("ImageFileName","");
                SharedPrefManager.putString("ImagePath","");


            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            SharedPrefManager.putString("ImageFileName","");
            SharedPrefManager.putString("ImagePath","");


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
                               SharedPrefManager.putString("ImagePath",downloadPhotoUrl.toString());
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
        return true;
    }

}