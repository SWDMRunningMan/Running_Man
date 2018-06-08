package com.example.asdfg.runningman;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class Room extends AppCompatActivity {
    TextView setRoomName;
    TableLayout seekerTable, hiderTable;
    Button btn1, btn2, btn3, btn4, btn5;
    TextView text1, text2;
    Intent intent1, intent2;
    Socket sock;
    String ID;
    int rID=-1;
    DataOutputStream outstream;
    DataInputStream instream;
    protected static String ip = "192.168.55.4";
    int port = 7777;
    String userName;
    String RoomName ;
    String playerNum ;
    String seekerNum ;
    String time ;
    String chance ;
    String owner;
    Handler handler;
    TableRow tableRow;
    TextView empty;
    View temp;
    int toggle=0;
    ArrayList<String> user=new  ArrayList<String>();
    ArrayList<String> userid=new  ArrayList<String>();
    ArrayList<Integer> feet=new  ArrayList<Integer>();
    ArrayList<String> seeker=new  ArrayList<String>();
    ArrayList<String> hider=new  ArrayList<String>();
    int num,num_S,num_H;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        try {
            sock = new Socket(ip, port);
            outstream = new DataOutputStream(sock.getOutputStream());
            instream = new DataInputStream(sock.getInputStream());
            Intent getIntent = getIntent();
            userName = getIntent.getStringExtra("loginID");
            ID = getIntent.getStringExtra("code");
            RoomName = getIntent.getStringExtra("roomName");
            playerNum = getIntent.getStringExtra("playerNum");
            seekerNum = getIntent.getStringExtra("seekerNum");
            time = getIntent.getStringExtra("time");
            chance = getIntent.getStringExtra("chance");
            owner= getIntent.getStringExtra("owner");
            rID= getIntent.getIntExtra("rID", -1);
            btn1 = findViewById(R.id.seekerButton);//seeker
            btn2 = findViewById(R.id.hiderButton);//hider
            btn3 = findViewById(R.id.startGame);//start
            btn4 = findViewById(R.id.roomExit);
            text1 = findViewById(R.id.text1);//seeker목록
            text2 = findViewById(R.id.text2);//hider목록
            seekerTable = findViewById(R.id.seekerTable);
            hiderTable = findViewById(R.id.hiderTable);
            intent1 = new Intent(getApplicationContext(), Seeker.class);
            intent2 = new Intent(getApplicationContext(), Hider.class);
            handler = new Handler();
        /*
            서버에서 방정보(유저목록, 누가 어디에 있는지)
            가져오고 자신을 자동으로 seeker나 hider에 위치 시키고 서버로 전송
         */
            outstream.writeUTF("-1 " +ID);
            outstream.flush();
            ID = instream.readUTF();
            if(owner.equals(ID)){
                btn3.setVisibility(View.VISIBLE);
            }
            final MyThread thread=new MyThread(sock);
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btn1.setOnClickListener(new View.OnClickListener() { // seeker쪽으로 유저 이동
            @Override
            public void onClick(View v) {
                //text1목록에 유저추가
                //갱신
                if(toggle==1) {
                    try {
                        Log.v("M", "101");
                        outstream.writeUTF("101 " + String.valueOf(rID) + " " + String.valueOf(ID));
                        outstream.flush();
                        Log.v("M", "100");
                        outstream.writeUTF("100 " + String.valueOf(rID) + " " + String.valueOf(ID));
                        outstream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    toggle = 0;
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() { // hider쪽으로 유저 이동
            @Override
            public void onClick(View v) {
                //text2목록에 유저추가
                //갱신
                if(toggle==0) {
                    try {
                        Log.v("M", "102");
                        outstream.writeUTF("102 " + String.valueOf(rID) + " " + String.valueOf(ID));
                        outstream.flush();
                        Log.v("M", "100");
                        outstream.writeUTF("100 " + String.valueOf(rID) + " " + String.valueOf(ID));
                        outstream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    toggle = 1;
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggle==0) {    // *seeker인 경우
                    intent1.putExtra("ID",ID);
                    intent1.putExtra("rID", rID);
                    intent1.putExtra("num_S", num_S);
                    intent1.putExtra("num_H", num_H);
                    intent1.putExtra("time", time);
                    intent1.putExtra("chance", chance);
                    intent1.putExtra("user", user);
                    intent1.putExtra("userid", userid);
                    intent1.putExtra("seeker", seeker);
                    intent1.putExtra("hider", hider);
                    startActivity(intent1);
                    finish();
                }
                if(toggle==1 ){    ///*hider인 경우
                    intent2.putExtra("ID",ID);
                    intent2.putExtra("rID", rID);
                    intent2.putExtra("num_S", num_S);
                    intent2.putExtra("num_H", num_H);
                    intent2.putExtra("time", time);
                    intent2.putExtra("chance", chance);
                    intent2.putExtra("user", user);
                    intent2.putExtra("userid", userid);
                    intent2.putExtra("seeker", seeker);
                    intent2.putExtra("hider", hider);
                    startActivity(intent2);
                    finish();
                }
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    /*
     * thread로 유저목록 실시간으로 불러오기
     */

    }
    class MyThread extends Thread {
        Socket socket;
        DataOutputStream outstream;
        DataInputStream instream;

        public MyThread(Socket sc) {
            socket=sc;
        }
        public void run() {
            try {
                outstream = new DataOutputStream(socket.getOutputStream());
                instream = new DataInputStream(socket.getInputStream());
                outstream.writeUTF("100 "+ String.valueOf(rID)+" "+ID);
                outstream.flush();
            } catch (Exception e) {
            }
            while (true) {
                try {
                    int m=instream.readInt();
                    if(m==100){
                        user.clear();
                        userid.clear();
                        feet.clear();
                        seeker.clear();
                        hider.clear();
                        String msg;
                        String str[];
                        msg=instream.readUTF();
                        str=msg.split(" ");
                        num=Integer.valueOf(str[0]);num_S=Integer.valueOf(str[1]);num_H=Integer.valueOf(str[2]);
                        for(int j=0;j<num;j++) {
                            msg=instream.readUTF();
                            str=msg.split(" ");
                            user.add(str[0]);
                            userid.add(str[1]);
                            feet.add(Integer.valueOf(str[2]));
                        }
                        for(int j=0;j<num_S;j++) {
                            msg=instream.readUTF();
                            seeker.add(msg);
                        }
                        for(int j=0;j<num_H;j++) {
                            msg=instream.readUTF();
                            hider.add(msg);
                        }
                        handler.post(new Runnable() {
                            public void run() {
                                Log.v("M","run");
                                seekerTable.removeAllViews();
                                hiderTable.removeAllViews();
                                for (int i = 0; i < Integer.valueOf(seekerNum); i++) {
                                    tableRow = new TableRow(Room.this);
                                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                    empty = new TextView(Room.this);
                                    if (i<num_S) {
                                        String N=null;
                                        Log.v("M",num+" "+num_S);
                                        for(int j=0;j<num;j++){

                                            if(userid.get(j).equals(seeker.get(i))){
                                                N=user.get(j);

                                                break;
                                            }
                                        }
                                        empty.setText(N);
                                    }
                                    else
                                        empty.setText("empty");
                                    empty.setTextSize(15);
                                    empty.setPadding(5, 7, 5, 7);
                                    empty.setGravity(Gravity.CENTER);
                                    empty.setBackgroundColor(Color.argb(176, 255, 255, 255));
                                    empty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    temp = new View(Room.this);
                                    temp.setBackgroundColor(Color.argb(255, 255, 255, 255));
                                    temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                                    tableRow.addView(empty);
                                    seekerTable.addView(tableRow);
                                    seekerTable.addView(temp);
                                }
                                for (int i = 0; i <Integer.valueOf(playerNum)-Integer.valueOf(seekerNum); i++) {
                                    tableRow = new TableRow(Room.this);
                                    tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                    empty = new TextView(Room.this);
                                    if (i<num_H) {
                                        String N=null;
                                        for(int j=0;j<num;j++){
                                            if(userid.get(j).equals(hider.get(i))){
                                                N=user.get(j);
                                                break;
                                            }
                                        }
                                        empty.setText(N);
                                    }
                                    else
                                        empty.setText("empty");
                                    empty.setTextSize(15);
                                    empty.setPadding(5, 7, 5, 7);
                                    empty.setGravity(Gravity.CENTER);
                                    empty.setBackgroundColor(Color.argb(176, 255, 255, 255));
                                    empty.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    temp = new View(Room.this);
                                    temp.setBackgroundColor(Color.argb(255, 255, 255, 255));
                                    temp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10));
                                    tableRow.addView(empty);
                                    hiderTable.addView(tableRow);
                                    hiderTable.addView(temp);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }

            }
        }
    }
}
