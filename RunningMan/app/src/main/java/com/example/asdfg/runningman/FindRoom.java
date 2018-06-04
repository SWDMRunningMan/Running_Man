package com.example.asdfg.runningman;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class FindRoom extends AppCompatActivity {
    EditText edit;
    Button btn;
    String str;
    Intent intent;
    Socket sock;
    int ID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.9.88.71";
    int port = 7777;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ID=intent.getIntExtra("code",-1);
        try {
            if(ID==-1){
                outstream.writeUTF("-1");
            }else{
                outstream.writeUTF("-1 "+String.valueOf(ID));
            }
            outstream.flush();
            ID=instream.readInt();
        } catch(Exception e) {
            e.printStackTrace();
        }
     /*   setContentView(R.layout.findroom);
        edit=findViewById(R.id.edit);
       btn=findViewById(R.id.btn);
        intent=new Intent(this,Room.class);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                str = edit.getText().toString();
                if (true str==서버의 방 아이디) {
                }else{
                    Intent intent1=new Intent(getApplicationContext(),Room.class);
                    intent1.putExtra("userName",userName);
                    intent1.putExtra("code",ID);
                    startActivity(intent1);
                    finish();
                }
            }
        });*/
    }
}