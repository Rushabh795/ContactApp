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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rushabh.contactapp.data.Contact;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextView tvName ,tvNickName;
    TextInputEditText edMobileNumber,edEmail,edAddress;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


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
         setData();
    }

    private void setData() {
//        String strID = getIntent().getExtras().getString("ID");
        String strFullName = getIntent().getExtras().getString("contact_FullName");
        String strMobileNum = getIntent().getExtras().getString("contact_Mobile");
        String strEmailID = getIntent().getExtras().getString("contact_Email");
        String strAddress = getIntent().getExtras().getString("contact_Address");
        String strNickName = getIntent().getExtras().getString("contact_NickName");
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
//        databaseReference = firebaseDatabase.getReference("Contact").child(strID);

        tvName.setText(strFullName.toString());
        tvNickName.setText(strNickName.toString());
        edMobileNumber.setText(strMobileNum.toString());
        edEmail.setText(strEmailID.toString());
        edAddress.setText(strAddress.toString());
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

