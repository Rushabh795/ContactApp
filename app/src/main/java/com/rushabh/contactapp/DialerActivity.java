package com.rushabh.contactapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

public class DialerActivity extends AppCompatActivity {
EditText edInputText;
MaterialButton btnCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialer);
        bindID();
    }

    private void bindID() {
        edInputText = findViewById(R.id.edInputText);
        btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCall = edInputText.getText().toString();
//                Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+ strCall));
//                startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strCall, null));
                startActivity(intent);


            }
        });
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnZero:
                edInputText.setText(edInputText.getText() + "0");
                break;

            case R.id.btnOne:
                edInputText.setText(edInputText.getText() + "1");
                break;

            case R.id.btnTwo:
                edInputText.setText(edInputText.getText() + "2");
                break;

            case R.id.btnThree:
                edInputText.setText(edInputText.getText() + "3");
                break;

            case R.id.btnFour:
                edInputText.setText(edInputText.getText() + "4");
                break;

            case R.id.btnFive:
                edInputText.setText(edInputText.getText() + "5");
                break;

            case R.id.btnSix:
                edInputText.setText(edInputText.getText() + "6");
                break;

            case R.id.btnSeven:
                edInputText.setText(edInputText.getText() + "7");
                break;

            case R.id.btnEight:
                edInputText.setText(edInputText.getText() + "8");
                break;

            case R.id.btnNine:
                edInputText.setText(edInputText.getText() + "9");
                break;
        }
    }

}