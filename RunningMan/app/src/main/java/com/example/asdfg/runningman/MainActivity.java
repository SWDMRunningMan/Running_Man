package com.example.yea2.runningman;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
    Button btn1,btn2,btn3,makeRoom,cancel;
    Intent intent,intent1,intent2;
    TextView textView;
    String userName,roomList,roomName;
    RadioGroup time,chance;
    RadioButton min5,min10,min15,sec30,sec60,sec120;
    EditText editText,roomNameText,playerNum,seekerNum;
    PopupWindow window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // textView = findViewById(R.id.roomList);
        // roomList= 방 리스트 어디서 갖고오지?
        // textView.setText(roomList);

        editText = findViewById(R.id.searchRoomName);
        roomName = editText.getText().toString();

        intent = getIntent(); // login한 닉네임
        userName = intent.getStringExtra("loginID");

        btn1=findViewById(R.id.searchBtn); // 방검색하기
        btn2=findViewById(R.id.roomMakeBtn); //방만들기
        btn3=findViewById(R.id.roomEnterBtn); //방접속하기

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       // 방찾기
               // roomList안에 roomName 검색색
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {      // 방만들기
            @Override
            public void onClick(View v) {                   // 방만들기
                View popupView = getLayoutInflater().inflate(R.layout.popupwindow,null);
                window = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
                window.setAnimationStyle(-1);
                window.setFocusable(true);
                window.update();
                window.showAsDropDown(editText,20,400);

                roomNameText=popupView.findViewById(R.id.roomNameText);
                playerNum=popupView.findViewById(R.id.playerNum);
                seekerNum=popupView.findViewById(R.id.seekerNum);
                time = popupView.findViewById(R.id.timeGroup);
                chance = popupView.findViewById(R.id.chanceGroup);
                makeRoom=popupView.findViewById(R.id.makeRoom);
                cancel=popupView.findViewById(R.id.cancel);

                makeRoom.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        intent=new Intent(getApplicationContext(),Room.class);
                        intent.putExtra("roomName",roomNameText.getText().toString());
                        intent.putExtra("playerNum",playerNum.getText().toString());
                        intent.putExtra("seekerNum",seekerNum.getText().toString());
                        int timeID = time.getCheckedRadioButtonId();
                        if(timeID == R.id.min5)
                            intent.putExtra("time",min5.getText().toString());
                        if(timeID==R.id.min10)
                            intent.putExtra("time",min10.getText().toString());
                        if(timeID==R.id.min15)
                            intent.putExtra("time",min15.getText().toString());
                        int chanceID = chance.getCheckedRadioButtonId();
                        if(chanceID == R.id.sec30)
                            intent.putExtra("chance",sec30.getText().toString());
                        if(chanceID == R.id.sec60)
                            intent.putExtra("chance",sec60.getText().toString());
                        if(chanceID == R.id.sec120)
                            intent.putExtra("chance",sec120.getText().toString());
                        startActivity(intent);
                        window.dismiss();
                        finish();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        window.dismiss();
                    }
                });
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){      // 방 접속하기
            public void onClick(View v){                  // 방접속
                startActivity(intent2);
                finish();
            }
        });
    }
}
