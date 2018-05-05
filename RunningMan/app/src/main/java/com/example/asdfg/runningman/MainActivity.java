package com.example.yea2.runningman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button btn1,btn2,btn3;
    Intent intent,intent1,intent2;
    TextView textView;
    String userName,roomList,roomName;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.roomList);
        // roomList= 방 리스트 어디서 갖고오지?
        // textView.setText(roomList);

        editText = findViewById(R.id.searchRoomName);
        roomName = editText.getText().toString();

        intent = getIntent(); // login한 닉네임
        userName = intent.getStringExtra("loginID");

        //intent1 = new Intent(this,MakeRoom.class);
        //intent2 = new Intent(this,FindRoom.class);

        btn1=findViewById(R.id.searchBtn); // 방검색하기
        btn2=findViewById(R.id.roomMakeBtn); //방만들기
        btn3=findViewById(R.id.roomEnterBtn); //방접속하기

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       // 방찾기
               // roomList안에 roomName 검색
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {      // 방만들기
            @Override
            public void onClick(View v) {
                startActivity(intent1);
                finish();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){      // 방 접속하기
            public void onClick(View v){
                startActivity(intent2);
                finish();
            }
        });
    }
}
