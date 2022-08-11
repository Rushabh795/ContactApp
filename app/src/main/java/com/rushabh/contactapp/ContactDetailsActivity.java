package com.rushabh.contactapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.rushabh.contactapp.data.Contact;
import com.squareup.picasso.Picasso;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextInputEditText tvName ,tvNickName;
    TextInputEditText edMobileNumber,edEmail,edAddress;
    ImageView ivProfile;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String strID ;

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
        edAddress = findViewById(R.id.edAddress);
        ivProfile= findViewById(R.id.ivProfile);
         setData();
    }

    private void setData() {
        strID = getIntent().getExtras().getString("ID");
        String strFullName = getIntent().getExtras().getString("contact_FullName");
        String strMobileNum = getIntent().getExtras().getString("contact_Mobile");
        String strEmailID = getIntent().getExtras().getString("contact_Email");
        String strAddress = getIntent().getExtras().getString("contact_Address");
        String strNickName = getIntent().getExtras().getString("contact_NickName");
        String strImage = getIntent().getExtras().getString("contact_Image");
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
        databaseReference = firebaseDatabase.getReference("Contact").child(strID);
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
}

