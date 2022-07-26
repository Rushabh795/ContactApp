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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.rushabh.contactapp.data.Contact;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextView tvName;
    TextInputEditText edMobileNumber,edEmail,edAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        bindID();
    }

    private void bindID() {
        tvName= findViewById(R.id.tvName);
        edMobileNumber = findViewById(R.id.edMobileNumber);
        edEmail = findViewById(R.id.edEmail);
        edAddress = findViewById(R.id.edAddress);
        setData();
    }

    private void setData() {
        String strFullName = getIntent().getExtras().getString("contact_FullName");
        String strMobileNum = getIntent().getExtras().getString("contact_Mobile");
        String strEmailID = getIntent().getExtras().getString("contact_Email");
        String strAddress = getIntent().getExtras().getString("contact_Address");
        tvName.setText(strFullName.toString());
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

