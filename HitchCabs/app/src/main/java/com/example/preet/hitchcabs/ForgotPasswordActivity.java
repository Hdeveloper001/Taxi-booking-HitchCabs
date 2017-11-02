package com.example.preet.hitchcabs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final EditText txt_email = (EditText)findViewById(R.id.txt_email);

        Button btn_close = (Button)findViewById(R.id.btn_close);
        Button btn_submit = (Button)findViewById(R.id.btn_submit);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_email.getText().length() == 0){

                }

            }
        });
    }
}
