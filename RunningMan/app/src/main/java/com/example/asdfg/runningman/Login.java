package com.example.yea2.runningman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
EditText loginID;
Button enterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginID=findViewById(R.id.loginID);
        enterBtn=findViewById(R.id.enterBtn);

        enterBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //room
            }
        });
    }
}
