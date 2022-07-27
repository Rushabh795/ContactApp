package com.rushabh.contactapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rushabh.contactapp.data.Contact;

public class AddContactActivity extends AppCompatActivity {
    TextInputEditText edFirstName , edLastname ,edNickName, edMobileNumber , edEmail ,edAddress ;
    MaterialButton btSave;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar savePB;
   public long lngContactID=0;
    Contact contact;
    boolean isAllFieldsChecked = false;


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
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
        contact = new Contact();
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
                    contact.setStrFirstName(strFirstName);
                    contact.setStrLastName(strLastName);
                    contact.setStrNickName(strNickName);
                    contact.setStrMobileNum(strMobileNumber);
                    contact.setStrEmail(strEmail);
                    contact.setStrAdd(strAddress);
//                contact.setId(String.valueOf(lngContactID+1));
                    databaseReference.child(String.valueOf(lngContactID + 1)).setValue(contact);
                    // displaying a toast message.
                    Toast.makeText(AddContactActivity.this, "Contact Saved", Toast.LENGTH_SHORT).show();
                    // starting a main activity.
                    startActivity(new Intent(AddContactActivity.this, MainActivity.class));
                }
            }
        });
    }
    private boolean CheckAllFields() {
        if (edFirstName.length() == 0) {
            edFirstName.setError("This field is required");
            return false;
        }

        if (edMobileNumber.length() == 0) {
            edMobileNumber.setError("This field is required");
            return false;
        }
        return true;
    }

}