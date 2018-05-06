package com.example.yea2.runningman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        setContentView(R.layout.activity_make_room);           // 방 만드는 화면 빈 공간이 많은데 scroll로 바꿀지? margin해서 딱 맞추면 입력할 떄 살짝 불편 키보드 끄고 맨 아래꺼 클릭해야댐
        /*edit=findViewById(R.id.edit);
        btn=findViewById(R.id.btn);
        intent=new Intent(this,Room.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = edit.getText().toString();
                //방아이디 생성
                if (str.length()>5) {      // str조건검증

                }else{
                    startActivity(intent);
                    finish();
                }
            }
        });*/
    }
}