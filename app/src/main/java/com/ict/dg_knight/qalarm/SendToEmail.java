package com.ict.dg_knight.qalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendToEmail extends AppCompatActivity {

    EditText editTextAddress, editTextSubject, editTextMessage;
    Button buttonIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_email);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        editTextAddress = (EditText)findViewById(R.id.editTextAddress);
        editTextSubject = (EditText)findViewById(R.id.editTextSubject);
        editTextMessage = (EditText)findViewById(R.id.editTextMessage);

        Button buttonIntent = (Button)findViewById(R.id.buttonIntent);
        buttonIntent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");
                intent.putExtra(Intent.EXTRA_EMAIL
                        , new String[] {editTextAddress.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT
                        , editTextSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT
                        , editTextMessage.getText().toString());
                startActivity(Intent.createChooser(intent, "Send email with"));
            }
        });


    }
}
