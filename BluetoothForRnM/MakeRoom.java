package com.example.geonsu.runningman;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MakeRoom extends AppCompatActivity {
    EditText roomNameText,seekerNum,timeLimit,chanceNum;
    Button makeRoom,cancel;
    String str;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_room);

        roomNameText=findViewById(R.id.roomNameText);
        seekerNum=findViewById(R.id.seekerNum);
        timeLimit=findViewById(R.id.timeLimit);
        chanceNum=findViewById(R.id.chanceNum);
        makeRoom=findViewById(R.id.makeRoom);
        cancel=findViewById(R.id.cancel);

        makeRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = roomNameText.getText().toString();
                //방아이디 생성
                if (str.length()>5) {      // str조건검증
                    Toast.makeText(getApplicationContext(),"5자 이하의 방 제목을 입력해주세요",Toast.LENGTH_SHORT);
                }else
                {
                    intent=new Intent(getApplicationContext(),Room.class);
                    intent.putExtra("roomName",roomNameText.getText().toString());
                    intent.putExtra("seekerNum",seekerNum.getText().toString());
                    intent.putExtra("timeLimit",timeLimit.getText().toString());
                    intent.putExtra("makeRoom",makeRoom.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
}