package com.example.asdfg.runningman;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Room extends AppCompatActivity {
TextView setRoomName;
    Button btn1,btn2,btn3,btn4;
    TextView text1,text2;
    Intent intent1,intent2;
    PopupWindow window;
    Socket sock;
    int ID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.9.116.245";
    int port = 7777;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock= new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent getIntent = getIntent();
        userName = getIntent.getStringExtra("loginID");
        ID=getIntent.getIntExtra("code",-1);
        String RoomName = getIntent.getStringExtra("roomName");
        String playerNum = getIntent.getStringExtra("playerNum");
        String seekerNum = getIntent.getStringExtra("seekerNum");
        String time = getIntent.getStringExtra("time");
        String chance = getIntent.getStringExtra("chance");

    btn4 =findViewById(R.id.roomExit);
    btn4.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });
     /*   btn1=findViewById(R.id.btn1);//seeker
        btn2=findViewById(R.id.btn2);//hider
        btn3=findViewById(R.id.btn3);//ready
        text1=findViewById(R.id.text1);//seeker목록
        text2=findViewById(R.id.text2);//hider목록
        intent1=new Intent(this,Seeker.class);
        intent2=new Intent(this,Hider.class);*/
        /*
            서버에서 방정보(유저목록, 누가 어디에 있는지)
            가져오고 자신을 자동으로 seeker나 hider에 위치 시키고 서버로 전송
         */
       /* btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text1목록에 유저추가
                //갱신
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
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
                    Intent intent1=new Intent(getApplicationContext(),Seeker.class);
                    intent1.putExtra("userName",userName);
                    intent1.putExtra("code",ID);
                    startActivity(intent1);
                    finish();
                }
                if(true ){    ///*hider인 경우
                    Intent intent2=new Intent(getApplicationContext(),Hider.class);
                    intent2.putExtra("userName",userName);
                    intent2.putExtra("code",ID);
                    startActivity(intent2);
                    finish();
                }
            }
        });*/
    }
    /*
     try {
            if(ID==-1){
                outstream.writeUTF("-1");
            }else{
                outstream.writeUTF("-1 "+String.valueOf(ID));
            }
            outstream.flush();
            ID=instream.readInt();
            outstream.writeUTF("1 "+ userName+ " " + String.valueOf(ID));
            outstream.flush();
            outstream.close();
            instream.close();
            sock.close();
        } catch(Exception e) {
            e.printStackTrace();
     */
}
