package com.example.asdfg.runningman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MakeRoom extends AppCompatActivity {
    EditText edit;
    Button btn;
    String str;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makeroom);
        edit=findViewById(R.id.edit);
        btn=findViewById(R.id.btn);
        intent=new Intent(this,Room.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = edit.getText().toString();
                //방아이디 생성
                if (str.length()>5/*str조건검증*/) {

                }else{
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}