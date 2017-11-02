package com.example.preet.hitchcabs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnGoSignIn = (Button)findViewById(R.id.btnSignin);
        btnGoSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),SigninActivity.class);
                startActivity(i);
            }
        });

        Button btnGoSignUp = (Button)findViewById(R.id.btnSignup);
        btnGoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),SignupActivity.class);
                startActivity(i);
            }
        });

        Button btnSkipSign = (Button)findViewById(R.id.btnSignskip);
        btnSkipSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }
}
