package com.example.asdfg.runningman;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.asdfg.runningman.R;

public class Room extends AppCompatActivity {
TextView setRoomName;
TableLayout seekerTable,hiderTable;
    Button btn1,btn2,btn3,btn4,btn5;
    TextView text1,text2;
    Intent intent1,intent2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent getIntent = getIntent();
        final String userName = getIntent.getStringExtra("userName");
        final String RoomName = getIntent.getStringExtra("roomName");
        final String playerNum = getIntent.getStringExtra("playerNum");
        final String seekerNum = getIntent.getStringExtra("seekerNum");
        final String time = getIntent.getStringExtra("time");
        final String chance = getIntent.getStringExtra("chance");

        btn1=findViewById(R.id.seekerButton);//seeker
        btn2=findViewById(R.id.hiderButton);//hider
        btn3=findViewById(R.id.startGame);//start
        btn4 =findViewById(R.id.roomExit);
        text1=findViewById(R.id.text1);//seeker목록
        text2=findViewById(R.id.text2);//hider목록
        seekerTable = findViewById(R.id.seekerTable);
        hiderTable=findViewById(R.id.hiderTable);
        intent1=new Intent(getApplicationContext(),Hider.class);
        intent2=new Intent(getApplicationContext(),Hider.class);
        /*
            서버에서 방정보(유저목록, 누가 어디에 있는지)
            가져오고 자신을 자동으로 seeker나 hider에 위치 시키고 서버로 전송
         */
        for(int i=0;i<Integer.parseInt(seekerNum);i++){
            final TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            TextView empty = new TextView(this);
            if(i==0)
                empty.setText(userName);
            else
                empty.setText("empty");
            empty.setTextSize(15);
            empty.setPadding(5,7,5,7);
            empty.setGravity(Gravity.CENTER);
           empty.setBackgroundColor(Color.argb(176,255,255,255));
            empty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            final View temp = new View(this);
            temp.setBackgroundColor(Color.argb(255,255,255,255));
            temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            tableRow.addView(empty);
            seekerTable.addView(tableRow);
            seekerTable.addView(temp);
        }
        for(int i=0;i<Integer.parseInt(playerNum)-Integer.parseInt(seekerNum);i++){
            final TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT));
            TextView empty = new TextView(this);
            empty.setText("empty");
            empty.setTextSize(15);
            empty.setPadding(5,7,5,7);
            empty.setGravity(Gravity.CENTER);
            empty.setBackgroundColor(Color.argb(176,255,255,255));
            empty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.WRAP_CONTENT));
            final View temp = new View(this);
            temp.setBackgroundColor(Color.argb(255,255,255,255));
            temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
            tableRow.addView(empty);
            hiderTable.addView(tableRow);
            hiderTable.addView(temp);
        }
        btn1.setOnClickListener(new View.OnClickListener() { // seeker쪽으로 유저 이동
            @Override
            public void onClick(View v) {
                //text1목록에 유저추가
                //갱신
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() { // hider쪽으로 유저 이동
            @Override
            public void onClick(View v) {
                //text2목록에 유저추가
                //갱신
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true) {    // *seeker인 경우
                    intent1.putExtra("roomName",RoomName);
                    intent1.putExtra("playerNum",playerNum);
                    intent1.putExtra("seekerNum",seekerNum);
                    intent1.putExtra("time",time);
                    intent1.putExtra("chance",chance);

                    startActivity(intent1);
                    finish();
                }
             /*   if(true ){    ///*hider인 경우
                    startActivity(intent2);
                    finish();
                }*/
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /*
     * thread로 유저목록 실시간으로 불러오기
     */
}
