package com.rushabh.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.rushabh.contactapp.adapter.ContactAdapter;
import com.rushabh.contactapp.data.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAddNew , fabDialer , fabEdit;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView rvContact;
    private ArrayList<Contact> arrContact;
    private ContactAdapter apContact;
    private ProgressBar igProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindID();
    }
    private void bindID() {
        fabAddNew = findViewById(R.id.fabAddNew);
        fabDialer = findViewById(R.id.fabDialer);
        fabEdit = findViewById(R.id.fabEdit);
        rvContact= findViewById(R.id.rvContact);
        igProgress= findViewById(R.id.igProgress);
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
        arrContact = new ArrayList<>();
        // on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference().child("Contact");
        apContact = new ContactAdapter(arrContact, this, this::onContactClick);
        // setting layout malinger to recycler view on below line.
        rvContact.setLayoutManager(new LinearLayoutManager(this));
        // setting adapter to recycler view on below line.
        rvContact.setAdapter(apContact);
        // on below line calling a method to fetch courses from database.
        getContact();
        fabAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
                startActivity(intent);
            }
        });

        fabDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,DialerActivity.class);
                startActivity(intent);
            }
        });


        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ContactDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getContact() {
        arrContact.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                igProgress.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                arrContact.add(snapshot.getValue(Contact.class));
                // notifying our adapter that data has changed.
                apContact.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                igProgress.setVisibility(View.GONE);
                apContact.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                apContact.notifyDataSetChanged();
                igProgress.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                apContact.notifyDataSetChanged();
                igProgress.setVisibility(View.GONE);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void onContactClick(int i) {

    }
}