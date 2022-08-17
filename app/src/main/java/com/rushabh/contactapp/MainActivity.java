package com.rushabh.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.rushabh.contactapp.adapter.ContactAdapter;
import com.rushabh.contactapp.data.ConnectionDetector;
import com.rushabh.contactapp.data.Contact;
import com.rushabh.contactapp.data.SharedPrefManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAddNew , fabDialer , fabEdit;
    private SwipeRefreshLayout pullToRefresh ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView rvContact;
    private ArrayList<Contact> arrContact;
    private ContactAdapter apContact;
    private ProgressBar igProgress;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;

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
        pullToRefresh = findViewById(R.id.pullToRefresh);
        cd = new ConnectionDetector(MainActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        arrContact = new ArrayList<>();

        SharedPrefManager.init(MainActivity.this);
        if (isInternetPresent) {
            setData();
        } else {
            Snackbar snackbar = Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "Please Check your connectivity.", Snackbar.LENGTH_LONG);
            snackbar.show();
            igProgress.setVisibility(View.GONE);

        }

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isInternetPresent) {
                    setData();
                    pullToRefresh.setRefreshing(false);
                }
                else {
                    Snackbar snackbar = Snackbar.make(MainActivity.this.findViewById(android.R.id.content), "Please Check your connectivity.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    igProgress.setVisibility(View.GONE);
                    pullToRefresh.setRefreshing(false);
                }
            }
        });
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

    private void setData() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://contact-bb046-default-rtdb.firebaseio.com/");
        // on below line we are getting database reference.

        databaseReference = firebaseDatabase.getReference().child("Contact");
 getContact();

    }


    private void getContact() {
        arrContact.clear();
        igProgress.setVisibility(View.GONE);
        apContact = new ContactAdapter(arrContact,MainActivity.this);
        rvContact.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvContact.setAdapter(apContact);
        apContact.notifyDataSetChanged();

        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                igProgress.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                arrContact.add(snapshot.getValue(Contact.class));
               String strName =  snapshot.getKey().toString();

                // setting layout malinger to recycler view on below line.
                rvContact.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                // setting adapter to recycler view on below line.
                rvContact.setAdapter(apContact);
//
                // notifying our adapter that data has changed.
                apContact.notifyDataSetChanged();
//                itCount =  apContact.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                igProgress.setVisibility(View.GONE);
                apContact.notifyDataSetChanged();
//                itCount =  apContact.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                apContact.notifyDataSetChanged();
                igProgress.setVisibility(View.GONE);
//                itCount =  apContact.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                apContact.notifyDataSetChanged();
                igProgress.setVisibility(View.GONE);
//                itCount =  apContact.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                apContact.notifyDataSetChanged();
                igProgress.setVisibility(View.GONE);
//                itCount =  apContact.getItemCount();
//                SharedPrefManager.putInt("ID",itCount);
            }
        });
    }


    private void onContactClick(int i) {

    }
}