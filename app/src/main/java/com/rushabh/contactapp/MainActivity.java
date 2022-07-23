package com.rushabh.contactapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fabAddNew , fabDialer , fabEdit;
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
}