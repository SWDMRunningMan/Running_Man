package com.example.asdfg.runningman;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Seeker extends AppCompatActivity {
    Socket sock;
    int ID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.35.95";
    int port = 7777;
    Intent intent;
    String userName;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeker);
        /*게임진행
           걸음수 기록
           중간에 사진 불러오기
           유저눌렀을때 유저에게 메세지 전달
           확인받으면 UI변경
           시간종료시 종료
         */
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
        try {
            if (ID == -1) {
                outstream.writeUTF("-1");
            } else {
                outstream.writeUTF("-1 " + String.valueOf(ID));
            }
            outstream.flush();
            ID = instream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent1=new Intent(getApplicationContext(),GameOver.class);
        intent1.putExtra("userName",userName);
        intent1.putExtra("code",ID);
        startActivity(intent1);
    }
}