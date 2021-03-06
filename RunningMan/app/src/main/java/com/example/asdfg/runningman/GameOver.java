package com.example.asdfg.runningman;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameOver extends AppCompatActivity {
    Socket sock;
    String ID;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.0.5";
    int port = 7777;
    int rID=-1;
    Intent intent;
    String userName,step;
    LinearLayout linearLayout;
    TextView yourwalking,first,second,third;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);
        /*
        게임종료 메세지 출력
        자기 걸음수 서버에 전송
        서버에서 순위 받아오기
        출력*/
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock= new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        intent = getIntent(); // login한 닉네임
        userName = intent.getStringExtra("loginID");
        ID=intent.getStringExtra("code");
        rID=intent.getIntExtra("rID", -1);
        try {
            outstream.writeUTF("-1 "+ID+" " +rID);
            outstream.flush();
            ID=instream.readUTF();
        } catch(Exception e) {
            e.printStackTrace();
        }

        yourwalking = findViewById(R.id.yourwalking);
        first=findViewById(R.id.first);
        second=findViewById(R.id.second);
        third=findViewById(R.id.third);

        yourwalking.setText(step + " 걸음 걸었습니다!");
       /* 서버에서 1등 2등 3등 아이디 & 걸음 수 갖고오기
        first.setText(누가 + " 걸음 걸었습니다");
        second.setText(누가 + " 걸음 걸었습니다");
        third.setText(누가 + " 걸음 걸었습니다");
         */

        linearLayout=findViewById(R.id.layout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                intent1.putExtra("userName",userName);
                intent1.putExtra("code",ID);
                startActivity(intent1);
            }
        });
        /*

        */
    }
    protected void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("loginID", userName);
        intent.putExtra("code", ID);
        startActivity(intent);
        super.onDestroy();
        try {
            outstream.close();
            instream.close();
            sock.close();
        } catch (IOException e) {
        }
    }
}