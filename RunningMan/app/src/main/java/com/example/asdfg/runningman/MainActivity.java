package com.example.asdfg.runningman;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MainActivity extends Activity {
    Button btn1,btn2,btn3,makeRoom,cancel;
    Intent intent,intent1,intent2;
    TextView textView;
    String userName,roomList,roomName;
    RadioGroup time,chance;
    RadioButton min5,min10,min15,sec30,sec60,sec120;
    EditText editText,roomNameText,playerNum,seekerNum;
    PopupWindow window;
    Socket sock;
    int ID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.9.116.245";
    int port = 7777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // textView = findViewById(R.id.roomList);
        // roomList= 방 리스트 어디서 갖고오지?
        // textView.setText(roomList);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock= new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        editText = findViewById(R.id.searchRoomName);
        roomName = editText.getText().toString();

        intent = getIntent(); // login한 닉네임
        userName = intent.getStringExtra("loginID");
        ID=intent.getIntExtra("code",-1);
        btn1=findViewById(R.id.searchBtn); // 방검색하기
        btn2=findViewById(R.id.roomMakeBtn); //방만들기
        btn3=findViewById(R.id.roomEnterBtn); //방접속하기

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {       // 방찾기
                 try {    // roomList안에 roomName 검색색try {
                     if(ID==-1) {

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
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {      // 방만들기
            @Override
            public void onClick(View v) {                   // 방만들기
                final View popupView = getLayoutInflater().inflate(R.layout.popupwindow,null);
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
                    public void onClick(View v) {
                        if (roomNameText.getText().toString().length() < 2)
                            Toast.makeText(MainActivity.this, "방 제목은 2자 이상이여야합니다", Toast.LENGTH_LONG).show();
                        else {
                            String room=roomNameText.getText().toString();
                            String pNum=playerNum.getText().toString();
                            String sNum=seekerNum.getText().toString();
                            intent1 = new Intent(getApplicationContext(), Room.class);
                            intent1.putExtra("roomName", room);
                            intent1.putExtra("playerNum", pNum);
                            intent1.putExtra("seekerNum", sNum);
                            int t=5;
                            int c=0;
                            int timeID = time.getCheckedRadioButtonId();
                            if (timeID == R.id.min5) {
                                intent1.putExtra("time", "5");
                                t=5;
                            }
                            if (timeID == R.id.min10) {
                                intent1.putExtra("time", "10");
                                t=10;
                            }
                            if (timeID == R.id.min15) {
                                intent1.putExtra("time", "15");
                            }
                            int chanceID = chance.getCheckedRadioButtonId();
                            if (chanceID == R.id.sec30) {
                                intent1.putExtra("chance", "30");
                                c=30;
                            }
                            if (chanceID == R.id.sec60) {
                                intent1.putExtra("chance", "60");
                                c=60;
                            }
                            if (chanceID == R.id.sec120) {
                                intent1.putExtra("chance", "120");
                                c=120;
                            }
                            intent1.putExtra("userName",userName);
                            intent1.putExtra("code",ID);
                            try {
                                if(ID==-1){
                                    outstream.writeUTF("-1");
                                }else{
                                    outstream.writeUTF("-1 "+String.valueOf(ID));
                                }
                                outstream.flush();
                                ID=instream.readInt();
                                outstream.writeUTF("1 "+ userName+ " " + String.valueOf(ID)+ " "+ room+ " "+ pNum+ " "+ sNum+ " "+ String.valueOf(t)+ " "+ String.valueOf(c));
                                outstream.flush();
                                outstream.close();
                                instream.close();
                                sock.close();
                            } catch(Exception e) {
                                e.printStackTrace();
                            }

                            startActivity(intent1);
                            window.dismiss();
                        }
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
                //방제목
                //if(인원수 꽉 차지 않았으면)
                try {    // roomList안에 roomName 검색색try {
                    if(ID==-1) {

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
                }
                intent2 = new Intent(getApplicationContext(), Room.class);
                intent2.putExtra("userName",userName);
                intent2.putExtra("code",ID);
                startActivity(intent2);
            }
        });
    }
}