package com.rushabh.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class DialerActivity extends AppCompatActivity {
private TextInputEditText edNumber;
private MaterialButton btnCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        bindID();
    }
    private void bindID() {
        edNumber = findViewById(R.id.edNumber);
        btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCall = edNumber.getText().toString();
                Snackbar snackbar = Snackbar
                        .make(DialerActivity.this.findViewById(android.R.id.content), "Calling on "+ strCall, Snackbar.LENGTH_LONG);
                snackbar.show();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strCall, null));
                startActivity(intent);
            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnZero:
                edNumber.setText(edNumber.getText() + "0");
                break;

            case R.id.btnOne:
                edNumber.setText(edNumber.getText() + "1");
                break;

            case R.id.btnTwo:
                edNumber.setText(edNumber.getText() + "2");
                break;

            case R.id.btnThree:
                edNumber.setText(edNumber.getText() + "3");
                break;

            case R.id.btnFour:
                edNumber.setText(edNumber.getText() + "4");
                break;

            case R.id.btnFive:
                edNumber.setText(edNumber.getText() + "5");
                break;

            case R.id.btnSix:
                edNumber.setText(edNumber.getText() + "6");
                break;

            case R.id.btnSeven:
                edNumber.setText(edNumber.getText() + "7");
                break;

            case R.id.btnEight:
                edNumber.setText(edNumber.getText() + "8");
                break;

            case R.id.btnNine:
                edNumber.setText(edNumber.getText() + "9");
                break;
            case R.id.btnStar:
                edNumber.setText(edNumber.getText() + "+");
                break;


        }
    }

}