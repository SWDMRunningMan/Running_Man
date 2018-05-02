package com.example.asdfg.runningman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2;
    Intent intent1,intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent1 = new Intent(this,MakeRoom.class);
        intent2 = new Intent(this,FindRoom.class);
        btn1=findViewById(R.id.btn1); //방만들기
        btn2=findViewById(R.id.btn2); //방찾기
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent1);
                finish();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
                finish();
            }
        });
    }
}
