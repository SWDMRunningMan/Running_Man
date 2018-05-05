package com.example.yea2.runningman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
EditText loginID;
Button enterBtn;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginID=findViewById(R.id.loginID);
        enterBtn=findViewById(R.id.enterBtn);

        enterBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                //room
                String name = loginID.getText().toString();
                intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("loginID",name);
                startActivity(intent);
                finish();
            }
        });
    }
}
